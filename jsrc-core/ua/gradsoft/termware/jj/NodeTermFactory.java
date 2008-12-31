/*
 * NodeTermFactory.java
 *
 *
 * Copyright (c) 2004-2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termware.jj;

import java.lang.reflect.Field;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareRuntimeException;
import ua.gradsoft.termware.exceptions.ExternalException;

/**
 *
 * @author Ruslan Shevchenko
 */
public class NodeTermFactory {

    NodeTermFactory(String parserName)
    {
       this(parserName,null);
    }
    
    NodeTermFactory(String parserName, String prefix)
    {
       parserName_=parserName; 
       prefix_=prefix;
    }
    
    public  INode jjCreate(int id)
    {
       try { 
        return new NodeTerm(id,getParserSymbol(id)); 
       }catch(TermWareException ex){
           throw new TermWareRuntimeException(ex);
       }
    }
    
    public String getParserSymbol(int id) throws TermWareException
    {
        String[] constants=getParserConstants();
        if (prefix_==null) {
            return constants[id];
        }else{
            return prefix_+constants[id];
        }
    }
    
    private String[] getParserConstants() throws TermWareException
    {
      try {  
        if (parserConstants_==null) {
            if (constantsClass_==null) {
                constantsClass_=Class.forName(parserName_+"TreeConstants");               
            }
            Field parserConstantsField=constantsClass_.getDeclaredField("jjNodeName");
            Object o = constantsClass_.newInstance();
            parserConstants_=(String[])parserConstantsField.get(o);
        }
        return parserConstants_;
      }catch(ClassNotFoundException ex){
          throw new ExternalException(ex);
      }catch(NoSuchFieldException ex){
          throw new ExternalException(ex);
      }catch(InstantiationException ex){
          throw new ExternalException(ex);
      }catch(IllegalAccessException ex){
          throw new ExternalException(ex);
      }
    }
    
    private String parserName_=null;
    private String prefix_=null;
    private Class  constantsClass_=null;
    private String[] parserConstants_=null;
    
}
