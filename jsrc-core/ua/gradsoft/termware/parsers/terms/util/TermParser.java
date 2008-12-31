
package ua.gradsoft.termware.parsers.terms.util;


import java.util.*;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.IParser;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.parsers.terms.TermReader;

public class TermParser implements IParser
{

  TermParser(TermReader reader)
  {
   reader_=reader;
  }

  public Term readTerm() throws TermWareException
  {
   return reader_.readStatementWrapped();
  }

  /**
   *return mapping between names and indexes of free variables.
   */
  public StringIndex getXStringIndex()
  {
   return reader_.getStringIndex();  
  }
  
  public boolean eof()
  {
   return reader_.eofReached();
  } 

  private TermReader reader_;

};