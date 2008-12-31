package ua.gradsoft.termware.transformers.sys;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002-2005
 * $Id: JavaFactsTransformer.java,v 1.1 2005-01-09 19:28:21 rssh Exp $
 */

import java.util.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.util.*;
import ua.gradsoft.termware.exceptions.*;


/**
 *<TABLE><TR>
 *<TR><TD>javaFacts(name,className) </TD><TD> add to current environment facts database with name <code> name </code> </TD></TR>
 *<TR><TD></TD>                          <TD> implemented by java class <code> className </code></TD></TR></TABLE>
 */
public class JavaFactsTransformer extends AbstractBuildinTransformer {
    
    /**
     *@return true
     */
    public boolean internalsAtFirst() {
        return true;
    }
    
    
    public  Term  transform(Term t, TermSystem sys, TransformationContext ctx) throws TermWareException {
        return static_transform(t,sys,ctx); }
    
    
    static public  Term  static_transform(Term t, TermSystem sys, TransformationContext ctx) throws TermWareException {
        return static_transform(t,sys,ctx,sys.getInstance().getRoot()); }
    
    static public  Term  static_transform(Term t, TermSystem sys, TransformationContext ctx, Domain where) throws TermWareException {
        if (!t.getName().equals("javaFacts")) return t;
        if (t.getArity()!=2) {
            return t;
        }
        String factsName=t.getSubtermAt(0).getName();
        String className=t.getSubtermAt(1).getName();
        Object o=JavaLangReflectHelper.instantiateObject(className);
        sys.getInstance().addJavaFacts(factsName,(IFacts)o);
        ctx.setChanged(true);
        return TermFactory.createBoolean(true);
    }
    
    
    public String getDescription() {
        return staticDescription_;
    }
    
    public String getName() {
        return "javaFacts";
    }
    
    private final static String staticDescription_=
            "<TABLE><TR>"+
            "<TR><TD>javaFacts(name,className) </TD><TD> add to current environment facts database with name <code> name </code> </TD></TR>"+
            "<TR><TD></TD>                          <TD> implemented by java class <code> className </code></TD></TR></TABLE>";
    
}
