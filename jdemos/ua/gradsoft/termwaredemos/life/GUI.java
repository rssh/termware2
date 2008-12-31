
package ua.gradsoft.termwaredemos.life;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;

/** 
 * The Game Of Life GUI.
 *
 *   Controls of the Game of Life (Shape and speed selector, next and start/stop-button).
 * based on GUI, from org.bitstorm.gameoflife by Edwin Martin
 *
 */
public class GUI extends Frame implements Runnable
{

        public GUI(TermSystem termSystem, FieldModel fieldModel)
        {
          isActive_=false;
          activeSignal_=new Object();
          
          termSystem_=termSystem;
          current_=null;
            
          fieldCanvas_=new FieldCanvas(fieldModel,10);  
          createControlPanel();
         
	  setBackground(new Color(0x999999));
	  setLayout(new BorderLayout());
	  add(BorderLayout.SOUTH, controlPanel_);
	  add(BorderLayout.NORTH, fieldCanvas_);
          
      
          enableEvents(AWTEvent.WINDOW_EVENT_MASK);
          
          addWindowListener(new WindowAdapter() {
              public void windowClosing(WindowEvent ev) {
                  quit_=true;
                  setActive(false);
                  System.exit(0);
              }
          });
          
	  validate();
          pack();
          setVisible(true);
        }
        
        
        public void createControlPanel() 
        {
         controlPanel_=new Panel();

         // pulldown menu with speeds
	 Choice speedChoice = new Choice();
	
	 // add speeds
	 speedChoice.addItem(slow);
	 speedChoice.addItem(fast);
	 speedChoice.addItem(hyper);
	
	 // when item is selected
	 speedChoice.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				String arg = (String) e.getItem();
				if (slow.equals(arg)) // slow
					delayBetweenSteps_=100;
				else if (fast.equals(arg)) // fast
					delayBetweenSteps_=10;
				else if (hyper.equals(arg)) // hyperspeed
					delayBetweenSteps_=0;
			}
	 });
	
	
	 startstopButton_ = new Button(startLabelText);
			
	 startstopButton_.addActionListener(
	 new ActionListener() {
		public void actionPerformed(ActionEvent e) {
                          if (!isActive_) {
                              setActive(true);
                          }else{
                              setActive(false);
                          }
		}
	 });
         
         clearButton_ = new Button(clearLabelText);
         clearButton_.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
                 setActive(false);
                 fieldCanvas_.clear();
             }
         });
         
         printStateTermButton_ = new Button(printStateTermLabelText);
         printStateTermButton_.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
                try {
                  Term t = ((AbstractLifeFacts)(termSystem_.getFacts())).generateStateTermFromCanvas();
                  System.out.println(TermHelper.termToString(t));
                }catch(TermWareException ex){
                    System.err.println(ex.getMessage());
                    ex.printStackTrace();
                }
             }
         });
         
         printCurrentTermButton_ = new Button(printCurrentTermLabelText);
         printCurrentTermButton_.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
                  termSystem_.setStop(true);
                  printCurrent_=true;                             
             }
         });
         
	
         
	 controlPanel_.add(startstopButton_);
         controlPanel_.add(clearButton_);
         controlPanel_.add(printStateTermButton_);
         controlPanel_.add(printCurrentTermButton_);
         
	 //controlPanel_.add(speedChoice);
         //controlPanel_.add(nReductionsLabel_);
         controlPanel_.validate();
	}
	
	/**
	 * Set the number of generations in the control bar.
	 * @param generations number of generations
	 */
	public void setNumerOfReductions( int nReductions ) {
		nReductionsLabel_.setText(nReductionsLabelText + nReductions);
	}
	
       
       
        public synchronized boolean isActive()
        {
          return isActive_;
        }
        
        public synchronized void  setActive(boolean b)
        {
          synchronized(activeSignal_) {
            if (b) {
              startstopButton_.setLabel(stopLabelText);
            }else{
              startstopButton_.setLabel(startLabelText);
            }
            isActive_=b;
            if (!b) {
                termSystem_.setStop(true);
            }
            activeSignal_.notify();  
          }
        }
        
        public void startTermSystemThread()
        {
           if (termSystemThread_==null) {
               termSystemThread_=new Thread(this);
               termSystemThread_.start();
           }
        }
        
        public void run() 
        {
           setActive(false);
           quit_=false;
           printCurrent_=false;
           int nReductions=0; 
           while(!quit_) {
               if (!isActive()) {
                   System.out.println("before wait");
                   try {
                     synchronized(activeSignal_) {
                       activeSignal_.wait();
                     }
                   }catch(InterruptedException ex){
                       /* ignore */
                   }
                   System.out.println("after wait");
               }else{
                   try {
                     if (!printCurrent_) {
                        current_= ((AbstractLifeFacts)(termSystem_.getFacts())).generateStateTermFromCanvas();
                     }
                     if (isActive()) {
                       printCurrent_=false;
                       current_=termSystem_.reduce(current_);
                       if (printCurrent_) {
                           System.out.print("current term:"+TermHelper.termToString(current_));
                           setActive(true);
                       }
                     }
                   }catch(TermWareException ex){
                       termSystem_.getEnv().show(ex);
                   }
               }
           }
        }
        
        public  FieldCanvas   getCanvas()
        { return fieldCanvas_; }
        
	private Label  nReductionsLabel_;
        private Button startstopButton_;
        private Button clearButton_;
        private Button printStateTermButton_;
        private Button printCurrentTermButton_;
        private Panel  controlPanel_;        
        private FieldCanvas fieldCanvas_;    

        
        private int      delayBetweenSteps_=0;

        private  boolean  isActive_;
        private  Object   activeSignal_;
        private  Thread   termSystemThread_=null;
        private  boolean  quit_;
        private  boolean  printCurrent_;
	
	private TermSystem  termSystem_;
        private Term        current_;

        private final String nReductionsLabelText = "Reductions: ";
	private final String slow = "Slow";
	private final String fast = "Fast";
	private final String hyper = "Hyper";
	private final String startLabelText = "Start";
	private final String stopLabelText = "Stop";
        private final String clearLabelText = "Clear";
        private final String printStateTermLabelText = "Print State";
        private final String printCurrentTermLabelText = "Print Current";
        
}

