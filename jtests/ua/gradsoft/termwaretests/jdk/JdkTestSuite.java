package ua.gradsoft.termwaretests.jdk;

import junit.framework.TestSuite;

/**
 *test suite for JDK stuff
 * @author rssh
 */
public class JdkTestSuite extends TestSuite
{

    public JdkTestSuite()
    {
        addTestSuite(HashMapPutTest.class);
    }


}
