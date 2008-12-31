/*
 * JavaGeneratorHelper.java
 *
 * Created on July 3, 2007, 8:25 PM
 *
 */

package ua.gradsoft.termware.debug;

import java.io.PrintWriter;
import java.util.List;

/**
 *Helper class for generation of Java source code.
 * @author rssh
 */
public class JavaGeneratorHelper {
    
    
    public static int generatePackage(PrintWriter writer, String sourceName)
    {
       writer.print("package ");
       String packageName=sourceName;
       if (packageName.endsWith(".def")) {
           packageName=packageName.substring(0,packageName.length()-4);
       }
       packageName=packageName.replace('/','.');
       writer.print(packageName);
       writer.println(";");
       return 1;
    }
    
    public static int generateImport(PrintWriter writer, String imported)
    {
        writer.print("import ");
        writer.print(imported);
        writer.println(";");
        return 1;
    }
    
    public static int generateClassEntry(PrintWriter writer, String className, String toExtend, List<String> toImplement)
    {
        writer.print("public class ");
        writer.print(className);      
        if (toExtend!=null){
            writer.print(" extends ");
            writer.print(toExtend);            
        }
        if (toImplement!=null && toImplement.size()!=0) {
            writer.print(" implements ");
            boolean frs=true;
            for(String s: toImplement) {
                if (frs) {
                    frs=false;
                }else{
                    writer.print(",");
                }
                writer.print(s);
            }
        }
        writer.println();
        return 1;
    }    
    
    
}
