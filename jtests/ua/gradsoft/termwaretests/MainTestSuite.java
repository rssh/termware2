/*
 * MainSuite.java
 *
 */

package ua.gradsoft.termwaretests;

import junit.framework.*;
import ua.gradsoft.termwaretests.debug.DebugTestSuite;
import ua.gradsoft.termwaretests.demos.labels.LabelsTestCase;
import ua.gradsoft.termwaretests.demos.life.LifeTestCase;        
import ua.gradsoft.termwaretests.jdk.JdkTestSuite;
import ua.gradsoft.termwaretests.jsr223.JSR223TestSuite;
import ua.gradsoft.termwaretests.util.UtilTestSuite;
import ua.gradsoft.termwaretests.systems.SystemsTestSuite;
import ua.gradsoft.termwaretests.old.OldTestCase;
import ua.gradsoft.termwaretests.prettyprinting.PrettyPrintingWriterTest;
import ua.gradsoft.termwaretests.strategies.StrategiesTestSuite;
import ua.gradsoft.termwaretests.systems.ConditionalRuleTestCase;
import ua.gradsoft.termwaretests.txml.TXMLTestSuite;

/**
 *Main test suite.
 * @author Ruslan Shevchenko
 */
public class MainTestSuite extends TestSuite
{
    
    /** Creates a new instance of MainSuite */
    public MainTestSuite() {
        
        
        addTest(new UtilTestSuite());
        addTest(new TestSuite(TermTest.class));
        addTest(new TestSuite(SubstitutionTestCase.class));
        addTest(new TestSuite(TransformationContextTest.class));        
        addTest(new TestSuite(TermSystemTest.class));        
        addTest(new SystemsTestSuite());        
        addTest(new TestSuite(OldTestCase.class));
        addTest(new TestSuite(ClassPatternTest.class));       
        addTest(new TestSuite(SetPatternTest.class));
        addTest(new TestSuite(ArgsPatternTest.class));
        addTest(new TestSuite(ListInArrayTest.class));
        addTest(new TestSuite(ParseTest.class));
        addTest(new TestSuite(ConditionalRuleTestCase.class));
        addTest(new TestSuite(FactsReduceTest.class));
        addTest(new TestSuite(TermSystemConcreteFirstTestCase.class));
        addTest(new TXMLTestSuite());        
        addTest(new TestSuite(LifeTestCase.class));
        addTest(new TestSuite(LabelsTestCase.class));
        addTest(new StrategiesTestSuite());
        addTest(new JdkTestSuite());
      
        addTest(new DebugTestSuite());
        addTest(new JSR223TestSuite());
         
        
        addTest(new TestSuite(PrettyPrintingWriterTest.class));
        
    }
    
    public static void main(String[] args)
    {
          junit.textui.TestRunner.run(new MainTestSuite());
    }
    

    public void testMakeJunitHappy() {}    
    
}
