/*
 * FileAndLineAccessTestCase.java
 *
 * Created on July 4, 2007, 11:44 AM
 *
 */

package ua.gradsoft.termwaretests.debug;

import java.util.Iterator;
import junit.framework.TestCase;
import ua.gradsoft.termware.DefaultFacts;
import ua.gradsoft.termware.ITermTransformer;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.debug.SourceCodeAccessHelper;
import ua.gradsoft.termware.debug.SourceCodeLocation;
import ua.gradsoft.termware.strategies.FirstTopStrategy;
import ua.gradsoft.termware.util.RuleTransformer;
import ua.gradsoft.termware.util.TransformersStar;

/**
 *Test case for access information about source code.
 * @author rssh
 */
public class FileAndLineAccessTestCase extends TestCase
{
    
    protected void setUp()
    {
       TermWare.setInDebug(true);
       TermWare.getInstance().init();
    }
    
    protected void tearDown()
    {
        TermWare.setInDebug(false);
    }
    
    
    public void testInlineUnification() throws Exception
    {
        TermSystem system = new TermSystem(new FirstTopStrategy(),new DefaultFacts());               
        system.addRule("S([$x:$y]) [ isNumber($x) ] -> $x+S($y) !-> UNKNOWN");
        system.addRule("S($x) [ isNumber($x) ] -> $x !-> UNKNOWN");
        system.addRule("$x+UNKNOWN -> UNKNOWN");      
        system.addRule("S([]) -> 0 ");

        TransformersStar star = system.getStrategy().getStar();
        Iterator<ITermTransformer> it = star.iterator("plus");
        ITermTransformer tr = it.next();
        assertTrue("transformer for plus must be rule", tr instanceof RuleTransformer);

        RuleTransformer rtr = (RuleTransformer)tr;
        SourceCodeLocation scl = SourceCodeAccessHelper.getLocationOfTerm(rtr.getInPattern());
        
        //System.err.println();
        //System.err.println("File="+scl.getFileName()+", line="+scl.getBeginLine());
        assertTrue(scl.getFileName().endsWith("FileAndLineAccessTestCase.java"));
        
        
    }
    
    public void testUnification1() throws Exception
    {
       TermSystem xyz = TermWare.getInstance().getRoot().getOrCreateSubdomain("examples").resolveSystem("xyz");
       TransformersStar star = xyz.getStrategy().getStar();
       Iterator<ITermTransformer> it=star.iterator("x");
       ITermTransformer tr = it.next();
       assertTrue("transformer for x must be rule", tr instanceof RuleTransformer);
       RuleTransformer rtr = (RuleTransformer)tr;
       SourceCodeLocation scl = SourceCodeAccessHelper.getLocationOfTerm(rtr.getInPattern());
       assertEquals("name of examples/xyz","examples/xyz.def",scl.getFileName());              
    }
    
    
    
}
