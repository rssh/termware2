/*
 * String_ParseTransformer.java
 *
 * Created 10, 02, 2004, 10:46
 */

package ua.gradsoft.termware.transformers.string;


import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;


/**
 * Transformer for string::parse
 * string.parse("x+y")=x+y
 * when parsing is not possible - throw exception.
 */
public final class StringParseTransformer extends AbstractBuildinTransformer {
    
    
    public String getDescription() {
        return "parse own argument as term";
    }
    
    public String getName() {
        return "parse";
    }
    
    public Term transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
      return static_transform(t,system,ctx);
    }
    
    public static Term static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException 
    {
      if (t.getArity()!=1) {
          return t;
      }
      Term frs=t.getSubtermAt(0);
      if (!frs.isString()) return t;
      try {
          Term retval=system.getInstance().getTermFactory().createParsedTerm(frs.getString());
          ctx.setChanged(true);
          return retval;
      }catch(TermWareException ex){
          // TODO: log ?
          throw ex;
      }
    }

    /**
     *@return false
     */
    public boolean internalsAtFirst() {
        return false;
    }

    
 }
