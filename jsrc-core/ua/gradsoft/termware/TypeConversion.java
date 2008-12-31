/*
 * IConversionRules.java
 *
 */

package ua.gradsoft.termware;

import java.math.*;
import java.util.*;
import java.text.*;
import java.lang.reflect.*;
import ua.gradsoft.termware.exceptions.*;
import ua.gradsoft.termware.util.JavaLangReflectHelper;


/**
 * Conversion rules between terms and java objects.
 */
public class TypeConversion {
    
    TypeConversion(TermWareInstance instance)
    {
        instance_=instance;
    }
    
    public static final int POSSIBLE=1;
    public static final int NOT_POSSIBLE=0;
    public static final int MAYBE_POSSIBLE=-1;
    
    public static abstract class TermToBoolean
    {
        /**
         * must return one from 
         *POSSIBLE, NOT_POSSIBLE, MAYBE_POSSIBLE
         *user can override this method.
         *default implementation return MAYBE_POSSIBLE
         */
        public int possible(Term t, TermWareInstance instance)
        {
            return MAYBE_POSSIBLE;
        }
        
        /**
         *convert t to boolean
         */
        public abstract boolean convert(Term t, TermWareInstance instance) throws ConversionException;
    }
    
    /**
     * user can set subclass of this class to receive default mapping from terms to chars.
     */
    public static abstract class TermToChar
    {
        /**
         * must return one from 
         *POSSIBLE, NOT_POSSIBLE, MAYBE_POSSIBLE
         *user can override this method.
         *default implementation return MAYBE_POSSIBLE
         */
        public int possible(Term t, TermWareInstance instance)
        {
            return MAYBE_POSSIBLE;
        }
        
        /**
         *convert t to boolean
         */
        public abstract char convert(Term t, TermWareInstance instance) throws ConversionException;
    }
    
    public static abstract class TermToString
    {
        /**
         * must return one from 
         *POSSIBLE, NOT_POSSIBLE, MAYBE_POSSIBLE
         *user can override this method.
         *default implementation return MAYBE_POSSIBLE
         */
        public int possible(Term t, TermWareInstance instance)
        {
            return MAYBE_POSSIBLE;
        }
        
        /**
         *convert t to string
         */
        public abstract String convert(Term t, TermWareInstance instance) throws ConversionException;
    }
    
    public static abstract class TermToNumber
    {
        /**
         * must return one from 
         *POSSIBLE, NOT_POSSIBLE, MAYBE_POSSIBLE
         *user can override this method.
         *default implementation return MAYBE_POSSIBLE
         */
        public int possible(Term t, TermWareInstance instance)
        {
            return MAYBE_POSSIBLE;
        }
        
        /**
         *convert t to number
         */
        public abstract Number convert(Term t, TermWareInstance instance) throws ConversionException;
    }
    
    public static abstract class TermToObject
    {
        /**
         * must return one from 
         *POSSIBLE, NOT_POSSIBLE, MAYBE_POSSIBLE
         *user can override this method.
         *default implementation return MAYBE_POSSIBLE
         */
        public int possible(Term t, TermWareInstance instance)
        {
            return MAYBE_POSSIBLE;
        }
        
        /**
         *convert t to object
         */
        public abstract Object convert(Term t, TermWareInstance instance) throws ConversionException;
    }
    
    /**
     * Callback class for specifying conversions of terms to array.
     */
    public static abstract class TermToObjectArray
    {
        public int possible(Term t, TermWareInstance instance)
        {
            return MAYBE_POSSIBLE;
        }
        public abstract Object[] convert(Term t, TermWareInstance instance) throws ConversionException;
    }
    
    
     
    public static abstract class ObjectToString
    {
        /**
         * must return one from 
         *POSSIBLE, NOT_POSSIBLE, MAYBE_POSSIBLE
         *user can override this method.
         *default implementation return MAYBE_POSSIBLE
         */
        public int possible(Object o, TermWareInstance instance)
        {
            return MAYBE_POSSIBLE;
        }
        
        /**
         *convert t to string
         */
        public abstract String convert(Object o, TermWareInstance instance) throws ConversionException;
    }

     public static abstract class ObjectToNumber
    {
        /**
         * must return one from 
         *POSSIBLE, NOT_POSSIBLE, MAYBE_POSSIBLE
         *user can override this method.
         *default implementation return MAYBE_POSSIBLE
         */
        public int possible(Object o, TermWareInstance instance)
        {
            return MAYBE_POSSIBLE;
        }
        
        /**
         *convert t to number
         */
        public abstract Number convert(Object o, TermWareInstance instance) throws ConversionException;
    }
     
     
     /**
      * callback class for object to term conversion.
      */
    public abstract class ObjectToTerm
    {
        /**
         * must return one from 
         *POSSIBLE, NOT_POSSIBLE, MAYBE_POSSIBLE
         *user can override this method.
         *default implementation return MAYBE_POSSIBLE
         */
        public int possible(Object o, TermWareInstance instance)
        {
            return MAYBE_POSSIBLE;
        }
        
        /**
         *convert object to term
         */
        public abstract Term convert(Object o, TermWareInstance instance) throws ConversionException;
    }

    /**
     * get term value as boolean.
     *(convert, if necessory)
     */
    public boolean getAsBoolean(Term t) throws ConversionException
    {
      try {
        if (t.isBoolean()) {
          return t.getBoolean();
        }else if (t.isByte()) {
          return t.getByte()!=0;
        }else if (t.isString()) {
          if (t.getString().equals("true")) {
              return true;
          }else if(t.getString().equals("false")) {
              return false;
          }
        }
        if (termToBooleanConverters_!=null) {
          TermToBoolean converter=null;
          converter = (TermToBoolean)termToBooleanConverters_.get(t.getNameIndex());
          if (converter!=null) {
              if (converter.possible(t,instance_)!=0) {
                  return converter.convert(t,instance_);
              }
          }
        }
      }catch(TermWareException ex){
         new ConversionException(t,t.getClass(),Boolean.class,ex.getMessage()); 
      }
      throw new ConversionException(t,t.getClass(),Boolean.class,"can't find conversion");
    }
    
    
    public char    getAsChar(Term t) throws ConversionException
    {
      try {
        if (t.isChar()) {
          return t.getChar();
        }else if (t.isString()) {
          if (t.getString().length()==1) {
              return t.getString().charAt(0);
          }
        }
        if (termToCharConverters_!=null) {
          Object o = termToCharConverters_.get(t.getNameIndex());
          if (o!=null) {
            TermToChar converter = (TermToChar)o;
            if (converter.possible(t,instance_)!=0) {
                  return converter.convert(t,instance_);
            }
          }
        }
      }catch(ConversionException ex){
          throw ex;
      }catch(TermWareException ex){
          throw new ConversionException(t,t.getClass(),Character.TYPE,ex.getMessage());
      }
      throw new ConversionException(t,t.getClass(),JavaLangReflectHelper.getCharacterClass(),"Can't find conversion");
    }
    
    
    
    public String     getAsString(Term t) throws ConversionException
    {
        int tp=t.getPrimaryType0();
        switch(tp) {
          case PrimaryTypes.NIL: return "Nil";
          case PrimaryTypes.ATOM: return t.getName();
          case PrimaryTypes.BYTE: return Byte.toString(t.getByte());
          case PrimaryTypes.CHAR: return Character.toString(t.getChar());
          case PrimaryTypes.BIG_DECIMAL: 
              return NumberFormat.getNumberInstance().format(t.getBigDecimal());
          case PrimaryTypes.BIG_INTEGER:    
              return NumberFormat.getNumberInstance().format(t.getBigInteger());
          case PrimaryTypes.COMPLEX_TERM:
              if (termToStringConverters_!=null) {
                  Object o = termToStringConverters_.get(t.getNameIndex());
                  if (o!=null) {
                      TermToString converter = (TermToString)o;
                      return converter.convert(t,instance_);
                  }
              }
              break;
          case PrimaryTypes.DOUBLE: return Double.toString(t.getDouble());
          case PrimaryTypes.FLOAT: return Float.toString(t.getFloat());
          case PrimaryTypes.INT: return Integer.toString(t.getInt());
          case PrimaryTypes.JAVA_OBJECT: 
              Object o;
              if (objectToStringConverters_!=null) {
                  o=objectToStringConverters_.get(t.getNameIndex());
                  if (o!=null) {
                      ObjectToString converter = (ObjectToString)o;
                      return converter.convert(t,instance_);
                  }
              }
              break;
          case PrimaryTypes.LONG: return Long.toString(t.getLong());
          case PrimaryTypes.SHORT: return Short.toString(t.getShort());
          case PrimaryTypes.STRING: return t.getString();
          default:
              break;
        }
        throw new ConversionException(t,t.getClass(),JavaLangReflectHelper.getStringClass(),"can't find conversion");
    }
    
    
    
    public Number    getAsNumber(Term t) throws ConversionException
    {
     try {   
       int tp=t.getPrimaryType0();
       if ((tp & 0x00F0)==0x10) {
         return t.getNumber();
       }
       switch(tp) {
         case PrimaryTypes.NIL: return new Integer(0);
         case PrimaryTypes.ATOM: return getStringAsNumber(t.getName());
         case PrimaryTypes.STRING: return getStringAsNumber(t.getString());
         case PrimaryTypes.CHAR: return getCharAsNumber(t.getChar());
         case PrimaryTypes.BOOLEAN: return getBooleanAsNumber(t.getBoolean());
         case PrimaryTypes.COMPLEX_TERM:
             if (termToNumberConverters_!=null) {
                 Object o=termToNumberConverters_.get(t.getNameIndex());
                 if (o!=null) {
                     TermToNumber converter = (TermToNumber)o;
                     return converter.convert(t,instance_);
                 }
             }
             break;
         case PrimaryTypes.JAVA_OBJECT: 
             if (t.getJavaObject() instanceof Number) {
                 return (Number)t.getJavaObject();
             }
             if (objectToNumberConverters_!=null) {
                 Object o;
                 o = objectToNumberConverters_.get(t.getNameIndex());
                 if (o!=null) {
                     ObjectToNumber converter = (ObjectToNumber)o;
                     return converter.convert(t.getJavaObject(),instance_);
                 }
             }
             
       }
     }catch(ConversionException ex){
         throw ex;
     }catch(TermWareException ex){
         throw new ConversionException(t,t.getClass(),Number.class,ex.getMessage());
     }
     throw new ConversionException(t,t.getClass(),Number.class,NO_CONVERSION_FOUND_);
    }
    
    public BigDecimal getAsBigDecimal(Term t) throws ConversionException
    {
      try {  
        if (t.isNumber()) {
          if (t.isBigDecimal()) {
              return t.getBigDecimal();
          }else if (t.isBigInteger()) {
              return new BigDecimal(t.getBigInteger());
          }else if (t.isByte()) {
              return new BigDecimal(new Byte(t.getByte()).doubleValue());
          }else if (t.isBoolean()) {
              return new BigDecimal(t.getBoolean() ? 1.0 : 0.0);
          }else if (t.isDouble()) {
              return new BigDecimal(t.getDouble());
          }else if (t.isFloat()) {
              return new BigDecimal(t.getFloat());
          }else if (t.isInt()){
              return new BigDecimal((double)t.getInt());
          }else if (t.isLong()) {
              return new BigDecimal((double)t.getLong());
          }else if (t.isShort()) {
              return new BigDecimal((double)t.getShort());
          }else {
              throw new ConversionException(t,t.getClass(),BigDecimal.class,"unknown primary type");
          }
        }else if (t.isChar()) {
            // TODO
        }else if (t.isComplexTerm()) {
            return getNumberAsBigDecimal(getAsNumber(t));            
        }else if (t.isJavaObject()) {
            return getNumberAsBigDecimal(getAsNumber(t));
        }else if (t.isString()) {
            try {
                return new BigDecimal(t.getString());
            }catch(NumberFormatException ex){
                throw new ConversionException(t,t.getClass(),BigDecimal.class,ex.getMessage());
            }
        }
      }catch(ConversionException ex){
          throw ex;
      }catch(TermWareException ex){
          new ConversionException(t,t.getClass(),BigDecimal.class,ex.getMessage());
      }
      throw new ConversionException(t,t.getClass(),BigDecimal.class,NO_CONVERSION_FOUND_);
    }
    
    public BigDecimal getNumberAsBigDecimal(Number nm)
    {
        if (nm instanceof BigDecimal) {
            return (BigDecimal)nm;
        }else if (nm instanceof BigInteger) {
            return new BigDecimal((BigInteger)nm);
        }else{
            return new BigDecimal(nm.doubleValue());
        }
    }
    
    public BigInteger getAsBigInteger(Term t) throws ConversionException
    {
      try {
        switch(t.getPrimaryType0()) {
            case PrimaryTypes.ATOM :
                break;
            case PrimaryTypes.BIG_DECIMAL:
               {
                 BigDecimal dc = t.getBigDecimal();
                 if (dc.scale()==0) {
                    return dc.unscaledValue();
                 }else{
                    try{
                       return dc.setScale(0).unscaledValue();
                    }catch(ArithmeticException ex){
                        throw new ConversionException(t,t.getClass(),BigInteger.ONE.getClass(),ex.getMessage());
                    }
                 }
                }
                // unreachable.
                //break;
            case PrimaryTypes.BIG_INTEGER:
                return t.getBigInteger();
            case PrimaryTypes.BYTE:
                return BigInteger.valueOf((long)t.getByte());
            case PrimaryTypes.CHAR:
                break;
            case PrimaryTypes.COMPLEX_TERM:
                return getNumberAsBigInteger(getAsNumber(t));
            case PrimaryTypes.DOUBLE:
                return BigInteger.valueOf((long)t.getDouble());
            case PrimaryTypes.FLOAT:
                return BigInteger.valueOf((long)t.getFloat());
            case PrimaryTypes.INT:
                return BigInteger.valueOf((long)t.getInt());
            case PrimaryTypes.JAVA_OBJECT:
            {
                if (objectToNumberConverters_!=null) {
                    // TODO: navigate throught inheritance.
                    Object o=objectToNumberConverters_.get(t.getJavaObject().getClass());
                    if (o!=null) {
                        ObjectToNumber converter = (ObjectToNumber)o;
                        if (converter.possible(o,instance_)!=NOT_POSSIBLE) {
                            return getNumberAsBigInteger(converter.convert(o,instance_));
                        }
                    }
                }
            }
            break;
            case PrimaryTypes.LONG:
                return BigInteger.valueOf(t.getLong());
            case PrimaryTypes.SHORT:
                return BigInteger.valueOf((long)t.getShort());
            case PrimaryTypes.NIL:
                return BigInteger.ZERO;
            case PrimaryTypes.STRING:
                try {
                    return new BigInteger(t.getString());
                }catch(NumberFormatException ex){
                    throw new ConversionException(t,t.getClass(),JavaLangReflectHelper.getBigIntegerClass(),ex.getMessage());
                }
        }
      }catch(ConversionException ex){
          throw ex;
      }catch(TermWareException ex){
          new ConversionException(t,t.getClass(),BigInteger.class,ex.getMessage());
      }
      throw new ConversionException(t,t.getClass(),BigInteger.class,NO_CONVERSION_FOUND_);
    }

    public BigInteger getNumberAsBigInteger(Number nm) throws ConversionException
    {
      if (nm instanceof BigDecimal) {
          BigDecimal dc = (BigDecimal)nm;
          if (dc.scale()==0) {
              return dc.unscaledValue();
          }else{
              try {
                return dc.setScale(0).unscaledValue();
              }catch(ArithmeticException ex){
                  throw new ConversionException(dc, BigDecimal.class,BigInteger.class ,ex.getMessage());
              }
          }
      }else if (nm instanceof BigInteger) {
          return (BigInteger)nm;
      }else{
          return BigInteger.valueOf(nm.longValue());
      }
    }
    
    public byte       getAsByte(Term t) throws ConversionException
    {
      try {  
        switch(t.getPrimaryType0()) {
            case PrimaryTypes.ATOM :
                break;
            case PrimaryTypes.BIG_DECIMAL:
                return t.getBigDecimal().byteValue();
            case PrimaryTypes.BIG_INTEGER:
                return t.getBigInteger().byteValue();
            case PrimaryTypes.BYTE:
                return t.getByte();
            case PrimaryTypes.CHAR:
                break;
            case PrimaryTypes.COMPLEX_TERM:
                return getAsNumber(t).byteValue();
            case PrimaryTypes.DOUBLE:
                return (byte)t.getDouble();
            case PrimaryTypes.FLOAT:
                return (byte)t.getFloat();
            case PrimaryTypes.INT:
                return (byte)t.getInt();
            case PrimaryTypes.JAVA_OBJECT:
                return getAsNumber(t).byteValue();
            case PrimaryTypes.LONG:
                return (byte)t.getLong();
            case PrimaryTypes.NIL:
                return 0;
            case PrimaryTypes.SHORT:
                return (byte)t.getShort();
            case PrimaryTypes.STRING:
                try {
                    return Byte.parseByte(t.getString());
                }catch(NumberFormatException ex){
                    throw new ConversionException(t,t.getClass(),Byte.TYPE,ex.getMessage());
                }
                //unreachable break
                //break;
            default:
                break;
        }
      }catch(ConversionException ex){
          throw ex;
      }catch(TermWareException ex){
        throw new ConversionException(t,t.getClass(),Byte.TYPE, ex.getMessage());
      }
      throw new ConversionException(t,t.getClass(),Byte.TYPE, NO_CONVERSION_FOUND_);
    }
    
    /**
     * convert <code> t </code> to double value.
     *@param t - term to convert.
     *@return appropriative double value, if conversion is possible.
     */
    public double     getAsDouble(Term t) throws ConversionException
    {
      try {
        switch(t.getPrimaryType0()){
          case PrimaryTypes.ATOM:
              break;
          case PrimaryTypes.BIG_DECIMAL:
              return t.getBigDecimal().doubleValue();
          case PrimaryTypes.BIG_INTEGER:
              return t.getBigInteger().doubleValue();
          case PrimaryTypes.BYTE:
              return (double)t.getByte();
          case PrimaryTypes.CHAR:
              break;
          case PrimaryTypes.COMPLEX_TERM:
              return getAsNumber(t).doubleValue();
          case PrimaryTypes.DOUBLE:
              return t.getDouble();
          case PrimaryTypes.FLOAT:
              return t.getFloat();
          case PrimaryTypes.INT:
              return (double)t.getInt();
          case PrimaryTypes.JAVA_OBJECT:
              return getAsNumber(t).doubleValue();
          case PrimaryTypes.LONG:
              return (double)t.getLong();
          case PrimaryTypes.NIL:
              break;
          case PrimaryTypes.SHORT:
              return (double)t.getShort();
          case PrimaryTypes.STRING:
              try {
                  return Double.parseDouble(t.getString());
              }catch(NumberFormatException ex){
                  throw new ConversionException(t,t.getClass(),Double.TYPE,ex.getMessage());
              }
        }
      }catch(ConversionException ex){
          throw ex;
      }catch(TermWareException ex){
         throw new ConversionException(t,t.getClass(),Double.TYPE, ex.getMessage());
      }
      throw new ConversionException(t,t.getClass(),Double.TYPE, NO_CONVERSION_FOUND_);
    }
    
    public float      getAsFloat(Term t) throws ConversionException
    {
      try {
        switch(t.getPrimaryType0()) {
            case PrimaryTypes.ATOM:
                break;
            case PrimaryTypes.BIG_DECIMAL:
                return t.getBigDecimal().floatValue();
            case PrimaryTypes.BIG_INTEGER:
                return t.getBigInteger().floatValue();
            case PrimaryTypes.BYTE:
                return (float)t.getByte();
            case PrimaryTypes.CHAR:
                break;
            case PrimaryTypes.COMPLEX_TERM:
                return getAsNumber(t).floatValue();
            case PrimaryTypes.DOUBLE:
                return (float)t.getDouble();
            case PrimaryTypes.FLOAT:
                return t.getFloat();
            case PrimaryTypes.INT:
                return (float)t.getInt();
            case PrimaryTypes.JAVA_OBJECT:
                return getAsNumber(t).floatValue();
            case PrimaryTypes.LONG:
                return (float)t.getLong();
            case PrimaryTypes.NIL:
                break;
            case PrimaryTypes.SHORT:
                return (float)t.getShort();
            case PrimaryTypes.STRING:
                try {
                    return Float.parseFloat(t.getString());
                }catch(NumberFormatException ex){
                    throw new ConversionException(t,t.getClass(),Float.TYPE,ex.getMessage());
                }
        }
      }catch(ConversionException ex){
          throw ex;
      }catch(TermWareException ex){
          throw new ConversionException(t,t.getClass(),Float.TYPE,ex.getMessage());
      }
      throw new ConversionException(t,t.getClass(),Float.TYPE,NO_CONVERSION_FOUND_);
    }
        
    public int        getAsInt(Term t) throws ConversionException
    {
      try {
        switch(t.getPrimaryType0())  {
          case PrimaryTypes.ATOM:
              break;
          case PrimaryTypes.BIG_DECIMAL:
              return t.getBigDecimal().intValue();
          case PrimaryTypes.BIG_INTEGER:
              return t.getBigInteger().intValue();
          case PrimaryTypes.BYTE:
              return (int)t.getByte();
          case PrimaryTypes.BOOLEAN:
              return (t.getBoolean() ? 1 : 0);
          case PrimaryTypes.CHAR:
              break;
          case PrimaryTypes.COMPLEX_TERM:
              return getAsNumber(t).intValue();
          case PrimaryTypes.DOUBLE:
              return (int)t.getDouble();
          case PrimaryTypes.FLOAT:
              return (int)t.getFloat();
          case PrimaryTypes.INT:
              return (int)t.getInt();
          case PrimaryTypes.JAVA_OBJECT:
              return getAsNumber(t).intValue();
          case PrimaryTypes.LONG:
              return (int)t.getLong();
          case PrimaryTypes.NIL:
              break;
          case PrimaryTypes.SHORT:
              return (int)t.getShort();
          case PrimaryTypes.STRING:
              try {
                  return Integer.parseInt(t.getString());
              }catch(NumberFormatException ex){
                  throw new ConversionException(t,t.getClass(),Integer.TYPE,ex.getMessage());
              }
          default:
              break;
        }
      }catch(ConversionException ex){
          throw ex;
      }catch(TermWareException ex){
          throw new ConversionException(t,t.getClass(),Integer.TYPE,ex.getMessage());
      }
      throw new ConversionException(t,t.getClass(),Integer.TYPE,NO_CONVERSION_FOUND_);
    }
   
    /**
     * get long term value from <code> t </code>, convert to required type if necessory
     *@param t - term to extract value from.
     *@return long value.
     */
    public long       getAsLong(Term t) throws ConversionException
    {
      try {
        switch(t.getPrimaryType0()) {
            case PrimaryTypes.ATOM:
                break;
            case PrimaryTypes.BIG_DECIMAL:
                return t.getBigDecimal().longValue();
            case PrimaryTypes.BIG_INTEGER:
                return t.getBigInteger().longValue();
            case PrimaryTypes.BOOLEAN:
                return (t.getBoolean() ? 1 : 0);
            case PrimaryTypes.BYTE:
                return (long)t.getByte();
            case PrimaryTypes.CHAR:
                break;
            case PrimaryTypes.COMPLEX_TERM:
                return getAsNumber(t).longValue();
            case PrimaryTypes.DOUBLE:
                return (long)t.getDouble();
            case PrimaryTypes.FLOAT:
                return (long)t.getFloat();
            case PrimaryTypes.JAVA_OBJECT:
                return getAsNumber(t).longValue();
            case PrimaryTypes.LONG:
                return t.getLong();
            case PrimaryTypes.INT:
                return (long)t.getInt();
            case PrimaryTypes.NIL:
                break;
            case PrimaryTypes.SHORT:
                return (long)t.getShort();
            case PrimaryTypes.STRING:
                if (!stringConversionEnabled_) break;
                try {
                    return Long.parseLong(t.getString());
                }catch(NumberFormatException ex){
                    throw new ConversionException(t,t.getClass(),Long.TYPE,ex.getMessage());
                }
            default:
                break;
        }
      }catch(ConversionException ex){
          throw ex;
      }catch(TermWareException ex){
          throw new ConversionException(t,t.getClass(),Long.TYPE,ex.getMessage());
      }
      throw new ConversionException(t,t.getClass(),Long.TYPE,NO_CONVERSION_FOUND_);
    }
    
    
    /**
     * get short term value, convert to required type if necessory
     */
    public short      getAsShort(Term t) throws ConversionException
    {
      try {
        switch (t.getPrimaryType0()) {
          case PrimaryTypes.ATOM:
              break;
          case PrimaryTypes.BIG_DECIMAL:
              // TODO: think about conversion rules.
              return t.getBigDecimal().shortValue();
          case PrimaryTypes.BIG_INTEGER:
              return t.getBigInteger().shortValue();
          case PrimaryTypes.BOOLEAN:
              return (t.getBoolean() ? (short)1 : (short)0);
          case PrimaryTypes.BYTE:
              return (short)t.getByte();
          case PrimaryTypes.CHAR:
              break;
          case PrimaryTypes.COMPLEX_TERM:
              return getAsNumber(t).shortValue();
          case PrimaryTypes.DOUBLE:
              return (short)t.getDouble();
          case PrimaryTypes.FLOAT:
              return (short)t.getFloat();
          case PrimaryTypes.INT:
              return (short)t.getInt();
          case PrimaryTypes.JAVA_OBJECT:
              return getAsNumber(t).shortValue();
          case PrimaryTypes.LONG:
              return (short)t.getLong();
          case PrimaryTypes.NIL:
              break;
          case PrimaryTypes.SHORT:
              return t.getShort();
          case PrimaryTypes.STRING:
              if (stringConversionEnabled_) {
                  try {
                      return Short.parseShort(t.getString());
                  }catch(NumberFormatException ex){
                      throw new ConversionException(t,t.getClass(),Short.TYPE,ex.getMessage());
                  }
              }                                
        }
      }catch(ConversionException ex){
          throw ex;
      }catch(TermWareException ex){
          throw new ConversionException(t,t.getClass(),Short.TYPE,ex.getMessage());
      }
      throw new ConversionException(t,t.getClass(),Short.TYPE,NO_CONVERSION_FOUND_);
    }
    
    public Object getAsObject(Term t) throws ConversionException
    {
        switch(t.getPrimaryType0()) {
            case PrimaryTypes.ATOM:
                //TODO: AtomToObject
                return t;
            case PrimaryTypes.BIG_DECIMAL:
                return t.getBigDecimal();
            case PrimaryTypes.BIG_INTEGER:
                return t.getBigInteger();
            case PrimaryTypes.BOOLEAN:
                return new Boolean(t.getBoolean());
            case PrimaryTypes.BYTE:
                return new Byte(t.getByte());
            case PrimaryTypes.CHAR:
                return new Character(t.getChar());
            case PrimaryTypes.COMPLEX_TERM:
                // list -> array
                  if (t.getNameIndex().equals(TermWareSymbols.CONS_INDEX)) {
                     Object[] arr=getAsObjectArray(t);
                     return arr;
                  }
                if (termToObjectConverters_!=null) {
                    // TODO: handle inheritance
                    Object o =termToObjectConverters_.get(t.getNameIndex());
                    if (o!=null) {
                        TermToObject converter = (TermToObject)o;
                        if (converter.possible(t,instance_)!=NOT_POSSIBLE) {
                            return converter.convert(t,instance_);
                        }
                    }
                }
                if (termToNumberConverters_!=null) {
                    // TODO: handle inheritance
                    Object o = termToNumberConverters_.get(t.getNameIndex());
                    if (o!=null) {
                        TermToNumber converter = (TermToNumber)o;
                        if (converter.possible(t,instance_)!=NOT_POSSIBLE) {
                            return converter.convert(t,instance_);
                        }
                    }
                }
                // if not -- return term unchanged.
                return t;
            case PrimaryTypes.DOUBLE:
                return new Double(t.getDouble());
            case PrimaryTypes.FLOAT:
                return new Float(t.getFloat());
            case PrimaryTypes.INT:
                return new Integer(t.getInt());
            case PrimaryTypes.JAVA_OBJECT:
                return t.getJavaObject();
            case PrimaryTypes.LONG:
                return new Long(t.getLong());
            case PrimaryTypes.NIL:
                return this;
            case PrimaryTypes.SHORT:
                return new Short(t.getShort());
            case PrimaryTypes.STRING:
                return t.getString();
            default:
                return t;
        }
    }
    
    public Object[] getAsObjectArray(Term t) throws ConversionException
    {
     if (t.isNil()) {
         return JavaLangReflectHelper.getEmptyArray();
     }
     if (t.isComplexTerm()) {         
       if (t.getNameIndex().equals(TermWareSymbols.CONS_INDEX)) {
          // list -> array
          Term curr=t;
          ArrayList<Object> al = new ArrayList<Object>();
          while(curr.getNameIndex().equals(TermWareSymbols.CONS_INDEX)) {
               al.add(getAsObject(curr.getSubtermAt(0)));
          }
       }else if(t.getNameIndex().equals(TermWareSymbols.SET_INDEX)) {
           // set -> array
           Object[] retval = new Object[t.getArity()];
           for(int i=0;i<t.getArity();++i){
                retval[i]=getAsObject(t.getSubtermAt(i));
           }
           return retval;
       }else{
           if (termToObjectArrayConverters_!=null) {
              Object o=termToObjectArrayConverters_.get(t.getNameIndex());
              if (o!=null) {
                       TermToObjectArray converter = (TermToObjectArray)o;
                       if (converter.possible(t,instance_)!=NOT_POSSIBLE) {
                           return converter.convert(t,instance_);
                       }
              }
           }
       }
     }
     throw new ConversionException(t,t.getClass(),JavaLangReflectHelper.getObjectArrayClass(),NO_CONVERSION_FOUND_);     
    }
    
    public Object getAsVoid(Term t) throws ConversionException
    {
        if (t.isNil()) {
            return null;
        }
        throw new ConversionException(t,t.getClass(),Void.TYPE,NO_CONVERSION_FOUND_);
    }
    
    public Object getAsObjectWithClass(Class<?> aClass,Term t) throws ConversionException
    {
     try {
       if (aClass.isAssignableFrom(t.getClass())) {
           return t;
       }  
       if (t.isJavaObject()) {
          if (aClass.isAssignableFrom(t.getJavaObject().getClass())) {
              return t.getJavaObject();
          }
       }
       if (aClass.isArray()) {
          Class elementClass=aClass.getComponentType();
          return getAsObjectArrayWithClass(elementClass,t);
       }else if (aClass.isPrimitive()) {
          if (aClass.equals(Boolean.TYPE)) {
              return new Boolean(getAsBoolean(t));
          }else if (aClass.equals(Character.TYPE)) {
              return new Character(getAsChar(t));
          }else if (aClass.equals(Byte.TYPE)) {
              return new Byte(getAsByte(t));
          }else if (aClass.equals(Short.TYPE)) {
              return new Short(getAsShort(t));
          }else if (aClass.equals(Integer.TYPE)) {
              return new Integer(getAsInt(t));
          }else if (aClass.equals(Long.TYPE)) {
              return new Long(getAsLong(t));
          }else if (aClass.equals(Float.TYPE)) {
              return new Float(getAsFloat(t));
          }else if (aClass.equals(Double.TYPE)) {
              return new Double(getAsDouble(t));
          }else if (aClass.equals(Void.TYPE)) {
              return getAsVoid(t);
          }else{
              // unknown Java primitive type.
              throw new ConversionException(t,t.getClass(),Void.TYPE,"Unknown Java primitive type (new JDK version?)");
          }
       }else{
          // check for well-known classes
          if (aClass.isAssignableFrom(JavaLangReflectHelper.getBigIntegerClass())) {
              return getAsBigInteger(t);
          }else if (aClass.isAssignableFrom(JavaLangReflectHelper.getBigDecimalClass())){
              return getAsBigDecimal(t);
          }else if (aClass.isAssignableFrom(String.class)) {
              return getAsString(t);
          }
          
          // check for collections:
          
          if (aClass.isInstance(Collection.class)) {
              if (aClass.isInstance(Set.class)) {
                  if (aClass.isInterface()) {
                      if (aClass.equals(Set.class)) {
                          // create HashSet
                          if (t.isComplexTerm()) {
                              if (t.getNameIndex().equals(TermWareSymbols.SET_INDEX)) {
                                 HashSet<Object> hs=new HashSet<Object>();
                                 for (int i=0; i<t.getArity();++i){
                                     hs.add(getAsObject(t.getSubtermAt(i)));
                                 }
                                 return hs;
                              }
                          }
                      }else{
                      // TODO: check for other standard interfaces, 
                      // SortedSet
                      }
                  }else{
                     // this is concrete set class
                     if (t.isComplexTerm()) {
                         if (t.getNameIndex().equals(TermWareSymbols.SET_INDEX)){
                            Set retval=(Set)aClass.newInstance();
                            for(int i=0;i<t.getArity();++i){
                               retval.add(getAsObject(t.getSubtermAt(i)));
                            }
                            return retval;                             
                         }
                     }
                  }
              }else if (aClass.isInstance(Map.class)) {
                      // TODO: check for other standard interfaces, 
                      // Map
                      // SortedMap                
              }else if (aClass.isInstance(List.class)) {
                  if (aClass.isInterface()) {
                      // this can be only List interface.
                      // let's create and fill ArrayList for our list.
                      if (t.isComplexTerm()){
                          if (t.getNameIndex().equals(TermWareSymbols.CONS_INDEX)) {
                             Term curr=t;
                             ArrayList<Object> retval=new ArrayList<Object>();
                             while(!curr.isNil()) {
                                 retval.add(getAsObject(t.getSubtermAt(0)));
                                 curr=curr.getSubtermAt(1);
                             }
                          }
                      }else if(t.isNil()){
                          return new ArrayList();
                      }
                  }
              }
          }// end of collections
          
          //now try to find transformer for this class
          if (termToObjectWithClassConvertersByClass_!=null) {
              Object o=termToObjectWithClassConvertersByClass_.get(aClass);
              if (o!=null) {
                  TermToObject converter=(TermToObject)o;
                  if (converter.possible(t,instance_)!=NOT_POSSIBLE) {
                      return converter.convert(t, instance_);
                  }
              }
          }
          if (termToObjectWithClassConvertersByTerm_!=null) {
              Object o=termToObjectWithClassConvertersByTerm_.get(t.getNameIndex());
              if (o!=null) {
                  TermToObject converter=(TermToObject)o;
                  if (converter.possible(t,instance_)!=NOT_POSSIBLE) {
                      return converter.convert(t, instance_);
                  }
              }
          }
          
          // now try to associate term with constructor call
          //  TODO: try to interpret as subclasses constructor.
          while (aClass.getName().endsWith(t.getName())) {
              int lastIndex = aClass.getName().lastIndexOf(t.getName());
              if (lastIndex==-1) break; // impossible ?
              if (aClass.getName().charAt(lastIndex-1)!='.') break;
              Constructor[] constructors = aClass.getConstructors();
              ConversionException svEx=null;
              for(int i=0; i<constructors.length; ++i){
                  if ((constructors[i].getModifiers() & Modifier.PUBLIC)==0) continue;
                  Class[] constructorArgumentsTypes = constructors[i].getParameterTypes();
                  if (t.getArity()!=constructorArgumentsTypes.length) {
                      continue;
                  }
                  Object[] constructorArguments=new Object[t.getArity()];
                  try {
                      for(int j=0;j<t.getArity();++j){
                          constructorArguments[i]=getAsObjectWithClass(constructorArgumentsTypes[j],t.getSubtermAt(j));
                      }
                      Object retval = constructors[i].newInstance(constructorArguments);
                      return retval;
                  }catch(ConversionException ex){
                      // try to find more appropriative constructor, i. e. do nothing
                      svEx=ex;
                      continue;
                  }
              }
              if (svEx!=null) {
                  throw new ConversionException(t,t.getClass(),aClass,"can't instantiate constructor:"+svEx.getMessage());
              }
              break;
          }          
       }
     }catch(InstantiationException ex){
         throw new TermWareRuntimeException(ex);
     }catch(IllegalAccessException ex){
         throw new TermWareRuntimeException(ex);
     }catch(InvocationTargetException ex){
         throw new TermWareRuntimeException(ex);
     }
     throw new ConversionException(t,t.getClass(),aClass,NO_CONVERSION_FOUND_);
    }
    
    public Object[] getAsObjectArrayWithClass(Class aClass,Term t) throws ConversionException
    {
      // t must be List.
      boolean termIsList = t.getNameIndex().equals(TermWareSymbols.CONS_INDEX);

      if (!termIsList) {
          //passing of aClass is incorrect here.
          // we must construct type of array from thos class, but JVM have no such
          // power yet.
          throw new ConversionException(t,t.getClass(),aClass,"term must be list");          
      }
      Term curr=t;
      int len=0;
      while(true) {
          if (t.getArity()!=2) {
              throw new RuntimeAssertException("arity of list must be 2",t);
          }
          curr=curr.getSubtermAt(1);
          ++len;
          if (curr.isNil()) break;
          Object nameIndex=curr.getNameIndex();
          if (!nameIndex.equals(TermWareSymbols.CONS_INDEX)) {
                  throw new RuntimeAssertException("incorrect list array",t);
          }
      }
      Object[] retval=new Object[len];
      curr=t;
      int i=0;
      while(!curr.isNil()) {
          retval[i]=getAsObjectWithClass(aClass,curr.getSubtermAt(0));
          ++i;
          curr=curr.getSubtermAt(1);
      }
      return retval;
    }
    
    
   // public Object getAsObjectWithClass(Class aClass, Object o);
    
    /**
     * adopt object to term use default rules and custom converters.
     */
    public Term  adopt(Object v) throws ConversionException
    {
        if (v==null) {
            return TermFactory.createNIL();
        }
        if (v instanceof Term) {
            return (Term)v;
        }else if (v instanceof String) {
            return TermFactory.createString((String)v);
        }else if (v instanceof Character) {
            return instance_.getTermFactory().createChar(((Character)v).charValue());
        }else if (v instanceof Byte) {
            return instance_.getTermFactory().createByte(((Byte)v).byteValue());
        }else if (v instanceof Short) {
            return instance_.getTermFactory().createShort(((Short)v).shortValue());
        }else if (v instanceof Integer) {
            return instance_.getTermFactory().createInt(((Integer)v).intValue());
        }else if (v instanceof Long) {
            return instance_.getTermFactory().createLong(((Long)v).longValue());
        }else if (v instanceof BigDecimal) {
            return instance_.getTermFactory().createBigDecimal((BigDecimal)v);
        }else if (v instanceof BigInteger) {
            return instance_.getTermFactory().createBigInteger((BigInteger)v);
        }else if (v instanceof Float) {
            return instance_.getTermFactory().createFloat(((Float)v).floatValue());
        }else if (v instanceof Double) {
            return instance_.getTermFactory().createDouble(((Double)v).doubleValue());
        }else if (v instanceof Boolean) {
            return instance_.getTermFactory().createBoolean(((Boolean)v).booleanValue());
        }else{
            
            if (objectToTermConverters_!=null) {
                // TODO: check superclasseses and interfaces of v
                Object o=objectToTermConverters_.get(v.getClass());
                if (o!=null) {
                    ObjectToTerm converter=(ObjectToTerm)o;
                    if (converter.possible(v,instance_)!=NOT_POSSIBLE) {
                        return converter.convert(v,instance_);
                    }
                }
            }
            
            if (v.getClass().isArray()) { // now check collections
                // array -> list
                return adoptArray((Object[])v);
            }else if (v instanceof List) {
                return adoptArray(((List)v).toArray());
            }
            
            // now create JTerm
            try {
              return instance_.getTermFactory().createJTerm(v);
            }catch(TermWareException ex){
                throw new ConversionException(v,v.getClass(),Term.class,"exception during converting:"+ex.getMessage());
            }
        }
        //throw new ConversionException(v,v.getClass(),Term.class,NO_CONVERSION_FOUND_);
    }
  
    
    public Term  adoptArray(Object[] v) throws ConversionException
    {
      Term retval=instance_.getTermFactory().createNil();  
      if (v.length==0) return retval;
      return adoptArray(v,v.length-1,retval);
    }

    public Term  adoptArray(Object[] v, int i, Term tail) throws ConversionException
    {
      try {
        if (i<0) return tail;
        return adoptArray(v,i-1,new ListTerm(adopt(v[i]),tail));
      }catch(TermWareException ex){
          throw new TermWareRuntimeException(ex);
      }
    }

    
    public Term  adopt(Term t)
    {
        return t;
    }
    
    public Term  adopt(byte v)
    {
     return instance_.getTermFactory().createByte(v);
    }
    
    public Term  adopt(double v)
    {
     return new DoubleTerm(v);   
    }
    
    public Term  adopt(float f)
    { 
      return new FloatTerm(f);
    }
    
    public Term  adopt(int i)
    { 
      return new IntTerm(i);
    }
    
    public Term  adopt(long i)
    {
      return new LongTerm(i);
    }
    
    public Term  adopt(short i)
    {
      return new ShortTerm(i);  
    }
    
    public Term adopt(boolean v)
    {
        return new BooleanTerm(v);
    }
    
    public Number getStringAsNumber(String s)
    {
      if (s.indexOf('.')==-1) {
          return new Double(s);
      }else{
          return new BigInteger(s);
      }
    }
    
    public Number getCharAsNumber(char ch) throws ConversionException
    {
      if (Character.isDigit(ch)) {
          return new Short(Character.toString(ch));
      }else{
          throw new ConversionException(new Character(ch),Character.TYPE,Number.class,"must be digit");
      }
    }
    
    public Number getBooleanAsNumber(boolean b)
    {
      if (b) return new Integer(1); else return new Integer(0);  
    }
    
    
    public synchronized void addTermToBooleanConverter(Comparable nameIndex, TermToBoolean converter) 
    {
     addToMapField(termToBooleanConvertersField_,nameIndex,converter);
    }

    
    public synchronized void removeTermToBooleanConverter(Comparable nameIndex) 
    {
      removeFromMapField(termToBooleanConvertersField_,nameIndex);
    }
    
    
    public synchronized void addTermToCharConverter(Comparable nameIndex,TermToChar converter)
    {
      try {
        Field f = getClass().getField("termToCharConverters_");
        addToMapField(f,nameIndex,converter);
      }catch(NoSuchFieldException ex){
          throw new TermWareRuntimeException(ex);
      }
    }
    
    
    public synchronized void removeTermToCharConverter(Comparable nameIndex)
    {
      try{  
        Field f = getClass().getField("termToCharConverters_");
        removeFromMapField(f,nameIndex);
      }catch(NoSuchFieldException ex){
          throw new TermWareRuntimeException(ex);
      }
    }
       
    
    public synchronized void addTermToStringConverter(Comparable nameIndex,TermToString converter)
    {
      addToMapField(termToStringConvertersField_,nameIndex,converter);
    }
        
    public synchronized void removeTermToStringConverter(Comparable nameIndex)
    {
        removeFromMapField(termToStringConvertersField_,nameIndex);
    }

    
    public synchronized void addTermToNumberConverter(Comparable nameIndex,TermToNumber converter)
    {
      try {
        Field f = getClass().getField("termToNumberConverters_");
        addToMapField(f,nameIndex,converter);
      }catch(NoSuchFieldException ex){
          throw new TermWareRuntimeException(ex);
      }      
    }
        
    public synchronized void removeTermToNumberConverter(Comparable nameIndex)
    {
      try {
        Field f = getClass().getField("termToNumberConverters_");
        removeFromMapField(f,nameIndex);
      }catch(NoSuchFieldException ex){
          throw new TermWareRuntimeException(ex);
      }      
    }
    
        
    public synchronized void addObjectToStringConverter(Class classToConvert, ObjectToString converter)
    {
      try {
        Field f=getClass().getField("objectToStringConverters_");
        addToMapField(f,classToConvert,converter);
      }catch(NoSuchFieldException ex){
          throw new TermWareRuntimeException(ex);
      }              
    }
    
    public synchronized void removeObjectToStringConverter(Class classToConvert)
    {
      try {
        Field f=getClass().getDeclaredField("objectToStringConverters_");
        removeFromMapField(f,classToConvert);
      }catch(NoSuchFieldException ex){
          throw new TermWareRuntimeException(ex);
      }            
    }

    public synchronized void addObjectToNumberConverter(Class classToConvert,ObjectToNumber converter)
    {
      try {
        Field f=getClass().getDeclaredField("objectToNumberConverters_");
        addToMapField(f,classToConvert,converter);
      }catch(NoSuchFieldException ex){
          throw new TermWareRuntimeException(ex);
      }              
    }
    
    public synchronized void removeObjectToNumberConverter(Class classToConvert)
    {
        if (objectToStringConverters_!=null) {
            objectToStringConverters_.remove(classToConvert);
        }
    }
    
    public synchronized void addObjectToTermConverter(Class classToConvert,ObjectToTerm converter)
    {
      try {
        Field f=getClass().getField("objectToTermConverters_");
        addToMapField(f,classToConvert,converter);
      }catch(NoSuchFieldException ex){
          throw new TermWareRuntimeException(ex);
      }              
    }
    
    public synchronized void removeObjectToTermConverter(Class classToConvert)
    {
        if (objectToTermConverters_!=null) {
            objectToTermConverters_.remove(classToConvert);
        }
    }

    private void lazyInitMapField(Field field)
    {
      try {
        if (field.get(this)==null) {
          field.set(this, Collections.synchronizedMap(new HashMap<Class<?>,Object>()));
        }
      }catch(IllegalAccessException ex){
          throw new TermWareRuntimeException(ex);
      }              
    }
    
    private void addToMapField(Field field, Comparable  nameIndex, Object o)
    {
      lazyInitMapField(field);
      try {
        ((Map)field.get(this)).put(nameIndex, o);
      }catch(IllegalAccessException ex){
          throw new TermWareRuntimeException(ex);
      }                      
    }
    
    
    private void addToMapField(Field field,Class<?> theClassToAdd, Object o)
    {
     lazyInitMapField(field);
     try {
       ((Map<Class<?>,Object>)field.get(this)).put(theClassToAdd,o);
      }catch(IllegalAccessException ex){
          throw new TermWareRuntimeException(ex);
      }                           
    }
    
    private void removeFromMapField(Field field, Comparable nameIndex)
    {
      try {
        Object fo = field.get(this);
        if (fo==null) return;
        Map map=(Map)fo;
        map.remove(nameIndex);
      }catch(IllegalAccessException ex){
          throw new TermWareRuntimeException(ex);
      }                              
    }
    
    
    private void removeFromMapField(Field field, Class classToRemove)
    {
     try{
      Object o = field.get(this);
      if (o==null) return;
      ((Map)o).remove(classToRemove);
     }catch(IllegalAccessException ex){
          throw new TermWareRuntimeException(ex);
     }                                    
    }

    
    private Map termToBooleanConverters_=null;
    private static Field termToBooleanConvertersField_;
    static {
      try {
          termToBooleanConvertersField_=TypeConversion.class.getDeclaredField("termToBooleanConverters_");
      }catch(NoSuchFieldException ex){
         // System.err.println("NoSuchField: termToBooleanConverters_");
          throw new TermWareRuntimeException(ex);
      }
    }
    
    private Map termToCharConverters_=null;
    
    private Map termToStringConverters_=null;
    
    private static Field termToStringConvertersField_;
    static {
      try {
          termToStringConvertersField_=TypeConversion.class.getDeclaredField("termToStringConvertersField_");
      }catch(NoSuchFieldException ex){
          throw new TermWareRuntimeException(ex);
      }
    }
    
    private Map termToNumberConverters_=null;
    private Map termToObjectConverters_=null;
    private Map termToObjectArrayConverters_=null;
    
    private Map termToObjectWithClassConvertersByTerm_=null;
    private Map termToObjectWithClassConvertersByClass_=null;
    
    private Map objectToStringConverters_=null;
    private Map objectToNumberConverters_=null;
    private Map objectToBigDecimalConverters_=null;
    private Map objectToTermConverters_=null;
    
    private boolean  stringConversionEnabled_=true;
    
    private TermWareInstance instance_;
    
    
    private final String NO_CONVERSION_FOUND_="no conversion found.";
    
}
