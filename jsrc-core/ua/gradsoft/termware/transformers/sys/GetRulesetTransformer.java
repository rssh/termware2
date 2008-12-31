package ua.gradsoft.termware.transformers.sys;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002
 * $Id: GetRulesetTransformer.java,v 1.4 2007-01-26 19:27:19 rssh Exp $
 */

import java.util.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.set.*;
import ua.gradsoft.termware.util.*;
import ua.gradsoft.termware.exceptions.*;


/**
 * getRuleset(x)  -  return ruleset of system with name 'x'
 **/
public class GetRulesetTransformer extends AbstractBuildinTransformer {
    
    /**
     *@return true
     */
    public boolean internalsAtFirst() {
        return true;
    }
    
    
    
    /**
     *@see GetRulesetTransformer#static_transform
     **/
    public  Term  transform(Term t, TermSystem sys,  TransformationContext ctx) throws TermWareException {
        return static_transform(t,sys,ctx); }
    
    
    /**
     * transform <code> getRuleset(t) </code> to set-term with all rules from <code> t </code>
     **/
    static public  Term  static_transform(Term t, TermSystem sys,  TransformationContext ctx) throws TermWareException {
        if (!t.getName().equals("getRuleset")) return t;
        if (t.getArity()!=1) {
            throw new AssertException("getRuleset must have arity 1");
        }
        TermSystem argSystem=sys.getInstance().resolveSystem(t.getSubtermAt(0));
        
        SetTerm result=(SetTerm)sys.getInstance().getTermFactory().createSetTerm();
        
        TransformersStar star=argSystem.getStrategy().getStar();
        Iterator it=star.iterator();
        
        while(it.hasNext()) {
            ITermTransformer current=(ITermTransformer)it.next();
            if (current instanceof AbstractRuleTransformer) {
                result.insert(((AbstractRuleTransformer)current).getTerm());
            }else{
                // Hmm, interesting question: may be mot insert buildin transformers ?
                //result.insert(new JTerm(current));
            }
            
        }
        
        ctx.setChanged(true);
        
        return result;
    }
    
    
    public String getDescription() {
        return
                "getRuleset(x)  -  return ruleset of system with name 'x'";
    }
    
    public String getName() {
        return "getRuleset";
    }
    
    
    
}
