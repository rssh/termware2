package ua.gradsoft.termware.transformers.general;

/*
 * (C) Rusla Shevchen <Ruslan@Shevchenko.Kiev.UA> 2002
 * $Id: MinusTransformer.java,v 1.1 2005-04-02 18:29:23 rssh Exp $
 */


import java.util.*;
import java.math.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;



/**
 * minus transforer.
 * minus(x,y) = x - y
 * minus(x) = - x
 **/
public class MinusTransformer extends AbstractBuildinTransformer {
    
    
    public Term transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        return static_transform(t,system,ctx);
    }
    
    public  static Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        // TODO: write output to log.
        if (!t.getName().equals("minus")) return t;
        Term retval=t;
        if (t.getArity()==1) {
            Term frs = t.getSubtermAt(0);
            switch(frs.getPrimaryType0()){
                case PrimaryTypes.BIG_DECIMAL:
                    ctx.setChanged(true);
                    retval=system.getInstance().getTermFactory().createBigDecimal(frs.getBigDecimal().negate());
                    break;
                case PrimaryTypes.BIG_INTEGER:
                    ctx.setChanged(true);
                    retval=system.getInstance().getTermFactory().createBigInteger(frs.getBigInteger().negate());
                    break;
                case PrimaryTypes.BYTE:
                    ctx.setChanged(true);
                    retval=system.getInstance().getTermFactory().createInt(-frs.getByte());
                    break;
                case PrimaryTypes.DOUBLE:
                    ctx.setChanged(true);
                    retval=system.getInstance().getTermFactory().createDouble(-frs.getDouble());
                    break;
                case PrimaryTypes.FLOAT:
                    ctx.setChanged(true);
                    retval=system.getInstance().getTermFactory().createFloat(-frs.getFloat());
                    break;
                case PrimaryTypes.INT:
                    ctx.setChanged(true);
                    retval=system.getInstance().getTermFactory().createInt(-frs.getInt());
                    break;
                case PrimaryTypes.LONG:
                    ctx.setChanged(true);
                    retval=system.getInstance().getTermFactory().createLong(-frs.getLong());
                    break;
                case PrimaryTypes.SHORT:
                    ctx.setChanged(true);
                    retval=system.getInstance().getTermFactory().createInt(-frs.getShort());
                    break;
                default:
                    // return unchanged.
                    return t;
            }
        }else if (t.getArity()==2) {
            Term frs = t.getSubtermAt(0);
            Term snd = t.getSubtermAt(1);
            switch(frs.getPrimaryType0()) {
                case PrimaryTypes.BIG_DECIMAL:
                    switch(snd.getPrimaryType0()) {
                        case PrimaryTypes.BIG_DECIMAL:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createBigDecimal(frs.getBigDecimal().subtract(snd.getBigDecimal()));
                            break;
                        case PrimaryTypes.BIG_INTEGER:
                        case PrimaryTypes.BYTE:
                        case PrimaryTypes.DOUBLE:
                        case PrimaryTypes.FLOAT:
                        case PrimaryTypes.INT:
                        case PrimaryTypes.LONG:
                        case PrimaryTypes.SHORT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createBigDecimal(frs.getBigDecimal().subtract(snd.getAsBigDecimal(system.getInstance())));
                            break;
                        default:
                            // return uncganged
                            return t;
                    }
                    break;
                case PrimaryTypes.BIG_INTEGER:
                    switch(snd.getPrimaryType0()){
                        case PrimaryTypes.BIG_DECIMAL:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createBigDecimal(frs.getAsBigDecimal(system.getInstance()).subtract(snd.getBigDecimal()));
                            break;
                        case PrimaryTypes.BIG_INTEGER:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createBigInteger(frs.getBigInteger().subtract(snd.getBigInteger()));
                            break;
                        case PrimaryTypes.BYTE:
                        case PrimaryTypes.INT:
                        case PrimaryTypes.LONG:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createBigInteger(frs.getBigInteger().subtract(snd.getAsBigInteger(system.getInstance())));
                            break;
                        case PrimaryTypes.DOUBLE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createDouble(frs.getAsDouble(system.getInstance())-snd.getDouble());
                            break;
                        case PrimaryTypes.FLOAT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createFloat(frs.getAsFloat(system.getInstance())-snd.getFloat());
                            break;
                        default:
                            // unchanged
                            return t;
                    }
                case PrimaryTypes.BYTE:
                    switch(snd.getPrimaryType0()) {
                        case PrimaryTypes.BIG_DECIMAL:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createBigDecimal(frs.getAsBigDecimal(system.getInstance()).subtract(snd.getBigDecimal()));
                            break;
                        case PrimaryTypes.BIG_INTEGER:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createBigInteger(frs.getAsBigInteger(system.getInstance()).subtract(snd.getBigInteger()));
                            break;
                        case PrimaryTypes.BYTE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createInt(frs.getByte()-snd.getByte());
                            break;
                        case PrimaryTypes.DOUBLE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createDouble(frs.getByte()-snd.getDouble());
                            break;
                        case PrimaryTypes.FLOAT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createFloat(frs.getByte()-snd.getFloat());
                            break;
                        case PrimaryTypes.INT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createInt(frs.getByte()-snd.getInt());
                            break;
                        case PrimaryTypes.LONG:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createLong(frs.getByte()-snd.getLong());
                            break;
                        case PrimaryTypes.SHORT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createInt(frs.getByte()-snd.getShort());
                            break;
                        default:
                            /* unchanged */
                            break;
                    }
                    break;
                case PrimaryTypes.DOUBLE:
                    switch(snd.getPrimaryType0()) {
                        case PrimaryTypes.BIG_DECIMAL:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createBigDecimal(frs.getAsBigDecimal(system.getInstance()).subtract(snd.getBigDecimal()));
                            break;
                        case PrimaryTypes.BIG_INTEGER:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createDouble(frs.getDouble()-snd.getAsDouble(system.getInstance()));
                            break;
                        case PrimaryTypes.BYTE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createDouble(frs.getDouble()-snd.getByte());
                            break;
                        case PrimaryTypes.DOUBLE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createDouble(frs.getDouble()-snd.getDouble());
                            break;
                        case PrimaryTypes.FLOAT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createDouble(frs.getDouble()-snd.getFloat());
                            break;
                        case PrimaryTypes.INT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createDouble(frs.getDouble()-snd.getInt());
                            break;
                        case PrimaryTypes.LONG:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createDouble(frs.getDouble()-snd.getLong());
                            break;
                        case PrimaryTypes.SHORT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createDouble(frs.getDouble()-snd.getShort());
                            break;
                        default:
                            // unchanged.
                            break;
                    }
                    break;
                case PrimaryTypes.FLOAT:
                    switch(snd.getPrimaryType0()) {
                        case PrimaryTypes.BIG_DECIMAL:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createBigDecimal(frs.getAsBigDecimal(system.getInstance()).subtract(snd.getBigDecimal()));
                            break;
                        case PrimaryTypes.BIG_INTEGER:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createDouble(frs.getFloat()-snd.getAsDouble(system.getInstance()));
                            break;
                        case PrimaryTypes.BYTE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createFloat(frs.getFloat()-snd.getByte());
                            break;
                        case PrimaryTypes.DOUBLE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createDouble(frs.getFloat()-snd.getDouble());
                            break;
                        case PrimaryTypes.FLOAT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createFloat(frs.getFloat()-snd.getFloat());
                            break;
                        case PrimaryTypes.INT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createFloat(frs.getFloat()-snd.getInt());
                            break;
                        case PrimaryTypes.LONG:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createFloat(frs.getFloat()-snd.getLong());
                            break;
                        case PrimaryTypes.SHORT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createFloat(frs.getFloat()-snd.getLong());
                            break;
                        default:
                            /* nothing */
                            break;
                    }
                    break;
                case PrimaryTypes.INT:
                    switch(snd.getPrimaryType0()) {
                        case PrimaryTypes.BIG_DECIMAL:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createBigDecimal(frs.getAsBigDecimal(system.getInstance()).subtract(snd.getBigDecimal()));
                            break;
                        case PrimaryTypes.BIG_INTEGER:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createBigInteger(frs.getAsBigInteger(system.getInstance()).subtract(snd.getBigInteger()));
                            break;
                        case PrimaryTypes.BYTE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createInt(frs.getInt()-snd.getByte());
                            break;
                        case PrimaryTypes.DOUBLE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createDouble(frs.getInt()-snd.getDouble());
                            break;
                        case PrimaryTypes.FLOAT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createFloat(frs.getInt()-snd.getFloat());
                            break;
                        case PrimaryTypes.INT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createInt(frs.getInt()-snd.getInt());
                            break;
                        case PrimaryTypes.LONG:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createLong(frs.getInt()-snd.getLong());
                            break;
                        case PrimaryTypes.SHORT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createInt(frs.getInt()-snd.getShort());
                            break;
                        default:
                            // nothing
                            break;
                    }
                    break;
                case PrimaryTypes.LONG:
                    switch(snd.getPrimaryType0()) {
                        case PrimaryTypes.BIG_DECIMAL:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createBigDecimal(frs.getAsBigDecimal(system.getInstance()).subtract(snd.getBigDecimal()));
                            break;
                        case PrimaryTypes.BIG_INTEGER:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createBigInteger(frs.getAsBigInteger(system.getInstance()).subtract(snd.getBigInteger()));
                            break;
                        case PrimaryTypes.BYTE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createLong(frs.getLong()-snd.getByte());
                            break;
                        case PrimaryTypes.DOUBLE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createDouble(frs.getLong()-snd.getDouble());
                            break;
                        case PrimaryTypes.FLOAT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createFloat(frs.getLong()-snd.getFloat());
                            break;
                        case PrimaryTypes.INT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createLong(frs.getLong()-snd.getInt());
                            break;
                        case PrimaryTypes.LONG:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createLong(frs.getLong()-snd.getLong());
                            break;
                        case PrimaryTypes.SHORT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createLong(frs.getLong()-snd.getShort());
                            break;
                        default:
                            break;
                    }
                    break;
                case PrimaryTypes.SHORT:
                    switch(snd.getPrimaryType0()) {
                        case PrimaryTypes.BIG_DECIMAL:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createBigDecimal(frs.getAsBigDecimal(system.getInstance()).subtract(snd.getBigDecimal()));
                            break;
                        case PrimaryTypes.BIG_INTEGER:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createBigInteger(frs.getAsBigInteger(system.getInstance()).subtract(snd.getBigInteger()));
                            break;
                        case PrimaryTypes.BYTE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createInt(frs.getShort()-snd.getByte());
                            break;
                        case PrimaryTypes.DOUBLE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createDouble(frs.getShort()-snd.getDouble());
                            break;
                        case PrimaryTypes.FLOAT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createFloat(frs.getShort()-snd.getFloat());
                            break;
                        case PrimaryTypes.INT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createInt(frs.getShort()-snd.getInt());
                            break;
                        case PrimaryTypes.LONG:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createLong(frs.getShort()-snd.getLong());
                            break;
                        case PrimaryTypes.SHORT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createInt(frs.getShort()-snd.getShort());
                            break;
                        default:
                            break;
                    }
                default:
                    break;
            }
        }else{
            throw new AssertException("minus must have arity 1 or 2");
        }
        return retval;
    }
    
    
    public String getDescription() {
        return STATIC_DESCRIPTION_;
    }
    
    public String getName() {
        return "minus";
    }
    
    private static final String STATIC_DESCRIPTION_ = "minus(x,y) - ariphmetics difference<BR>"+
            "minus(x) - ariphmetics negation";
    
    public boolean internalsAtFirst() {
        return false;
    }
    
    
    
}

