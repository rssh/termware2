/*
 * FileInputStreamSource.java
 *
 * Created 9, 05, 2005, 8:34
 */

package ua.gradsoft.termware.termloaders;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import ua.gradsoft.termware.InputStreamSource;
import ua.gradsoft.termware.exceptions.ExternalException;

/**
 *Input stream source for file system.
 * @author Ruslan Shevchenko
 */
public class FileInputStreamSource implements InputStreamSource
{
    
    /** Creates a new instance of FileInputStreamSource 
     *@param file  file to open.
     *@param resourceName - name of resource, which refered from debug entries.
     *(usially - path relative to som systems)
     */    
    public FileInputStreamSource(File file, String resourceName) {
        file_=file;        
        resourceName_=resourceName;
    }
        
    /**
     * create input stream for file found.
     */
    public InputStream getInputStream() throws ExternalException {
      try {  
        return new BufferedInputStream(new FileInputStream(file_));
      }catch(FileNotFoundException ex){
          throw new ExternalException(ex);
      }
    }

    /**
     * get absolute path of file.
     */
    public String getResourceName() {
        if (resourceName_==null) {
            resourceName_=file_.getAbsolutePath();
        }
        return resourceName_;
    }
    
     private File file_;
     private String resourceName_;
    
}
