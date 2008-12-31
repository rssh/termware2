/*
 * IntersectionTransformer.java
 *
 * Created on: 14, 08, 2005, 14:44
 *
 * Owner: Ruslan Shevchenko
 *
 * Description:
 *
 * History:
 *
 * Copyright (c) 2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termware.transformers.general;
import ua.gradsoft.termware.AbstractBuildinTransformer;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareSymbols;
import ua.gradsoft.termware.TransformationContext;
import ua.gradsoft.termware.set.SetOfTerms;
import ua.gradsoft.termware.set.SetTerm;
import ua.gradsoft.termware.util.LogHelper;

/**
 *intersection($x,$y) - intersection of sets $x and $y, i. e."+
 *<ul>
 *  <li><code>intersection({},$S) = {}</code></li>
 *  <li><code>intersection({$x:$y},$S) = $x in $S ? { $x:intersection($y,$S)} : intersection($y,$S)</code></li>
 *</ul>
 */
public class IntersectionTransformer extends AbstractBuildinTransformer
{

    public Term transform(Term t,TermSystem system,TransformationContext ctx) throws TermWareException
    {
      return static_transform(t,system,ctx);  
    }
    
    public Term static_transform(Term t,TermSystem system,TransformationContext ctx) throws TermWareException
  {
      Term retval=t;  
      if (system.isLoggingMode()) {
          LogHelper.log(system,IntersectionTransformer.class,"compute intersection:",t);
      }
      if (t.getArity()==2) {
          retval=intersection2(t,system,ctx);
      }else{
          retval=intersectionGeneral(t,system,ctx);
      }  
      if (system.isLoggingMode()) {
          LogHelper.log(system,IntersectionTransformer.class,"result:",retval);
      }
      return retval;
    }
    
    private static Term intersection2(Term t,TermSystem system,TransformationContext ctx) throws TermWareException
    {
       Term retval=t; 
       Term x=t.getSubtermAt(0);
       Term y=t.getSubtermAt(1);
       if (x.isComplexTerm()) {
           if (x.getNameIndex().equals(TermWareSymbols.SET_INDEX) && y.getNameIndex().equals(TermWareSymbols.SET_INDEX)) {
               SetOfTerms retset = new SetOfTerms();
               //SetTerm retval=system.getInstance().getTermFactory().createSetTerm();
               //SetTerm ySet=(SetTerm)y.getTerm();
               for(int i=0; i<x.getArity();++i){
                   Term current = x.getSubtermAt(i);
                   if (y.containsSubtermBoundEqualsTo(current)) {
                       retset.addAlreadySorted(current);
                   }                                          
               }
               ctx.setChanged(true);               
               retval=new SetTerm(retset);
           }else if (x.getNameIndex().equals(TermWareSymbols.SET_INDEX)) {
               if (x.getArity()==0) {
                   ctx.setChanged(true);
                   retval = new SetTerm();
               }               
           }else if (y.getNameIndex().equals(TermWareSymbols.SET_INDEX)) {
               if (y.getArity()==0) {
                   ctx.setChanged(true);
                   retval = new SetTerm();
               }
           }
       }
       return retval;
    }
    
    public static Term intersectionGeneral(Term t,TermSystem system,TransformationContext ctx) throws TermWareException
    {
        // check that all subterms are sets, and may be on of them is empty.       
        if (t.getArity()==0) {
            return t;
        }
        Term retval=t;
        boolean allSets=true;
        boolean existsEmpty=false;
        for(int i=0; i<t.getArity(); ++i) {
            Term c=t.getSubtermAt(i);            
            if (!c.getNameIndex().equals(TermWareSymbols.SET_INDEX)) {
                allSets=false;
            }else{
                if (c.getArity()==0) {
                    existsEmpty=true;                   
                }
            }
        }
        if (existsEmpty) {
            retval = new SetTerm();
            ctx.setChanged(true);
        }else if(allSets){
            Term x=t.getSubtermAt(0);
            SetOfTerms retset=new SetOfTerms();
            for(int i=0; i<x.getArity(); ++i){
                Term element = x.getSubtermAt(i);
                boolean foundInAll=true;
                for(int j=1; j<t.getArity(); ++j) {
                    Term set = t.getSubtermAt(j);
                    if (!set.containsSubtermBoundEqualsTo(element)) {
                        foundInAll=false;
                        break;
                    }
                }
                if (foundInAll) {
                    retset.addAlreadySorted(element);
                }
            }
            retval=new SetTerm(retset);
            ctx.setChanged(true);            
        }
        return retval;
    }
    
    public String getName()
    { return "intersection"; }
    
    public String getDescription()
    {
      return 
              "intersection($x,$y) - intersection of sets $x and $y, i. e."+
              "<ul>\n"+
              " <li><code>intersection({},$S) = {}</code></li>\n"+
              " <li><code>intersection({$x:$y},$S) = $x in $S ? { $x:intersection($y,$S)} : intersection($y,$S)</code></li>\n"+
              " </ul>\n"
              ;
    }
    
}
