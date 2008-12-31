/*
 * String_SplitTransformer.java
 *
 * Created 10, 02, 2004, 7:59
 */

package ua.gradsoft.termware.transformers.string;


import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;


/**
 *Transformer for string.split
 * string.split(s,re) - split string <code> s </code> by regular expression <code> re </code>
 */
public final class StringSplitTransformer extends AbstractBuildinTransformer {
    
    
    public String getDescription() {
        return "string.split(s,re) - split string <code> s </code> by regular expression <code> re </code>";
    }
    
    public String getName() {
        return "split";
    }
    
    public Term transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        return static_transform(t,system,ctx);
    }
    
    public Term static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException
    {
       if (t.getArity()!=2) return t;
       Term frs=t.getSubtermAt(0);
       if (!frs.isString()) return t;
       Term snd=t.getSubtermAt(1);
       if (!snd.isString()) return t;
       String[] sa=frs.getString().split(snd.getString());
       t=system.getInstance().getTermFactory().createNil();
       for(int i=0; i<sa.length; ++i) {
           t=system.getInstance().getTermFactory().createTerm("cons",system.getInstance().getTermFactory().createString(sa[sa.length-i-1]),t);
       }
       ctx.setChanged(true);
       return t;
    }

    public boolean internalsAtFirst() {
        return false;
    }

    
}
