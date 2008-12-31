/*
 * JavaCompiledFileAndLine.java
 *
 * Created on July 13, 2007, 8:33 PM
 *
 */

package ua.gradsoft.termware.debug;

/**
 *Get file and line for java complied object.
 * @author rssh
 */
public class JavaCompiledFileAndLine {

    public static SourceCodeLocation deduceFileAndLine(int stackLevel)
    {
      StackTraceElement[] elements = Thread.currentThread().getStackTrace();
      SourceCodeLocation retval=null;
      if (elements.length==0) {
          // it means that JVM does not provide stack traces.
          retval=new SourceCodeLocation("unknown",0,0);
      }else if (stackLevel > elements.length) {
          // top caller is less, that requested stack level.
          // so, get least recent call.
          retval=deduceFileAndLine(elements[elements.length-1]);
      }else{
          retval=deduceFileAndLine(elements[stackLevel]);
      }
      return retval;
    }
    
    public static SourceCodeLocation deduceFileAndLine(StackTraceElement e)
    {
       String fname = e.getFileName();
       if (fname==null) {
           //try to deduce name of file from name of class.
           fname=classNameToFileName(e.getClassName());
       }
       int line = e.getLineNumber();
       // this is give us negative line number on running without debug information.  
       if (line < 0) {
           line=0;
       }
       return new SourceCodeLocation(fname,line,line);
    }
    
    static String classNameToFileName(String className)
    {
      String retval=className;  
      int nestedIndex = className.indexOf("$");
      if (nestedIndex!=-1) {
          retval=retval.substring(0,nestedIndex);
      }
      retval=retval.replace(".","/");
      retval=retval+".java";
      return retval;
    }
    
}
