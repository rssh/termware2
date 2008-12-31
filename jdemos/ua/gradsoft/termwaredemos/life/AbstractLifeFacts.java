/*
 * AbstractLifeFacts.java
 *
 */

package ua.gradsoft.termwaredemos.life;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.set.*;

/**
 * Abstract Fact DB for Life Game.
 * @author  Ruslan Shevchenko
 */
public abstract class AbstractLifeFacts extends DefaultFacts {
    
    /** 
     * Creates a new instance of AbstractLifeFacts 
     **/
    public AbstractLifeFacts() throws TermWareException
    {
        canvas_=null;
    }
    
    /**
     * get Active Canvas
     **/
    public FieldCanvas getCanvas()
    { return canvas_; }
    
    /**
     * set Active Canvas
     **/
    public void       setCanvas(FieldCanvas canvas)
    { canvas_=canvas; }
    
    
    
    /**
     * get FieldModel in drowed Canvas
     **/
    public FieldModel getCanvasFieldModel()
    { return canvas_.getFieldModel(); }
    
    /**
     * set Field Model into canvas
     **/
    public void    setCanvasFieldModel(FieldModel fieldModel)
    {
      canvas_.setFieldModel(fieldModel);
    }
    
    
    /**
     * generate Cell Set from canvas
     **/
/*    public ITerm  generateCellSet() throws TermWareException
    {
        SetTerm retval = ITermFactory.createSetTerm();
        FieldModel fieldModel=canvas_.getFieldModel();
        for (int x=0; x<fieldModel.getNRows(); ++x) {
            for(int y=0; y<fieldModel.getNColumns(); ++y) {
                if (fieldModel.getCell(x, y)) {
                    retval.insert(ITermFactory.createTerm("l", x, y));
                }
            }
        }
        return retval;
    }
*/
    
    public abstract Term generateStateTermFromCanvas() throws TermWareException;
    
    
    private FieldCanvas canvas_;
    
}
