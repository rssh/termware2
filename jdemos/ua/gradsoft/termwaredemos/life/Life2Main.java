/*
 * Life2Main.java
 *
 */

package ua.gradsoft.termwaredemos.life;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.envs.*;
import ua.gradsoft.termware.exceptions.*;

/**
 *Main class for Life game.
 * @author  Ruslan Shevchenko
 */
public class Life2Main {
    
    /** Creates a new instance of Life1Main */
    public Life2Main() {
    }
    
    /**
     * main function
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Life2Main app = new Life2Main();
        try {
            app.run(args);
        }catch(TermWareException ex){
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    
    public void run(String[] args) throws TermWareException
    {
      
      /* load system definition from default destinations:
       *   <termware.path>/systems/examples/Life1.def
       */
     // TermWareSingleton.getSystem("sys").setDebugMode(true);
      TermSystem life2System=TermWare.getInstance().getOrCreateDomain("examples").resolveSystem("Life2");
      
      //life2System.setDebugMode(true);
      //life1System.setDebugEntity("Facts");
      //life2System.setDebugEntity("Rules");
      
      /* create field model */
      FieldModel fieldModel=new FieldModel(60,70);
      GUI gui=new GUI(life2System,fieldModel);
      ((AbstractLifeFacts)life2System.getFacts()).setCanvas(gui.getCanvas());
      gui.setVisible(true);
      gui.startTermSystemThread();
    }
    
}
