package ua.gradsoft.termware.transformers.general;

/*
 * (C) Rusla Shevchen <Ruslan@Shevchenko.Kiev.UA> 2002-2005
 * $Id: DivideTransformer.java,v 1.3 2007-07-13 20:50:20 rssh Exp $
 */


import java.util.*;
import java.math.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;



/**
 *ariphmetics division.
 * divide(x,y) = x/y
 **/
public class DivideTransformer extends AbstractBuildinTransformer {
    

    public boolean internalsAtFirst() {
        return false;
    }

    
    public Term transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        return static_transform(t,system,ctx);
    }
    
    public  static Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
      if (system.isLoggingMode()) {
            if (system.checkLoggedEntity("All")||system.checkLoggedEntity(DivideTransformer.class.getName())) {
                system.getEnv().getLog().print(MultiplyTransformer.class.getName());
                system.getEnv().getLog().print(": t=");
                t.println(system.getEnv().getLog());
            }
        }

        if (!t.getName().equals("divide")) return t;
        Term retval=t;
        if (t.getArity()==1) {
            return t;
        }else if (t.getArity()==2) {
            Term frs = t.getSubtermAt(0);
            Term snd = t.getSubtermAt(1);
            switch(frs.getPrimaryType0()) {
                case PrimaryTypes.BIG_DECIMAL:
                    switch(snd.getPrimaryType0()) {
                        case PrimaryTypes.BIG_DECIMAL:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createBigDecimal(frs.getBigDecimal().divide(snd.getBigDecimal(),system.getInstance().getDecimalScale(),system.getInstance().getRoundingMode()));
                            break;
                        case PrimaryTypes.BIG_INTEGER:
                        case PrimaryTypes.BYTE:
                        case PrimaryTypes.DOUBLE:
                        case PrimaryTypes.FLOAT:
                        case PrimaryTypes.INT:
                        case PrimaryTypes.LONG:
                        case PrimaryTypes.SHORT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createBigDecimal(frs.getBigDecimal().divide(snd.getAsBigDecimal(system.getInstance()),system.getInstance().getDecimalScale(),system.getInstance().getRoundingMode()));
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
                            retval=system.getInstance().getTermFactory().createBigDecimal(frs.getAsBigDecimal(system.getInstance()).divide(snd.getBigDecimal(),system.getInstance().getDecimalScale(),system.getInstance().getRoundingMode()));
                            break;
                        case PrimaryTypes.BIG_INTEGER:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createBigInteger(frs.getBigInteger().divide(snd.getBigInteger()));
                            break;
                        case PrimaryTypes.BYTE:
                        case PrimaryTypes.INT:
                        case PrimaryTypes.LONG:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createBigInteger(frs.getBigInteger().divide(snd.getAsBigInteger(system.getInstance())));
                            break;
                        case PrimaryTypes.DOUBLE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createDouble(frs.getAsDouble(system.getInstance())/snd.getDouble());
                            break;
                        case PrimaryTypes.FLOAT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createFloat(frs.getAsFloat(system.getInstance())/snd.getFloat());
                            break;
                        default:
                            // unchanged
                            
                    }
                    break;
                case PrimaryTypes.BYTE:
                    switch(snd.getPrimaryType0()) {
                        case PrimaryTypes.BIG_DECIMAL:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createBigDecimal(frs.getAsBigDecimal(system.getInstance()).divide(snd.getBigDecimal(),system.getInstance().getDecimalScale(),system.getInstance().getRoundingMode()));
                            break;
                        case PrimaryTypes.BIG_INTEGER:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createBigInteger(frs.getAsBigInteger(system.getInstance()).divide(snd.getBigInteger()));
                            break;
                        case PrimaryTypes.BYTE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createInt(frs.getByte()/snd.getByte());
                            break;
                        case PrimaryTypes.DOUBLE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createDouble(frs.getByte()/snd.getDouble());
                            break;
                        case PrimaryTypes.FLOAT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createFloat(frs.getByte()/snd.getFloat());
                            break;
                        case PrimaryTypes.INT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createInt(frs.getByte()/snd.getInt());
                            break;
                        case PrimaryTypes.LONG:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createLong(frs.getByte()/snd.getLong());
                            break;
                        case PrimaryTypes.SHORT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createInt(frs.getByte()/snd.getShort());
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
                            retval=system.getInstance().getTermFactory().createBigDecimal(frs.getAsBigDecimal(system.getInstance()).divide(snd.getBigDecimal(),system.getInstance().getDecimalScale(),system.getInstance().getRoundingMode()));
                            break;
                        case PrimaryTypes.BIG_INTEGER:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createDouble(frs.getDouble()/snd.getAsDouble(system.getInstance()));
                            break;
                        case PrimaryTypes.BYTE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createDouble(frs.getDouble()/snd.getByte());
                            break;
                        case PrimaryTypes.DOUBLE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createDouble(frs.getDouble()/snd.getDouble());
                            break;
                        case PrimaryTypes.FLOAT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createDouble(frs.getDouble()/snd.getFloat());
                            break;
                        case PrimaryTypes.INT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createDouble(frs.getDouble()/snd.getInt());
                            break;
                        case PrimaryTypes.LONG:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createDouble(frs.getDouble()/snd.getLong());
                            break;
                        case PrimaryTypes.SHORT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createDouble(frs.getDouble()/snd.getShort());
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
                            retval=system.getInstance().getTermFactory().createBigDecimal(frs.getAsBigDecimal(system.getInstance()).divide(snd.getBigDecimal(),system.getInstance().getDecimalScale(),system.getInstance().getRoundingMode()));
                            break;
                        case PrimaryTypes.BIG_INTEGER:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createDouble(frs.getFloat()/snd.getAsDouble(system.getInstance()));
                            break;
                        case PrimaryTypes.BYTE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createFloat(frs.getFloat()/snd.getByte());
                            break;
                        case PrimaryTypes.DOUBLE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createDouble(frs.getFloat()/snd.getDouble());
                            break;
                        case PrimaryTypes.FLOAT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createFloat(frs.getFloat()/snd.getFloat());
                            break;
                        case PrimaryTypes.INT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createFloat(frs.getFloat()/snd.getInt());
                            break;
                        case PrimaryTypes.LONG:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createFloat(frs.getFloat()/snd.getLong());
                            break;
                        case PrimaryTypes.SHORT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createFloat(frs.getFloat()/snd.getLong());
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
                            retval=system.getInstance().getTermFactory().createBigDecimal(frs.getAsBigDecimal(system.getInstance()).divide(snd.getBigDecimal(),system.getInstance().getDecimalScale(),system.getInstance().getRoundingMode()));
                            break;
                        case PrimaryTypes.BIG_INTEGER:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createBigInteger(frs.getAsBigInteger(system.getInstance()).divide(snd.getBigInteger()));
                            break;
                        case PrimaryTypes.BYTE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createInt(frs.getInt()/snd.getByte());
                            break;
                        case PrimaryTypes.DOUBLE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createDouble(frs.getInt()/snd.getDouble());
                            break;
                        case PrimaryTypes.FLOAT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createFloat(frs.getInt()/snd.getFloat());
                            break;
                        case PrimaryTypes.INT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createInt(frs.getInt()/snd.getInt());
                            break;
                        case PrimaryTypes.LONG:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createLong(frs.getInt()/snd.getLong());
                            break;
                        case PrimaryTypes.SHORT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createInt(frs.getInt()/snd.getShort());
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
                            retval=system.getInstance().getTermFactory().createBigDecimal(frs.getAsBigDecimal(system.getInstance()).divide(snd.getBigDecimal(),system.getInstance().getDecimalScale(),system.getInstance().getRoundingMode()));
                            break;
                        case PrimaryTypes.BIG_INTEGER:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createBigInteger(frs.getAsBigInteger(system.getInstance()).divide(snd.getBigInteger()));
                            break;
                        case PrimaryTypes.BYTE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createLong(frs.getLong()/snd.getByte());
                            break;
                        case PrimaryTypes.DOUBLE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createDouble(frs.getLong()/snd.getDouble());
                            break;
                        case PrimaryTypes.FLOAT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createFloat(frs.getLong()/snd.getFloat());
                            break;
                        case PrimaryTypes.INT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createLong(frs.getLong()/snd.getInt());
                            break;
                        case PrimaryTypes.LONG:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createLong(frs.getLong()/snd.getLong());
                            break;
                        case PrimaryTypes.SHORT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createLong(frs.getLong()/snd.getShort());
                            break;
                        default:
                            break;
                    }
                    break;
                case PrimaryTypes.SHORT:
                    switch(snd.getPrimaryType0()) {
                        case PrimaryTypes.BIG_DECIMAL:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createBigDecimal(frs.getAsBigDecimal(system.getInstance()).divide(snd.getBigDecimal(),system.getInstance().getDecimalScale(),system.getInstance().getRoundingMode()));
                            break;
                        case PrimaryTypes.BIG_INTEGER:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createBigInteger(frs.getAsBigInteger(system.getInstance()).divide(snd.getBigInteger()));
                            break;
                        case PrimaryTypes.BYTE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createInt(frs.getShort()/snd.getByte());
                            break;
                        case PrimaryTypes.DOUBLE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createDouble(frs.getShort()/snd.getDouble());
                            break;
                        case PrimaryTypes.FLOAT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createFloat(frs.getShort()/snd.getFloat());
                            break;
                        case PrimaryTypes.INT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createInt(frs.getShort()/snd.getInt());
                            break;
                        case PrimaryTypes.LONG:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createLong(frs.getShort()/snd.getLong());
                            break;
                        case PrimaryTypes.SHORT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createInt(frs.getShort()/snd.getShort());
                            break;
                        default:
                            break;
                    }
                default:
                    break;
            }
        }else{
            throw new AssertException("divide must have arity 2");
        }
        
        if (system.isLoggingMode()) {
            if (system.checkLoggedEntity("All")||system.checkLoggedEntity(MultiplyTransformer.class.getName())) {
                system.getEnv().getLog().print(DivideTransformer.class.getName());
                system.getEnv().getLog().print(": return ");
                retval.println(system.getEnv().getLog());
            }
        }

        return retval;
    }
    
    
    public String getDescription() {
        return STATIC_DESCRIPTION_;
    }
    
 
 public String getName() {
     return "divide";
 }

    
    private static final String STATIC_DESCRIPTION_ = "ariphmetics division.  (syntax shugar is x/y)";

    
    
    
}

