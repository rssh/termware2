/*
 * SystemsSuite.java
 *
 */

package ua.gradsoft.termwaretests.systems;

import junit.framework.*;

/**
 * Suite for tests for various term systems (buildin and custom).
 */
public class SystemsTestSuite extends TestSuite
{
    
    /** Creates a new instance of SystemsSuite */
    public SystemsTestSuite() {
        this.addTestSuite(StringSystemTests.class);
        this.addTestSuite(GeneralSystemPlusTests.class);
        this.addTestSuite(GeneralSystemMinusTest.class);
        this.addTestSuite(GeneralSystemMultiplyTest.class);
        this.addTestSuite(GeneralSystemDivideTest.class);
        this.addTestSuite(GeneralSystemTest.class);
        this.addTestSuite(GeneralSystemCompareTests.class);
        this.addTestSuite(GeneralSystemApplyTest.class);
        this.addTestSuite(GeneralSystemLetTest.class);
        this.addTestSuite(GeneralSystemListTest.class);
        this.addTestSuite(GeneralSystemRJavaTest.class);
        this.addTestSuite(GeneralSystemtestDoubleAriphmetics.class);
        this.addTestSuite(ListSystemTest.class);
        this.addTestSuite(SimpleRulesTestCase.class);
        this.addTestSuite(Number2StringTestCase.class);
    }
    
    
}
