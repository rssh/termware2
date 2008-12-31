/*
 * MannersData.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termwaredemos.benchmarks.manners;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Random;

/**
 * Generate data for manners.
 */
public class MannersData {
    
    int nGuests_=64;
    int maxHobbies_=3;
    int minHobbies_=2;
    String fname_=null;
    String droolsFname_=null;
    boolean generateDrools_=false;
    
    
    
    public static void main(String[] args)
    {
        MannersData mannersData=new MannersData();
        mannersData.run(args);
    }
    
    public void run(String[] args)
    {
        parseOptions(args);
        PrintStream out = null;
        PrintStream droolsOut = null;
        try {
          out = createPrintStream(fname_);
          if (generateDrools_) {
              droolsOut = createPrintStream(droolsFname_);
          }
          generate(out, droolsOut);
        }catch(IOException ex){
            ex.printStackTrace();
        }finally{
            if (out!=null) {
              out.close();
            }
            if (droolsOut!=null) {
                droolsOut.close();
            }
        }
    }
    
    
    public void parseOptions(String[] args)
    {      
      if (args.length >= 1)  {
          fname_=args[0];
      }
      if (args.length >= 2) {
          nGuests_ = Integer.parseInt(args[1]);
      }
      if (args.length >= 3){
          maxHobbies_ = Integer.parseInt(args[2]);
      }
      if (args.length >= 4) {
          minHobbies_ = Integer.parseInt(args[3]);
      }
      if (minHobbies_ > maxHobbies_) {
          System.out.println("minHobbies must be less then maxHobbies");
          System.exit(0);
      }
      if (args.length >=5) {
          generateDrools_=true;
          droolsFname_=args[4];
      }
      if (fname_==null) {
          fname_="manners_"+nGuests_+".dat";
      }
    }
    
    public PrintStream createPrintStream(String fname) throws IOException
    {
        File f = new File(fname);                       
        FileOutputStream fileOutputStream = new FileOutputStream(f);        
        PrintStream retval = new PrintStream(new BufferedOutputStream(fileOutputStream));        
        return retval;
    }
    
    public void generate(PrintStream out,PrintStream droolsOut)
    {
      Random rnd = new Random();  
      int maxMale = nGuests_/2;
      int maleCount = 0;
      int maxFemale = nGuests_/2;
      int femaleCount = 0;
      out.println("[");
      for(int i=0; i<nGuests_; ++i) {
          char sex = rnd.nextBoolean() ? 'm' : 'f';
          if (maleCount >= maxMale) {
              sex='f';
          }else if (femaleCount >= maxFemale) {
              sex='m';
          }
          if (sex=='m') {
              ++maleCount; 
          }else{
              ++femaleCount;
          }
          String guestName="n"+(i+1);
          out.print("guest("+guestName+","+sex+",");
          out.print("{");
          int nHobbies = minHobbies_+rnd.nextInt(maxHobbies_-minHobbies_+1);
          HashSet hobbies = new HashSet();
          for(int j=0; j<nHobbies;++j){
              Integer hobby=null;                        
              do {
                hobby = new Integer(rnd.nextInt(nHobbies)+1);
              }while(hobbies.contains(hobby));
              hobbies.add(hobby);
              out.print(hobby);  
              if (j!=nHobbies-1){
                  out.print(",");
              }
              if (droolsOut!=null) {
                droolsOut.println("(guest (name "+guestName+") (sex "+sex+") (hobby "+hobby+"))");
              }
          }

          out.print("})");
          if (i!=nGuests_-1) {
              out.print(",");
          }
          out.println();                    
      }  
      out.println("];");
      if (droolsOut!=null) {
          droolsOut.println("(last_seat (seat "+nGuests_+"))");
          droolsOut.print("(context (state start))");
      }
    }
    
}
