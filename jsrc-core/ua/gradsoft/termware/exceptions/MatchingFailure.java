package ua.gradsoft.termware.exceptions;


import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;

/**
 * matching failure.
 * note, that MatchingFailure is not exception itself.
 * i. e. it throwed during normal processing.
 * So, we do not put 'Exception' suffix here.
 **/
public class MatchingFailure extends TermWareException
{
 public MatchingFailure(Object frs, Object snd)
 {
  super("matching failure");
  frs_=frs;
  snd_=snd;
 }

 public String getMessage()
 {
   Term tfrs=(Term)frs_;
   Term tsnd=(Term)snd_;
   String sfrs;
   sfrs=((tfrs==null) ? "(build-in)" : TermHelper.termToString(tfrs));
   String ssnd;
   ssnd=((tsnd==null) ? "(build-in)" : TermHelper.termToString(tsnd));
   return ("can't match "+sfrs+" and "+ssnd); 
 }

 private Object frs_;
 private Object snd_;
}
