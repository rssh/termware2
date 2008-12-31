/*
 */

package ua.gradsoft.termware.printers;

import java.io.PrintWriter;
import ua.gradsoft.termware.IPrettyPrinter;

/**
 *Common functionality for pretty printing.
 * 
 * @see ua.gradsoft.termware.IPrettyPrinter
 */
public abstract class AbstractPrettyPrinter extends AbstractPrinter implements IPrettyPrinter
{

    protected AbstractPrettyPrinter(PrintWriter out,String outTag)
    {
       super(out,outTag); 
       out_=new PrettyPrintWriter(out);
    }
 
    public final PrettyPrintWriter getPrettyPrintingOut()
    {
       return out_; 
    }

    public int getPageWidth()
    {
       return out_.getLineWidth(); 
    }
    
    public void setPageWidth(int width)
    {
       out_.setLineWidth(width); 
    }
    
    @Override
    protected void finalize() throws Throwable
    {
       out_.flush();
       super.finalize();
    }
    
    /**
     * Child must use API of out_ for output,
     *to use implemented pretty-printing algorithm. 
     */
    protected PrettyPrintWriter out_;
}
