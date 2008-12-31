/*
 * APIGen.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 *http://www.gradsoft.ua
 * All Rights Reserved
 */


package ua.gradsoft.termware;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import ua.gradsoft.termware.envs.SystemEnv;
import ua.gradsoft.termware.exceptions.AssertException;


/**
 * Generator of HTML Help API for TermWare systems.
 * @author Ruslan Shevchenko
 */
public class APIGen {

        
 
    private APIGen() {
    }
    
    private void init(String[] args) throws TermWareException
    {
      for(int i=0; i<args.length; ++i) {
          if (args[i].equals("-o")) {
              if (i==args.length-1) {
                  throw new AssertException("Option -o require argument");
              }
              outDir_=args[++i];
          }
      }
      if (outDir_==null) {
          throw new AssertException("option -o is not set");
      }
      IEnv env = new SystemEnv();
      
      TermWare.getInstance().init();
      File outDirFile=new File(outDir_);
      if (outDirFile.exists()) {
        if (!outDirFile.isDirectory()) {          
          throw new AssertException("argument of option -o must be directory");
        }
      }else{
        System.out.println("created drectory :"+outDir_);  
        outDirFile.mkdir();
      }
    }
    
    private void generateIndexPage() throws IOException
    {
      PrintStream out=openPrintStream("","index.html");
      printHeader(out,"TermWare specification");
      out.println("<FRAMESET cols=\"20%,80%\">");
      out.println("<FRAMESET rows=\"30%,70%\">");
      out.println("<FRAME src=\"systems.html\" name=\"systemsFrame.html\">");
      out.println("<FRAME src=\"all-patterns.html\" name=\"patternFrame\">");
      out.println("</FRAMESET>");
      out.println("<FRAME src=\"ma.html\" name=\"transformerFrame\">");
      out.println("</FRAMESET>");
      out.println("<NOFRAMES>");
      out.println("<H2> Frame Alert</H2>");
      out.println("<p>");
      out.println("This document is designed to be viewed using the frames feature. If you see this message, you are using a non-frame-capable web client.");
      out.println("</HTML>");
      out.close();
    }
    
    private void generateSystemsPage()  throws IOException, TermWareException
    {
      PrintStream out=openPrintStream("","systems.html");   
      printHeader(out,"TermWare systems");
      out.println("<BODY BGCOLOR=\"white\">");
      out.println("<TABLE BORDER=\"0\" WIDTH=\"100%\">");
      out.println("<TR>");
      out.println("<TD NOWRAP><FONT size=\"+1\">");
      out.println("Systems");
      out.println("</FONT></TD>");
      out.println("</TR></TABLE>");
      out.println("<TABLE BORDER=\"0\" WIDTH=\"100%\">");
      out.println("<TR><TD NOWRAP><P>");
      out.println("<FONT>");
      printListOfSystems(out,TermWare.getInstance().getRoot(),"");
      out.println("<BR>");
      out.println("<FONT +1>");
      out.println("<A href=\"all-patterns.html\" target=\"patternFrame\" >");
      out.println("all patterns");
      out.println("</A></FONT>");
      out.println("</TD>");
      out.println("</TR>");
      out.println("</TABLE>");
      out.println("<P>&nbsp;</BODY></HTML>");
      out.close();
    }
    
    
    
    private void printListOfSystems(PrintStream out,Domain domain, String prefix) throws TermWareException
    {
     SortedSet directSystemNames=domain.getNamesOfSystems();
     Iterator it=directSystemNames.iterator();
     while(it.hasNext()) {
         String name=(String)it.next();
         out.print("<A HREF=\""+prefix+name+"/all-patterns.html\" TARGET=\"patternFrame\">" );
         out.print(name);
         out.print("</A><BR>");
         out.println();
     }
     SortedSet subdomainNames=domain.getNamesOfDirectSubdomains();
     it=subdomainNames.iterator();
     while(it.hasNext()) {
         String name=(String)it.next();
         printListOfSystems(out, domain.getDirectSubdomain(name), name+"/");
     }
    }
    
    private void generateAllPatternsPage() throws IOException
    {
      PrintStream out=openPrintStream("","all-patterns.html");   
      printHeader(out,"TermWare patterns list");
      out.println("<BODY BGCOLOR=\"white\">");
      out.println("<TABLE BORDER=\"0\" WIDTH=\"100%\">");
      out.println("<TR>");
      out.println("<TD NOWRAP><FONT size=\"+1\">");
      out.println("Patterns");
      out.println("</FONT></TD>");
      out.println("</TR></TABLE>");
      out.println("<TABLE BORDER=\"0\" WIDTH=\"100%\">");
      out.println("<TR><TD NOWRAP><P>");
      out.println("<FONT>");
      printListOfAllPatterns(out,TermWare.getInstance().getRoot(),"",0);
      out.println("</FONT>");
      out.println("</TD>");
      out.println("</TR>");
      out.println("</TABLE>");
      out.println("<P>&nbsp;</BODY></HTML>");
      out.close();      
    }
    
    
        
    private void printListOfAllPatterns(PrintStream out, Domain domain, String prefix,int level)
    {
        Iterator patternsIterator=allPatterns_.entrySet().iterator();
        while(patternsIterator.hasNext()) {
            Map.Entry me=(Map.Entry)patternsIterator.next();
            String patternName=(String)me.getKey();
            HashSet indexEntries=(HashSet)me.getValue();
            String indexUrl;
            if (indexEntries.isEmpty()) {
                // impossible.
                indexUrl="#";
            }else{
                Iterator it=indexEntries.iterator();
                if (it.hasNext()) {
                    PatternIndexEntry entry=(PatternIndexEntry)it.next();
                    indexUrl=entry.getPrefix()+entry.getSystemName()+"/index.html#"+patternName;
                }else{
                    //impossible
                    indexUrl="#";
                }
            }
            out.println("<A href=\""+indexUrl+"\" TARGET=\"transformerFrame\" >"+patternName+"</A>");
            out.println("<BR>");
        }
    }
    
    private void buildListOfAllPatterns() throws TermWareException
    {
        buildListOfAllPatterns(TermWare.getInstance().getRoot(),"",0);
    }
    
    private void buildListOfAllPatterns(Domain domain, String prefix, int level) throws TermWareException
    {
     SortedSet directSystemNames=domain.getNamesOfSystems();
     Iterator systemsIterator=directSystemNames.iterator();
     while(systemsIterator.hasNext()) {
         String name=(String)systemsIterator.next();
         TermSystem system=domain.resolveSystem(name);
         SortedSet<String> names=system.getPatternNames();
         Iterator namesIterator=names.iterator();
         while(namesIterator.hasNext()) {
             String patternName=(String)namesIterator.next();
             if (!allPatterns_.containsKey(patternName)) {
                 HashSet indexEntries=new HashSet();
                 allPatterns_.put(patternName,indexEntries);
             }
             HashSet systemsSet=(HashSet)allPatterns_.get(patternName);
             systemsSet.add(new APIGen.PatternIndexEntry(prefix,domain,name,level));
         }
     }
     SortedSet domainsSet=domain.getNamesOfDirectSubdomains();
     Iterator domainsIterator=domainsSet.iterator();
     while(domainsIterator.hasNext()) {
         String subdomainName=(String)domainsIterator.next();
         Domain subdomain=domain.getDirectSubdomain(subdomainName);
         buildListOfAllPatterns(subdomain, prefix+subdomainName+"/",level+1);
     }
    }
    
    private void generateSystemPages() throws IOException, TermWareException
    {
      generateSystemPages(TermWare.getInstance().getRoot(),"",0);        
    }

    private void generateSystemPages(Domain domain,String prefix,int level) throws IOException, TermWareException
    {
     SortedSet directSystemNames=domain.getNamesOfSystems();
     Iterator systemsIterator=directSystemNames.iterator();
     while(systemsIterator.hasNext()) {
         String name=(String)systemsIterator.next();
         PrintStream out=openPrintStream(prefix+name, "all-patterns.html");
         printHeader(out, "patterns for "+name);
         out.println("<BODY BGCOLOR=\"white\">");
         out.println("<TABLE BORDER=\"0\" WIDTH=\"100%\">");
         out.println("<TR>");
         out.println("<TD NOWRAP><FONT size=\"+1\">");
         out.println("Patterns");
         out.println("</FONT></TD>");
         out.println("</TR></TABLE>");
         out.println("<TABLE BORDER=\"0\" WIDTH=\"100%\">");
         out.println("<TR><TD NOWRAP><P>");
         out.println("<FONT>");
         
         TermSystem system=domain.resolveSystem(name);
         SortedSet names=system.getPatternNames();
         Iterator namesIterator=names.iterator();
         while(namesIterator.hasNext()) {
             String patternName=(String)namesIterator.next();
             String uri=prefix+name+"/index.html#"+patternName;
             out.print("<A href=\"../");
             for(int i=0; i<level; ++i) {
                 out.print("../");
             }
             out.print(uri+"\" TARGET=\"transformerFrame\" >");
             out.print(patternName);
             out.println("</A>");
             out.println("<BR>");
         }
         out.println("</FONT>");
         out.println("</TD>");
         out.println("</TR>");
         out.println("</TABLE>");
         out.println("<P>&nbsp;</BODY></HTML>");
         out.close();      
         out=openPrintStream(prefix+name, "index.html");
         TermSystem ts=domain.resolveSystem(name);
         printSystemIndexPage(out,prefix+name,ts);
         out.close();
     }
     SortedSet domainsSet=domain.getNamesOfDirectSubdomains();
     Iterator domainsIterator=domainsSet.iterator();
     while(domainsIterator.hasNext()) {
         String subdomainName=(String)domainsIterator.next();
         Domain subdomain=domain.getDirectSubdomain(subdomainName);
         generateSystemPages(subdomain, prefix+subdomainName+"/",level+1);
     }
    }
    
    private void printSystemIndexPage(PrintStream out, String systemName, TermSystem ts) throws IOException, TermWareException
    {
     printHeader(out,systemName);
     out.println("<BODY BGCOLOR=\"white\">");
     SortedSet patternNames=ts.getPatternNames();
     Iterator patternsIterator=patternNames.iterator();
     while(patternsIterator.hasNext()) {
         String patternName=(String)patternsIterator.next();
         out.println("<HR><A NAME=\""+patternName+"\"><!-- --></A>");
         out.println("<FONT +1>"+patternName+"</FONT><BR>");
         out.println("<ul>");
         Iterator it=ts.getStrategy().getStar().iterator(patternName);
         HashSet sourceOnlyRules=new HashSet();
         while(it.hasNext()) {
             ITermTransformer tr=(ITermTransformer)it.next();             
             String description = tr.getDescription();
             if (description.length()==0) {
                 sourceOnlyRules.add(tr);
             }else{
                 out.println("<li>");
                 out.print(description);
                 out.println("</li>");
             }
         }
         if (!sourceOnlyRules.isEmpty()) {
            Iterator sourceOnlyIterator=sourceOnlyRules.iterator();
            while(sourceOnlyIterator.hasNext()) {
                ITermTransformer tr=(ITermTransformer)sourceOnlyIterator.next();
                out.println("<li><code>");
                out.println(tr.getSource());
                out.println("</code></li>");
            }
         }
         out.println("</ul>");
     }
     out.println("</BODY></HTML>");
    }
    
    private void generateMaPage() throws IOException, TermWareException
    {
        PrintStream out=openPrintStream("","ma.html"); 
        printHeader(out, "TermWare Systems API");
        out.println("<BODY BGCOLOR=\"white\">");
        out.println("</BODY></HTML>");
        out.close();
    }
    
    private void process() throws IOException, TermWareException
    {
       System.out.println("generate index page");
       generateIndexPage(); 
       System.out.println("generate 'ma' page");
       generateMaPage();
       System.out.println("generate list of systems");
       generateSystemsPage();
       System.out.println("buildinf list of patterns");
       buildListOfAllPatterns();
       System.out.println("generate list of patterns");
       generateAllPatternsPage();
       System.out.println("generate system pages");
       generateSystemPages();
    }
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
      APIGen app=new APIGen();
      try {
        app.init(args);
      }catch(TermWareException ex){
          System.err.println("exception during initialization:"+ex.getMessage());
          ex.printStackTrace();
          return;
      }
      try {
        app.process();
      }catch(IOException ex){
          System.err.println("exception:"+ex.getMessage());
          ex.printStackTrace();
      }catch(TermWareException ex){
          System.err.println("exception:"+ex.getMessage());
          ex.printStackTrace();
      }
    }

    private PrintStream openPrintStream(String prefix, String fname) throws IOException
    {
      String outDirPath;
      if (prefix.length()==0) {
           outDirPath = outDir_+File.separator;
      }else{
           outDirPath = outDir_+File.separator+prefix+File.separator;
      }
      File outDir=new File(outDirPath);
      if (!outDir.exists()) {
          outDir.mkdirs();
      }
      File outFname=new File(outDirPath+fname);
      PrintStream out=new PrintStream(new BufferedOutputStream(new FileOutputStream(outFname)));
      return out;
    }

    private void printHeader(PrintStream out, String title) throws IOException
    {
      out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\"\"http://www.w3.org/TR/REC-html40/loose.dtd\">");
      out.println("<HTML>");
      out.println("<HEAD>");
      out.print("<!-- Generated by TermWare APIGen on ");
         out.print(DateFormat.getDateTimeInstance().format(new Date()));  
      out.println("-->");
      out.println("<TITLE>");
      out.println(title);
      out.println("</TITLE>");
      out.println("<META NAME=\"keywords\" CONTENT=\"TermWare: API Specification\">");
      out.println("</HEAD>");
    }

    
    private String outDir_ = "docs/TAPI/";

    class PatternIndexEntry
    {
       PatternIndexEntry(String prefix, Domain domain, String systemName, int level)
       {
           prefix_=prefix;
           domain_=domain;
           systemName_=systemName;
           level_=level;
       }
       
       public String getPrefix()
       { return prefix_; }
       
       public String getSystemName()
       { return systemName_; }
       
        private String prefix_;
        private Domain domain_;
        private String systemName_;
        private int    level_;
    }
    
    // TreeMap<String,HashSet<PatternIndexEntry> >
    private TreeMap allPatterns_ = new TreeMap();

    
}
