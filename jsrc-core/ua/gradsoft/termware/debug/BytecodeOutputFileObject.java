/*
 * BytecodeOutputFileObject.java
 *
 * Created on July 3, 2007, 10:43 PM
 */

package ua.gradsoft.termware.debug;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;

/**
 *FileObject for in-memory compilation
 * @author rssh
 */
public class BytecodeOutputFileObject extends SimpleJavaFileObject
{
    
    /** Creates a new instance of BytecodeOutputFileObject */
    public BytecodeOutputFileObject(String name) throws URISyntaxException
    {
        super(new URI("file://"+name),Kind.CLASS);
        output_=new ByteArrayOutputStream();
    }

    /**
     * get output stream.
     */
    @Override   
    public OutputStream openOutputStream() {
        return output_;        
    }    
    
    /**
     * get resulting bytes.
     */
    public byte[] getBytes()
    {
      return output_.toByteArray();  
    }
    
    
    private ByteArrayOutputStream output_;
    
}
