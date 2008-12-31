package ua.gradsoft.termware.transformers.sys;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002
 * $Id: SetPropertyTransformer.java,v 1.2 2007-07-13 20:50:21 rssh Exp $
 */

import java.util.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.util.*;
import ua.gradsoft.termware.exceptions.*;

                           
/**
 *Transformer for setProperty.
 * setProperty(name,t) - reduced to true with effect of setting term system property
 *                       'name' to 't'
 *<br>
 * (for now exists one system property: 'debug')
 **/
public class SetPropertyTransformer extends AbstractBuildinTransformer
{

    /**
     *@return false
     */
    public boolean internalsAtFirst() {
        return false;
    }
    

 public  Term  transform(Term t, TermSystem sys, TransformationContext ctx) throws TermWareException
  { return static_transform(t,sys,ctx); }


 static public  Term  static_transform(Term t, TermSystem sys, TransformationContext ctx) throws TermWareException
 {
  if (!t.getName().equals("setProperty")) return t;
  if (t.getArity()!=2) {
     throw new AssertException("setProperty must have arity 2");
  }  
  if (t.getSubtermAt(0).getName().equals("debug")) {
     if (t.getSubtermAt(1).isBoolean()) {
        sys.setLoggingMode(t.getSubtermAt(1).getBoolean());
        sys.setLoggedEntity("All");
     }else{
        throw new AssertException("value of debug property must be boolean");
     }
  }else{
     throw new AssertException("unknown property of term system:"+t.getName());
  }
  ctx.setChanged(true);
  return TermFactory.createBoolean(true);
 }

 
 /**
  * return description of this transformer.
  */
 public String getDescription() {
     return staticDescription_;
 }
 
 /**
  * return name of this transformer
  */
 public String getName() {
     return "setProperty";
 }
 
 
 private final static String staticDescription_=
  " setProperty(name,t) - reduced to true with effect of setting system property " +
  "                       'name' to 't' "
  ;


 
}
 