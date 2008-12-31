/*
 * TermLoader.java
 *
 */

package ua.gradsoft.termware;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import ua.gradsoft.termware.exceptions.ExternalException;
import ua.gradsoft.termware.exceptions.ResourceNotFoundException;

/**
 *Abstract class for loading of terms from extern al storage.
 *(such as file system or classpath or more)
 * @author Ruslan Shevchenko
 */
public abstract class TermLoader {
            
    
   public  Term load(String name,
                     IParserFactory parserFactory,
                     Term parserFactoryArgs)
                                              throws TermWareException
  {
    InputStreamSource inputStreamSource=null;
    InputStream inputStream = null;
    InputStreamReader inputStreamReader = null;
   
    inputStreamSource = getSource(name);
    inputStream = inputStreamSource.getInputStream();
    inputStreamReader = new InputStreamReader(inputStream,Charset.forName("UTF-8"));

    try {
     IParser parser = parserFactory.createParser(inputStreamReader,inputStreamSource.getResourceName(),parserFactoryArgs,instance_);
     Term t=null;
     Term retval=null;
     while(!parser.eof()) {
          t=parser.readTerm();
        if (retval==null) {
          retval=t;
        }else {
          if (t==null) {
              return retval;
          }else{
              if (!t.isNil()) {
                // TODO: for now we 'cons' few terms in file ?
                // TODO: may be discard this or find semantics for few  terms in file
                // TODO: throught ';'
                retval=instance_.getTermFactory().createTerm("cons",retval,t);
              }
          }
        }
     }
     if (retval==null) {
         return instance_.getTermFactory().createNil();
     }
     return retval;
   }catch(TermWareException ex){
     throw ex;
   }finally{
     try {
       inputStream.close();
     }catch(IOException ex){
       instance_.getEnv().getLog().println("exception during closing "+inputStreamSource.getResourceName()+":"+ex.getMessage());       
     }
   }
  }

    
    
    
    /**
     * locate source by absolute name.
     */
    public abstract InputStreamSource getSource(String name) throws ResourceNotFoundException, ExternalException;
    
   
    /**
     * add search path
     */
    public abstract void addSearchPath(String path) throws ExternalException;
    
    /**
     *return read-only list of searh patches.
     */
    public abstract List<String> getSearchPathes() throws ExternalException;
            
     
    void  setTermWareInstance(TermWareInstance instance)
    {
     instance_=instance;   
    }
    
    protected TermWareInstance instance_;
    
}
