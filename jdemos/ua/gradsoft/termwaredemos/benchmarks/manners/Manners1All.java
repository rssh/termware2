/*
 * Manners1All.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termwaredemos.benchmarks.manners;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareSymbols;


/**
 *
 * @author Ruslan Shevchenko
 */
public class Manners1All {
    
    
    public static void main(String[] args)
    {
      Manners1All manners1All = new Manners1All();
      manners1All.run(args);
    }
    
    public void run(String[] args)
    {
      if ((args.length) == 0) {
          System.err.println("Usage: manners1All <absolute-path-to-datafile>");
          System.exit(0);          
      }  
      try {
        init();
        loadGuests(args[0]);
        runSystem();
      }catch(TermWareException ex){
          ex.printStackTrace();
      }
    }
    
    
    
    public void init() throws TermWareException
    {
      TermWare.getInstance().init();  
      mannersSystem_=TermWare.getInstance().getOrCreateDomain("examples").resolveSystem("manners1All");
    }
    
    public void loadGuests(String fname) throws TermWareException
    {
      guests_=TermWare.getInstance().getTermFactory().createNIL();
      Term t = TermWare.getInstance().load(fname);
      while(t.isComplexTerm() && t.getNameIndex().equals(TermWareSymbols.CONS_INDEX)) {
          Guest guest = new Guest(t.getSubtermAt(0));
          guests_=TermWare.getInstance().getTermFactory().createTerm("cons", 
                                                                     TermWare.getInstance().getTermFactory().createJTerm(guest), 
                                                                     guests_);
          t=t.getSubtermAt(1);
      }      
    }
    

    public void runSystem() throws TermWareException
    {
      mannersSystem_.setLoggingMode(true);
      //mannersSystem_.setLoggedEntity(FirstTopStrategy.class.getName());
      //mannersSystem_.setLoggedEntity(ConditionalRuleTransformer.class.getName());
      //mannersSystem_.setLoggedEntity(IntersectionTransformer.class.getName());
      mannersSystem_.setLoggedEntity("All");
      Term t=TermWare.getInstance().getTermFactory().createTerm("FIND",guests_);
      t=mannersSystem_.reduce(t);  
      
      if (t.isBoolean()) {
          if (t.getBoolean()) {
              System.out.println("solution was found");
          }else{
              System.out.println("solution was not found.");
          }
      }else{
          System.out.print("System does not reduce, t is:");
          t.println(System.out);
      }
    }
    
    private Term guests_;
    private TermSystem mannersSystem_;

    
}
