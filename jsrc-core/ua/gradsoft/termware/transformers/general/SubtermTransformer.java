
package ua.gradsoft.termware.transformers.general;

import ua.gradsoft.termware.AbstractBuildinTransformer;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TransformationContext;
import ua.gradsoft.termware.annotations.TransformerDescription;
import ua.gradsoft.termware.annotations.TransformerName;

/**
 *
 * @author rssh
 */
/**
 *Subterm trnasformer
 */
@TransformerName("subterm")
@TransformerDescription(
  "subterm(t,i) - get i-th subterm of t is one exists, otherwise NIL"
  )
public class SubtermTransformer extends AbstractBuildinTransformer
{

    private SubtermTransformer()
    {}

    public Term transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        if (t.getArity()!=2) {
            return t;
        }else{
            Term ct = t.getSubtermAt(0);
            Term it = t.getSubtermAt(1);
            if (it.isNumber()) {
               ctx.setChanged(true);
               int i = it.getNumber().intValue();
               if (ct.getArity() >= i) {
                    return system.getInstance().getTermFactory().createNil();
               } else {
                    return ct.getSubtermAt(i);
               }
            }else{
                return t;
            }
        }
    }

    public static SubtermTransformer INSTANCE = new SubtermTransformer();

}
