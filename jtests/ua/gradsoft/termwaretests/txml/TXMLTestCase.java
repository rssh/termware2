/*
 * TXMLTestCase.java
 *
 * Created on: 9, 08, 2005, 2:35
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termwaretests.txml;

import junit.framework.TestCase;
import ua.gradsoft.termware.Domain;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author Ruslan Shevchenko
 */
public class TXMLTestCase extends TestCase
{
    
    /** Creates a new instance of TXMLTestCase */
    public TXMLTestCase(String name) {
        super(name);
    }
    
    protected void setUp()
    {
      TermWare.getInstance().init();  
    }
    
    public void testDefs1_1_XML() throws TermWareException
    {    
      Term systemTerm=TermWare.getInstance().load("ua/gradsoft/termwaretests/txml/defs1_1.xml", TermWare.getInstance().getParserFactory("XMLTermWare"), TermWare.getInstance().getTermFactory().createNIL());    
      
     // System.err.print("received term:"+TermHelper.termToString(systemTerm));
      
    //  TermWare.getInstance().getSysSystem().setDebugMode(true);      
    //  TermWare.getInstance().getSysSystem().setDebugEntity("All");
      TermWare.getInstance().getSysSystem().reduce(systemTerm);
      TermSystem system=TermWare.getInstance().resolveSystem("QQQ");
    }
    
    public void testDefs2_XML() throws TermWareException
    {    
      Term systemTerm=TermWare.getInstance().load("ua/gradsoft/termwaretests/txml/defs2.xml", TermWare.getInstance().getParserFactory("XMLTermWare"), TermWare.getInstance().getTermFactory().createNIL());    
      
      //System.err.print("received term:"+TermHelper.termToString(systemTerm));
      
    //  TermWare.getInstance().getSysSystem().setDebugMode(true);      
    //  TermWare.getInstance().getSysSystem().setDebugEntity("All");
      TermWare.getInstance().getSysSystem().reduce(systemTerm);
      Domain testDomain=TermWare.getInstance().getDomain("TestDomain");
      TermSystem system1=testDomain.resolveSystem("TestSystem1");
      TermSystem system2=testDomain.resolveSystem("TestSystem2");
    }
    
}
