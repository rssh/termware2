/*
 */

package ua.gradsoft.termwaretests.prettyprinting;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import junit.framework.TestCase;
import ua.gradsoft.termware.NILTerm;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermFactory;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.printers.PrettyPrintWriter;
import ua.gradsoft.termware.printers.PrettyPrintingBreakType;

/**
 *
 * @author rssh
 */
public class PrettyPrintingWriterTest extends TestCase {
    

    private void prettyPrintTerm(Term t, PrettyPrintWriter ppw, 
            PrettyPrintingBreakType beginBreakType, 
            PrettyPrintingBreakType endBreakType,
            boolean                nlAfterComma)
    {
       if (t.isComplexTerm()) {
           ppw.putBegin(beginBreakType,0,0);
           ppw.putString(t.getName());
           ppw.putString("(");           
           ppw.putInconsistentBreak(0);
           boolean isFirst=true;
           for(int i=0; i<t.getArity(); ++i) {
               if (!isFirst) {
                 ppw.putString(",");
                 if (nlAfterComma) {
                     ppw.putConsistentBreak(0);                   
                 }else{
                     ppw.putInconsistentBreak(1);                   
                 }
                 
               }else{
                 isFirst=false;  
               }
               prettyPrintTerm(t.getSubtermAt(i),ppw,beginBreakType,endBreakType,nlAfterComma);
           }
           ppw.putInconsistentBreak(0);
           ppw.putString(")");
           ppw.putEnd(endBreakType,0,0);
       }else{
           String s = TermHelper.termToString(t);
           ppw.putInconsistentBreak(0);
           ppw.putString(s);
           ppw.putInconsistentBreak(0);
       }
    }
    

    private Term createIntList(int size) throws TermWareException
    {
        TermFactory tf = TermWare.getInstance().getTermFactory();
        Term retval = NILTerm.getNILTerm();
        for(int i=0; i<size; ++i) {
            retval = tf.createTerm("cons", tf.createInt(i),retval);
        }
        return retval;
    }
    
    /*
    public void testPrettyPrintParseOne() throws Exception
    {
        boolean debug=true;
        Term l = createIntList(200);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter pwr = new PrintWriter(out);
        PrettyPrintWriter ppwr = new PrettyPrintWriter(pwr);
        
        prettyPrintTerm(l,ppwr,PrettyPrintingBreakType.INCONSISTENT,PrettyPrintingBreakType.FITS,false);
            
        ppwr.flush();
        String s= out.toString();
        
        if (debug) {
          System.out.println("---");
          System.out.println(s);
          System.out.println("---");
        }
        
    }
    */

    
    public void doTestPrettyPrintParseTwo(int x, PrettyPrintingBreakType beginBreak, PrettyPrintingBreakType endBreak, boolean nlAfterComma, boolean debug) throws Exception
    {        
        Term l = createIntList(x);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter pwr = new PrintWriter(out);
        PrettyPrintWriter ppwr = new PrettyPrintWriter(pwr);
        
        prettyPrintTerm(l,ppwr,beginBreak,endBreak,nlAfterComma);
           
        ppwr.flush();
        String s= out.toString();
        
        if (debug) {
           System.out.println("---");
           System.out.println(s);
           System.out.println("---");
        }
        
        
        int minNLines = s.length()/80;
        
        String[] lines = s.split("\n");
        if (debug) {
          System.out.printf("s.length=%d,lines.length=%d\n", s.length(), lines.length);
        }
        
        assertTrue(lines.length >= minNLines);
        
        Term t1 = TermWare.getInstance().getTermFactory().createParsedTerm(s);        
        
    }

    public void testPrettyPrintParseTwo1() throws Exception
    {
      doTestPrettyPrintParseTwo(10,PrettyPrintingBreakType.CONSISTENT, PrettyPrintingBreakType.INCONSISTENT, false, false);
    }
    
    public void testPrettyPrintParseTwo2() throws Exception
    {
      doTestPrettyPrintParseTwo(10,PrettyPrintingBreakType.INCONSISTENT, PrettyPrintingBreakType.INCONSISTENT, false,false);
    }

    public void testPrettyPrintParseTwo3() throws Exception
    {
      doTestPrettyPrintParseTwo(10,PrettyPrintingBreakType.INCONSISTENT, PrettyPrintingBreakType.FITS, false,false);
    }

    public void testPrettyPrintParseTwo1_1() throws Exception
    {
      doTestPrettyPrintParseTwo(30,PrettyPrintingBreakType.CONSISTENT, PrettyPrintingBreakType.FITS, false, false);
    }

    public void testPrettyPrintParseTwo1_2() throws Exception
    {
      doTestPrettyPrintParseTwo(30,PrettyPrintingBreakType.FITS, PrettyPrintingBreakType.FITS, false, false);
    }

    public void testPrettyPrintParseTwo1_3() throws Exception
    {
      doTestPrettyPrintParseTwo(30,PrettyPrintingBreakType.FITS, PrettyPrintingBreakType.INCONSISTENT, false, false);
    }

    public void testPrettyPrintParseTwo1_4() throws Exception
    {
      doTestPrettyPrintParseTwo(30,PrettyPrintingBreakType.INCONSISTENT, PrettyPrintingBreakType.INCONSISTENT, false, false);
    }

    public void testPrettyPrintParseTwo1_5() throws Exception
    {
      doTestPrettyPrintParseTwo(30,PrettyPrintingBreakType.FITS, PrettyPrintingBreakType.INCONSISTENT, true, false);
    }
    

}
