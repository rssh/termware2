package ua.gradsoft.termware.transformers.general;

/*
 * (C) Ruslan Shevchen <Ruslan@Shevchenko.Kiev.UA> 2002 - 2005
 * $Id: ModTransformer.java,v 1.2 2007-07-13 20:50:20 rssh Exp $
 */


import java.util.*;
import java.math.*;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.exceptions.*;


/**
 * transfromer for mod(x,y) or x%y.
 * ariphmetics module of 'x' by 'y'

 **/
public class ModTransformer extends AbstractBuildinTransformer {
    

    public boolean internalsAtFirst() {
        return false;
    }

    
    public Term transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        return static_transform(t,system,ctx);
    }
    
    public  static Term  static_transform(Term t, TermSystem system, TransformationContext ctx) throws TermWareException {
        if (system.isLoggingMode()) {
            if (system.checkLoggedEntity("All")||system.checkLoggedEntity(ModTransformer.class.getName())) {
                system.getEnv().getLog().print(ModTransformer.class.getName());
                system.getEnv().getLog().print(": t=");
                t.println(system.getEnv().getLog());
            }
        }
        if (!t.getName().equals("mod")) return t;
        Term retval=t;
        if (t.getArity()==1) {
            return t;
        }else if (t.getArity()==2) {
            Term frs = t.getSubtermAt(0);
            Term snd = t.getSubtermAt(1);
            switch(frs.getPrimaryType0()) {
                case PrimaryTypes.BIG_DECIMAL:
                     break;
                case PrimaryTypes.BIG_INTEGER:
                    switch(snd.getPrimaryType0()){
                        case PrimaryTypes.BIG_INTEGER:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createBigInteger(frs.getBigInteger().mod(snd.getBigInteger()));
                            break;
                        case PrimaryTypes.BYTE:
                        case PrimaryTypes.INT:
                        case PrimaryTypes.LONG:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createBigInteger(frs.getBigInteger().mod(snd.getAsBigInteger(system.getInstance())));
                            break;
                        case PrimaryTypes.DOUBLE:
                        case PrimaryTypes.FLOAT:
                        default:
                            // unchanged
                            break;
                    }
                    break;
                case PrimaryTypes.BYTE:
                    switch(snd.getPrimaryType0()) {                                    
                        case PrimaryTypes.BIG_DECIMAL:
                            break;
                        case PrimaryTypes.BIG_INTEGER:
                            {
                             int q=frs.getAsBigInteger(system.getInstance()).mod(snd.getBigInteger()).intValue();
                             retval=system.getInstance().getTermFactory().createByte((byte)q);
                            }
                            ctx.setChanged(true);
                            break;
                        case PrimaryTypes.BYTE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createInt(frs.getByte() % snd.getByte());
                            break;
                        case PrimaryTypes.DOUBLE:
                        case PrimaryTypes.FLOAT:
                            break;
                        case PrimaryTypes.INT:
                            retval=system.getInstance().getTermFactory().createInt(frs.getByte() % snd.getInt());
                            break;
                        case PrimaryTypes.LONG:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createLong(frs.getByte() % snd.getLong());
                            break;
                        case PrimaryTypes.SHORT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createInt(frs.getByte() % snd.getShort());
                            break;
                        default:
                            /* unchanged */
                            break;
                    }
                    break;
                case PrimaryTypes.DOUBLE:
                case PrimaryTypes.FLOAT:
                    break;
                case PrimaryTypes.INT:
                    switch(snd.getPrimaryType0()) {
                        case PrimaryTypes.BIG_DECIMAL:
                            break;
                        case PrimaryTypes.BIG_INTEGER:
                            break;
                        case PrimaryTypes.BYTE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createInt(frs.getInt() % snd.getByte());
                            break;
                        case PrimaryTypes.DOUBLE:
                        case PrimaryTypes.FLOAT:
                            break;
                        case PrimaryTypes.INT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createInt(frs.getInt() % snd.getInt());
                            break;
                        case PrimaryTypes.LONG:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createLong(frs.getInt() % snd.getLong());
                            break;
                        case PrimaryTypes.SHORT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createInt(frs.getInt() % snd.getShort());
                            break;
                        default:
                            // nothing
                            break;
                    }
                    break;
                case PrimaryTypes.LONG:
                    switch(snd.getPrimaryType0()) {
                        case PrimaryTypes.BIG_DECIMAL:
                            break;
                        case PrimaryTypes.BIG_INTEGER:
                            {
                             ctx.setChanged(true);
                             long q=frs.getAsBigInteger(system.getInstance()).mod(snd.getBigInteger()).longValue();
                             retval=system.getInstance().getTermFactory().createLong(q);
                            }
                            break;
                        case PrimaryTypes.BYTE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createLong(frs.getLong() % snd.getByte());
                            break;
                        case PrimaryTypes.INT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createLong(frs.getLong()%snd.getInt());
                            break;
                        case PrimaryTypes.LONG:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createLong(frs.getLong()%snd.getLong());
                            break;
                        case PrimaryTypes.SHORT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createLong(frs.getLong()%snd.getShort());
                            break;
                        default:
                            break;
                    }                                                                  
                    break;
                case PrimaryTypes.SHORT:
                    switch(snd.getPrimaryType0()) {
                        case PrimaryTypes.BIG_INTEGER:
                            {
                             ctx.setChanged(true);
                             short q=frs.getAsBigInteger(system.getInstance()).mod(snd.getBigInteger()).shortValue();
                             retval=system.getInstance().getTermFactory().createShort(q);
                            }
                            break;
                        case PrimaryTypes.BYTE:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createInt(frs.getShort()%snd.getByte());
                            break;
                        case PrimaryTypes.INT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createInt(frs.getShort() % snd.getInt());
                            break;
                        case PrimaryTypes.LONG:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createLong(frs.getShort() % snd.getLong());
                            break;
                        case PrimaryTypes.SHORT:
                            ctx.setChanged(true);
                            retval=system.getInstance().getTermFactory().createShort((short)(frs.getShort()%snd.getShort()));
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }else{
            throw new AssertException("mod must have arity 2");
        }
        
        if (system.isLoggingMode()) {
            if (system.checkLoggedEntity("All")||system.checkLoggedEntity(ModTransformer.class.getName())) {
                system.getEnv().getLog().print(ModTransformer.class.getName());
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
      return "mod";
  }

  private final static String STATIC_DESCRIPTION_=
       "mod(x,y) - ariphmetics module of 'x' by 'y'";
        
    
    
}

