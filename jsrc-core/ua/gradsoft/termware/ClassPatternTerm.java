/*
 * ClassPattern.java
 *
 */

package ua.gradsoft.termware;

import ua.gradsoft.termware.exceptions.AssertException;
import ua.gradsoft.termware.exceptions.JavaClassNotFoundException;
import ua.gradsoft.termware.exceptions.MatchingFailure;
import ua.gradsoft.termware.exceptions.TermIndexOutOfBoundsException;

/**
 *Term for class pattern.
 * JObject with class 'name' math pattern @class(name,$x)
 * @author Ruslan Shevchenko
 */
public class ClassPatternTerm extends AbstractComplexTerm {
    
    
    /** Creates a new instance of ClassPattern */
    private ClassPatternTerm(String name,Term t) throws JavaClassNotFoundException {
        try {
            matchedClass_ = Class.forName(name);
            matchedClassName_=new StringTerm(name);
            matchedTerm_=t.getTerm();
        }catch(ClassNotFoundException ex){
            throw new JavaClassNotFoundException(name);
        }
    }
    
    public static Term createClassPatternTerm(String name,Term t) throws TermWareException {
        Class<?> matchedClass;
        try {
            matchedClass = Class.forName(name);
        }catch(ClassNotFoundException ex){
            throw new JavaClassNotFoundException(name);
        }
        if (t.isJavaObject()){
            if (matchedClass.isInstance(t.getJavaObject())) {
                return t;
            }else{
                throw new AssertException("creation of class pattern with incompatible java type, class pattern:"+name+", object type:"+t.getJavaObject().getClass().getName());
            }
        }else{
            if (t.getNameIndex().equals(TermWareSymbols.CLASS_PATTERN_INDEX)) {
                String secondClassName=t.getSubtermAt(0).getString();
                Class<?> secondClass;
                try {
                    secondClass = Class.forName(secondClassName);
                }catch(ClassNotFoundException ex){
                    throw new JavaClassNotFoundException(secondClassName);
                }
                if (secondClass.isAssignableFrom(matchedClass)) {
                    return t;
                }else if (matchedClass.isAssignableFrom(secondClass)) {
                    return new ClassPatternTerm(secondClassName,t.getSubtermAt(1));
                }else{
                    throw new AssertException("tying to create class pattern from other non-assignable class pattern");
                }
            }else{
                return new ClassPatternTerm(name,t);
            }
        }
    }
    
    
    public static Term createClassPatternTerm(Term x1,Term x2) throws TermWareException
    {
        if (x1.isString()) {
            return createClassPatternTerm(x1.getString(),x2);
        }else{
            throw new AssertException("first argument of class pattern constuctor must be string (name of class)");
        }
    }
    
    
    /**
     *get name of term.
     *@return CLASS_PATTERN_STRING
     */
    public String getName() {
        return TermWareSymbols.CLASS_PATTERN_STRING;
    }
   
    /**
     *get index of name in termware symbol term.
     *@return TermWareSymbols.CLASS_PATTERN_INDEX
     */
    public Object getNameIndex() {
        return TermWareSymbols.CLASS_PATTERN_INDEX;
    }
    
    public String getPatternName() {
        return TermWareSymbols.JOBJECT_STRING;
    }
    
    public Object getPatternNameIndex() {
        return TermWareSymbols.JOBJECT_INDEX;
    }
    
    
    public int getArity() {
        return 2;
    }
    
    public Term getSubtermAt(int i) {
        switch(i) {
            case 0: return matchedClassName_;
            case 1: return matchedTerm_;
            default:
                throw new TermIndexOutOfBoundsException(this,i);
        }
    }
    
    public void setSubtermAt(int i,Term t) throws TermWareException {
        switch(i) {
            case 0:
                if (t.isString()) {
                    matchedClassName_=t;
                    try {
                        matchedClass_=Class.forName(t.getString());
                    }catch(ClassNotFoundException ex){
                        throw new JavaClassNotFoundException(t.getString());
                    }
                }else{
                    throw new AssertException("first subtern of @class must be string");
                }
                break;
            case 1:
                matchedTerm_=t;
                break;
            default:
                throw new TermIndexOutOfBoundsException(this,i);
        }
    }
    
    public Term createSame(Term[] newBody) throws TermWareException {
        if (newBody.length!=2) {
            throw new AssertException("arity of @class must be 2");
        }
        return createClassPatternTerm(newBody[0].getString(),newBody[1].getTerm());
    }
    
    public Term termClone() throws TermWareException {
        return new ClassPatternTerm(matchedClassName_.getString(),matchedTerm_.termClone());
    }
    
    /**
     * unification of <code> this </code> and <code> t </code> via substitution s,
     *whith bound propositional variables.
     */
    public boolean boundUnify(Term t,Substitution s) throws TermWareException {
        if (t.isNil()) return false;
        if(t.isX()) {
            s.put(t,this);
            return true;
        }else {
            if (t.isJavaObject()) {                
                if (matchedClass_.isAssignableFrom(t.getJavaObject().getClass())) {
                    if (matchedTerm_.isX()) {
                        try {
                           s.put(t,matchedTerm_);
                        }catch(MatchingFailure ex){
                            return false;
                        }
                        return true;
                    }else if(matchedTerm_.isJavaObject()) {
                        return matchedTerm_.getJavaObject().equals(t.getJavaObject());
                    }
                }else{
                    return false;
                }
            }else if (PrimaryTypes.isPrimitive(t.getPrimaryType0())) {
                return false;
            }else if (t.getNameIndex().equals(TermWareSymbols.CLASS_PATTERN_INDEX)) {
                //TODO: @class are matched.
                if (t.getArity()!=2) return false;
                if (t.getSubtermAt(0).isString()) {
                    if (!matchedClassName_.equals(t.getSubtermAt(0).getString())) {
                        return false;
                    }else{
                        return matchedTerm_.boundUnify(t.getSubtermAt(1), s);
                    }
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }
        // unreachable, but required by compiler.
        return false;
    }
    
    public int findSubtermIndexBoundEqualsTo(Term x) throws TermWareException {
        return -1;
    }
    
    public  PartialOrderingResult  concreteOrder(Term x,Substitution s) throws TermWareException
    {
        if (x.isX()) {
            Term t = s.get(x.minFv());
            if (t!=null) {
                return concreteOrder(t,s);
            }else{
                s.put(x,this);
                return PartialOrderingResult.LESS;
            }
        }else if (x.isComplexTerm()) {
            if (x.getNameIndex().equals(TermWareSymbols.CLASS_PATTERN_INDEX)) {
                if (x.getArity()!=2) {
                    return PartialOrderingResult.NOT_COMPARABLE;
                }
                Term sx0=x.getSubtermAt(0);
                Term sx1=x.getSubtermAt(1);
                if (!sx0.isString()) {
                    return PartialOrderingResult.NOT_COMPARABLE;
                }        
                if (!sx1.isX()) {
                    return PartialOrderingResult.NOT_COMPARABLE;
                }
                String c0 = sx0.getString();
                try {
                    Class<?> mClass = Class.forName(c0);
                    if (mClass.equals(matchedClass_)) {
                        return PartialOrderingResult.EQ;
                    }
                    if (mClass.isAssignableFrom(matchedClass_)) {
                        return PartialOrderingResult.MORE;
                    }
                    if (matchedClass_.isAssignableFrom(mClass)) {
                        return PartialOrderingResult.LESS;
                    }else{
                        return PartialOrderingResult.NOT_COMPARABLE;
                    }
                }catch(ClassNotFoundException ex){
                    // class pattern with unknown name.
                    return PartialOrderingResult.NOT_COMPARABLE;
                }
            }else if (x.getNameIndex().equals(TermWareSymbols.CLASS_PATTERN_INDEX)) {
                if (x.getArity()!=2) {
                    return PartialOrderingResult.NOT_COMPARABLE;
                }  
                Term sx0=x.getSubtermAt(0);
                Term sx1=x.getSubtermAt(1);
                if (!sx0.isX()) {                    
                    return PartialOrderingResult.NOT_COMPARABLE;
                }
                if (!sx1.isX()) {
                    return PartialOrderingResult.NOT_COMPARABLE;
                }
                s.put(sx0,matchedClassName_);
                s.put(sx1,matchedTerm_);
                return PartialOrderingResult.LESS;
            }else{
                return PartialOrderingResult.NOT_COMPARABLE;
            }           
        }else if (x.isJavaObject()) {
            Object o = x.getJavaObject();
            Class<?> oClass = o.getClass();           
            if (oClass.isAssignableFrom(matchedClass_)) {
                return PartialOrderingResult.LESS;
            }else{
                return PartialOrderingResult.NOT_COMPARABLE;
            }
        }else{
            return PartialOrderingResult.NOT_COMPARABLE;
        }
    }
    
    
    private Term  matchedClassName_;
    private Class<?> matchedClass_;
    private Term  matchedTerm_;

    
}
