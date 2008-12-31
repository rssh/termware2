package ua.gradsoft.termware.transformers.general;

import java.lang.reflect.Method;
import ua.gradsoft.termware.AbstractBuildinTransformer;
import ua.gradsoft.termware.Substitution;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TransformationContext;
import ua.gradsoft.termware.exceptions.InvalidSystemNameException;
import ua.gradsoft.termware.exceptions.InvocationException;
import ua.gradsoft.termware.util.ConditionalRuleTransformer;
import ua.gradsoft.termware.util.JavaLangReflectHelper;
import ua.gradsoft.termware.util.LogHelper;
import ua.gradsoft.termware.util.RuleTransformer;


/**
 * Transformer for 'apply'.
 *<ul>
 *  <li> apply(x) - synonim to reduce <code> x </code> <li>
 *  <li> apply(x,y):
 *             <ul>
 *              <li> x is name of system -- apply system <code> x</code> to term <code>y</code> </li>
 *              <li> x is JTerm -- invoke method <code> y </code> to object in <code> x </code> </li>
 *              <li> x is rule - apply rule to y </li>
 *              <li> x is ruleset - apply ruleset to y </li>
 *             </ul>
 *  </li>
 *</ul>
 * note, that <code> apply<code> have syntax shugar -- <code> x.y </code>
 */
public class ApplyTransformer extends AbstractBuildinTransformer {
    
    private ApplyTransformer() {}
    
    public static ApplyTransformer INSTANCE = new ApplyTransformer();
    
    public  Term  transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        return static_transform(t,system,ctx); }
    
    
    
    static public  Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        //   if (!t.getName().equals("apply")) return t;
        if (system.isLoggingMode()) {
            LogHelper.log(system,ApplyTransformer.class," t=",t);
        }
        if (t.getArity()==1) {
            boolean svChanged = ctx.isChanged();
            ctx.setChanged(false);
            Term result;
            // try {
            result=system.reduce(t.getSubtermAt(0),ctx);
            // }catch(TermWareException ex){
            //   return t;
            // }
            if (!ctx.isChanged()) {
                if (system.isLoggingMode()) {
                    LogHelper.log(system,ApplyTransformer.class,"apply with arity=1, not changed ",result);
                }
                ctx.setChanged(svChanged);
                return t;
            }else{
                if (system.isLoggingMode()) {
                    LogHelper.log(system,ApplyTransformer.class,"apply with arity=1, reduced term is ",result);
                }
                return result;
            }
        }else if(t.getArity()==2) {
            Term x=t.getSubtermAt(0);
            Term y=t.getSubtermAt(1);
            if (x.isJavaObject()) {
                t = static_transform_jcall(t,x.getJavaObject(),y,system,ctx);
            }else{
                if (x.isComplexTerm() && x.getName().equals("_name")) {
                    t = static_transform_systemcall(t,x,y,system,ctx);
                }else if (x.isAtom()) {
                    t = static_transform_systemcall(t,x,y,system,ctx);
                }else if (x.isComplexTerm() && x.getName().equals("rule") && x.getArity()==2){
                    t = static_transform_rule(t,x,y,system,ctx);
                }else if (x.isComplexTerm() && x.getName().equals("if_rule")){
                    t = static_transform_conditional_rule(t,x,y,system,ctx);
                }
            }
        }
        return t;
    }
    
    static public Term static_transform_systemcall(Term original,Term systemLocation,Term arg,TermSystem system,TransformationContext ctx) throws TermWareException {
        //system.setLoggingMode(true);
        //system.setLoggedEntity("SystemReductions");
        //system.setLoggedEntity("Rules");

        if (system.isLoggingMode())  {
            LogHelper.log(system,ApplyTransformer.class,"system apply for ",arg);
            //LogHelper.log(system,ApplyTransformer.class,"current substitution is ",ctx.getCurrentSubstitution());
        }
        try {
            TermSystem isystem=system.getInstance().resolveSystem(systemLocation);
           // { 
           //   isystem.setLoggingMode(system.isLoggingMode()); 
           //   isystem.setLoggedEntities(system.getLoggedEntities());
           // }
 //           isystem.setLoggingMode(true);
 //           isystem.setLoggedEntity("All");
            boolean svChanged = ctx.isChanged();
            ctx.setChanged(false);
            Term sarg = arg.subst(ctx.getCurrentSubstitution());
            //System.err.println("arg is :"+TermHelper.termToString(arg));
            //System.err.println("sarg is :"+TermHelper.termToString(sarg));               
            Substitution svSubst = new Substitution();
            svSubst.merge(ctx.getCurrentSubstitution());
            //ctx.newCurrentSubstitution();
            Term t1=isystem.reduce(arg,ctx);
            //ctx.getCurrentSubstitution().copyOver(svSubst);
            if (ctx.isChanged()) {
                if (system.isLoggingMode()) {
                    LogHelper.log(system,ApplyTransformer.class,"apply result ",t1);
                    LogHelper.log(system,ApplyTransformer.class,"substitution is ",ctx.getCurrentSubstitution());
                    LogHelper.log(system,ApplyTransformer.class,"prev substitution is ",svSubst);
                }
                return t1;
            }else{
                ctx.setChanged(svChanged);
                if (system.isLoggingMode()) {
                    LogHelper.log(system,ApplyTransformer.class,"apply unchanged ");
                }
                return original;
            }
        }catch(InvalidSystemNameException ex){
            system.getEnv().getLog().println("exception during apply:"+ex.getMessage());
            return original;
  //      }catch(TermWareException ex){
  //          ctx.setChanged(false)
  //          system.getEnv().getLog().println("exception during apply:"+ex.getMessage());
  //          throw new InvocationException(ex);            
        }
    }
    
    static public Term static_transform_jcall(Term original,Object jObject,Term arg,TermSystem system,TransformationContext ctx) throws TermWareException {
        if (system.isLoggingMode()) {
            LogHelper.log(system,ApplyTransformer.class,"static_transform_jcall o="+jObject.toString()+", arg=",arg);
        }
        Class objectClass = jObject.getClass();
        if (!arg.isComplexTerm()) {
            if (system.isLoggingMode()) {
                LogHelper.log(system,ApplyTransformer.class,"jcall return unchanged");
            }
            // we can aply only methods.
            return original;
        }
        Method m = JavaLangReflectHelper.findClassMethodByNameAndArity(objectClass,arg.getName(),arg.getArity());
        if (system.isLoggingMode()) {
            LogHelper.log(system,ApplyTransformer.class,"method found");
        }
        Class[] parameterTypes = m.getParameterTypes();
        Object oRetval=null;
        boolean invoked=false;
        boolean conversionError=false;
        if (parameterTypes.length==0) {
            oRetval = JavaLangReflectHelper.invokeObjectMethod(jObject,m);
            invoked = true;
        }else{
            Object[] parameters=new Object[parameterTypes.length];
            int ictx=0;
            if (parameterTypes[0].equals(TransformationContext.class)) {
                ictx=1;
                parameters[0]=ctx;
            }
            for(int i=0; i<arg.getArity(); ++i) {
                Term t = system.reduce(arg.getSubtermAt(i));
                parameters[ictx+i]=system.getInstance().getTypeConversion().getAsObjectWithClass(parameterTypes[ictx+i], t);
            }
            oRetval = JavaLangReflectHelper.invokeObjectMethod(jObject,m, parameters);
            invoked = true;
        }
        if (invoked) {
            Term retval = system.getInstance().getTypeConversion().adopt(oRetval);
            ctx.setChanged(true);
            if (system.isLoggingMode()) {
                LogHelper.log(system,ApplyTransformer.class,"jcall return ",retval);
            }
            return retval;
        }else{
            if (system.isLoggingMode()) {
                LogHelper.log(system,ApplyTransformer.class, "jcall return unchanged");
            }
            return original;
        }
    }
    
    static public Term static_transform_rule(Term origin, Term rule, Term argument, TermSystem system, TransformationContext ctx) throws TermWareException {
        //Term inPattern=rule.getSubtermAt(0);
        //Term outPattern=rule.getSubtermAt(1);
        boolean svChanged=ctx.isChanged();
        ctx.setChanged(false);
        //Term t=RuleTransformer.static_transform(inPattern,outPattern,argument,system,ctx);
        // now ruleTransformer is needed, becouse it contains debug stub.
        //TODO:
        //  Think about optimization in non-debug mode.
        RuleTransformer ruleTransformer=new RuleTransformer(rule);
        Term t = ruleTransformer.transform(argument,system,ctx);
        if (ctx.isChanged()) {
            return t;
        }else{
            ctx.setChanged(svChanged);
            return origin;
        }
    }
    
    static public Term static_transform_conditional_rule(Term origin, Term rule, Term argument, TermSystem system, TransformationContext ctx) throws TermWareException {
        //  if (!rule.getName().equals("if_rule")) {
        //throw new AssertException("ConditionalRuleTransformer constructor argument must be if_rule");
        //     return origin;
        //  }
        if (rule.getArity()<3 || rule.getArity() > 5) {
            //throw new AssertException("if_rule arity must be between 3 and 5");
            return origin;
        }
        ConditionalRuleTransformer transformer = new ConditionalRuleTransformer(rule);
        Term t = transformer.transform(argument,system,ctx);
        return (ctx.isChanged() ? t : origin);
    }
    
    
    // public boolean internalsAtFirst() {
    //     return false;
    // }
    
    
    
    public String getName() {
        return "apply";
    }
    
    public String getDescription() {
        return staticDescription_;
    }
    
    
    static String staticDescription_=
            "<ul>\n"+
            " <li> apply(x) - synonim to reduce <code> x </code> <li>\n"+
            " <li> apply(x,y)  \n"+
            "    <ul>\n"+
            "      <li> x is name of system -- apply system <code> x</code> to term <code>y</code> </li>\n"+
            "      <li> x is JTerm -- invoke method <code> y </code> to object in <code> x </code> </li>\n"+
            "      <li> x is rule -- try to apply this rule to <code> y </code> </li>\n"+
            "   </ul>\n"+
            " </li>\n"+
            "</ul>\n"+
            " note, that <code> apply<code> have syntax shugar -- <code> x.y </code>"
            ;
    
    
}
