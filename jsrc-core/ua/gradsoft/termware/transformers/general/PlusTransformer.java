package ua.gradsoft.termware.transformers.general;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002 - 2005
 * $Id: PlusTransformer.java,v 1.6 2008-03-24 22:33:10 rssh Exp $
 */

import java.util.*;
import java.math.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;
import ua.gradsoft.termware.util.LogHelper;


/**
 * tramsformer for plus
 *<pre>
 *    +x -> x
 *    x+y -> ariphmetics sum of x and y if they are numeric.
 *</pre>
 **/
public class PlusTransformer extends AbstractBuildinTransformer {
    
    private PlusTransformer()
    {}
    
    public static PlusTransformer INSTANCE = new PlusTransformer();
    
    
    public  Term  transform(Term t, TermSystem termSys, TransformationContext ctx) throws TermWareException {
        return static_transform(t,termSys,ctx); }
    
    public  static Term  static_transform(Term t, TermSystem termSys, TransformationContext ctx) throws TermWareException {
        if (termSys.isLoggingMode()) {
          LogHelper.log(termSys, PlusTransformer.class, "PlusTransformer, t=",t);  
        }
        
        if (t.getArity()==1) {
            // +(x) -> x
            ctx.setChanged(true);
            t= t.getSubtermAt(0);
        }else if(t.getArity()==2) {
            Term frs=t.getSubtermAt(0);
            Term snd=t.getSubtermAt(1);
            if (frs.isNumber() && snd.isNumber()) {
                switch(frs.getPrimaryType0()) {
                    case PrimaryTypes.BIG_DECIMAL:
                    {
                        BigDecimal bc=frs.getBigDecimal().add(snd.getAsBigDecimal(termSys.getInstance()));
                        ctx.setChanged(true);
                        t= termSys.getInstance().getTermFactory().createBigDecimal(bc);
                    }
                    break;
                    case PrimaryTypes.BIG_INTEGER:
                    {
                        if (snd.isBigDecimal()){
                            BigDecimal bc=frs.getAsBigDecimal(termSys.getInstance()).add(snd.getBigDecimal());
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createBigDecimal(bc);
                        }else if (snd.isDouble()) {
                            double d=frs.getBigInteger().doubleValue()+snd.getDouble();
                            ctx.setChanged(true);
                            t= termSys.getInstance().getTermFactory().createDouble(d);
                        }else if (snd.isFloat()) {
                            double d=frs.getBigInteger().doubleValue()+snd.getFloat();
                            ctx.setChanged(true);
                            t= termSys.getInstance().getTermFactory().createDouble(d);
                        }else{
                            BigInteger bi=frs.getBigInteger().add(snd.getAsBigInteger(termSys.getInstance()));
                            ctx.setChanged(true);
                            t = termSys.getInstance().getTermFactory().createBigInteger(bi);
                        }
                    }
                    break;
                    case PrimaryTypes.BYTE:
                    {
                        if (snd.isBigDecimal()) {
                            BigDecimal bc=snd.getBigDecimal().add(frs.getAsBigDecimal(termSys.getInstance()));
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createBigDecimal(bc);
                        }else if (snd.isBigInteger()) {
                            BigInteger bi=frs.getAsBigInteger(termSys.getInstance()).add(snd.getBigInteger());
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createBigInteger(bi);
                        }else if (snd.isDouble()) {
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createDouble((double)frs.getByte()+snd.getDouble());
                        }else if (snd.isFloat()) {
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createFloat((float)frs.getByte()+snd.getFloat());
                        }else if (snd.isByte()){
                            byte b1=frs.getByte();
                            byte b2=snd.getByte();
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createInt(b1+b2);
                        }else if (snd.isShort()) {
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createShort((short)((short)frs.getByte()+snd.getShort()));
                        }else if (snd.isInt()) {
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createInt((int)frs.getByte()+snd.getInt());
                        }else if (snd.isLong()) {
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createLong((long)frs.getByte()+snd.getLong());
                        }else{
                            throw new AssertException("Unknown number type of "+TermHelper.termToString(snd));
                        }
                    }
                    break;
                    case PrimaryTypes.DOUBLE:
                    {
                        if (snd.isBigDecimal()) {
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createBigDecimal(frs.getAsBigDecimal(termSys.getInstance()).add(snd.getBigDecimal()));
                        }else{
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createDouble(frs.getDouble()+snd.getAsDouble(termSys.getInstance()));
                        }
                    }
                    break;
                    case PrimaryTypes.FLOAT:
                        if (snd.isBigDecimal()) {
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createBigDecimal(frs.getAsBigDecimal(termSys.getInstance()).add(snd.getBigDecimal()));
                        }else if (snd.isDouble()){
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createDouble((double)frs.getFloat()+snd.getDouble());
                        }else{
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createFloat(frs.getFloat()+snd.getAsFloat(termSys.getInstance()));
                        }
                        break;
                    case PrimaryTypes.INT:
                        if (snd.isBigDecimal()) {
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createBigDecimal(frs.getAsBigDecimal(termSys.getInstance()).add(snd.getBigDecimal()));
                        }else if(snd.isBigInteger()) {
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createBigInteger(frs.getAsBigInteger(termSys.getInstance()).add(snd.getBigInteger()));
                        }else if(snd.isDouble()) {
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createDouble((double)frs.getInt()+snd.getDouble());
                        }else if(snd.isFloat()) {
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createFloat((float)frs.getInt()+snd.getFloat());
                        }else if(snd.isLong()) {
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createLong((long)frs.getInt()+snd.getLong());
                        }else{
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createInt(frs.getInt()+snd.getAsInt(termSys.getInstance()));
                        }
                        break;
                    case PrimaryTypes.LONG:
                        if (snd.isBigDecimal()) {
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createBigDecimal(frs.getAsBigDecimal(termSys.getInstance()).add(snd.getBigDecimal()));
                        }else if(snd.isBigInteger()) {
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createBigInteger(frs.getAsBigInteger(termSys.getInstance()).add(snd.getBigInteger()));
                        }else if(snd.isDouble()) {
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createDouble((double)frs.getLong()+snd.getDouble());
                        }else if(snd.isFloat()) {
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createFloat((float)frs.getLong()+snd.getFloat());
                        }else if(snd.isLong()) {
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createLong(frs.getLong()+snd.getLong());
                        }else{
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createLong(frs.getLong()+snd.getAsLong(termSys.getInstance()));
                        }
                        break;
                    case PrimaryTypes.SHORT:
                        if (snd.isBigDecimal()) {
                            BigDecimal bc=snd.getBigDecimal().add(frs.getAsBigDecimal(termSys.getInstance()));
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createBigDecimal(bc);
                        }else if (snd.isBigInteger()) {
                            BigInteger bi=frs.getAsBigInteger(termSys.getInstance()).add(snd.getBigInteger());
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createBigInteger(bi);
                        }else if (snd.isDouble()) {
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createDouble((double)frs.getShort()+snd.getDouble());
                        }else if (snd.isFloat()) {
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createFloat((float)frs.getShort()+snd.getFloat());
                        }else if (snd.isInt()) {
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createInt((int)frs.getShort()+snd.getInt());
                        }else if (snd.isLong()) {
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createLong((long)frs.getShort()+snd.getLong());
                        }else{
                            ctx.setChanged(true);
                            t=termSys.getInstance().getTermFactory().createInt(frs.getShort()+snd.getAsShort(termSys.getInstance()));
                        }
                        break;
                    default:
                        throw new AssertException("unknown number type of "+TermHelper.termToString(frs));
                }
            }else{
                // nothing. (!frs.isNumber() && snd.isNumber() )
            }
        }else{
            throw new AssertException("arity(plus) must be 1 or 2");
        }
        
        if (termSys.isLoggingMode()) {
            t.print(termSys.getEnv().getLog());
            termSys.getEnv().getLog().println();
        }
        
        return t;
    }
    
    
    public String getDescription() {
        return STATIC_DESCRIPTION_;
    }
    
    public String getName() {
        return "plus";
    }
    
    
    private final static String STATIC_DESCRIPTION_ =
            "plus(x,y) = <code> x+y </code> - ariphmetics plus<BR>"+
            "<pre>\n"+
            "    +x -> x \n" +
            "    x+y -> ariphmetics sum of x and y if they are numeric.\n" +
            "</pre>\n";
    
    
    
}
