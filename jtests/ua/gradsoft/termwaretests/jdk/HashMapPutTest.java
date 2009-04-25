
package ua.gradsoft.termwaretests.jdk;

import java.util.HashMap;
import junit.framework.TestCase;

/**
 *Test for put on hashmap
 * @author rssh
 */
public class HashMapPutTest extends TestCase
{

    
    public void testHashMapPut() throws Exception
    {
        HashMap<String,String> hm1 = new HashMap<String,String>();
        HashMap<String,String> hm2 = new HashMap<String,String>();
        hm2.put("1","1");
        hm2.put("2","2");
        hm1.putAll(hm2);
        assertTrue(hm1.size()!=0);        
    }

}
