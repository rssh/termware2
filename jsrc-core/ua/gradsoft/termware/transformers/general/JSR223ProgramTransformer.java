/*
 * JSR223ProgramTransformer.java
 *
 * Created on July 19, 2007, 3:40 PM
 *
 */
package ua.gradsoft.termware.transformers.general;

import ua.gradsoft.termware.AbstractBuildinTransformer;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermFactory;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareSymbols;
import ua.gradsoft.termware.TransformationContext;
import ua.gradsoft.termware.annotations.TransformerDescription;
import ua.gradsoft.termware.annotations.TransformerName;

/**
 *Transformer for sequential processing for list of subterms.
 * @author rssh
 */
@TransformerName("JSR223Program")
@TransformerDescription(
  "sequentally reduce all subterms and always return true."
)
public class JSR223ProgramTransformer extends AbstractBuildinTransformer
{
    
    /**
     * Creates a new instance of JSR223ProgramTransformer
     */
    private JSR223ProgramTransformer() {
    }
    
    public static final JSR223ProgramTransformer INSTANCE = new JSR223ProgramTransformer();
    
     public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
  { return static_transform(t,system,ctx); }


static public  Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
 {
  Term args = t.getSubtermAt(0);
  if (args.isComplexTerm() && args.getNameIndex().equals(TermWareSymbols.CONS_INDEX)) {
      while(!args.isNil()) {
          Term ct = args.getSubtermAt(0);
          args = args.getSubtermAt(1);
          ct=system.reduce(ct,ctx);
      }
  }else{
      args = system.reduce(args,ctx);
  }
  ctx.setChanged(true);
  return TermFactory.createBoolean(true);
 }


    
}

