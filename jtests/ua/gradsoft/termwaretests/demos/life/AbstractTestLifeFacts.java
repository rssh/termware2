/*
 * AbstractTestLifeFacts.java
 *
 */

package ua.gradsoft.termwaretests.demos.life;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 * Abstract Fact DB for Life Game.
 * @author  Ruslan Shevchenko
 */
public abstract class AbstractTestLifeFacts extends DefaultFacts {
    
    /** 
     * Creates a new instance of AbstractTestLifeFacts 
     **/
    public AbstractTestLifeFacts() throws TermWareException
    {
      nGenerations_=0;  
      maxNGenerations_=1000;
    }
    
    
    public void setMaxNGenerations(int maxNGenerations)
    {
      maxNGenerations_=maxNGenerations;  
    }
    
    
    /**
     * get FieldModel 
     **/
    public FieldModel getFieldModel()
    { return fieldModel_; }
    
    /**
     * set Field Model into canvas
     **/
    public void setFieldModel(FieldModel fieldModel)
    {  
       fieldModel_=fieldModel;
       nextFieldModel_ = new FieldModel(getFieldModel().getNRows(),
                                        getFieldModel().getNColumns()
                                        );
    }
    
    
    
    public  void showGeneration()
    {
      //System.err.println("call of showGeneration");
      FieldModel prevGeneration=getFieldModel();
      setFieldModel(nextFieldModel_);  
      nextFieldModel_=prevGeneration;
      nextFieldModel_.clear();
    }

    
    
    public boolean existsCell(int x, int y)
    {
     return getFieldModel().getCell(x,y);   
    }
    
    public  int n(int x, int y)
    {
      return getFieldModel().nNeighbourds(x, y);
    }
    
    public  void putCell(int x, int y)
    {
      nextFieldModel_.setCell(x,y,true);
    }
    
    public  void removeCell(int x, int y)
    {
      nextFieldModel_.setCell(x,y,false);
    }

     public void generateNextTestSet(TransformationContext ctx, Term t) throws TermWareException
    {
      if (!t.isX()) {
          throw new AssertException("argument of generateNextTestSet must be a propositional variable");
      }
      Term retval=generateStateTermFromNextFieldModel();
      ctx.getCurrentSubstitution().put(t, retval);
      ++nGenerations_;
      if (nGenerations_>=maxNGenerations_) {
          ctx.setStop(true);
      }
    }

    
    public abstract Term generateStateTermFromNextFieldModel() throws TermWareException;
    
    
    protected FieldModel fieldModel_;
    protected FieldModel nextFieldModel_;
    
    protected int nGenerations_;
    protected int maxNGenerations_;
    
}
