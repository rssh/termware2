/*
 * String_SubstTransformer.java
 *
 * Created on  10, 02, 2004, 7:05
 */

package ua.gradsoft.termware.transformers.string;

import java.util.*;
import java.util.regex.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;



/**
 *Transformer for String.subst
 *<ul>
 * <li> code  string::subst(s,x,y) replace all occurences of regular exptression x to y in s and return changed string.
 * <li> String.subst("a","a","b")="b"
 * <li> String.subst("qqsd000sd","sd",a)="qqa000a"
 * <li> String.subst("qqsd000sd","q*sd","a")="a000sd"
 *</ul>
 * @author  Ruslan Shevchenko
 */
public final class StringSubstTransformer extends AbstractBuildinTransformer {
    
    
    public String getDescription() {
      return "String.subst(s,x,y) replace all occurences, which match regular expression <code> x </code> to <code> y </code> in <code> s </code> and return changed string.";
    }
    
    public String getName() {
        return "subst";
    }
    
    public Term transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        return static_transform(t,system,ctx);
    }
    
    public static Term static_transform(Term t, TermSystem system,  TransformationContext ctx) throws TermWareException 
    {
      if (system.isLoggingMode()) {
        if (system.checkLoggedEntity("All")||system.checkLoggedEntity("String")) {
          system.getEnv().getLog().print(StringSubstTransformer.class.getName());
          system.getEnv().getLog().print(": apply String.subst, term=");
          t.print(system.getEnv().getLog());
          system.getEnv().getLog().println();
        }
      }
        
      if (t.getArity()!=3 || 
          !t.getSubtermAt(0).isString()||
          !t.getSubtermAt(1).isString()|| 
          !t.getSubtermAt(2).isString()) {
            if (system.isLoggingMode()) {
              if (system.checkLoggedEntity("All")||system.checkLoggedEntity("String")) {
                  system.getEnv().getLog().print(StringSubstTransformer.class.getName());
                  system.getEnv().getLog().print(": failed");
              }
            }
            return t;
      }
      
      Pattern re=Pattern.compile(t.getSubtermAt(1).getString());
      Matcher m=re.matcher(t.getSubtermAt(0).getString());
      t=system.getInstance().getTermFactory().createString(m.replaceAll(t.getSubtermAt(2).getString()));
      ctx.setChanged(true);

      if (system.isLoggingMode()) {
        if (system.checkLoggedEntity("All")||system.checkLoggedEntity("String")) {
           system.getEnv().getLog().print(StringSubstTransformer.class.getName());
           system.getEnv().getLog().print(": result=");
           t.print(system.getEnv().getLog());
           system.getEnv().getLog().println();
        }
      }
      
      return t;
    }

    public boolean internalsAtFirst() {
        return false;
    }

    
    
}
