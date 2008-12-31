package ua.gradsoft.termware.util;


import ua.gradsoft.termware.*;
import ua.gradsoft.termware.debug.DebugStubRunHelper;
import ua.gradsoft.termware.debug.SetFactsDebugStub;
import ua.gradsoft.termware.debug.SubstitutionDebugStub;
import ua.gradsoft.termware.debug.UnificationDebugStub;
import ua.gradsoft.termware.exceptions.*;
import ua.gradsoft.termware.transformers.general.LetTransformer;


/**
 * x->y
 */
public class RuleTransformer extends AbstractRuleTransformer {
    
    
    /**
     * create rule transformer with term <code> t </code>
     */
    public RuleTransformer(Term rule) throws TermWareException {
        if (!rule.getName().equals("rule"))
            throw new AssertException("RuleTransformer constructor argument must be rule");
        inPattern_=rule.getSubtermAt(0);
        if (isWherePattern(inPattern_)) {
            inWhere_=inPattern_;
            inPattern_=inPattern_.getSubtermAt(0);
        }else{
            inWhere_=null;
        }
        if (isLetPattern(inPattern_)) {
            inLet_=inPattern_;
            inPattern_=inPattern_.getSubtermAt(1);
        }
        outPattern_=rule.getSubtermAt(1);
        if (TermHelper.isAttributed(rule)) {
            Term nameAttribute = TermHelper.getAttribute(rule,"name");
            if (!nameAttribute.isNil()) {
                name_=TermHelper.termToString(nameAttribute);
            }else{
                name_=inPattern_.getName();
            }
            Term descriptionAttribute = TermHelper.getAttribute(rule,"description");
            if (!descriptionAttribute.isNil()) {
                description_=TermHelper.termToString(descriptionAttribute);
            }else{
                description_="";
            }
        }else{
            name_=inPattern_.getName();
            description_="";
        }
    }
    
    public Term transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        return non_static_transform(inPattern_,outPattern_,t,system,ctx);
    }
    
    private Term non_static_transform(Term inPattern, Term outPattern, Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        
        if (system.isLoggingMode()) {
            if (system.checkLoggedEntity("All")||system.checkLoggedEntity("Rules")||system.checkLoggedEntity(RuleTransformer.class.getName())) {
                system.getEnv().getLog().print(RuleTransformer.class.getName());
                system.getEnv().getLog().print(": apply rule, term=");
                t.print(system.getEnv().getLog());
                system.getEnv().getLog().print("\nrule=[");
                inPattern.print(system.getEnv().getLog());
                system.getEnv().getLog().print("->");
                outPattern.print(system.getEnv().getLog());
                system.getEnv().getLog().print("]\n");
            }
        }
        
        boolean x=false;
        try {
            if (TermWare.isInDebug()) {
                Class<? extends UnificationDebugStub> stubClass = getUnificationDebugStub(system);
                x=DebugStubRunHelper.runUnificationDebugStub(stubClass,t,inPattern,ctx.newCurrentSubstitution());
            }else{
                x=inPattern.freeUnify(t,ctx.newCurrentSubstitution());
            }
        }catch(MatchingFailure ex){
            /* ignore */
            ; // make JECheck happy
        }
        
        if (x) {
            if (system.isLoggingMode()) {
                LogHelper.log(system,"Rules",RuleTransformer.class,"substitution=",ctx.getCurrentSubstitution());
            }
            ctx.setChanged(true);
            Substitution unifySubstitution = ctx.getCurrentSubstitution();
            if (TermWare.isInDebug()) {
                 Class<? extends SubstitutionDebugStub> stubClass = getSubstitutionDebugStub(system);
                 t=DebugStubRunHelper.runSubstitutionDebugStub(stubClass,outPattern,unifySubstitution);               
            }else{
                t=outPattern.subst(unifySubstitution);
            }
            if (inWhere_!=null) {
                t=LetTransformer.static_transform_letexpr(inWhere_.getSubtermAt(1).subst(unifySubstitution),t,system,ctx);
            }
            if (inLet_!=null) {
                t=LetTransformer.static_transform_letexpr(inLet_.getSubtermAt(0).subst(unifySubstitution),t,system,ctx);
            }            
            if (isWherePattern(t)) {
                t=LetTransformer.static_transform_letexpr(t.getSubtermAt(1),t.getSubtermAt(0),system,ctx);
                
            }
            if (isLetPattern(t)) {
                t=LetTransformer.static_transform_letexpr(t.getSubtermAt(0),t.getSubtermAt(1),system,ctx);
            }
            if (isActionPattern(t)) {
                Term ft=t.getSubtermAt(1);
                //Substitution prevS=ctx.swapCurrentSubstitution(new Substitution());
                ctx.newCurrentSubstitution();
                if (TermWare.isInDebug()) {
                    Class<? extends SetFactsDebugStub> stubClass = getSetFactsDebugStub(ft,system);
                    DebugStubRunHelper.runSetFactsDebugStub(stubClass,system,ft,ctx);
                }else{
                   system.setFact(ft,ctx);
                }
                if (!ctx.getCurrentSubstitution().isEmpty()) {
                    if (system.isLoggingMode()) {
                        LogHelper.log(system,"Rules",RuleTransformer.class,"facts substitution=",ctx.getCurrentSubstitution());
                    }
                    if (TermWare.isInDebug()) {
                        Class<? extends SubstitutionDebugStub> stubClass = getFactsSubstitutionDebugStub(ft,system);
                        DebugStubRunHelper.runSubstitutionDebugStub(stubClass,t.getSubtermAt(0),ctx.getCurrentSubstitution());
                    }else{
                        t=t.getSubtermAt(0).subst(ctx.getCurrentSubstitution());
                    }
                }else{
                    if (system.isLoggingMode()) {
                        LogHelper.log(system,"Rules",RuleTransformer.class,"facts substitution is empty");
                    }
                    t=t.getSubtermAt(0);
                }
            }
            
            if (system.isLoggingMode()) {
                LogHelper.log(system,"Rules",RuleTransformer.class,"result=",t);
            }
            
        }else{
            
            if (system.isLoggingMode()) {
                LogHelper.log(system,"Rules",RuleTransformer.class,"substitution failed");
            }
            
        }
        
        return t;
    }
    
    
    public final Term getInPattern() {
        return inPattern_; 
    }
    
    public final Term getOutPattern() {
        return outPattern_; }
    
    public String getDescription() {
        return description_;
    }
    
    public String getName() {
        return name_;
    }
    
    public void setDescription(String description) {
        description_=description;
    }
    
    public void setName(String name) {
        name_=name;
    }
    
    public String getSource() {
        if (source_==null) {
            try {
                source_ = TermHelper.termToPrettyString(TermWare.getInstance().getTermFactory().createTerm("rule", inPattern_, outPattern_),
                        "TermWare",
                        TermWare.getInstance().getTermFactory().createNIL()
                        );
            }catch(TermWareException ex){
                source_ = ex.getMessage();
            }
        }
        return source_;
    }
    
    public Term getTerm() throws TermWareException {
        return TermWare.getInstance().getTermFactory().createTerm("rule",inPattern_,outPattern_);
    }
    
    
    private Class<? extends UnificationDebugStub> getUnificationDebugStub(TermSystem ts) throws TermWareException {
        if (unificationDebugStubClass_==null) {
            unificationDebugStubClass_=ts.getInstance().getDebugStubGenerator().generateUnificationDebugStub(inPattern_);
        }
        return unificationDebugStubClass_;
    }
    
    private Class<? extends SubstitutionDebugStub> getSubstitutionDebugStub(TermSystem ts) throws TermWareException
    {
        if (substitutionDebugStub_==null) {
            substitutionDebugStub_=ts.getInstance().getDebugStubGenerator().generateSubstitutionDebugStub(outPattern_);
        }
        return substitutionDebugStub_;
    }
    
    private Class<? extends SetFactsDebugStub> getSetFactsDebugStub(Term action, TermSystem ts) throws TermWareException
    {
        if (setFactsDebugStub_==null) {
            setFactsDebugStub_=ts.getInstance().getDebugStubGenerator().generateSetFactsDebugStub(action);
        }
        return setFactsDebugStub_;
    }

    private Class<? extends SubstitutionDebugStub> getFactsSubstitutionDebugStub(Term action, TermSystem ts) throws TermWareException
    {
        if (factsSubstitutionDebugStub_==null) {
            factsSubstitutionDebugStub_=ts.getInstance().getDebugStubGenerator().generateFactsSubstitutionDebugStub(action);
        }
        return factsSubstitutionDebugStub_;
    }
    
    
    private Term inPattern_;
    private Term inLet_;
    private Term inWhere_;
    private Term outPattern_;
    private String description_;
    private String name_;
    private String source_=null;
    private Class<? extends UnificationDebugStub>  unificationDebugStubClass_=null;
    private Class<? extends SubstitutionDebugStub> substitutionDebugStub_=null;
    private Class<? extends SetFactsDebugStub>     setFactsDebugStub_=null;
    private Class<? extends SubstitutionDebugStub> factsSubstitutionDebugStub_=null;
    
}
