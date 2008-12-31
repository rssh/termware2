/*
 * PartialOrderingResult.java
 *
 * Created on April 8, 2007, 2:15 PM
 *
 */

package ua.gradsoft.termware;

/**
 *Possible results of partial ordering
 */
public enum PartialOrderingResult 
{

    LESS,
    EQ,
    MORE,
    NOT_COMPARABLE;
    
    public static PartialOrderingResult merge(PartialOrderingResult x, PartialOrderingResult y)
    {
       switch(x) {
           case LESS:
               switch(y) {
                   case LESS:
                   case EQ:    
                       return LESS;                    
                   case MORE:
                       return EQ;
                   case NOT_COMPARABLE:
                       return NOT_COMPARABLE;
               }
           case EQ:
               return y;
           case MORE:
               switch(y) {
                   case LESS:
                       return EQ;
                   case EQ:
                   case MORE:
                       return MORE;
                   case NOT_COMPARABLE:
                       return NOT_COMPARABLE;
               }
           case NOT_COMPARABLE:
               return NOT_COMPARABLE;
       }    
       //unreacheble, but compiler does not know about this
       return NOT_COMPARABLE;
    }
    
}
