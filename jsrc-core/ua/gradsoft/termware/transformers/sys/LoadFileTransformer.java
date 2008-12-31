package ua.gradsoft.termware.transformers.sys;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002, 2003
 * $Id: LoadFileTransformer.java,v 1.3 2007-07-13 20:50:21 rssh Exp $
 */

import java.util.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.util.*;
import ua.gradsoft.termware.exceptions.*;


/**
 *Trandformer for loadFile.
 * loadFile(fname) - load file with name <code> fname </code> into current runtime
 * loadFile(fname,lang) - load file with name <code> fname </code> into current runtime,
 *   using parser 'lang'.
 *predefined number of languages: tscript, Java, IDL, SQL
 *Other language parsers can be loaded into runtime by application developer.
 *@see IParserFactory
 **/
public class LoadFileTransformer extends AbstractBuildinTransformer {
    
    /**
     *@return true
     */
    public boolean internalsAtFirst() {
        return true;
    }
    
    public  Term  transform(Term t, TermSystem sys, TransformationContext ctx) throws TermWareException {
        return static_transform(t,sys,ctx); }
    
    
    static public  Term  static_transform(Term t, TermSystem sys, TransformationContext ctx) throws TermWareException {
        if (!t.getName().equals("loadFile")) return t;
        if (sys.isLoggingMode()) {
            sys.getEnv().getLog().print("native:");
            t.print(sys.getEnv().getLog());
            sys.getEnv().getLog().println();
        }
        Term retval=t;
        if (t.getArity()==1) {
            retval=sys.getInstance().load(t.getSubtermAt(0).getName());
        }else if(t.getArity()==2) {
            // TODO: check that name and parser is string
            String fname =  t.getSubtermAt(0).getName();
            String parserName =  t.getSubtermAt(1).getName();
            IParserFactory parserFactory = sys.getInstance().getParserFactory(parserName);
            retval=sys.getInstance().load(fname,parserFactory,TermFactory.createNIL());
        }else if(t.getArity()==3) {
            // TODO: check that name and parser is string
            String fname =  t.getSubtermAt(0).getName();
            String parserName =  t.getSubtermAt(1).getName();
            IParserFactory parserFactory = sys.getInstance().getParserFactory(parserName);
            retval=sys.getInstance().load(fname,parserFactory,t.getSubtermAt(2));
        }else{
            if (sys.isLoggingMode()) {
                sys.getEnv().getLog().print("native:");
                t.print(sys.getEnv().getLog());
                sys.getEnv().getLog().println("- not changed.");
            }
            return t;
        }
        ctx.setChanged(true);
        if (sys.isLoggingMode()) {
            sys.getEnv().getLog().print("native:");
            t.print(sys.getEnv().getLog());
            sys.getEnv().getLog().println("- loaded.");
        }
        return retval;
    }
    
    
    public String getDescription() {
        return staticDescription_;
    }
    
    public String getName() {
        return "loadFile";
    }
    
    
    private static final String staticDescription_=
            " loadFile(fname) - load file with name <code> fname </code> into current runtime <br>" +
            " loadFile(fname,lang) - load file with name <code> fname </code> into current runtime <br>"+
            "   using parser 'lang'.<br>"+
            " predefined number of languages: tscript, Java, IDL, SQL <br>" +
            " Other language parsers can be loaded into runtime by application developer."
            ;
    
}
