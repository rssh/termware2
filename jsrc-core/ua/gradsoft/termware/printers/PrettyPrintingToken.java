/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.gradsoft.termware.printers;

/**
 *
 * @author rssh
 */
public class PrettyPrintingToken {
    
    public enum Kind {
        STRING,
        BEGIN,
        END,
        BREAK,   
        EOF    
    };

    PrettyPrintingToken(Kind kind, PrettyPrintingBreakType breakType, String value, int length, int offset)
    {
      kind_=kind;
      breakType_=breakType;
      value_=value;
      len_=length;      
      offset_=offset;
    }
    
    public static PrettyPrintingToken createString(String value)
    {
      return new PrettyPrintingToken(Kind.STRING,PrettyPrintingBreakType.FITS,value,value.length(),0);  
    }

    public static PrettyPrintingToken createBegin(PrettyPrintingBreakType breakType, int size, int offset)
    {
      return new PrettyPrintingToken(Kind.BEGIN,breakType,null,size,offset);  
    }

    public static PrettyPrintingToken createEnd(PrettyPrintingBreakType breakType, int size, int offset)
    {
      return new PrettyPrintingToken(Kind.END,breakType,null,size,offset);  
    }
    
    public static PrettyPrintingToken createBreak(PrettyPrintingBreakType breakType, int size, int offset)
    {
      return new PrettyPrintingToken(Kind.BREAK,breakType,null,size,offset);  
    }

    public static PrettyPrintingToken createEof()
    {
      return new PrettyPrintingToken(Kind.EOF,PrettyPrintingBreakType.FITS,null,0,0);  
    }
    
    
    public Kind getKind()
    { return kind_; }
    

    public PrettyPrintingBreakType getBreakType()
    { return breakType_; }
            
    
    
    public int getLength()
    {
        return len_;
    }
    
    public int getOffset()
    {
        return offset_;
    }
    
    public String getString()
    {
      if (kind_==Kind.STRING) {
          return value_;
      }else if (kind_==Kind.BREAK) {
          if (len_!=0) {
              if (value_==null) {
                StringBuilder sb = new StringBuilder();
                for(int i=0; i<len_; ++i){
                  sb.append(' ');
                }
                value_ = sb.toString();
              }
              return value_;
          }else{
              return "";
          }
      }else{
          return "";
      } 
    }
    
    @Override
    public String toString()
    {
      StringBuilder sb = new StringBuilder();
      sb.append(kind_.toString());
      sb.append("/");
      sb.append(breakType_.toString());
      if (value_!=null)
      {
          sb.append("[");
          sb.append(value_);
          sb.append("]");
      }else{
          sb.append("[null]");
      }
      sb.append("/");
      sb.append(Integer.toString(len_));
      sb.append("/");
      sb.append(Integer.toString(offset_));
      return sb.toString();
    }
    
    private Kind   kind_;
    private PrettyPrintingBreakType breakType_;
    
    private String value_;
    
    /**
     * 
     */     
    private int    len_;

    /**
     * offset of this token, when one is immediatly after newline.     
     */
    private int    offset_;
}
