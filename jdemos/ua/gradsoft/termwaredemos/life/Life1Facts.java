/*
 * Life1Facts.java
 *
 */

package ua.gradsoft.termwaredemos.life;

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
public class Life1Facts extends AbstractLifeFacts {
    
    /** Creates a new instance of Life1Facts */
    public Life1Facts() throws TermWareException
    {
    }
    
    public void setCanvas(FieldCanvas canvas)
    {
       super.setCanvas(canvas);
       nextGeneration_ = new FieldModel(canvas.getFieldModel().getNRows(),
                                        canvas.getFieldModel().getNColumns()
                                        );
    }
    
    
    public boolean existsCell(int x, int y)
    {
     return getCanvasFieldModel().getCell(x,y);   
    }
    
    public  int n(int x, int y)
    {
      return getCanvasFieldModel().nNeighbourds(x, y);
    }
    
    public  void putCell(int x, int y)
    {
      nextGeneration_.setCell(x,y,true);
    }
    
    public  void removeCell(int x, int y)
    {
      nextGeneration_.setCell(x,y,false);
    }
    
    public  void showGeneration()
    {
      FieldModel prevGeneration=getCanvasFieldModel();
      setCanvasFieldModel(nextGeneration_);  
      nextGeneration_=prevGeneration;
      nextGeneration_.clear();
    }
    
    public void generateNextTestSet(TransformationContext ctx,Term t) throws TermWareException
    {
      if (!t.isX()) {
          throw new AssertException("argument of generateNextTestSet must be a propositional variable");
      }
      Term retval=generateStateTermFromCanvas();
      ctx.getCurrentSubstitution().put(t, retval);
    }
    
    public Term generateStateTermFromCanvas() throws TermWareException 
    {
        
        SetTerm retval=(SetTerm)TermWare.getInstance().getTermFactory().createSetTerm();
        FieldModel fieldModel=getCanvasFieldModel();
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
    
    private FieldModel nextGeneration_=null;
    
}
