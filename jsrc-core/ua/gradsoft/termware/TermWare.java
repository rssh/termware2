/*
 * TermWare.java
 *
 */

package ua.gradsoft.termware;

import ua.gradsoft.termware.transformers.facts.AssignTransformer;
import ua.gradsoft.termware.transformers.sys.*;
import ua.gradsoft.termware.transformers.string.*;
import ua.gradsoft.termware.transformers.general.*;
import ua.gradsoft.termware.transformers.list.ListCarTransformer;
import ua.gradsoft.termware.transformers.list.ListCdrTransformer;
import ua.gradsoft.termware.transformers.list.ListInsertTransformer;
import ua.gradsoft.termware.transformers.list.ListLengthTransformer;
import ua.gradsoft.termware.transformers.list.ListAppendTransformer;
import ua.gradsoft.termware.transformers.list.ReverseTransformer;
import ua.gradsoft.termware.transformers.list.SublistTransformer;

/**
 * Singleton for termware
 * @author  Ruslan Shevchenko
 */
public class TermWare {
    
    /**
     * add transformers from general system.
     */
    public static  void addGeneralTransformers(TermSystem system)  throws TermWareException {
        
        system.addNormalizer("plus",PlusTransformer.INSTANCE);
        system.addNormalizer("minus",new MinusTransformer());
        system.addNormalizer("multiply",new MultiplyTransformer());
        system.addNormalizer("divide",new DivideTransformer());
        system.addNormalizer("mod",new ModTransformer());
        system.addNormalizer("eq",new EqTransformer());
        system.addNormalizer("neq",new NeqTransformer());
        system.addNormalizer("less",new LessTransformer());
        system.addNormalizer("less_eq", new LessEqTransformer());
        system.addNormalizer("greater",  new GreaterTransformer());
        system.addNormalizer("greater_eq", new GreaterEqTransformer());
        system.addNormalizer("logical_or",new LogicalOrTransformer());
        system.addNormalizer("logical_and",new LogicalAndTransformer());
        system.addNormalizer("logical_not",new LogicalNotTransformer());
        
        system.addNormalizer("apply",ApplyTransformer.INSTANCE);
        system.addNormalizer("let", LetTransformer.INSTANCE);        
        system.addNormalizer("if", new IfTransformer());       
        system.addNormalizer("reduce", new ReduceTransformer());
        system.addNormalizer("unify",UnifyTransformer.INSTANCE);
        
        system.addNormalizer("term_name",new TermNameTransformer());
        system.addNormalizer("term_arity",ArityTransformer.INSTANCE);
        system.addNormalizer("arity",ArityTransformer.INSTANCE);
        system.addNormalizer("clone",CloneTransformer.INSTANCE);
                
        
        system.addNormalizer("isString",new IsStringTransformer());
        system.addNormalizer("isAtom",new IsAtomTransformer());
        system.addNormalizer("isNil", IsNilTransformer.INSTANCE);
        system.addNormalizer("isX",  new IsXTransformer());
        system.addNormalizer("isBoolean", new IsBooleanTransformer());
        system.addNormalizer("isNumber", new IsNumberTransformer());
        system.addNormalizer("isChar", new IsCharTransformer());
        system.addNormalizer("isByte", new IsByteTransformer());
        system.addNormalizer("isJavaObject", new IsJavaObjectTransformer());
        
        system.addNormalizer("isBigDecimal", new IsBigDecimalTransformer());
        system.addNormalizer("isBigInteger", new IsBigIntegerTransformer());
        system.addNormalizer("isLong", new IsLongTransformer());
        system.addNormalizer("isInt", new IsIntTransformer());
        system.addNormalizer("isShort", new IsShortTransformer());
        system.addNormalizer("isDouble", new IsDoubleTransformer());
        system.addNormalizer("isFloat", new IsFloatTransformer());
        system.addNormalizer("isList",IsListTransformer.INSTANCE);
        
        system.addNormalizer("toBoolean", new ToBooleanTransformer());
        system.addNormalizer("toBigDecimal", new ToBigDecimalTransformer());
        system.addNormalizer("toBigInteger", new ToBigIntegerTransformer());
        system.addNormalizer("toLong", new ToLongTransformer());
        system.addNormalizer("toInt", new ToIntTransformer());    
        system.addNormalizer("toDouble", new ToDoubleTransformer());
        system.addNormalizer("toString", ToStringTransformer.INSTANCE);
        
        system.addNormalizer("intersection", new IntersectionTransformer());
        
        system.addNormalizer(PrintStringTransformer.INSTANCE.getName(),PrintStringTransformer.INSTANCE);
        system.addNormalizer(JSR223ProgramTransformer.INSTANCE.getName(),JSR223ProgramTransformer.INSTANCE);
        
        //
        // system.addRule(" ($x+$y)+$z -> $x+($y+$z) ");
        // system.addRule(" $x*($y+$z)->$x*$y+$x*$z ");
        // system.addRule(" ($x+$y)*$z->$x*$z+$y*$z ");
        
        system.addRule(" -(-$x) -> $x; ");
        // system.addRule(" -($x+$y) -> (-$x)+(-$y); ");
        // system.addRule(" -($x-$y) -> (-$x)+$y; ");
        system.addRule(" $x-$y -> $x+(-$y); ");
        system.addRule(" $x+(-$x) -> 0 ");
        system.addRule(" 1*$x->$x ");
        system.addRule(" $x*1->$x ");
        system.addRule(" 0*$x->0 ");
        system.addRule(" $x*0->0 ");
        //
        
    }
    
    /**
     * add gensys transformers to system <code> system </code>
     */
    public static  void addGenSysTransformers(TermSystem system)  throws TermWareException {
        system.addNormalizer("domain",new DomainTransformer());
        system.addNormalizer("javaFacts",new JavaFactsTransformer());
        system.addNormalizer("setFact",new SetFactTransformer());
        system.addNormalizer("checkFact",new SetFactTransformer());
        system.addNormalizer("javaStrategy",new JavaStrategyTransformer());
        system.addNormalizer("system",new SystemTransformer());
        system.addNormalizer("loadFile",new LoadFileTransformer());
        system.addNormalizer("printTerm",new PrintTermTransformer());
        system.addNormalizer("setProperty",new SetPropertyTransformer());
        system.addNormalizer("getProperty",new GetPropertyTransformer());
        
        system.addNormalizer("getRuleset",new GetRulesetTransformer());
        
        system.addNormalizer(GetCurrentTimeInMillisTransformer.INSTANCE_.getName(),
                             GetCurrentTimeInMillisTransformer.INSTANCE_);
                
    }
    
    public static void  addStringTransformers(TermSystem system) throws TermWareException {
        system.setReduceFacts(false);
        system.addNormalizer("length", new StringLengthTransformer());
        system.addNormalizer("concat", StringConcatTransformer.INSTANCE);
        system.addNormalizer("parse", new StringParseTransformer());
        system.addNormalizer("plus",  StringConcatTransformer.INSTANCE);
        system.addNormalizer("convert", new StringConvertTransformer());
        system.addNormalizer("split", new StringSplitTransformer());
        system.addNormalizer("subst", new StringSubstTransformer());
        system.addNormalizer("matches",StringMatchesTransformer.INSTANCE);
        system.addRule("apply(String,$x) [ isString($x) ] -> $x");
    }
    
    public static void addListTransformers(TermSystem system) throws TermWareException
    {
        system.addNormalizer("append",ListAppendTransformer.INSTANCE);
        system.addNormalizer("car",ListCarTransformer.INSTANCE);
        system.addNormalizer("cdr",ListCdrTransformer.INSTANCE);
        system.addNormalizer("insert",ListInsertTransformer.INSTANCE);
        system.addNormalizer("length",ListLengthTransformer.INSTANCE);
        system.addNormalizer("reverse",ReverseTransformer.INSTANCE);
        system.addNormalizer("sublist",SublistTransformer.INSTANCE);       
    }
    
    public static synchronized TermWareInstance getInstance() {
        if (instance_==null) {
            instance_=new TermWareInstance();
            instance_.init();
        }
        return instance_;
    }
    
    public  static synchronized TermWareInstance createNewInstance() {
        TermWareInstance newInstance = new TermWareInstance();
        newInstance.setParentInstance(getInstance());
        newInstance.init();
        return newInstance;
    }
    
    /**
     *If we prepared for rule debug ?
     *When this switch is set, debug stubs is generated via rule processing,
     *to allow source-level debug of rules.
     */
    public static boolean isInDebug()
    {
        return inDebug_;
    }
    
    /**
     *set debug switch.
     *@see TermWare#isInDebug
     */
    public static void setInDebug(boolean inDebug)
    {
        inDebug_=inDebug;
    }
    
    private static TermWareInstance instance_=null;
    private static boolean          inDebug_=false;
    
    public static final String NAME_SEPARATOR="/";
    public static final Term[] EMPTY_TERM_ARRAY=new Term[0];
    
    public static final String VERSION="2.2b";
    
}
