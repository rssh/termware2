/*
 * NodeTerm.java
 *
 * Copyright (c) 2004-2009 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termware.jj;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Map;
import ua.gradsoft.termware.Attributed;
import ua.gradsoft.termware.PartialOrderingResult;
import ua.gradsoft.termware.Substitution;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareRuntimeException;


/**
 *Term which can be instance of JJTree node.
 *If you want to use this term, you must define own SimpleNode interface
 *as extension of NodeTerm and delegate all work in jjt* methods in SimpleNode
 *to appropriative jjt*Internal methods of NodeTerm. 
 * @author Ruslan Shevchenko
 */
public  class NodeTerm extends Term implements INode, Attributed
{

   
    /**
     * create NodeTerm based on existing node.
     */
    public NodeTerm(INode node) 
    {
      try {  
        Term[] body = new Term[node.jjtGetNumChildren()];
        for(int i=0; i<node.jjtGetNumChildren();++i) {
            INode curr = node.jjtGetChildInternal(i);
            if (curr instanceof NodeTerm) {
                body[i]=(NodeTerm)curr;
            }else{
                body[i]=new NodeTerm(curr);
            }
        }
        term_=TermWare.getInstance().getTermFactory().createComplexTerm(node.getName(), body);
      }catch(TermWareException ex){
          throw new TermWareRuntimeException(ex);
      }
    }    
    
    /**
     * credte NodeTerm from normal term.
     * id_ is set to -1.
     */
    public NodeTerm(Term t) {
        id_=-1;
        term_=t;
    }
    
       

    /**
     * create NodeTerm with given id and name
     */
    public NodeTerm(int id, String name) 
    {
     try {   
      id_=id;  
      term_=TermWare.getInstance().getTermFactory().createComplexTerm(name,new Term[0]);
     }catch(TermWareException ex){
         throw new TermWareRuntimeException(ex);
     }
    }

    /**
     * add child at given place. If arity of existing term is less than <code> i </code> 
     * -- add <code> NIL </code> subterms as emty trees, otherwise -- replace existing subterm.
     *Implementator can delegate functionality of jjtAddChild of own node interface
     *in JJTree SimpleNode. <i>  add is bad name (this in not add in strice sence), but this is semantics of
     *JJTree node interface </i>
     */
    public void jjtAddChildInternal(INode n, int i) 
    {
      try{  
        Term tn;    
        if (n instanceof NodeTerm) {
            tn=(NodeTerm)(n);
        }else{
            tn=new NodeTerm(n);
        }                  
        if (i>=term_.getArity()) {            
            Term[] newBody=new Term[i+1];
            int j;
            for(j=0; j<term_.getArity(); ++j) {
              newBody[j]=term_.getSubtermAt(j);
            }
            for(j=term_.getArity();j<i;++j){
                newBody[j]=TermWare.getInstance().getTermFactory().createNIL();
            }
            newBody[i]=tn;            
            term_=term_.createSame(newBody);
        }else{
            term_.setSubtermAt(i,tn);
        }
        
      }catch(TermWareException ex){
          throw new TermWareRuntimeException(ex);
      }
    }

    
    
     /**
     * set child at given place, replcing existing.
     *Implementator can use this in JJTree actions.
     */
    public void jjtSetChildInternal(INode n, int i) 
    {
      try{  
        NodeTerm tn;    
        if (n instanceof NodeTerm) {
            tn=(NodeTerm)(n);
        }else{
            tn=new NodeTerm(n);
        }   
        term_.setSubtermAt(i,tn);
      }catch(TermWareException ex){
          throw new TermWareRuntimeException(ex);
      }
    }

    /**
     * Insert node at index <code> i <code>
     * If arity of existing term is less than <code> i </code> -- add <code> NIL </code>
     * subterms as emty trees, otherwise -- insert <code> n </code> at index i, moving next elements
     * up at one index. (this is  difference from jjtAddInternal).
     *Implementator can use this in JJTree actions for adding extra custom subterms from actions
     *to parse tree.
     */
    public void jjtInsertChildInternal(INode n, int i)
    {
      try {
          NodeTerm tn;
          if (n instanceof NodeTerm) {
             tn=(NodeTerm)(n);           
          }else{
              tn=new NodeTerm(n);
          }
          if (i>=term_.getArity()) {
              jjtAddChildInternal(tn,i);
          }else{
            Term[] newBody=new Term[term_.getArity()+1];
            int j;
            for(j=0; j<i;++j) {
                newBody[j]=term_.getSubtermAt(j);
            }
            newBody[i]=tn;
            for(j=i;j<term_.getArity();++j) {
              newBody[j+1]=term_.getSubtermAt(j);
            }                          
            term_=term_.createSame(newBody);
          }
      }catch(TermWareException ex){
          throw new TermWareRuntimeException(ex);
      }        
    }

    public void jjtClose() {        
    }

    public INode jjtGetChildInternal(int i) {
        Term t = term_.getSubtermAt(i);
        if (t instanceof INode) {
            return (INode)t;
        }else{
            return new NodeTerm(t);
        }
    }

    public int jjtGetNumChildren() {
        return term_.getArity();
    }

    public INode jjtGetParentInternal() {
        return parent_;
    }

    public void jjtOpen() {
    }

    public void jjtSetParentInternal(INode n) {
        parent_=n;
    }
    
    

    public boolean boundEquals(Term x) throws TermWareException {
        return term_.boundEquals(x);
    }

    public boolean boundUnify(Term t,Substitution s) throws TermWareException {
        return term_.boundUnify(t,s);
    }

    public Term createSame(Term[] newBody) throws TermWareException {
        return term_.createSame(newBody);
    }

    public boolean emptyFv() {
        return term_.emptyFv();
    }

    public int findSubtermIndexBoundEqualsTo(Term x) throws TermWareException {
        return term_.findSubtermIndexBoundEqualsTo(x);
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

    public BigInteger getBigInteger() throws UnsupportedOperationException {
        return term_.getBigInteger();
    }

    public boolean getBoolean() {
        return term_.getBoolean();
    }

    public byte getByte() {
        return term_.getByte();
    }

    public char getChar() {
        return term_.getChar();
    }

    public double getDouble() throws UnsupportedOperationException {
        return term_.getDouble();
    }

    public float getFloat() throws UnsupportedOperationException {
        return term_.getFloat();
    }

    public int getInt() throws UnsupportedOperationException {
        return term_.getInt();
    }

    public Object getJavaObject() {
        return term_.getJavaObject();
    }

    public long getLong() throws UnsupportedOperationException {
        return term_.getLong();
    }

    public String getName() {
        return term_.getName();
    }

    public Object getNameIndex() {
        return term_.getNameIndex();
    }

    public Number getNumber() {
        return term_.getNumber();
    }

    public int getPrimaryType0() {
        return term_.getPrimaryType0();
    }

    public short getShort() throws UnsupportedOperationException {
        return term_.getShort();
    }

    public String getString() {
        return term_.getString();
    }

    public Term getSubtermAt(int i) {
        return term_.getSubtermAt(i);
    }

    public Term getTerm() {
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
    }

    public void setSubtermAt(int i, Term t) throws TermWareException {
        if (t instanceof INode) {
            term_.setSubtermAt(i, t);
            ((INode)t).jjtSetParentInternal(this);
        }else{
            NodeTerm subterm=new NodeTerm(t);
            term_.setSubtermAt(i, subterm);
            subterm.jjtSetParentInternal(this);
        }
    }

    
    
    public void shiftFv(int newMinFv) throws TermWareException {
        term_.shiftFv(newMinFv);
    }

    public Term subst(Substitution s) throws TermWareException {
        return term_.subst(s);
    }

    public boolean substInside(Substitution s) throws TermWareException {
        return term_.substInside(s);
    }

    public Term termClone() throws TermWareException {
        return new NodeTerm(term_.termClone());
    }

    public int termCompare(Term x) {
        return term_.termCompare(x);
    }
    
    public PartialOrderingResult concreteOrder(Term x, Substitution s) throws TermWareException
    {
        return term_.concreteOrder(x,s);
    }
    
    public  Term getAttribute(String attribute) 
    {
        return TermHelper.getAttribute(term_,attribute);
    }
    
    public  Map<String,Term>  getAttributes()
    {
        if (term_ instanceof Attributed) {
            return ((Attributed)term_).getAttributes();
        }else{
            return Collections.emptyMap();
        }
    }
    
    public  void setAttribute(String attribute, Term value) 
    {
        term_=TermHelper.setAttribute(term_,attribute,value);
    }
    
    public  void setAttribute(String attribute, String value) 
    {
        term_=TermHelper.setAttribute(term_,attribute,value);
    }
    
    private INode parent_=null;
    int     id_=-1;
    private Term term_=TermWare.getInstance().getTermFactory().createNil();

   
}
