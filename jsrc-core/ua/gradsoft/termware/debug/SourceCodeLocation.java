/*
 * SourceCodeLocation.java
 * (C) Grad-Soft Ltd.
 * http://www.gradsoft.ua
 */

package ua.gradsoft.termware.debug;

/**
 *Location of some entity in source code.
 * @author rssh
 */
public class SourceCodeLocation {
    
    public SourceCodeLocation(String filename,int beginLine,int endLine) {
        filename_=filename;
        beginLine_=beginLine;
        endLine_=endLine;
    }
    
    public String getFileName()
    { return filename_; }
    
    public int getBeginLine()
    { return beginLine_; }
    
    public void setBeginLine(int beginLine)
    { beginLine_=beginLine; }
        
    public int getEndLine()
    { return endLine_; }
    
    public void setEndLine(int endLine)
    { endLine_=endLine; }
    
    public static final SourceCodeLocation UNKNOWN = new SourceCodeLocation("unknown",1,1);
    
    private String filename_;
    private int beginLine_;
    private int endLine_;
    
}
