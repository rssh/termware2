/*
 * ClassPathTermLoader.java
 *
 */

package ua.gradsoft.termware.termloaders;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import ua.gradsoft.termware.InputStreamSource;
import ua.gradsoft.termware.TermLoader;
import ua.gradsoft.termware.exceptions.ExternalException;
import ua.gradsoft.termware.exceptions.ResourceNotFoundException;

/**
 *TermLoader, which load terms from current classpath.
 * @author Ruslan Shevchenko
 */
public class ClassPathTermLoader extends TermLoader
{
    
    /** Creates a new instance of ClassPathTermLoader */
    public ClassPathTermLoader() {
    }

    /**
     * do nothing
     */
    public void addSearchPath(String path) {
       
    }
    
    /**
     * return empty list
     */
    public List<String>  getSearchPathes()
    { return Collections.emptyList(); }

    public InputStreamSource getSource(String name) throws ResourceNotFoundException, ExternalException 
    {
      ClassLoader cl=Thread.currentThread().getContextClassLoader();  
      if (cl==null) {
          throw new ResourceNotFoundException(name);
      }
      InputStream inputStream=cl.getResourceAsStream(name);  
      if (inputStream==null) {
          throw new ResourceNotFoundException(name);
      }
      return new GenericInputStreamSource(inputStream,name);
    }
    
}
