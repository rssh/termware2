/*
 * Life1Main.java
 *
 */

package ua.gradsoft.termwaredemos.life;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;


/**
 *Main class for life game.
 * @author  Ruslan Shevchenko
 */
public class Life1Main {
    
    /** Creates a new instance of Life1Main */
    public Life1Main() {
    }
    
    /**
     * main function
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Life1Main app = new Life1Main();
        try {
            app.run(args);
        }catch(TermWareException ex){
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    
    public void run(String[] args) throws TermWareException
    {
     // IEnv  env=new SystemEnv();
     // TermWareSingleton.init(env,args);
      
      /* load system definition from default destinations:
       *   <termware.path>/systems/examples/Life1.def
       */
     // TermWareSingleton.getSystem("sys").setDebugMode(true);
      TermWare.getInstance().init();
      TermSystem life1System=TermWare.getInstance().getOrCreateDomain("examples").resolveSystem("Life1");
      
    //  life1System.setDebugMode(true);
    //  life1System.setDebugEntity("All");
    //  life1System.setDebugEntity("Facts");
    //  life1System.setDebugEntity("Rules");
      
      /* create field model */
      FieldModel fieldModel=new FieldModel(60,70);
      GUI gui=new GUI(life1System,fieldModel);
      ((AbstractLifeFacts)life1System.getFacts()).setCanvas(gui.getCanvas());
      gui.setVisible(true);
      gui.startTermSystemThread();
    }
    
}
