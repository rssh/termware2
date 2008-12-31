/*
 * JavaLangReflectHelper.java
 *
 */

package ua.gradsoft.termware.util;

import java.lang.reflect.*;
import java.math.*;
import java.util.*;
import ua.gradsoft.termware.TransformationContext;


import ua.gradsoft.termware.exceptions.InvocationException;
import ua.gradsoft.termware.exceptions.JavaMethodNotFoundException;

/**
 * Helper class for work with java language reflection interfaces.
 */
public class JavaLangReflectHelper {
    
    
    public static final Class getStringClass() {
        return emptyString_.getClass(); }
    
    public static final Class getCharacterClass() {
        return anyCharacter_.getClass(); }
    
    public static final Class getBigIntegerClass() {
        return biZero_.getClass(); }
    
    public static final Class getBigDecimalClass() {
        return bdZero_.getClass(); }
    
    
    public static final Class getObjectArrayClass() {
        return emptyArray_.getClass();
    }
    
    public static final Object[] getEmptyArray() {
        return emptyArray_;
    }
    
    public static final List<Class<?> > getAllSuperInterfaces(Class<?> aClass) {
        ArrayList<Class<?> > l=new ArrayList<Class<?> >();
        getAllSuperInterfaces(aClass,l);
        return l;
    }
    
    public static final List<Class> getAllSuperClasses(Class aClass) {
        ArrayList<Class> l=new ArrayList<Class>();
        getAllSuperClasses(aClass,l);
        return l;
    }
    
    static final void getAllSuperInterfaces(Class<?> aClass, List<Class<?> > retval) {
        Class<?>[] interfaces=aClass.getInterfaces();
        if (interfaces.length==0) return;
        for(int i=0; i<interfaces.length; ++i) {
            retval.add(interfaces[i]);
            getAllSuperInterfaces(interfaces[i],retval);
        }
    }
    
    static final void getAllSuperClasses(Class<?> aClass, List<Class> retval) {
        Class<?> cl=aClass.getSuperclass();
        if (cl==null) return;
        retval.add(cl);
        if (!cl.equals(Object.class)) {
            getAllSuperClasses(cl,retval);
        }
    }
    
    /**
     * instantiate Object with given class name using empty constructor
     **/
    public static Object instantiateObject(String className) throws InvocationException {
        try {
            Constructor objClassConstructor = Class.forName(className)
            .getConstructor(emptyClassArray_);
            Object obj=objClassConstructor.newInstance(emptyArray_);
            return obj;
        }catch(ClassNotFoundException ex) {
            throw new InvocationException("Can't find class " + className + "; exception:"+
                    ex.toString());
        }catch(NoSuchMethodException ex) {
            throw new InvocationException("Can't find default constructor for class "
                    + className + "; exception"+
                    ex.toString());
        }catch(IllegalAccessException ex) {
            throw new InvocationException("Can't get default constructor for class "+className+
                    "; exception "+
                    ex.toString());
        }catch(InstantiationException ex) {
            throw new InvocationException("Can't create instance of "+className+"\n"+
                    ex.toString());
        }catch(InvocationTargetException ex) {
            throw new InvocationException("Can't create instance of "+className+"\n"+
                    ex.toString());
        }
    }
    
    /**
     * instantiate object with given class name, using constructor with one
     * argument of class argClass.
     **/
    public static Object instantiateObject(String className,
            Class argClass, Object arg)
            throws InvocationException {
        Class[] classArray=new Class[1];
        classArray[0]=argClass;
        Object[] argArray=new Object[1];
        argArray[0]=arg;
        try {
            Constructor objClassConstructor = Class.forName(className)
            .getConstructor(classArray);
            Object obj=objClassConstructor.newInstance(argArray);
            return obj;
        }catch(ClassNotFoundException ex) {
            throw new InvocationException("Can't find class " + className + "; exception:"+
                    ex.toString());
        }catch(NoSuchMethodException ex) {
            throw new InvocationException("Can't find constructor for class "
                    + className + " with " + argClass.getName()+" argument; exception"+
                    ex.toString());
        }catch(IllegalAccessException ex) {
            throw new InvocationException("Can't get constructor for class "+className+
                    "; exception "+
                    ex.toString());
        }catch(InstantiationException ex) {
            throw new InvocationException("Can't create instance of "+className+"\n"+
                    ex.toString());
        }catch(InvocationTargetException ex) {
            throw new InvocationException("Can't create instance of "+className+"\n"+
                    ex.toString());
        }
        
    }
    
    /**
     * find method in class <code> objectClass </code> or it superclass with given name and arity.
     *Method in result have name <code> name </code> and number of arguments <code> arity </code> or
     *<code> arity+1 </code>.  If number of parameters is <code> arity+1 </code> then first parameter
     *have type <code> TransformationContext </code>.
     *@return method with matching name and arity.
     *@exception JavaMethodNotFoundException if such method does not exists.
     */
    public static Method findClassMethodByNameAndArity(Class objectClass,String name, int arity) throws JavaMethodNotFoundException
    {
      Method retval=null;  
      Method[] methods = objectClass.getMethods();
      for(int i=0; i<methods.length; ++i) {
          if (methods[i].getName().equals(name)) {
              Class[] parameterTypes = methods[i].getParameterTypes();
              if (parameterTypes.length == arity+1) {
                  if (parameterTypes[0].equals(TransformationContext.class)) {
                      retval=methods[i];
                      break;
                  }
              }else if (parameterTypes.length == arity) {
                  retval=methods[i];
                  break;
              }
          }
      }
      if (retval==null) {
          throw new JavaMethodNotFoundException(objectClass,name);
      }
      return retval;
    }
    
    public static Object invokeObjectMethod(Object o, Method m) throws InvocationException
    {
        return invokeObjectMethod(o, m,emptyArray_);
    }
    
    public static Object invokeObjectMethod(Object o, Method m, Object[] arguments) throws InvocationException
    {
        Object retval=null;
        m.setAccessible(true);
        try{
          retval=m.invoke(o,arguments);  
        }catch(IllegalAccessException ex){
            throw new InvocationException(ex);
        }catch(InvocationTargetException ex){
            throw new InvocationException(ex.getTargetException());
        }
        return retval;
    }
    
    private static final String emptyString_ = "";
    private static final Character anyCharacter_ = new Character('a');
    private static final BigInteger biZero_ = BigInteger.ZERO;
    private static final BigDecimal bdZero_ = new BigDecimal(0);
    private static final Object[]   emptyArray_ = new Object[0];
    private static final Class[]    emptyClassArray_ = new Class[0];
    
}
