/*
 * SMAPHelper.java
 *
 * Created on July 3, 2007, 8:21 PM
 *(
 */

package ua.gradsoft.termware.debug;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import ua.gradsoft.termware.exceptions.ExternalException;

/**
 *Helper for work with JSR-45 SMAP files.
 * @author rssh
 */
public class SMAPHelper {

  
    
    public static void  generateHeader(PrintWriter writer, String outputFileName)
    {
      writer.println("SMAP");   
      writer.println(outputFileName);
      writer.println("TermWare");
    }
    
    public static void generateStratumSection(PrintWriter writer, String languageName)
    {
      writer.print("*S ");
      writer.println(languageName);
    }
    
    public static void generateFileSection(PrintWriter writer, int fileId, String filename)
    {
      writer.println("*F");  
      if (filename.contains("/")) {
          String shortName = filename.substring(filename.lastIndexOf('/')+1);
          writer.print("+ ");
          writer.print(fileId);
          writer.print(" ");
          writer.println(shortName);
          writer.println(filename);
      }else{
          writer.print(fileId);
          writer.print(" ");
          writer.println(filename);
      }
    }
    
    public static void startLineSection(PrintWriter writer)
    {
      writer.println("*L");  
    }
    
    /**
     * map enture source code location to outLineNumber
     */
    public static void generateLineInfo(PrintWriter writer,int fileId,SourceCodeLocation inLocation,int outLineNumber)
    {
      writer.print(inLocation.getBeginLine());
      writer.print("#");
      writer.print(fileId);
      writer.print(",");
      writer.print(inLocation.getEndLine()-inLocation.getBeginLine()+1);
      writer.print(":");
      writer.print(outLineNumber);
      writer.print(",1");
      writer.println();
    }
    
    public static void generateEndSection(PrintWriter writer)
    {
      writer.println("*E");    
    }
    
    public static byte[] insertSmap(byte[] classBytes, CharSequence smap) throws ExternalException
    {
      try {  
        SDEInserter inserter = new SDEInserter(classBytes,smap.toString().getBytes("UTF-8"));     
        return inserter.getResult();
      }catch(UnsupportedEncodingException ex){
          throw new ExternalException("SDE insertion failed",ex);
      }catch(IOException ex){
          throw new ExternalException("SDE insertion failed",ex);
      }
    }
    
    
}
