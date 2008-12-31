/*
 * BinaryFunction.java
 *
 */

package ua.gradsoft.termware.util;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Abstract interface for binary functions on terms.
 * @author Ruslan Shevchenko
 */
public interface TermBinaryFunction {
    
    Term run(Term frs, Term snd) throws TermWareException;
    
}
