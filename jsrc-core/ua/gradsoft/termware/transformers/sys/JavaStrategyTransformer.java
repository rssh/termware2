package ua.gradsoft.termware.transformers.sys;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002 - 2005
 * $Id: JavaStrategyTransformer.java,v 1.1 2005-01-09 19:28:21 rssh Exp $
 */

import java.util.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.util.*;
import ua.gradsoft.termware.exceptions.*;


/**
 * Transformer for javaStrategy(name,className)
 * add to current runtime environment strategy with name <code> name </code> implemented by <code> className </code>
 **/
public class JavaStrategyTransformer extends AbstractBuildinTransformer {
    
    /**
     *@return true
     */
    public boolean internalsAtFirst() {
        return true;
    }
    
    public  Term  transform(Term t, TermSystem sys, TransformationContext ctx) throws TermWareException {
        return static_transform(t,sys,ctx); }
    
    
    static public  Term  static_transform(Term t, TermSystem sys, TransformationContext ctx) throws TermWareException {
        if (!t.getName().equals("javaStrategy")) return t;
        if (t.getArity()!=2) {
            return t;
        }
        sys.getInstance().addJavaStrategy(t.getSubtermAt(0).getName(),t.getSubtermAt(1).getName());
        ctx.setChanged(true);
        return TermFactory.createBoolean(true);
    }
    
    
    public String getDescription() {
        return staticDescription_;
    }
    
    public String getName() {
        return "javaStrategy";
    }
    
    private final static String staticDescription_=
            "javaFacts(name,className) - add to current runtime environment strategy with name <code> name </code> implemented by <code> className </code> ";
    
    
}
