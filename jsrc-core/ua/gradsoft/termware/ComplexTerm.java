package ua.gradsoft.termware;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002-2009
 */


import ua.gradsoft.termware.exceptions.TermIndexOutOfBoundsException;



/**
 * Generic complex term
 *   (i. e. term with subterms, like f(x1..xN) )
 **/
public class ComplexTerm extends AbstractComplexTerm {
    
    ComplexTerm(String name, Term[] body, SymbolAdoptionPolicy symbolAdoptionPolicy) {
        this(new Name(name,symbolAdoptionPolicy),body);
    }
    
    
    /**
     * create complex term with subterms from name and array of subterms.
     *@param name - name of term.
     *@param body - array of subterms.
     *@param symbolAdoptionPolicy - policy of symbol adoption.
     */
    ComplexTerm(Name name, Term[] body) {
        name_=name;
        subterms_ = body;
        recheckEmptyFv();
    }
    
    protected ComplexTerm(Name name, Term[] body, boolean newEmptyFv) {
        name_=name;
        subterms_ = body;
        emptyFv_ = newEmptyFv;
    }
    
    /**
     * name for <code> f(x1..xN) </code> is f.
     * 
     * @see ua.gradsoft.termware.Term#getName
     * @return name
     */
    public final String   getName() {
        return name_.getStringValue();
    }
    
    public final Object getNameIndex() 
    {
        return name_.getIndex();
    }
    
    
    public final int      getArity() {
        return subterms_.length; }
    
    public final Term getSubtermAt(int i) {
        if (i > subterms_.length || i < 0) {
            throw new TermIndexOutOfBoundsException(this,i);
        }
        return subterms_[i];
    }
    
    public void setSubtermAt(int i, Term t) {
        if (i > subterms_.length || i < 0) {
            throw new TermIndexOutOfBoundsException(this,i);
        }
        Term prev=subterms_[i];
        subterms_[i]=t;
        if (emptyFv_) {
            if (!t.emptyFv()) {
                emptyFv_=false;
            }
        }else{
            if (t.emptyFv()) {
                if (!prev.emptyFv()) {
                    recheckEmptyFv();
                }
            }
        }
        resetFV();        
    }
    
    
    public Term createSame(Term[] newBody) {
        return new ComplexTerm(name_, newBody);
    }
    
    
    public Term termClone() throws TermWareException {
        Term[] newBody=new Term[getArity()];
        for(int i=0; i<getArity(); ++i) {
            newBody[i]=getSubtermAt(i).termClone();
        }
        return new ComplexTerm(name_,newBody,emptyFv_);
    }
            
    public   PartialOrderingResult concreteOrder(Term x, Substitution s) throws TermWareException
    {
        if (x.isX()) {
            Term t = s.get(x.minFv());
            if (t!=null) {
                return concreteOrder(t,s);
            }else{
                s.put(x,this); 
                return PartialOrderingResult.LESS;
            }
        }else if (x.isComplexTerm()) {
            if (x.getNameIndex().equals(getNameIndex())) {
                if (x.getArity()==getArity()) {
                    int arity = getArity();
                    PartialOrderingResult sr=PartialOrderingResult.EQ;
                    for(int i=0;i<arity;++i) {
                        Term cx = x.getSubtermAt(i);
                        Term tx = getSubtermAt(i);
                        PartialOrderingResult cr = tx.concreteOrder(cx,s);
                        sr=PartialOrderingResult.merge(sr,cr);
                    }
                    return sr;
                }else{
                    return PartialOrderingResult.NOT_COMPARABLE;
                }
            }else{
                // special cases, like patterns
                if (x.getNameIndex().equals(TermWareSymbols.ARGS_PATTERN_INDEX)) {
                   if (x.getArity()==2) {
                       Term xp = x.getSubtermAt(0);
                       Term xv = x.getSubtermAt(1);
                       if (xp.isX()) {
                           return PartialOrderingResult.merge(PartialOrderingResult.LESS,concreteOrderArgsList(xv,s));
                       }else if (xp.getNameIndex().equals(getNameIndex())) {
                           return concreteOrderArgsList(xv,s);
                       }else{
                           return PartialOrderingResult.NOT_COMPARABLE;
                       }
                   }else{
                       return PartialOrderingResult.NOT_COMPARABLE;
                   }
                }else{
                   // set's are handled in appropriative subclasses, so that's all
                    return PartialOrderingResult.NOT_COMPARABLE;
                }
            }
                
        }else{
            return PartialOrderingResult.NOT_COMPARABLE;
        }
    }

    private PartialOrderingResult concreteOrderArgsList(Term xv, Substitution s) throws TermWareException
    {
      if (xv.isX()) {
          Term xvt = s.get(xv.minFv());
          if (xvt!=null) {
              return concreteOrderArgsList(xvt,s);
          }else{
              int arity = this.getArity();
              Term svt = NILTerm.getNILTerm();
              for(int i=0; i<arity; ++i) {
                 Term ct = subterms_[arity-i-1];  
                 svt=new ListTerm(ct,svt);
              }
              s.put(xv,svt);
              return PartialOrderingResult.LESS;
          }          
      }else if (xv.isComplexTerm() && xv.getNameIndex().equals(TermWareSymbols.CONS_INDEX)) {
          int i=0;
          int arity=getArity();
          Term v=xv;
          PartialOrderingResult sr = PartialOrderingResult.EQ;
          while(v.isComplexTerm() && v.getNameIndex().equals(TermWareSymbols.CONS_INDEX)) {
              Term cv=v.getSubtermAt(0);
              Term nv=v.getSubtermAt(1);
              Term ti=getSubtermAt(i);
              if (nv.isNil()) {
                  if (cv.isX()) {
                     Term cvt=s.get(cv.minFv());
                     if (cvt!=null) {
                        return PartialOrderingResult.merge(sr,ti.concreteOrder(cvt,s));
                     }else{
                        s.put(cv,ti); 
                        return PartialOrderingResult.merge(sr,PartialOrderingResult.LESS);
                     }
                  }else{
                      //(a,b,c)
                      //cons(a,cons(b,cons(c,NIL)))
                      if (i==arity-1) {
                          return PartialOrderingResult.merge(sr,ti.concreteOrder(cv,s));
                      }else{
                          return PartialOrderingResult.NOT_COMPARABLE;
                      }
                  }
              }else{
                  PartialOrderingResult r=ti.concreteOrder(cv,s);
                  if (r==PartialOrderingResult.NOT_COMPARABLE) {
                      return r;
                  }else{
                      sr=PartialOrderingResult.merge(sr,r);
                  }
              }
              ++i;
              v=nv;
          }
          return sr;     
      }else{
          return PartialOrderingResult.NOT_COMPARABLE;
      }  
    }
    
    private  Name   name_;
    private  Term[] subterms_;
    
    
}
