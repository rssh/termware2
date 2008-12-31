
package ua.gradsoft.termware.parsers.terms.util;


import ua.gradsoft.termware.exceptions.*;

public class TermReaderUtils
{

 /*
  * extract string from string token
  * (i. e. "xxx" -> xxx) 
  */
 public static String token2String(String s) throws TermParseException
 {
  StringBuffer sb=new StringBuffer();
  for (int i=1; i<s.length()-1; ++i) {
      char ch=s.charAt(i);
      if (ch=='\\') {
          ++i;
          if (i==s.length()-1) {
              throw new TermParseException("'\\' is last character in string");
          }
          ch=s.charAt(i);
          switch(ch) {
              case '\\':
                  sb.append('\\');
                  break;
              case 'n':
                  sb.append('\n');
                  break;
              case 't':
                  sb.append('\t');
                  break;
              case 'b':
                  sb.append('\b');
                  break;
              case 'r':
                  sb.append('\r');
                  break;
              case 'f':
                  sb.append('\f');
                  break;
              case  '\'':
                  sb.append('\'');
                  break;
              case '"':
                  sb.append('"');
                  break;
              case '0':
                  if (i==s.length()-1) {
                      throw new TermParseException("\\0 is a last substring in string");
                  }
                  byte[] bytes=new byte[1];
                  int j=findEndOfInlineByte(s,i);
                  try {
                    bytes[0]=Byte.decode(s.substring(i,j)).byteValue();
                  }catch(NumberFormatException ex){
                      throw new TermParseException("can't parse byte expression in string");
                  }
                  i=j;
                  String bytesAsString=new String(bytes);
                  sb.append(bytesAsString);
                  break;
              default:
                  throw new TermParseException("Invalid character after escape");
          }
      }else{
          sb.append(ch);
      }
  }
  return sb.toString();
 }

 static int findEndOfInlineByte(String s, int i) throws TermParseException
 {
   int j=i+1;  
   int radix;
   char ch=s.charAt(j);
   switch(ch) {
       case '0': 
           radix=8;
           ++i;
           break;
       case '1':
       case '2':
       case '3':
       case '4':
       case '5':
       case '6':
       case '7':
       case '8':
       case '9':
           radix=10;
           break;
       case 'x':
       case 'X':
           radix=16;
           break;
       default:
           throw new TermParseException("invalid sequence after \\0");
   }
   while(j<s.length()-1) {
       ch=s.charAt(j);
       switch(ch) {
         case '0':
         case '1':
         case '2':
         case '3':
         case '4':
         case '5':
         case '6':
         case '7':
               break;
         case '8':
         case '9':
               if (radix==8) {
                   return j;
               }
               break;
         case 'a':
         case 'A':
         case 'b':
         case 'B':
         case 'c':
         case 'C':
         case 'd':
         case 'D':
         case 'e':
         case 'E':
         case 'f':
         case 'F':
               if (radix!=16) {
                   return j;
               }
               break;
         default:
               return j;
       }
       ++j;
   }
   return j;
 }
 
};