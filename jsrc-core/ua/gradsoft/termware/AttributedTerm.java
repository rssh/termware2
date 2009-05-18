/*
 * AttributedTerm.java
 *
 */

package ua.gradsoft.termware;

import java.io.PrintWriter;
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;
import java.math.BigInteger;

import ua.gradsoft.termware.exceptions.AssertException;
import ua.gradsoft.termware.exceptions.ConversionException;


/**
 *Term with attributes.
 * @author  Ruslan Shevchenko
 */
public class AttributedTerm extends Term implements Attributed
{
    
    AttributedTerm(Term term,Map<String,Term> attributes) throws TermWareException
    {
        if (term instanceof Attributed) {
           throw new AssertException("term already attributed");
        }
        term_=term;
        attributes_=attributes;
    }
    
    AttributedTerm(Term t) throws TermWareException
    {
        if (t instanceof Attributed) {
           throw new AssertException("term already attributed");
        }
        term_=t;
        attributes_=new HashMap<String,Term>();
    }
        
    
    public boolean boundEquals(Term x) throws TermWareException {
        return term_.boundEquals(x);
    }
    
    public boolean boundUnify(Term t, Substitution s) throws TermWareException {
        return term_.boundUnify(t,s);
    }
    
    public Term createSame(Term[] newBody) throws TermWareException {
        return term_.createSame(newBody);
    }
    
    public boolean emptyFv() {
        return term_.emptyFv();
    }
    
    public boolean freeEquals(Term x) throws TermWareException {
        return term_.freeEquals(x);
    }
    
    public boolean freeUnify(Term t, Substitution s) throws TermWareException {
        return term_.freeUnify(t,s);
    }
    
    public int getArity() {
        return term_.getArity();
    }
    
    public BigDecimal getBigDecimal() throws UnsupportedOperationException {
        return term_.getBigDecimal();
    }
    
    public BigDecimal getAsBigDecimal(TermWareInstance instance) throws ConversionException {
        return term_.getAsBigDecimal(instance);
    }
    
    
    public BigInteger getBigInteger() throws UnsupportedOperationException {
        return term_.getBigInteger();
    }

    public BigInteger getAsBigInteger(TermWareInstance instance) throws ConversionException {
        return term_.getAsBigInteger(instance);
    }

    
    public boolean getBoolean() {
        return term_.getBoolean();
    }
    
    public boolean getAsBoolean(TermWareInstance instance) throws ConversionException {
        return term_.getAsBoolean(instance);
    }
    
    
    public byte getByte() {
        return term_.getByte();
    }

    public byte getAsByte(TermWareInstance instance) throws ConversionException {
        return term_.getAsByte(instance);
    }

    public char getChar() {
        return term_.getChar();
    }

    public char getAsChar(TermWareInstance instance)throws ConversionException {
        return term_.getAsChar(instance);
    }
    
    public double getDouble() {
        return term_.getDouble();
    }
    
    public double getAsDouble(TermWareInstance instance)throws ConversionException {
        return term_.getAsDouble(instance);
    }
    
    
    public float getFloat() {
        return term_.getFloat();
    }

    public float getAsFloat(TermWareInstance instance)throws ConversionException {
        return term_.getAsFloat(instance);
    }

    
    public int getInt()  {
        return term_.getInt();
    }
    
    public int getAsInt(TermWareInstance instance) throws ConversionException {
        return term_.getAsInt(instance);
    }
    
    public Object getJavaObject() {
        return term_.getJavaObject();
    }
    
    public Object getAsJavaObject(TermWareInstance instance)throws ConversionException {
        return term_.getAsJavaObject(instance);
    }

    
    public long getLong() {
        return term_.getLong();
    }

    public long getAsLong(TermWareInstance instance)throws ConversionException {
        return term_.getAsLong(instance);
    }

    
    public String getName() {
        return term_.getName();
    }
    
    public Object getNameIndex() {
        return term_.getNameIndex();
    }

    public String getPatternName() {
        return term_.getPatternName();
    }

    public Object getPatternNameIndex() {
        return term_.getPatternNameIndex();
    }
    
    
    public Number getNumber() {
        return term_.getNumber();
    }
    
    public Number getAsNumber(TermWareInstance instance)throws ConversionException {
        return term_.getAsNumber(instance);
    }
    
    
    public int getPrimaryType0() {
        return term_.getPrimaryType0();
    }
    
    public short getShort() throws UnsupportedOperationException {
        return term_.getShort();
    }

    public short getAsShort(TermWareInstance instance) throws ConversionException
    {
        return term_.getAsShort(instance);
    }

    
    public String getString() {
        return term_.getString();
    }

  //  public String getAsString(TermWareInstance instance) {
  //      return term_.getAsString(instance);
  //  }

    
    public Term getSubtermAt(int i) {
        return term_.getSubtermAt(i);
    }
    
    
    public Term getTerm() {
        if (term_.getTerm() instanceof Attributed) {
            return term_.getTerm();
        }
        return this;
    }
    
    public int getXIndex() {
        return term_.getXIndex();
    }
    
    public boolean isAtom() {
        return term_.isAtom();
    }
    
    public boolean isBigDecimal() {
        return term_.isBigDecimal();
    }
    
    public boolean isBigInteger() {
        return term_.isBigInteger();
    }
    
    public boolean isBoolean() {
        return term_.isBoolean();
    }
    
    public boolean isByte() {
        return term_.isByte();
    }
    
    public boolean isChar() {
        return term_.isChar();
    }
    
    public boolean isComplexTerm() {
        return term_.isComplexTerm();
    }
    
    public boolean isDouble() {
        return term_.isDouble();
    }
    
    public boolean isFloat() {
        return term_.isFloat();
    }
    
    public boolean isInt() {
        return term_.isInt();
    }
    
    public boolean isJavaObject() {
        return term_.isJavaObject();
    }
    
    public boolean isLong() {
        return term_.isLong();
    }
    
    public boolean isNil() {
        return term_.isNil();
    }
    
    public boolean isNumber() {
        return term_.isNumber();
    }
    
    public boolean isShort() {
        return term_.isShort();
    }
    
    public boolean isString() {
        return term_.isString();
    }
    
    public boolean isX() {
        return term_.isX();
    }
    
    public int maxFv() throws TermWareException {
        return term_.maxFv();
    }
    
    public int minFv() throws TermWareException {
        return term_.minFv();
    }
    
    public void print(PrintWriter out) {
        term_.print(out);
        //out.print(" attributes:");
        //for(int i=0; i<)
    }
    
    public void setSubtermAt(int i, Term t) throws TermWareException {
        term_.setSubtermAt(i, t);
    }
    
    public void shiftFv(int newMinFv) throws TermWareException {
        term_.shiftFv(newMinFv);
    }


    public Term subst(Substitution s) throws TermWareException {
        // preserve attributes.
        if (term_.isX()) {
            Term rx = term_.subst(s);
            if (rx instanceof Attributed) {
                Map<String,Term> rxAttributes = ((Attributed)rx).getAttributes();
                if (rxAttributes.size()!=0) {
                  Map<String,Term> thisAttributes = this.getAttributes();
                  if (thisAttributes.size()!=0) {
                    try {
                        rxAttributes.putAll(thisAttributes);
                    } catch (UnsupportedOperationException ex){
                        AttributedTerm retval = new AttributedTerm(rx.getTerm());
                        retval.getAttributes().putAll(rxAttributes);
                        retval.getAttributes().putAll(thisAttributes);
                        return retval;
                    }  
                  }else{
                      return rx;
                  }
                }else{
                  return TermHelper.copyAttributes(rx, this);
                }
                return rx;
            }else{
                return new AttributedTerm(rx,attributes_);
            }
        } else if (term_ instanceof Attributed) {
           return term_.subst(s);
        } else if (term_.getTerm() instanceof Attributed ) {
            return term_.getTerm().subst(s);
        }else{
           return new AttributedTerm(term_.subst(s), attributes_);
        }
    }
    
    public boolean substInside(Substitution s) throws TermWareException {
        return term_.substInside(s);
    }
    
    public Term termClone() throws TermWareException {
        return new AttributedTerm(term_.termClone(),attributes_);
    }
    
    public int termCompare(Term x) {
        return term_.termCompare(x);
    }

    
    public PartialOrderingResult concreteOrder(Term x,Substitution s) throws TermWareException
    {
        return term_.concreteOrder(x,s);
    }

    public int findSubtermIndexBoundEqualsTo(Term x) throws TermWareException {
        return term_.findSubtermIndexBoundEqualsTo(x);
    }

    

    
    public Term unAttribute()
    {
      Term retval=term_.getTerm();
      if (retval instanceof AttributedTerm) {
          // impossible ? : TODO: check
          return ((AttributedTerm)retval).unAttribute();
      }else{
          return retval;
      }
    }
    
    
    public  void setAttribute(String attribute,Term value)
    {
        if (term_.getTerm() instanceof Attributed) {
            ((Attributed)term_.getTerm()).setAttribute(attribute, value);
        }else{
            attributes_.put(attribute, value);
        }
    }
    
    public  Term getAttribute(String attribute)
    {
        if (term_.getTerm() instanceof Attributed) {
            return ((Attributed)term_.getTerm()).getAttribute(attribute);
        }
        Object o = attributes_.get(attribute);
        if (o!=null) {
            return (Term)o;
        }else{
            return NILTerm.getNILTerm();
        }
    }
    
    public Map<String,Term> getAttributes()
    {
        if (term_.getTerm() instanceof Attributed) {
            return  ((Attributed)term_.getTerm()).getAttributes();
        }
        return attributes_;
    }
    
    private Term term_;
    private Map<String,Term>  attributes_;

   
      
}
