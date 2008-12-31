/*
 * TermWarePrinter.java
 *
 */

package ua.gradsoft.termware.printers.terms;

import java.io.*;
import java.util.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.printers.*;
import ua.gradsoft.termware.exceptions.*;

/**
 *Pretty printer for TermWare expressions.
 * @author  Ruslan Shevchenko
 */
public class TermWarePrinter extends AbstractPrettyPrinter {
    
    /** Creates a new instance of TermWarePrinter */
    public TermWarePrinter(PrintWriter out, String outTag ) {
        super(out,outTag);
    }
    
    public void writeTerm(Term t) throws TermWareException
    {
     HashMap<Term,Integer> trace=new HashMap<Term,Integer>();
     writeTerm(t,trace,0);
    }
    
    public void flush()
    {
      out_.flush();  
    }
    
    private void writeTerm(Term t, HashMap<Term,Integer> trace, int recIndex) throws TermWareException
    {
        //if (checkTrace(t,trace,recIndex)) 
        //   return;
        if (t.getArity()==2) {
            int code=getOperatorCode(t.getName());
            if (code!=-1) {
                int priority=getOperatorPriority(code);
                Term frs=t.getSubtermAt(0);
                Term snd=t.getSubtermAt(1);
                boolean frsParentscapes=false;
                boolean sndParentscapes=false;
                if (frs.isComplexTerm()) {    
                  int frsPriority=getOperatorPriority(frs.getName());
                  if (frsPriority < priority) frsParentscapes=true;
                }
                if (snd.isComplexTerm()) {
                  int sndPriority=getOperatorPriority(snd.getName());
                  if (sndPriority < priority) sndParentscapes=true;
                }
                out_.putBegin(PrettyPrintingBreakType.FITS);
                OperatorRecord opr=operatorRecords_[code];                
                if (opr.isPrefix) {                  
                    out_.putString(opr.prefix);                    
                }
                if (frsParentscapes) {
                    out_.putString("(");
                    out_.putBreak(PrettyPrintingBreakType.INCONSISTENT, 0, 2);
                }
                writeTerm(frs,trace,recIndex+1);
                if (frsParentscapes) {
                     out_.putString(")");
                     out_.putBreak(PrettyPrintingBreakType.INCONSISTENT, 0, -2);
                }
                if (opr.isInfix) {                    
                    out_.putString(opr.infix);
                    out_.putBreak(PrettyPrintingBreakType.INCONSISTENT, 0, 0);
                }else{
                    out_.putString(",");
                    out_.putBreak(PrettyPrintingBreakType.INCONSISTENT, 0, 0);
                }
                if (sndParentscapes) {
                    out_.putString("(");
                    out_.putBreak(PrettyPrintingBreakType.INCONSISTENT, 0, 0);
                }
                writeTerm(snd,trace,recIndex+1);
                if (sndParentscapes) {
                    out_.putString(")");
                }
                out_.putEnd(PrettyPrintingBreakType.FITS);
            }else if(t.getNameIndex().equals(TermWareSymbols.CONS_INDEX)){
                writeConsTerm(t,trace,recIndex);
            }else{
                writeNonOperatorTerm(t,trace,recIndex);
            }
        }else{
            writeNonOperatorTerm(t,trace,recIndex);
        }
        trace.put(t,new Integer(recIndex));
    }
    
    private void writeNonOperatorTerm(Term t,HashMap<Term,Integer> trace,int recIndex) throws TermWareException
    {      
     if (t.isComplexTerm()) {
        if (checkTrace(t,trace, recIndex)) {
           return;
        }
        if (t.getName().equals("if_rule")) {
          writeIfRule(t,trace,recIndex);   
        }else if(t.getName().equals("rule")) {
          writeRule(t,trace,recIndex);
        }else if(t.getName().equals("where")) {
            writeWhere(t,trace,recIndex);
        }else if(t.getName().equals("let")) {
            writeLet(t,trace,recIndex);
        }else if(t.getName().equals("action")){
            writeAction(t,trace,recIndex);
        }else{
         writeFunctionalTerm(t,trace,recIndex);   
        }
     }else{
         writeSimpleRepresentation(t);
     }
    }

    private void writeFunctionalTerm(Term t,HashMap<Term,Integer> trace,int recIndex) throws TermWareException
    {
         out_.putBegin(PrettyPrintingBreakType.INCONSISTENT,0,2);
         out_.putString(t.getName());
         out_.putString("(");      
         for(int i=0; i<t.getArity(); ++i) {
            writeTerm(t.getSubtermAt(i),trace,recIndex+1);
            if (i!=t.getArity()-1) {
                out_.putBreak(PrettyPrintingBreakType.INCONSISTENT, 0, 0);
                out_.putString(",");
                if (i==0) {
                   out_.putBreak(PrettyPrintingBreakType.INCONSISTENT, 0, 2); 
                }else{
                   out_.putBreak(PrettyPrintingBreakType.INCONSISTENT,0,0);
                }
            }
         }
         out_.putString(")");
         out_.putBreak(PrettyPrintingBreakType.INCONSISTENT,0,0);
         out_.putEnd(PrettyPrintingBreakType.INCONSISTENT,0,0);        
    }        
            
    
    private void writeIfRule(Term t,HashMap<Term,Integer> trace,int recIndex) throws TermWareException
    {
     if (t.getArity()>=3 && t.getArity()<=5) {
         out_.putBegin(PrettyPrintingBreakType.CONSISTENT,0,2);
         writeTerm(t.getSubtermAt(0));
         //out_.putBreak(PrettyPrintingBreakType.INCONSISTENT, 0,2);
         out_.putString("[");
         writeTerm(t.getSubtermAt(1));
         out_.putString("]");
         out_.putBreak(PrettyPrintingBreakType.INCONSISTENT, 0,2);
         out_.putString("->");
         writeTerm(t.getSubtermAt(2));         
         if (t.getArity()>=4) {
             Term elifList = t.getSubtermAt(3);             
             while(!elifList.isNil() 
                     && elifList.isComplexTerm() 
                     && elifList.getArity()==2
                     && elifList.getNameIndex()==TermWareSymbols.CONS_INDEX
                     ) {                 
                 Term elif = elifList.getSubtermAt(0);
                 elifList = elifList.getSubtermAt(1);
                 if (elif.getArity()==2) {
                     out_.putLineBreak();
                     out_.putString("|");
                     out_.putBreak(PrettyPrintingBreakType.INCONSISTENT, 1, 2);
                     out_.putString("[");
                     writeTerm(elif.getSubtermAt(0),trace,recIndex+1);
                     out_.putString("]");
                     out_.putBreak(PrettyPrintingBreakType.INCONSISTENT, 1, 2);
                     out_.putString("->");
                     out_.putBreak(PrettyPrintingBreakType.INCONSISTENT, 1, 2);
                     writeTerm(elif.getSubtermAt(0),trace,recIndex+1);
                 }else{
                     writeFunctionalTerm(elif,trace,recIndex+1);
                 }
             }
             if (!elifList.isNil()) {
                 // strange, but be patient.
                 writeFunctionalTerm(elifList,trace,recIndex+1);
             }
             if (t.getArity()>=5) {
                 Term otherwiseTerm = t.getSubtermAt(5);
                 out_.putLineBreak();
                 out_.putString("!->");
                 writeTerm(otherwiseTerm,trace,recIndex);
                 out_.putLineBreak();
             }
         }
         out_.putEnd(PrettyPrintingBreakType.CONSISTENT,0,2);
     }else{
         writeFunctionalTerm(t,trace,recIndex); 
     }
    }
    
    private void writeRule(Term t, HashMap<Term,Integer> trace, int recIndex) throws TermWareException
    {
      if (t.getArity()==2)  {
        out_.putBegin(PrettyPrintingBreakType.CONSISTENT,0,2);   
        writeTerm(t.getSubtermAt(0));
        out_.putBreak(PrettyPrintingBreakType.INCONSISTENT, 1, 1);
        out_.putString("->");
        out_.putBreak(PrettyPrintingBreakType.INCONSISTENT, 1, 1);
        writeTerm(t.getSubtermAt(1));
        out_.putEnd(PrettyPrintingBreakType.CONSISTENT,0,2);   
      }else{
        writeFunctionalTerm(t,trace,recIndex);   
      }
    }

    private void writeAction(Term t, HashMap<Term,Integer> trace, int recIndex) throws TermWareException
    {
      if (t.getArity()==2)  {
        out_.putBegin(PrettyPrintingBreakType.INCONSISTENT,0,2);   
        writeTerm(t.getSubtermAt(0));
        out_.putBreak(PrettyPrintingBreakType.INCONSISTENT, 1, 1);
        out_.putString("[");
        out_.putBreak(PrettyPrintingBreakType.INCONSISTENT, 1, 1);
        writeTerm(t.getSubtermAt(1));
        out_.putBreak(PrettyPrintingBreakType.INCONSISTENT, 1, 1);
        out_.putString("]");
        out_.putEnd(PrettyPrintingBreakType.CONSISTENT,0,2);   
      }else{
        writeFunctionalTerm(t,trace,recIndex);   
      }
    }
    
    private void writeLet(Term t, HashMap<Term,Integer> trace, int recIndex) throws TermWareException
    {
      if (t.getArity()==2) {
         out_.putBegin(PrettyPrintingBreakType.INCONSISTENT,0,2);   
         out_.putString("let");
         writeWhereAssignments(t.getSubtermAt(0),trace,recIndex);
         out_.putBreak(PrettyPrintingBreakType.INCONSISTENT, 1, 2);
         writeTerm(t.getSubtermAt(1),trace,recIndex+1);
         out_.putEnd(PrettyPrintingBreakType.INCONSISTENT,0,2);   
      }else{
         writeFunctionalTerm(t,trace,recIndex);    
      }  
    }

    private void writeWhere(Term t, HashMap<Term,Integer> trace, int recIndex) throws TermWareException
    {
      if (t.getArity()==2) {
         out_.putBegin(PrettyPrintingBreakType.INCONSISTENT,0,2);   
         writeTerm(t.getSubtermAt(0),trace,recIndex+1);
         out_.putBreak(PrettyPrintingBreakType.INCONSISTENT, 1, 2);
         out_.putString("where");
         writeWhereAssignments(t.getSubtermAt(1),trace,recIndex);                  
         out_.putEnd(PrettyPrintingBreakType.INCONSISTENT,0,2);   
      }else{
         writeFunctionalTerm(t,trace,recIndex);    
      }  
    }

    private void writeWhereAssignments(Term t, HashMap<Term,Integer> trace, int recIndex) throws TermWareException
    {
      if (t.isComplexTerm() && t.getNameIndex().equals(TermWareSymbols.CONS_INDEX) && t.getArity()==2)  {
         Term curr = t;
         out_.putString("(");
         out_.putBreak(PrettyPrintingBreakType.INCONSISTENT, 1, 2);
         boolean frs=true;
         while(!curr.isNil()) {
             if (!frs) {
                 out_.putString(",");
                 out_.putBreak(PrettyPrintingBreakType.INCONSISTENT, 1, 2);               
             }else{
                 frs=false;
             }
             Term pair = curr.getSubtermAt(0);
             curr=curr.getSubtermAt(1);
             if (pair.getArity()==2) {
                 writeTerm(pair.getSubtermAt(0),trace,recIndex);               
                 out_.putString("<-");
                 writeTerm(pair.getSubtermAt(1),trace,recIndex);                 
             }else{
                 writeTerm(pair,trace,recIndex+1);
             }
         }
         out_.putBreak(PrettyPrintingBreakType.INCONSISTENT, 1, 2);
         out_.putString(")");
      }else{
         writeFunctionalTerm(t,trace,recIndex);      
      }
    }
    
    
    private void writeSimpleRepresentation(Term t)
    {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      PrintWriter pwr = new PrintWriter(out);
      t.print(pwr);
      pwr.flush();
      out_.putString(out.toString());        
    }
            
    
    private boolean checkTrace(Term t, HashMap trace, int recIndex)
    {
      Object o = trace.get(t);
      if (o!=null) {
         Integer traceInteger=(Integer)o;
         out_.putString("@"+traceInteger.toString());
         return true;
      }else{
         return false;
      }
    }

    private void writeConsTerm(Term t,HashMap<Term,Integer> trace,int recIndex) throws TermWareException
    {
        if (checkTrace(t,trace, recIndex)) {
           return;
        }
        out_.putBegin(PrettyPrintingBreakType.INCONSISTENT);
        boolean frs = false;
        Term curr = t;
        out_.putString("[");
        out_.putInconsistentBreak(0);
        while(!curr.isNil()) {
            if (!frs) {                
                out_.putString(",");
                out_.putInconsistentBreak(1);
            }else{
                frs=true;
            }
            Term car = curr.getSubtermAt(0);
            writeTerm(car,trace,recIndex+1);            
            out_.putInconsistentBreak(0);           
            curr = curr.getSubtermAt(1);
            if (!curr.isNil()) {
                if (!curr.getNameIndex().equals(TermWareSymbols.CONS_INDEX)
                        ||
                        curr.getArity()!=2
                        ){
                    // invalid cons.
                    writeFunctionalTerm(curr,trace,recIndex+1);
                    break;
                }
            }
        }
        out_.putString("]");
        out_.putEnd(PrettyPrintingBreakType.INCONSISTENT);
    }
    
    
    static class OperatorRecord
    {
        String name;
        int priority;
        boolean isInfix;
        boolean isPrefix;
        boolean isSuffix;
        String infix;
        String prefix;
        String suffix;
        
        OperatorRecord(
                String theName,
                int    thePriority,
                boolean theIsInfix,
                boolean theIsPrefix,
                boolean theIsSuffix,
                String  theInfix,
                String  thePrefix,
                String  theSuffix
                      )
        {
          name=theName;
          priority=thePriority;
          isInfix=theIsInfix;
          isPrefix=theIsPrefix;
          isSuffix=theIsSuffix;
          infix=theInfix;
          prefix=thePrefix;
          suffix=theSuffix;
        }

    }
    
    static OperatorRecord[] operatorRecords_= {
        new OperatorRecord("rule",  1, true, false, false, "->", "", ""),
        new OperatorRecord("minus", 3, true,false,false, "-","",""),
        new OperatorRecord( "plus",  3, true,false,false, "+","","" ),
        new OperatorRecord("set",   3, false,true,true , "" ,"{","}"),
        new OperatorRecord("multiply", 5, true, false, false, "*", "","")
                
    };
    
    //TODO: change to binary search
    private int getOperatorCode(String name)
    {
        int code=0;
        while(code!=operatorRecords_.length) {
            if (operatorRecords_[code].name.equals(name)) {
                return code;
            }
            ++code;
        }
        return -1;
    }
    
    private int getOperatorPriority(int code)
    {
        if (code==-1) {
            return 100;
        }else{
            return operatorRecords_[code].priority;
        }
    }
    
    private int getOperatorPriority(String name)
    {
      return getOperatorPriority(getOperatorCode(name));  
    }
    
}
