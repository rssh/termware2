/*
 * FileSystemTermLoader.java
 *
 * (C) Rulsan Shevchenko <Ruslan@Shevchenko.Kiev.UA>, 2005
 */

package ua.gradsoft.termware.termloaders;

import java.io.File;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import ua.gradsoft.termware.InputStreamSource;
import ua.gradsoft.termware.TermLoader;
import ua.gradsoft.termware.exceptions.ExternalException;
import ua.gradsoft.termware.exceptions.ResourceNotFoundException;

/**
 *Loader for terms from file system.
 */
public class FileSystemTermLoader extends TermLoader
{
    
    FileSystemTermLoader()
    { super(); }
    
    public InputStreamSource getSource(String name) throws ResourceNotFoundException, ExternalException 
    {                
        File f=new File(name);
        //if (f.isAbsolute()) {
            if (f.canRead()) return new FileInputStreamSource(f,name);           
        //}
        lazyInitSearchPathes();
        ListIterator it=searchPathes_.listIterator();
        while(it.hasNext()) {
          Object o = it.next();
          String s = (String)o;
          if (!s.endsWith(File.separator)) {
            s=s+File.separator;
          }
          f = new File(s+name);          
          if (f.canRead()) return new FileInputStreamSource(f,name);    
        }
        throw new ResourceNotFoundException(name);
    }

    public void addSearchPath(String path) {
        if (searchPathes_==null) {
            searchPathes_=new ArrayList();
        }
        searchPathes_.add(path);
    }
    
    public List<String>  getSearchPathes() throws ExternalException
    {
       lazyInitSearchPathes();
       return searchPathes_;
    }
        
 private  synchronized void  lazyInitSearchPathes() throws ExternalException     
 {
  if (searchPathes_!=null) return;
  searchPathes_=new ArrayList();
  String path=System.getProperty("termware.path");
  String fileSeparator=System.getProperty("file.separator");

  if (path==null) {
      
      // not found, set manually to
      //   /usr/local/share/TermWare on UNIX
      //   C:\TermWare on Windows
      
      if (fileSeparator==null) {
        fileSeparator=File.separator;
      }else if (fileSeparator.equals("/")) { // we assume, that this is UNIX
        path="/usr/local/share/TermWare";
      }else{ // we assume that this is Win32.
        path="C:\\TermWare";
      }
  }else{
     // System.err.println("property termware.path is found");
  }
  String pathSeparator=System.getProperty("path.separator");
  if (pathSeparator==null) {
      pathSeparator=File.pathSeparator;
  }
  try {
   StringReader reader=new StringReader(path);
   StreamTokenizer tokenizer = new StreamTokenizer(reader);
   tokenizer.resetSyntax();
   tokenizer.wordChars('a','z');
   tokenizer.wordChars('A','Z'); 
   tokenizer.wordChars('0','9'); 
   tokenizer.wordChars(' ',' ');
   tokenizer.wordChars('.','.');
   tokenizer.wordChars(fileSeparator.charAt(0),fileSeparator.charAt(0));
   if (pathSeparator.equals(";")) {
     tokenizer.wordChars(':',':');
   }
   tokenizer.whitespaceChars(pathSeparator.charAt(0),pathSeparator.charAt(0));
   while(tokenizer.nextToken()!=StreamTokenizer.TT_EOF) {
     if (tokenizer.ttype==StreamTokenizer.TT_WORD) {
       searchPathes_.add(tokenizer.sval);      
     }else if(tokenizer.ttype=='"') {
       searchPathes_.add(tokenizer.sval.substring(1,tokenizer.sval.length()-1));
     }
   }
  }catch(IOException ex){
   throw new ExternalException(ex);
  }
 }

  private ArrayList  searchPathes_;
    
}
