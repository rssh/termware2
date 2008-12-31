/*
 * AbstractSetTerm.java
 *
 */

package ua.gradsoft.termware.set;

import java.util.Arrays;
import ua.gradsoft.termware.AbstractComplexTerm;
import ua.gradsoft.termware.AttributedTerm;
import ua.gradsoft.termware.PartialOrderingResult;
import ua.gradsoft.termware.PrimaryTypes;
import ua.gradsoft.termware.Substitution;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareRuntimeException;
import ua.gradsoft.termware.TermWareSymbols;
import ua.gradsoft.termware.exceptions.MatchingFailure;
import ua.gradsoft.termware.exceptions.SubtermNotFoundException;



/**
 * set term, - subterms are elements of set, ordered in our natural order
 */
public abstract class AbstractSetTerm extends AbstractComplexTerm {


 public abstract Term createSame(Term[] newBody) throws TermWareException;

 public final String   getName()  
   { return TermWareSymbols.SET_STRING; }
 
 public final Object   getNameIndex()
 {  return TermWareSymbols.SET_INDEX; }

 public abstract int      getArity();
   
 public abstract Term    getSubtermAt(int i);

 public abstract void   setSubtermAt(int i, Term t) throws TermWareException;

  
 /**
  * apply substitution <code> s </code> to current term.
  *@return true, if substitution change term, otherwise false
  **/
 public abstract boolean    substInside(Substitution s) throws TermWareException;


 public Term termClone()  throws TermWareException
 {
  SetOfTerms newSubterms = new SetOfTerms();
  for(int i=0; i<getArity(); ++i) {
     newSubterms.addAlreadySorted(getSubtermAt(i).termClone());
  }
  return new SetTerm(newSubterms,emptyFv_,false);
 }
 
//
//  RR primitives
//
 
 public int   findSubtermBoundEqualsTo(Term t) throws TermWareException
 {
   return index(t);  
 }

 /**
  * return first index, which matched term t or throw MatchingFailure.
  **/
 public  int   findBoundUnifyIndex(Term t, Substitution s) throws TermWareException
 {
  if (getArity()==0) throw new MatchingFailure(this,t);
  if (t.isX()) return 0;
  if (PrimaryTypes.isPrimitive(t.getPrimaryType0())) {
    try {
     return index(t);
    }catch(SubtermNotFoundException ex){
     throw new MatchingFailure(this,t);
    }
  }
  for(int i=0; i<getArity(); ++i){
    Substitution s1=new Substitution();
    if (getSubtermAt(i).boundUnify(t,s1)) {
       s.merge(s1);
       return i;
    }
  }
  throw new MatchingFailure(this,t);
 }

 public PartialOrderingResult concreteOrder(Term x, Substitution s) throws TermWareException
 {
     if (x.isX()) {
         Term t = s.get(x.minFv());
         if (t!=null) {
             return concreteOrder(t,s);
         }else{
            return PartialOrderingResult.LESS;
         }
     }else if (x.getNameIndex().equals(TermWareSymbols.SET_INDEX)) {         
         if (x.getArity()!=this.getArity()) {
             return PartialOrderingResult.NOT_COMPARABLE;
         }else{
             int arity = getArity();
             PartialOrderingResult[][] matrix=new PartialOrderingResult[arity][];    
             Substitution[][] smatrix = new Substitution[arity][];
             for(int i=0; i<arity; ++i){
                 matrix[i]=new PartialOrderingResult[arity];
                 smatrix[i]=new Substitution[arity];                 
             }
             // check all possible substitutions.
             for(int i=0; i<arity; ++i) {
                 Term ci=this.getSubtermAt(i);
                 for(int j=0; j<arity; ++j) {
                     Term cj=x.getSubtermAt(j);         
                     smatrix[i][j]=new Substitution();
                     smatrix[i][j].merge(s);
                     matrix[i][j]=ci.concreteOrder(cj,smatrix[i][j]);
                 }
             }
             // now try to find monotone path in matrix;
             return findConcreteOrderPath(matrix,smatrix,s);
         }
     }else if (x.getNameIndex().equals(TermWareSymbols.SET_PATTERN_INDEX)) {
         Term xfrs = x.getSubtermAt(0);
         Term xsnd = x.getSubtermAt(1);
         if (xfrs.isX()) {             
             if (xsnd.isX()) {
                 if (this.getArity() > 0) {
                   s.put(xfrs,this.getSubtermAt(0));
                   s.put(xsnd,createWithBodyExclude(this,0));
                   return PartialOrderingResult.LESS;
                 }else{
                   return PartialOrderingResult.NOT_COMPARABLE;  
                 }
             }else if (xsnd.isNil()) {
                 if (this.getArity()==1) {
                     s.put(xfrs,this.getSubtermAt(0));
                     return PartialOrderingResult.LESS;
                 }else{
                     return PartialOrderingResult.NOT_COMPARABLE;
                 }
             }else{                
                 if (this.getArity() > 0) {
                   s.put(xfrs,this.getSubtermAt(0));
                   return new SetTermWithoutElement(this,0).concreteOrder(xsnd,s);
                 }else{
                   // this is empty set. x is pattern: not comparable.  
                   return PartialOrderingResult.NOT_COMPARABLE;  
                 }                 
             }
         }else{
             try {              
                 int f = findBoundUnifyIndex(xfrs, s);
                 return new SetTermWithoutElement(this,f).concreteOrder(xsnd,s);
             }catch(MatchingFailure ex){
                 return PartialOrderingResult.NOT_COMPARABLE;
             }catch(TermWareException ex){
                 throw new TermWareRuntimeException(ex);
             }
         }
     }else{
         return PartialOrderingResult.NOT_COMPARABLE;
     }             
 }
  
 
 
 public  abstract void  insert(Term t)  throws TermWareException;
 
 public abstract boolean isEmpty();
 
 public abstract int index(Term t) throws TermWareException;
 
 /**
  * level of indirection - abstract set term can be setTerm or chain of
  *SetTermWithoutElmenent
  */
 public abstract int indirectionLevel();
 
 private static PartialOrderingResult findConcreteOrderPath(PartialOrderingResult[][] matrix, Substitution[][] smatrix, Substitution sSubst) throws TermWareException
 {
     boolean[] excludedRows = new boolean[matrix.length];
     Arrays.fill(excludedRows,false);
     boolean[] excludedColumns = new boolean[matrix.length];
     Arrays.fill(excludedColumns,false);
     PartialOrderingResult hipotese = PartialOrderingResult.EQ;
     return findConcreteOrderPath(matrix,smatrix,excludedRows,excludedColumns,hipotese,sSubst);
 }
 
 private static PartialOrderingResult findConcreteOrderPath(PartialOrderingResult[][] matrix, Substitution[][] smatrix,boolean[] excludedRows, boolean[] excludedColumns, PartialOrderingResult hipotese, Substitution sSubst) throws TermWareException
 {
     Substitution s = null;
     for(int i=0; i<matrix.length; ++i) {
         if (excludedRows[i]) {
             continue;
         }
         boolean wasFailure=false;
         excludedRows[i]=true;         
         for(int j=0; j<matrix.length; ++j) {
             if (excludedColumns[j]) {
                 continue;
             }            
             PartialOrderingResult ijres=null;             
             excludedColumns[j]=true;             
             switch(matrix[i][j]) {
                 case LESS:
                     switch(hipotese) {
                         case LESS:
                         case EQ:                            
                             s = new Substitution();
                             s.merge(sSubst);
                             s.merge(smatrix[i][j]);
                             ijres=findConcreteOrderPath(matrix,smatrix,excludedRows,excludedColumns,PartialOrderingResult.LESS,s);
                             break;
                         default:
                             ijres=PartialOrderingResult.NOT_COMPARABLE;
                             break;
                     }
                     break;
                 case EQ:
                     s=new Substitution();       
                     s.merge(sSubst);
                     s.merge(smatrix[i][j]);                     
                     ijres=findConcreteOrderPath(matrix,smatrix,excludedRows,excludedColumns,hipotese,s);
                     break;
                 case MORE:
                     switch(hipotese) {
                         case EQ:
                         case MORE:                             
                             s = new Substitution();
                             s.merge(sSubst);
                             s.merge(smatrix[i][j]);                                                  
                             ijres=findConcreteOrderPath(matrix,smatrix,excludedRows,excludedColumns,PartialOrderingResult.MORE,s);                         
                             break;
                         default:
                             ijres=PartialOrderingResult.NOT_COMPARABLE;
                             break;
                     }
                 default:
                     ijres=PartialOrderingResult.NOT_COMPARABLE;     
                     break;
             }
             if (ijres==PartialOrderingResult.NOT_COMPARABLE) {
                 excludedColumns[j]=false;
                 wasFailure=true;
             }else{
                 return ijres;
             }
         }
         if (wasFailure) {
             return PartialOrderingResult.NOT_COMPARABLE;
         }
     }
     if (s!=null) {
       sSubst.merge(s);
     }
     return hipotese;
 }
 
 final static int MAX_INDIRECTION_LEVEL_=3;
 
 /**
  * create SetTerm, same of excluded index.
  *(needed for internal work in package)
  */
 static AbstractSetTerm createWithBodyExclude(Term x, int excludedIndex)
 {
    Term xx = x.getTerm(); 
    if (xx instanceof AttributedTerm) {
        AttributedTerm axx = (AttributedTerm)xx;
        return createWithBodyExclude(axx.getTerm(),excludedIndex);
    }else if (xx instanceof AbstractSetTerm) {
        return new SetTermWithoutElement((AbstractSetTerm)xx,excludedIndex);
    }else{
        Term[] newBody = new Term[x.getArity()];
        for(int i=0; i<x.getArity(); ++i) {
            newBody[i]=x.getSubtermAt(i);
        }
        return createWithBodyExclude(new SetTerm(newBody),excludedIndex);
    }
 }
 
 
 
}

