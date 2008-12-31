/*
 * XMLTermParser.java
 *
 * Created 24, 07, 2005, 7:18
 *
 * Owner: Ruslan Shevchenko
 *
 * Description:
 *
 * History:
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termware.parsers.xml.terms;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import ua.gradsoft.termware.IParser;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareInstance;
import ua.gradsoft.termware.exceptions.ExternalException;
import ua.gradsoft.termware.exceptions.TermParseException;
import ua.gradsoft.termware.exceptions.TermSAXParseException;

/**
 * Parser interface for XML representation of TermWare rules.
 *for DTD and grammar see docs/jsr94api
 */
public class XMLTermParser implements IParser
{        
    
  

    public XMLTermParser(Reader inputReader,String inFname,TermWareInstance instance) throws TermWareException
    {
        instance_=instance;
        inputReader_=inputReader;
        instance_=instance;
        saxParse();
    }
    
    public Term  readTerm()  throws TermWareException
    {       
       if (it_.hasNext()) {
           return (Term)it_.next();
       }else{
           throw new TermParseException("eof reached");
       }       
    }

    public boolean  eof()
    {      
      return ! it_.hasNext();
    }
    
             

    private void saxParse() throws TermWareException
    {
       try { 
         SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
         SAXParser saxParser=saxParserFactory.newSAXParser();
         XMLTermWareEntityResolver entityResolver = new XMLTermWareEntityResolver();         
         XMLReader xmlReader = saxParser.getXMLReader();
         xmlReader.setEntityResolver(entityResolver);
         TermSAXContentHandler contentHandler = new TermSAXContentHandler(instance_);
         xmlReader.setContentHandler(contentHandler);
         xmlReader.parse(new InputSource(inputReader_));
         //saxParser.parse(inputStream_,contentHandler);
         terms_=contentHandler.getTerms();  
         it_=terms_.iterator();         
       }catch(SAXException ex){             
             Throwable throwable = ex.getCause();
             if (throwable==null) {                 
                 throw new TermSAXParseException(ex);
             }else{
                 if (throwable instanceof TermWareException) {
                     throw (TermWareException)throwable;
                 }
             }
         }catch(ParserConfigurationException ex){
             throw new ExternalException(ex);
         }catch(IOException ex){
             throw new ExternalException(ex);
         }          
    }
    
    /**
     * function fro debug properties.
     */
    private void printTerms(PrintStream out)
    {
      Iterator it=terms_.iterator();
      while(it.hasNext()) {
          Term t = (Term)it.next();
          t.println(out);
      }
    }
    
    /**
     *List of terms, which is constructed by parser.
     */
    private List terms_=null;
    
    /**
     * iterator over <code> terms_ </code>
     */
    private Iterator it_=null;
    
    
    /**
     * input stream, which are passed to SAX Parser
     */
    private Reader inputReader_;
    
    /**
     * name of file or resource, from which we read.
     */
    private String fname_;
    
    /**
     * instance of termware 
     */
    private TermWareInstance instance_;
    
}
