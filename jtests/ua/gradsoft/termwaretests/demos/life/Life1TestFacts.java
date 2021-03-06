/*
 * Life1Facts.java
 *
 */

package ua.gradsoft.termwaretests.demos.life;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;
import ua.gradsoft.termware.set.*;

/**
 *This is IFacts, corresponded to  Life1 term system
 *<pre>
 *domain(examples,
 *
 * system(Life1,javaFacts(Life1DB,"ua.kiev.gradsoft.TermWareDemos.Life.Life1Facts"),
 *        ruleset( 
 *            # $T - set of pairs to test.
 *
 *       { l($i,$j) | $T} [| existsCell($i,$j) && (n($i,$j)==2||n($i,$j)==3) |] -> $T // putCell($x,$y),
 *
 *       { l($i,$j) |$T } [| n($i,$j) == 3 |] -> $T // putCell($x,$y),
 *
 *       { } -> $T // showGeneration() && createNewTestSet($T)
 *              
 *               ),
 *        FirstTop)
 *       
 * );
 *</pre>
 * @author  Ruslan Shevchenko
 */
public class Life1TestFacts extends AbstractTestLifeFacts {
    
    /** Creates a new instance of Life1Facts */
    public Life1TestFacts() throws TermWareException
    {
    }
    
    
    
    
    public Term generateStateTermFromNextFieldModel() throws TermWareException 
    {
        //System.err.println("generate:");
        SetTerm retval=(SetTerm)TermWare.getInstance().getTermFactory().createSetTerm();
        FieldModel fieldModel=getFieldModel();
        for (int x=0; x<fieldModel.getNRows(); ++x) {
            for(int y=0; y<fieldModel.getNColumns(); ++y) {
                if (fieldModel.getCell(x, y)) {
                    retval.insert(TermWare.getInstance().getTermFactory().createTerm("l", x-1,  y-1));
                    retval.insert(TermWare.getInstance().getTermFactory().createTerm("l", x-1,  y  ));
                    retval.insert(TermWare.getInstance().getTermFactory().createTerm("l", x-1,  y+1));
                    retval.insert(TermWare.getInstance().getTermFactory().createTerm("l", x  ,  y-1));
                    retval.insert(TermWare.getInstance().getTermFactory().createTerm("l", x  ,  y  ));
                    retval.insert(TermWare.getInstance().getTermFactory().createTerm("l", x  ,  y+1));
                    retval.insert(TermWare.getInstance().getTermFactory().createTerm("l", x+1,  y-1));
                    retval.insert(TermWare.getInstance().getTermFactory().createTerm("l", x+1,  y  ));
                    retval.insert(TermWare.getInstance().getTermFactory().createTerm("l", x+1,  y+1));
                }
            }
        }        
        
        return retval;
    }    
    
       
    
}
