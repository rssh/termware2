package ua.gradsoft.termware.strategies;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002-2004
 * $Id: AbstractTermRewritingStrategy.java,v 1.1.1.1 2004-12-11 19:41:28 rssh Exp $
 */



import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.AbstractBuildinTransformer;
import ua.gradsoft.termware.ITermRewritingStrategy;
import ua.gradsoft.termware.TransformationContext;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.util.TransformersStar;
                           
/**
 * Base class for rewriting strategies.
 *   Strategy maintain set of transformers and in own transform method
 *   do applying of this transformers via subclass-specifics mechanism.
 */
public abstract class AbstractTermRewritingStrategy extends AbstractBuildinTransformer implements ITermRewritingStrategy 
{

 /**
  * constructor
  */
    public AbstractTermRewritingStrategy()
  { star_ = new TransformersStar(); }

 
 public boolean internalsAtFirst()
 { return false; }
    
 /**
  * main work of strategy is here: transform term <code> t </code> in system <system>
  *by applying transformers in 'Star' via subclass-specific algorithm.
  */
 public abstract Term transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException;

 /**
  * return set of  transformers inside strategy. 
  */
 public  TransformersStar  getStar()
   { return star_; }

 /**
  * true, if underlaying transformers include transformer for OTHERWISE keyword.
  */
 public boolean hasOtherwise() 
   { return star_.hasOtherwise(); }
 
 private TransformersStar  star_;
}
                                  