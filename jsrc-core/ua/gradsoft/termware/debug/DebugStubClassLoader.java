/*
 * DebugStubClassLoader.java
 *
 * Created on July 4, 2007, 1:51 AM
 *
 * (C) Grad-Soft Ltd, Kiev, Ukraine.
 *http://www.gradsoft.ua
 */

package ua.gradsoft.termware.debug;

/**
 *Class loader for compiled debug stubs.
 * @author rssh
 */
public class DebugStubClassLoader extends ClassLoader
{
    
    /** Creates a new instance of DebugStubClassLoader */
    public DebugStubClassLoader(String className, byte[] classBytes) {
        theClass_=this.defineClass(className,classBytes,0,classBytes.length);
    }
    
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (name.equals(theClass_.getName())) {
            return theClass_;
        }else{
            return super.findClass(name);
        }
    }
    
    public Class getStubClass()
    { return theClass_; }
    
    private Class<?> theClass_;
}
