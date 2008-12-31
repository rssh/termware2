/*
 * Life2Facts.java
 *
 */

package ua.gradsoft.termwaretests.demos.life;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;
import ua.gradsoft.termware.set.*;

/**
 *This is IFacts, corresponded to  Life2 term system
 * @author  Ruslan Shevchenko
 */
public class Life2TestFacts extends AbstractTestLifeFacts {
    
    /** Creates a new instance of Life2Facts */
    public Life2TestFacts() throws TermWareException
    {
    }
    
    
    
    
    public Term generateStateTermFromNextFieldModel() throws TermWareException 
    {
        TermFactory termFactory=TermWare.getInstance().getTermFactory();
        Term retval=termFactory.createNil();
        FieldModel fieldModel=getFieldModel();        
        for (int x=0; x<fieldModel.getNRows(); ++x) {
            for(int y=0; y<fieldModel.getNColumns(); ++y) {
                if (fieldModel.getCell(x, y)) {
                  // some (far from optimal) optimization
                  /********************************************
                   *x-2, y-2 * x-1, y-2 *  x, y-2 * x +1, y-2 *
                   ********************************************
                   *x-2, y-1 * x-1, y-1 *  x, y-1 * x+1, y-1  *
                   ********************************************
                   *x-2, y   * x-1, y   *  x, y   * x+1, y    *
                   * ******************************************
                   *x-2,y+1  * x-1, y+1 *  x, y+1 * x+1, y+1  *
                   ********************************************
                   *--- so, let's insert fields, which was not inserted previously
                  ****************************************/
                    
                  if (!fieldModel.getCell(x-2,y-2) && !fieldModel.getCell(x-1,y-2) && 
                      !fieldModel.getCell(x-2,y-1) && !fieldModel.getCell(x-1,y-1) ) {
                      retval=termFactory.createTerm("cons", termFactory.createTerm("l", x-1,  y-1), retval);
                  }
                  if (!fieldModel.getCell(x-2,y-1) && !fieldModel.getCell(x-1,y-1) && 
                      !fieldModel.getCell(x-2,y  ) && !fieldModel.getCell(x-1,y  ) ) {
                      retval=termFactory.createTerm("cons", termFactory.createTerm("l", x-1,  y  ), retval);
                  }
                   if (!fieldModel.getCell(x-2,y ) && !fieldModel.getCell(x-1,y) && !fieldModel.getCell(x,y) ) {
                      retval=termFactory.createTerm("cons", termFactory.createTerm("l", x-1,  y+1  ), retval);
                  }
                   if (!fieldModel.getCell(x-1,y-2 ) && !fieldModel.getCell(x,y-2) &&
                      !fieldModel.getCell(x-1,y-1) && !fieldModel.getCell(x ,y-1  ) ) {
                      retval=termFactory.createTerm("cons", termFactory.createTerm("l", x,  y-1  ), retval);
                  }
                   if (!fieldModel.getCell(x-1,y-1 ) && !fieldModel.getCell(x,y-1) && 
                      !fieldModel.getCell(x-1,y) ) {
                      retval=termFactory.createTerm("cons", termFactory.createTerm("l", x,  y  ), retval);
                  }
                   if (!fieldModel.getCell(x-1,y) ) {
                      retval=termFactory.createTerm("cons", termFactory.createTerm("l", x,  y+1  ), retval);
                  }
                   if (!fieldModel.getCell(x,y-2) && !fieldModel.getCell(x,y-1) ) {
                      retval=termFactory.createTerm("cons", termFactory.createTerm("l", x+1,  y-1  ), retval);
                  }
                   if (!fieldModel.getCell(x,y-1) ) {
                      retval=termFactory.createTerm("cons", termFactory.createTerm("l", x+1,  y  ), retval);
                  }
                  retval=termFactory.createTerm("cons", termFactory.createTerm("l", x+1,  y+1), retval);
                }
            }
        }       
        //System.err.println("check generation:"+TermHelper.termToString(retval));
        return retval;
    }    
       
    
}
