package ua.gradsoft.termware;

import java.io.*;

import ua.gradsoft.termware.exceptions.*;
import ua.gradsoft.termware.envs.*;
import ua.gradsoft.termware.strategies.*;
import ua.gradsoft.termware.parsers.terms.TermReader;

//import ua.gradsoft.termware.gui.GUIMain;


/**
 * Command line TermWare interpeter.
 **/
public class CMDMain {
    
    /**
     * print information about options to standart
     * error stream.
     **/
    public  static void usage() {
        copyright(System.err);
        System.err.println("Usage:");
        System.err.println("ua.gradsoft.termware.CMDMain <options>");
        System.err.println("where <options> one of:");
        System.err.println("  -d            - debug output");
        System.err.println("  -c 'command'  - eval term <command>");
        System.err.println("  -i 'fname'    - read command from file <fname>");
        System.err.println("  -o 'fname'    - write output to file <fname>");
        System.err.println("  -l 'fname'    - write log file to <fname> ");
        System.err.println("  -q            - supress output of copyright message");
        //System.err.println("  -g            - start graphics gui (experimental)");
        System.err.println("  -h            - print this help message");
    }
    
    public  static void copyright(PrintStream out) {
        out.println("TermWare (C) Grad-Soft [tm] , Kiev, Ukraine, 2002-2008");
        out.println("http://www.gradsoft.ua");
    }
    
    
    public static void main(String[] args) {
        CMDMain app=new CMDMain();
        app.run(args);
    }
    
    public  void parseOptions(String[] args) throws InvalidOptionException {
        int i=0;
        while(i<args.length) {        
            if (args[i].equals("-d")) {
                debug_=true;
            }else if (args[i].equals("-c")) {
                if (i+1==args.length) {
                    throw new InvalidOptionException("-c option require argument");
                }else{
                    ++i;
                    sInTerm_ = args[i];
                }
            }else if (args[i].equals("-i")) {
                if (i+1==args.length) {
                    throw new InvalidOptionException("-i option require argument");
                }else{
                    ++i;
                    inFname_=args[i];
                }
            }else if (args[i].equals("-o")) {
                if (i+1==args.length) {
                    throw new InvalidOptionException("-o option require argument");
                }else{
                    ++i;
                    outFname_=args[i];
                }
            }else if (args[i].equals("-l")) {
                if (i+1==args.length) {
                    throw new InvalidOptionException("-l option require argument");
                }else{
                    ++i;
                    logFname_=args[i];
                }
            }else if (args[i].equals("-q")) {
                noCopyright_=true;
            }else if (args[i].equals("-h")) {
                helpOnly_=true;
            }else if (args[i].equals("-g") || args[i].equals("--graphics")) {
                graphics_=true;
            }
            ++i;
        }
    }
    
    
    public void run(String[] args) {
        TermSystem system=null;
        IEnv env=null;
        Term envTerm=null;        
        Term envt1=TermWare.getInstance().getTermFactory().createAtom("system");
        Term envt2=TermWare.getInstance().getTermFactory().createAtom("system");
        Term envt3=TermWare.getInstance().getTermFactory().createAtom("system");
        
        try {
            parseOptions(args);
        }catch(InvalidOptionException ex){
            copyright(System.err);
            System.err.println(ex.getMessage());
            usage();
        }
        if (helpOnly_) {
            usage();
            return;
        }
        //  if (graphics_) {
        //    GUIMain.main(args);
        //    return;
        //  }

        try {
            if (inFname_!=null) {
                envt1=TermFactory.createString(inFname_);
            }
            if (outFname_!=null) {
                envt2=TermFactory.createString(outFname_);
            }
            if (logFname_!=null) {
                envt3=TermFactory.createString(logFname_);
            }
            
            
            
            ITermRewritingStrategy strategy = new TopDownStrategy();
            IFacts facts=new DefaultFacts();           
            system=new TermSystem(strategy,facts);
            system.setReduceFacts(false);
            
            env=new SystemLogEnv();

            TermWare.getInstance().init();


            TermWare.getInstance().setEnv(env);
            TermWare.addGeneralTransformers(system);
            TermWare.addGenSysTransformers(system);
            
        }catch(TermWareException ex){
            System.err.println("TermWare: exception during initialization");
            System.err.println("TermWare: "+ex.toString());
            ex.printStackTrace();
            return;
        }
        if (sInTerm_ != null) {
            try {
                inTerm_ = TermWare.getInstance().getTermFactory().createParsedTerm(sInTerm_);
            }catch(TermWareException ex){
                System.err.println("Can't parse initial term :");
                System.err.println(ex.getMessage());
                env.close();
                return;
            }
        }
        try {
            if (!noCopyright_) {
                copyright(System.out);
            }
            system.setLoggingMode(debug_);
            if (isInteractive()) {
                TermReader termReader = new TermReader(env.getInput(),"<stdin>",0,TermWare.getInstance());
                for(;;){
                    env.getOutput().print("TermWare>");
                    env.getOutput().flush();
                    Term t;
                    try {
                        t=termReader.readStatementWrapped();
                    }catch(TermWareException ex){
                        //ex.printStackTrace();
                        env.show(ex);
                        termReader.ReInit(env.getInput());
                        continue;
                    }
                    try {
                        t=system.reduce(t);
                        t.print(env.getOutput());
                        env.getOutput().println();                        
                    }catch(TermWareException ex){
                        //ex.printStackTrace();
                        env.show(ex);
                        continue;
                    }
                    if (t.getName().equals("quit")) {
                        break;
                    }
                }
            }else{
                if (inFname_ != null && inTerm_ != null) {
                    System.err.println("-c and -i options can't be used together");
                    usage();
                }else if (inFname_ != null && inTerm_ == null) {
                    inTerm_ = TermWare.getInstance().load(inFname_);
                }else if (inFname_ == null && inTerm_ != null) {
                    /* nothing */
                }else if (inTerm_==null) {
                    throw new AssertException("internal error: inTerm_==null");
                }
                system.setLoggingMode(true);
                system.setLoggedEntity("StrategyReductions");
                Term outTerm = system.reduce(inTerm_);                
                outTerm.println(env.getOutput());
            }
        }catch(Exception ex){
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }finally{
            env.close();
        }
    }
    
    private boolean isInteractive() {
        return sInTerm_==null && inTerm_==null && inFname_==null; }
    
    
    private boolean noCopyright_=false;
    private boolean helpOnly_=false;
    private boolean debug_=false;
    private boolean graphics_=false;
    private String  sInTerm_=null;
    private Term    inTerm_=null;
    private String  inFname_=null;
    private String  outFname_=null;
    private String  logFname_=null;
    
    
}
