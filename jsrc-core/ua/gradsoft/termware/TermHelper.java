/*
 * TermHelper.java
 * (C) Grad-Soft ltd, Kiev, Ukraine.
 */

package ua.gradsoft.termware;

import java.io.*;
import java.util.*;
import ua.gradsoft.termware.exceptions.AssertException;

import ua.gradsoft.termware.exceptions.RuntimeAssertException;
import ua.gradsoft.termware.util.TermCondition;

/**
 * Helper class for terms operations
 */
public  class TermHelper {
    
    /**
     *print <t> into string.
     *@return string representation of <code> t </code>
     */
    public static String termToString(Term t) 
    {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      PrintWriter pwr = new PrintWriter(out);
      t.print(pwr);
      pwr.flush();
      return out.toString();
    }
 
    /**
     * pretty print <code> t </code>
     */
    public static String termToPrettyString(Term t, String languageName, Term printerArgs) throws TermWareException
    {       
     ByteArrayOutputStream out = new ByteArrayOutputStream();
     IPrinterFactory ipf = TermWare.getInstance().getPrinterFactory(languageName);
     PrintWriter pwr = new PrintWriter(out);
     IPrinter printer = ipf.createPrinter(pwr, "internal", TermWare.getInstance().resolveSystem("sys"), printerArgs);
     printer.writeTerm(t);    
     printer.flush();    
     return out.toString();
    }

    public static String termToPrettyString(Term t) throws TermWareException
    {
        return termToPrettyString(t,"TermWare",NILTerm.getNILTerm());
    }

    
    
    public static int compareNameIndexes(final Object x, final Object y)
    {
        int retval;
        if (x instanceof Integer) {
            if (y instanceof Integer) {
                retval=((Integer)x).intValue() - ((Integer)y).intValue();
            }else{
                // let integer will be less than string.
                retval=-1;
            }
        }else if(x instanceof String) {
            if (y instanceof String) {
                retval=((String)x).compareTo((String)y);
            }else{
                retval=1;
            }
        }else{
            throw new RuntimeAssertException("incorrect type for name index");
        }        
        return retval;
    }
    
 /**
  * set attribute to term.  If t is not attributed - attribute one. (i. e. create
  * instance of AttributedTerm which wrap original term).
  *@return attributed term.
  **/
 public static Term  setAttribute(Term t, String name, Term attribute) /* throws TermWareException*/
 {
  try {
    if ( t instanceof Attributed ) {
      Attributed it=(Attributed)t;
      it.setAttribute(name, attribute);
      return t;
    }else{
      Term t1=t.getTerm();
      if (t1 instanceof Attributed) {
          Attributed it=(Attributed)t1;
          it.setAttribute(name, attribute);
          return t1;
      }else{
          AttributedTerm it=new AttributedTerm(t1);
          it.setAttribute(name, attribute);
          return it;
      }
    }
  }catch(TermWareException ex){
      throw new TermWareRuntimeException(ex);
  }
 }

  /**
   *set string attribute
  *@see TermHelper#setAttribute
  */
 public static Term  setAttribute(Term t, String name, String attribute) /*throws TermWareException*/
 {
   return setAttribute(t,name,TermFactory.createString(attribute));
 }

 /**
   *set int attribute
  *@see TermHelper#setAttribute
  */
 public static Term  setAttribute(Term t, String name, int attribute) 
 {
   return setAttribute(t,name,TermFactory.createInt(attribute));
 }
 
 
  /**
  * get attribute <code> name </code> of term <code> t </code>
  *@param t term
  *@param name  name of attribute to get.
  *@return attribute <code> name </code> of term <code> t </code> or NIL, if attribute is 
  * not set or term is not attributed.
  *@see AttributedTerm
  **/
 public static Term getAttribute(Term t, String name) 
 {
   if ( t instanceof Attributed ) {
       return ((Attributed)t).getAttribute(name);
   }else{
       Term t1=t.getTerm();
       if (t1 instanceof Attributed) {
           return ((Attributed)t).getAttribute(name);
       }else{
           return TermFactory.createNil();
       }
   }
 }
 
 /**
  *copy attributes from <code> src </code> term to <code> dst </code> term
  */
 public static Term copyAttributes(Term dst,Term src) 
 {     
     if (! (src instanceof Attributed) ) {
         return dst;
     }
     Term retval = dst;
     Map<String,Term> attributes=((Attributed)src).getAttributes();
     for(Map.Entry<String,Term> me: attributes.entrySet()) {
         retval = TermHelper.setAttribute(retval,me.getKey(),me.getValue());
     }
     return (Term)retval;
 }

  /**
  * return true, if term is attributed.
  *   (i. e. setAttribute(t,x,u)==t. )
  **/
 public static boolean isAttributed(Term t)
 {
  return (t instanceof Attributed) || (t.getTerm() instanceof Attributed) ;
 }
 
 /**
  * attribute tree: i.e. set attribute to each subterm, which satisficy condition.
  */
 public static Term attributeTree(Term tree, String attrName, Term attribute, TermCondition condition) throws TermWareException
 {
   Term retval=tree;  
   if (condition==null || condition.check(tree)) {
     retval=setAttribute(tree,attrName, attribute);
   }
   for(int i=0; i<tree.getArity(); ++i) {
       retval.setSubtermAt(i, attributeTree(retval.getSubtermAt(i), attrName, attribute, condition));
   }
   return retval;
 }


 
  /**
  * change $x entites to _x(minFv($x)).
  *@param t - term to change. (can be changed during processing).
  *
  *@return substituted term.
  * 
  **/
 public static Term escapeX(Term t) throws TermWareException
 {
  if (t.isComplexTerm()) {
     for(int i=0; i<t.getArity(); ++i) {
        Term current=t.getSubtermAt(i);
        if (current.isComplexTerm()||current.isX()) {
          t.setSubtermAt(i,escapeX(current));
        }
     }
     return t;
  }else if (t.isX()) {
    return TermWare.getInstance().getTermFactory().createTerm("_x",
                   TermWare.getInstance().getTermFactory().createInt(t.minFv()));
  }
  return t;
 }

 /**
  * change _x(i) entites to $i
  *@param t - term to change. (can be changed during processing).
  *
  *@return substituted term.
  * 
  **/
 public static Term unescapeX(Term t) throws TermWareException
 {
  if (t.isComplexTerm()) {
     if (t.getName().equals("_x") && t.getArity()==1) {        
       return TermWare.getInstance().getTermFactory().createX(t.getSubtermAt(0).getInt());
     }else{
       for(int i=0; i<t.getArity(); ++i) {
         Term current=t.getSubtermAt(i);
         if (current.isComplexTerm()) {
            t.setSubtermAt(i,unescapeX(current));
         }
       }
     }
  }
  return t;
 }

 
 /**
  * transform string in form (x/y/z) to form _name(x,y,z);
  **/
 public static Term stringToName(String name) throws TermWareException
 {
  String[] names=name.split("/");
  ArrayList<String> arNames=new ArrayList<String>();
  int i;
  for(i=0; i<names.length; ++i) {
      if (names[i].length()!=0) {
          arNames.add(names[i]);
      }
  }
  Term[] nameBody=new Term[arNames.size()];
  for(i=0;i<nameBody.length;++i){
      nameBody[i]=TermWare.getInstance().getTermFactory().createAtom((String)arNames.get(i));
  }
  return TermWare.getInstance().getTermFactory().createComplexTerm("_name", nameBody);
 }
 
 /**
  * reverse list
  */
 public static Term reverseList(TermWareInstance instance, Term t) throws TermWareException
 {
    Term retval=instance.getTermFactory().createNIL();
    while(!t.isNil() && t.getNameIndex().equals(TermWareSymbols.CONS_INDEX)) {
        if (t.getArity()==2) {
            retval=instance.getTermFactory().createTerm(TermWareSymbols.CONS_STRING,t.getSubtermAt(0),retval);
        }else{
            throw new AssertException("arity of cons must be 2");
        }
        t=t.getSubtermAt(1);
    }
    if (!t.isNil()) {
        throw new AssertException("argument to reverseList must be cons term");
    }
    return retval;
 }
 
 
 
}
