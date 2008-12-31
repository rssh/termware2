/*
 * ArgsPatternTerm.java
 *
 *
 * Copyright (c) 2006-2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termware;

import java.util.ArrayList;
import java.util.List;
import ua.gradsoft.termware.exceptions.AssertException;
import ua.gradsoft.termware.exceptions.TermIndexOutOfBoundsException;

/**
 *This term is used for unification of subterms of compired term to list.
 *It has special syntax in TermWare language: <name>...
 *i.e.
 *<code>
 *unify(f..($x),f(x1,x2)) will unify $x with [x1,x2]
 *unify(f..($x),f()) will unify $x with nil.
 *unify(f..($x),f(x1,x2,x3,x4)) will unify $x with [x1,x2,x3,x4]
 *</code>
 *and so on.
 *<code>
 *unify(f..($x),g(x1,x2)) will fail if g!=f.
 *</code>
 * $f..$x will successfully unficated with any functional term. <code> $f </code> will be
 *substituted with string (name of term).
 *
 *Of course, in right parts of rules this will unroll all back when substituted,
 *i. e.  <code>f..($x) when $x=[x1...xN]  -> f(x1,..xN) </code>
 *
 * @author Ruslan Shevchenko
 */
public class ArgsPatternTerm  extends AbstractComplexTerm 
{
    
    ArgsPatternTerm(Term pattern,Term variable) {
        pattern_=pattern;
        variable_=variable;
    }

    public static Term createArgsPatternTerm(Term pattern,Term variable) throws TermWareException
    {
        if (!variable.isX()) {
            // i. e. this is not ArgsPattern            
            if (variable.getNameIndex().equals(TermWareSymbols.CONS_INDEX)) {
                //System.err.println("!!1variable:"+TermHelper.termToString(variable));
                List<Term> body=new ArrayList<Term>();
                while(!variable.isNil()) {
                    body.add(variable.getSubtermAt(0));
                    variable=variable.getSubtermAt(1);
                }
                Term[] tbody = body.toArray(TermWare.EMPTY_TERM_ARRAY);
                return TermWare.getInstance().getTermFactory().createTerm(pattern.getName(),tbody);
            }else if(variable.isNil()) {
                Term[] tbody = TermWare.EMPTY_TERM_ARRAY;
                return TermWare.getInstance().getTermFactory().createTerm(pattern.getName(),tbody);
            }else{
                throw new AssertException("second argument of args_pattern must be list or sequence");
            }
        }else{
            return new ArgsPatternTerm(pattern,variable);
        }
    }
    
    
    /**     * 
     * @return TermWareSymbols.ARGS_PATTERN_STRING
     * 
     * @see ua.gradsoft.termware.Term#getName()
     */
    public String getName()
    { return TermWareSymbols.ARGS_PATTERN_STRING; }
    
    /**
     *@return TermWareSymbols.ARGS_PATTERN_INDEX
     */
    public Object getNameIndex()
    { return TermWareSymbols.ARGS_PATTERN_INDEX; }

    /**
     * for <code> f..($x) </code>  this will be <code> f </code>
     * 
     * @return name of patterm
     */
    @Override
    public String getPatternName()
    {
      return pattern_.getPatternName();
    }
    
    @Override
    public Object getPatternNameIndex()
    {
      return pattern_.getPatternNameIndex();
    }
    
    /**
     * @see ua.gradsoft.termware.Term#getArity()
     * 
     * @return 2
     */
    public final int      getArity()
   { return 2; }

    public Term    getSubtermAt(int i) 
   {
     switch(i) {
         case 0: return pattern_;
         case 1: return variable_;
         default:
          throw new TermIndexOutOfBoundsException(this,i);   
     }
   }

   public void   setSubtermAt(int i, Term t) 
   {
     switch(i)  {
      case 0: 
          pattern_=t;
          break;
      case 1: 
         variable_=t;
         break;
      default:
          throw new TermIndexOutOfBoundsException(this,i);          
     }
   }
 
   @Override
 public boolean boundUnify(Term t, Substitution s) throws TermWareException 
 {
   if(t.isNil()) return false;
   else if(PrimaryTypes.isPrimitive(t.getPrimaryType0())) return false;
   else if(t.isX()) {
     s.put(t,this);
     return true;
   }else {
       boolean patternMatched=false;
       if (pattern_.isX()) {
           s.put(pattern_,new StringTerm(t.getName()));
           patternMatched=true;
       }
       if (patternMatched || pattern_.getPatternNameIndex().equals(t.getNameIndex())) {
           if (variable_.isX()) { 
               Term retval = NILTerm.getNILTerm();
               for(int i=0; i<t.getArity(); ++i) {
                   retval=new ListTerm(t.getSubtermAt(t.getArity()-i-1),retval);
               }
               s.put(variable_,retval);
           }else{
               Term v = variable_;
               if (v.isComplexTerm() && v.getNameIndex().equals(TermWareSymbols.CONS_INDEX)) {
                   int i=0;
                   while(!v.isNil() && i<t.getArity()) {
                       if (v.getSubtermAt(0).boundUnify(t.getSubtermAt(i),s)) {
                           return false;
                       }
                       v=v.getSubtermAt(1);
                       ++i;
                   }
                   if (!v.isNil() || i<t.getArity() ) {                     
                           return false;
                   }                   
               }else{
                   return false;
               }
           }
       }else{
           return false;
       }     
   }
   return true;
 }

    public PartialOrderingResult concreteOrder(Term x, Substitution s) throws TermWareException
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
           if (pattern_.isX()) {
               s.put(pattern_,TermWare.getInstance().getTermFactory().createAtom(x.getName()));
               PartialOrderingResult r1 = concreteOrderArgsList(x,s);
               return PartialOrderingResult.merge(PartialOrderingResult.MORE,r1);
           }else if(pattern_.getNameIndex().equals(x.getNameIndex())) {
               PartialOrderingResult r1 = concreteOrderArgsList(x,s);
               return PartialOrderingResult.merge(PartialOrderingResult.EQ,r1);
           }else{
               return PartialOrderingResult.NOT_COMPARABLE;
           }
       }else{
           return PartialOrderingResult.NOT_COMPARABLE;
       } 
    }
    
    public Term createSame(Term[] args) throws TermWareException
    {
        if (args.length!=2) {
            throw new AssertException("args_pattern must have arity 2");
        }        
        return createArgsPatternTerm(args[0],args[1]);
    }
   
    
    
    public Term termClone() throws TermWareException
    {
       return createArgsPatternTerm(pattern_.termClone(),variable_.termClone()); 
    }
    
    private PartialOrderingResult concreteOrderArgsList(Term x,Substitution s) throws TermWareException
    {
        if (variable_.isX()) {
            Term sList=NILTerm.getNILTerm();     
            int xArity = x.getArity();
            for(int i=0; i<x.getArity(); ++i) {
                sList=new ListTerm(x.getSubtermAt(xArity-i-1),sList);
            }
            s.put(variable_,sList);
            return PartialOrderingResult.MORE;
        }
        Term v = variable_;
        int i=0;
        PartialOrderingResult sr=PartialOrderingResult.EQ;
        while(v.isComplexTerm() && v.getNameIndex().equals(TermWareSymbols.CONS_INDEX)) {
            if (x.getArity()<=i) {
                return PartialOrderingResult.NOT_COMPARABLE;
            }
            Term cx = x.getSubtermAt(i);
            Term cv = v.getSubtermAt(0);
            Term nv = v.getSubtermAt(1);
            if (nv.isNil()) {
                if (cv.isX()) {
                    return PartialOrderingResult.merge(sr,PartialOrderingResult.MORE);
                }
            }else{
                PartialOrderingResult r = v.getSubtermAt(0).concreteOrder(cx,s);
                sr=PartialOrderingResult.merge(sr,r);
                v=nv;
            }
            ++i;
        }
        return sr;
    }
    
    private Term  pattern_;    
    private Term  variable_;
    
    private static final long serialVersionUID = 20080112;
    
}
