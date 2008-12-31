/*
 * FileOrClassTermLoader.java
 *
 */

package ua.gradsoft.termware.termloaders;

import java.util.List;
import ua.gradsoft.termware.InputStreamSource;
import ua.gradsoft.termware.TermLoader;
import ua.gradsoft.termware.exceptions.ExternalException;
import ua.gradsoft.termware.exceptions.ResourceNotFoundException;

/**
 *TermLoader, which search at first in file system, if not found -- in currenct classpath.
 */
public class FileOrClassTermLoader extends TermLoader
{
    
    /** Creates a new instance of FileOrClassTermLoader */
    public FileOrClassTermLoader() 
    {
            fsLoader_=new FileSystemTermLoader();
            clLoader_=new ClassPathTermLoader();
    }
    

    public void  addSearchPath(String path)
    {
      fsLoader_.addSearchPath(path);  
    }
    
    public List<String>  getSearchPathes() throws ExternalException
    { return fsLoader_.getSearchPathes(); }
    
    public InputStreamSource getSource(String name) throws ResourceNotFoundException, ExternalException 
    {      
      try {
         return fsLoader_.getSource(name);   
      }catch(ResourceNotFoundException ex){
          return clLoader_.getSource(name);
      }
    }


    
    private FileSystemTermLoader fsLoader_;
    private ClassPathTermLoader  clLoader_;
    
}
