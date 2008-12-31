package ua.gradsoft.termware;

/*
 * (C) Ruslan Shevchenko <Ruslan@Shevchenko.Kiev.UA> 2002- 2005
 * (C) Grad-Soft Ltd <info@gradsoft.ua> 2003 - 2005
 * $Id: TermFactory.java,v 1.11 2008-01-13 01:09:33 rssh Exp $
 */

import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import ua.gradsoft.termware.debug.JavaCompiledFileAndLine;
import ua.gradsoft.termware.debug.SourceCodeLocation;
import ua.gradsoft.termware.exceptions.AssertException;

import ua.gradsoft.termware.parsers.terms.*;
import ua.gradsoft.termware.set.SetOfTerms;
import ua.gradsoft.termware.set.SetPatternTerm;
import ua.gradsoft.termware.set.SetTerm;

      
/**
 * Factory for terms.
 *Common usage: <code> TermWare.getInstance().getTermFactory().createTerm() </code>
 **/                     
public class TermFactory
{

  TermFactory(TermWareInstance instance)  
  {
    instance_=instance;  
  }
    
  public Term  createParsedTerm(String s) throws TermWareException
  { return createParsedTerm(s,JavaCompiledFileAndLine.deduceFileAndLine(3)); }

  public Term  createParsedTerm(String s, SourceCodeLocation startLocation) throws TermWareException
  { return (new TermReader(new StringReader(assureSemicolonSuffix(s)),startLocation.getFileName(),startLocation.getBeginLine()-1,getInstance())).readStatementWrapped(); }
  

 static public Term  createNIL()
  { return NILTerm.getNILTerm(); }
                        
 static public Term  createNil()
  { return NILTerm.getNILTerm(); }

 static public Term  createBoolean(boolean value)
  { return BooleanTerm.getBooleanTerm(value); }

 public Term  createTerm(boolean b)
  { return BooleanTerm.getBooleanTerm(b); }

 public Term  createByte(byte value)
  { return new ByteTerm(value); }

 public Term  createTerm(byte value)
  { return new ByteTerm(value); }

 
 
 public AtomTerm  createAtom(String name)
  { 
    return new AtomTerm(name,instance_.getSymbolAdoptionPolicy());   
  }
 
 static public ShortTerm  createShort(short value)
  { return new ShortTerm(value); }
 
 static public IntTerm  createInt(int value)
 { return IntTerm.getIntTerm(value); }
 
 static public LongTerm  createLong(long value)
  { return new LongTerm(value); }
 
 static public BigDecimalTerm  createBigDecimal(BigDecimal value)
  { return new BigDecimalTerm(value); }
 
 static public BigIntegerTerm  createBigInteger(BigInteger value)
  { return new BigIntegerTerm(value); }
 
 static public CharTerm  createChar(char value)
 { return new CharTerm(value); }
 
 static public StringTerm  createString(String value)
 { return new StringTerm(value); }
 
 static public FloatTerm  createFloat(float value)
 { return new FloatTerm(value); }
 
 /**
  *create double term with value <code> value </code>
  */
 static public DoubleTerm  createDouble(double value)
 { return new DoubleTerm(value); }
 
  
 
 /**
  * create propositional variable with index x.
  */
 static public  Term  createX(int index)
  { return new XTerm(index); }


 /**
  * return Term wrapper arroutnd java.lang.Object
  **/
 static public  Term  createJTerm(Object o)
                                                throws TermWareException
  {
    return new JTerm(o);
  }

 /**
  * return Term with name <code> name </code> and body <code> body </code>
  **/
 public  Term  createComplexTerm(String name, Term[] body) 
                                                throws TermWareException
 {     
    Name nname = new Name(name,instance_.getSymbolAdoptionPolicy()) ;
    return createComplexTerm(nname,body);
 }
 
 /**
  * The same as createComplexTerm
  */
 public Term createTerm(String name,Term[] body) throws TermWareException
 { return createComplexTerm(name,body); }
 
  /**
  * return complex term, where subterms are atoms with names from <code> stringBody </code>
  */ 
 public Term createTerm(String name,String[] stringBody) throws TermWareException
 {
   Term[] body=new Term[stringBody.length];  
   for(int i=0; i<body.length; ++i){
       body[i]=createAtom(stringBody[i]);
   }  
   return createTerm(name,body);
 } 
 
 public Term createComplexTerm(Name name,Term[] body) throws TermWareException
 {
     if (name.getIndex().equals(TermWareSymbols.SET_INDEX)) {
         return new SetTerm(body);
     }else if (name.getIndex().equals(TermWareSymbols.SET_PATTERN_INDEX)) {
         return SetPatternTerm.createSetPattern(body[0], body[1]);
     }else if (name.getIndex().equals(TermWareSymbols.CONS_INDEX)) {
         if (body.length==2) {
             return new ListTerm(body[0],body[1]);
         }else{
             throw new AssertException("cons term must have arity 2");
         }
     }else if (name.getIndex().equals(TermWareSymbols.CLASS_PATTERN_INDEX)) {
         if (body.length==2) {
             if (body[0].isString()) {
                return ClassPatternTerm.createClassPatternTerm(body[0].getString(), body[1]);
             }else{
                 throw new AssertException("first argument of class pattern must be string");
             }
         }else{
            throw new AssertException("class pattern term must have arity 2");         
         }
     }else if (name.getIndex().equals(TermWareSymbols.ARGS_PATTERN_INDEX)) {
         if (body.length==2) {
             return ArgsPatternTerm.createArgsPatternTerm(body[0],body[1]);
         }else{
             throw new AssertException("args_pattern term must have arity 2");         
         }
     }else{
         //String name=instance_.getSymbolTable().getName(nameIndex);
         return new ComplexTerm(name,body);
     }
 }
 
 
 public SetTerm createSetTerm() throws TermWareException
 {
     return new SetTerm(new Term[0]);
 }
 
 public SetTerm createSetTerm(SetOfTerms setOfTerms) throws TermWareException
 {
     return new SetTerm(setOfTerms);
 }
  
 /**
  * create set-pattern ter, <code> { x1 : x2 } </code>
  */
 public Term createSetPattern(Term x1,Term x2) throws TermWareException
 {
     return SetPatternTerm.createSetPattern(x1, x2);
 }
 
 /**
  * create set-pattern term <code> { x1 : x2 } </code>
  */
 public Term createSetPatternTerm(Term x1,Term x2) throws TermWareException
 {
     return SetPatternTerm.createSetPattern(x1, x2);
 }
 
 /**
  * create args-pattern term <code> f..($x) </code>
  */ 
  public Term createArgsPatternTerm(Term x1, Term x2) throws TermWareException
  {
      return ArgsPatternTerm.createArgsPatternTerm(x1,x2);
  }
 
 
  public Term createClassPatternTerm(String className,Term pattern) throws TermWareException
  {
      return ClassPatternTerm.createClassPatternTerm(className,pattern);
  }

  
  public Term createConsTerm(Term car,Term cdr) throws TermWareException
  {
      return new ListTerm(car,cdr);
  }
  
 public Term  createTerm(String name, Term x) throws TermWareException
  {
    Term[] body=new Term[1];
    body[0]=x;
    return createComplexTerm(name,body);
  }
  
  
  /**
   * create term <code> name(x1,x2) </code>
   *@param name - functional symbol
   *@param x1 - first argument
   *@param x2 - second argument
   */
  public Term  createTerm(String name, Term x1, Term x2) throws TermWareException
  {
    Term[] body=new Term[2];
    body[0]=x1;
    body[1]=x2;
    return createComplexTerm(name,body);
  }

  public Term  createTerm(String name, Term x1, Term x2, Term x3) throws TermWareException
  {
    Term[] body=new Term[3];
    body[0]=x1;
    body[1]=x2;
    body[2]=x3;
    return createComplexTerm(name,body);
  }
  
  /**
   *create term 'name(x1,x2)', transforming x1 to string term.
   */
  public Term  createTerm(String name,String x1,Term x2) throws TermWareException
  {
      return createTerm(name,createString(x1),x2);
  }
  
  public Term  createTerm(String name, Term x1, Term x2, Term x3, Term x4) throws TermWareException
  {
    Term[] body=new Term[4];
    body[0]=x1;
    body[1]=x2;
    body[2]=x3;
    body[3]=x4;
    return createComplexTerm(name,body);
  }
  
  /**
   * create list [i. e. cons(x1,cons(x2,...,cons(xN,NIL)..)) ] from array <code> array </code>
   */
  public Term  createList(Term[] array) 
  {
    if (array.length==0) {
       return createNil();
    }else if (array.length==1) {
       return new ListTerm(array[0],createNil());
    }else {
      return createListTail(array,0);
   }
  }

  
  /**
   * create tail of list, which contains body[firstIndex]...body[body.length-1]
   */
  private Term  createListTail(Term[] body, int firstIndex)                                               
  {
    int length=body.length-firstIndex;
    if (length==0) {
      return createNil();
    }else if(length==1) {
      return new ListTerm(body[firstIndex],createNil());
    }else {
      return new ListTerm(body[firstIndex],createListTail(body,firstIndex+1));
    }
  }

  /**
   * create list [i. e. cons(x1,cons(x2,...,cons(xN,NIL)..)) ] from 
   *  list of terms.
   */
  public Term  createList(List terms) 
  {
    if (terms.size()==0) {
       return createNil();
    }else if (terms.size()==1) {
       return new ListTerm((Term)terms.get(0),createNil());
    }else {
      return new ListTerm((Term)terms.get(0), createList(terms.subList(1, terms.size())));
   }
  }

  public Term createListInArray(Term[] terms, int index)
  {
     if (index >= terms.length) {
         throw new IndexOutOfBoundsException();
     }
     if (terms.length==0) {
         return createNil();
     }else{
         return new ListInArrayTerm(terms,index);
     }
  }
  
 public  Term  createComplexTerm0(String name)
                                                 throws TermWareException
  {
    Term[] body = new Term[0];
    return  createComplexTerm(name,body);
  }

                                               

  /**
   * Syntax sugar: equal to <code> createTerm(name,ITermFactory.createString(s)) </code>
   **/ 
  public final Term  createTerm(String name, String s)
                                                 throws TermWareException
  {
    return createTerm(name,TermFactory.createString(s));
  }

  /**
   * Syntax sugar: equal to <code> createTerm(name,ITermFactory.createInt(v),useIndexedNames) </code>
   **/ 
  public final Term  createTerm(String name, int v)
                                                 throws TermWareException
  {
    return createTerm(name,TermFactory.createInt(v));
  }
 

 
 /**
  *create term with 2 integer subterms.
  *syntax sugar arround <code> createComplexTerm </code>
  **/
 public final Term  createTerm(String name, int x, int y)
                                                 throws TermWareException
 {
     return createTerm(name,createInt(x), createInt(y));
 }

 
 
 static private String assureSemicolonSuffix(String s)
 {
  if (s.endsWith(";")) return s;
  return s+";";
 }

 final TermWareInstance getInstance()
 { return instance_; }
 
 private TermWareInstance instance_;
 
}
