package ua.gradsoft.termware.exceptions;

import ua.gradsoft.termware.TermWareException;

/**
 * throwed, when we try access unexistent index.
 **/
public class TermIndexOutOfBoundsException extends IndexOutOfBoundsException
{
    public TermIndexOutOfBoundsException(Object t, int index)
 { super("no such index in term, object="+t.toString()+",index="+index); }
}
                                                    