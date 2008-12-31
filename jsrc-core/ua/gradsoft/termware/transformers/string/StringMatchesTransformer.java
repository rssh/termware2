/*
 * StringMatchesTransformer.java
 *
 */

package ua.gradsoft.termware.transformers.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ua.gradsoft.termware.AbstractBuildinTransformer;
import ua.gradsoft.termware.BooleanTerm;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TransformationContext;
import ua.gradsoft.termware.annotations.TransformerDescription;
import ua.gradsoft.termware.annotations.TransformerName;
import ua.gradsoft.termware.util.LogHelper;

/**
 *
 * @author rssh
 */
@TransformerName("matches")
@TransformerDescription(
 "matches(s,regexpr) -- true if string <code> s </code> matches <code> regexpr </code>"+
 "matches(s,regexpr,$x1,..$xn) -- try to match <code> s </code> with <code> regexpr </code> and \n"+
 "   set in current substitution $x1...$xn to appropriative input subsequences of regular expressions.\n"+
 "   Note, that $x1...$xn must be a free variables and number of variables must be the same or less then\n"+
 "   number of subsequebces in regular expressions."        
            )
public class StringMatchesTransformer extends AbstractBuildinTransformer
{
    
    public static final StringMatchesTransformer INSTANCE = new StringMatchesTransformer();
    
    private StringMatchesTransformer() {}
    
    public Term transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        return static_transform(t,system,ctx);
    }
    
    
    public static Term static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException 
    {
      if (t.getArity()==2) {
          Term st = t.getSubtermAt(0);
          Term rt = t.getSubtermAt(1);
          if (st.isString() && rt.isString()) {
              boolean retval = st.getString().matches(rt.getString());
              ctx.setChanged(true);
              return BooleanTerm.getBooleanTerm(retval);
          }else{
              // must be strings.
              return t;
          }
      }else if (t.getArity() > 2){
          Term st = t.getSubtermAt(0);
          Term rt = t.getSubtermAt(1);         
          for(int i=2; i<t.getArity(); ++i) {
              if (!t.getSubtermAt(i).isX()) {
                  return t;
              }
          }        
          Pattern p = Pattern.compile(rt.getString());
          Matcher matcher = p.matcher(st.getString());
          boolean retval = matcher.matches();
          if (!retval) {
              ctx.setChanged(true);
              return BooleanTerm.getBooleanTerm(false);
          }else{
              int nGroups = matcher.groupCount();
              if (nGroups < t.getArity()-2) {
                  for(int i=0; i<nGroups; ++i) {
                      Term group = system.getInstance().getTermFactory().createString(matcher.group(i+1));
                      Term var = t.getSubtermAt(i+2);                      
                      ctx.getCurrentSubstitution().put(var,group);
                  }
              }else{
                  for(int i=2; i<t.getArity(); ++i){
                      Term group=system.getInstance().getTermFactory().createString(matcher.group(i-1));
                      Term var = t.getSubtermAt(i);
                      ctx.getCurrentSubstitution().put(var,group);
                  }
              }
              ctx.setChanged(true);
              if (system.isLoggingMode()) {                  
                  LogHelper.log(system,StringMatchesTransformer.class,"matches, substitution=",ctx.getCurrentSubstitution());
              }
              return BooleanTerm.getBooleanTerm(true);
          }          
      }else{
          return t;
      }
    }
    
    
    
}
