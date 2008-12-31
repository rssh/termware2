/*
 * SourceCodeAccessHelper.java
 *
 */

package ua.gradsoft.termware.debug;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;

/**
 *Provide helper entries for access to source code.
 * @author rssh
 */
public class SourceCodeAccessHelper 
{
    
   /**
    * if term was in source code, get location of this term in it,
    *otherwise - return SourceCodeLocation.UNKNOWN
    *@param t 
    *@return SourcCodeLocation if known, otherwise - UNKNOWN location. 
    */ 
   public static SourceCodeLocation  getLocationOfTerm(Term t)
   {   
      Term tfname = TermHelper.getAttribute(t,"__FILE__"); 
      Term tBeginLine = TermHelper.getAttribute(t,"__BEGIN_LINE__");
      Term tEndLine = TermHelper.getAttribute(t,"__END_LINE__");
      String fname=null;
      int    beginLine=0;
      int    endLine=0;
      boolean error=false;
      if (!tfname.isNil()) {
          fname=tfname.getString();
      }else{
          error=true;
      }
      if (tBeginLine.isInt()) {
          beginLine=tBeginLine.getInt();
      }else{          
          error=true;
      }
      if (tEndLine.isInt()) {
          endLine=tEndLine.getInt();
      }else{
          error=true;
      }
      return fname==null ? SourceCodeLocation.UNKNOWN : new SourceCodeLocation(fname,beginLine,endLine);
   }

   /**
    * if term was in source code, get location of begin line of this term,
    *otherwise - return SourceCodeLocation.UNKNOWN
    *@param t 
    *@return SourcCodeLocation if known, otherwise - UNKNOWN location. 
    */ 
   public static SourceCodeLocation  getLocationOfTermBegin(Term t)
   {   
      SourceCodeLocation retval = getLocationOfTerm(t); 
      if (retval!=SourceCodeLocation.UNKNOWN) {
          retval.setEndLine(retval.getBeginLine());
      }
      return retval;
   }

   /**
    * if term was in source code, get location of begin line of this term,
    *otherwise - return SourceCodeLocation.UNKNOWN
    *@param t 
    *@return SourcCodeLocation if known, otherwise - UNKNOWN location. 
    */ 
   public static SourceCodeLocation  getLocationOfTermEnd(Term t)
   {   
      SourceCodeLocation retval = getLocationOfTerm(t); 
      if (retval!=SourceCodeLocation.UNKNOWN) {
          retval.setBeginLine(retval.getEndLine());
      }
      return retval;
   }
   
   
}
