package ua.gradsoft.termware;

import java.io.PrintWriter;


/**
 * Term, which represent atom.
 * (i. e. uninterpreterted  entity)
 **/
public final class AtomTerm extends AbstractPrimitiveTerm {
    
    /**
     * construct new atom term.
     *@param name - name of term.     
     *@param symbolAdoptionPolicy - symbol adoption policy of current instance.
     * 
     **/
    AtomTerm(String name,SymbolAdoptionPolicy symbolAdoptionPolicy) {
        v_ = name;
        vIndex_=null;
        adoptName(symbolAdoptionPolicy);        
    }
    
    
    
    /**
     * return typename0 - "atom"
     *@return PrimaryTypes.ATOM
     */
    public final int  getPrimaryType0() {
        return PrimaryTypes.ATOM; }
    
    
    /**
     * true, if term is atom.
     *@return true
     */
    public final boolean  isAtom() {
        return true; }
    
    
    /**
     * return name of atom.
     *@return name of atom.
     */
    public final String   getName() {
        return v_; }
    
    
    /**
     *is this is number.
     *@return false
     */
    public final boolean isNumber() {
        return false; }
    
    /**
     *get number.
     *throw UnsupporterOperationException
     */
    public final Number getNumber() throws UnsupportedOperationException {
        throw new UnsupportedOperationException(); 
    }
    
    public final Object getNameIndex() {
        if (vIndex_!=null) {
            return vIndex_;
        }else{
            return v_;
        }
    }
    
    
    /**
     * check for equality.
     */
    public final boolean  eq(Term x) {
        if (!x.isAtom()) return false;
        return getNameIndex().equals(x.getNameIndex());
    }
    
    /**
     * universal compare <code> this </code> and <code> t </code>
     *@param t - term to compare.
     *@return v, which is:
     *<ul>
     * <li> <0, if this <!< t </li>
     * <li> =0, if this == x </li>
     * <li> >0, if this >!> t </li>
     *</ul>
     */
    public final int termCompare(Term t) {
        int x=PrimaryTypes.ATOM - t.getPrimaryType0();
        if (x!=0) return x;
        return TermHelper.compareNameIndexes(getNameIndex(),t.getNameIndex());
    }
    

    @Override
    public final PartialOrderingResult concreteOrder(Term x, Substitution s) throws TermWareException
    {
      if (x.isX()) {
          Term t = s.get(x.minFv());
          if (t!=null) {
              return concreteOrder(t,s);
          }else{
              s.put(x,this);
              return PartialOrderingResult.LESS;
          }
      } else {
          int cmp = termCompare(x);
          if (cmp == 0) {
              return PartialOrderingResult.EQ;
          }else{
              return PartialOrderingResult.NOT_COMPARABLE;
          }
      } 
    }

    /**
     * return self, since atoms are immutable.
     **/    
    public final Term termClone() {
        return this; }
    
    public final void print(PrintWriter out) {
        out.print(v_);
    }
    
    public  final String   toString() {
        return v_;
    }
    
    final void adoptName(SymbolAdoptionPolicy symbolAdoptionPolicy) 
    {        
        lastNameWatchTime_=System.currentTimeMillis();
        boolean addName=(symbolAdoptionPolicy==SymbolAdoptionPolicy.ADD);
        SymbolTable.Entry e = SymbolTable.getSymbolTable().adoptName(v_, true);
        if (e!=null) {
             v_=e.getName();
             vIndex_=e.getIndex();
        }
     }
    
    
    private String v_;
    private Integer vIndex_;
    private transient long   lastNameWatchTime_;
    
    private static final long serialVersionUID = 20080111;
    
}
