package ua.gradsoft.termware.transformers.general;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;
import ua.gradsoft.termware.util.LogHelper;
                          
/**
 * logical_not(x) = ! x 
 * <pre> 
 *   logical_not(boolean)  -> logical inversion of argument.
 * </pre>
 **/
public class LogicalNotTransformer extends AbstractBuildinTransformer
{

    
 public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
  { return static_transform(t,system,ctx); }


 static public  Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
 {
  LogHelper.log(system,LogicalNotTransformer.class,"t=", t);
  if (t.getArity()!=1) {
      throw new AssertException("Logical not must have arity 1");
  }
  if (t.getSubtermAt(0).isBoolean()) {
      ctx.setChanged(true);
      t = system.getInstance().getTermFactory().createBoolean( ! t.getSubtermAt(0).getBoolean()  );
  }
  LogHelper.log(system,LogicalNotTransformer.class,"return ",t);
  return t;
}
  
 public String getDescription() {
     return STATIC_DESCRIPTION_; 
 }
 
 public String getName() {
     return "logical_not";
 }
 
 private final static String STATIC_DESCRIPTION_ = 
  "logical_not(x) = ! x  \n"+
  " <pre> \n"+
  "  logical_not(boolean)  -> logical inversion of argument. \n"+
  " </pre> "
  ;

    public boolean internalsAtFirst() {
        return false;
    }
 
}
 