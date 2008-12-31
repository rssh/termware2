/*
 * Part of GradSoft TermWare framework.
 * 
 */
package ua.gradsoft.termware.printers;

import java.io.PrintWriter;
import java.util.LinkedList;

/**
 *Writer for pretty printing.
 * In addition to print functions, user also can 
 * mark beginning and ending of language constructs,
 * whitespaces and possible linebreaks.
 * So, unlike PrintWriter, which understand only one type 
 * of token: strings, PrettyPrintWriter understand
 * 
 * (inside implemented pretty printing algorithm from
 * <pre>
 * \@article{DBLP:journals/toplas/Oppen80,
 * author    = {Derek C. Oppen},
 * title     = {Prettyprinting},
 * journal   = {ACM Trans. Program. Lang. Syst.},
 * volume    = {2},
 * number    = {4},
 * year      = {1980},
 * pages     = {465-483},
 * ee        = {http://doi.acm.org/10.1145/357114.357115},
 * bibsource = {DBLP, http://dblp.uni-trier.de}
 *}
 * </pre>
 * )
 * main changes from article are:
 * <ul>
 *  <li> We try gracefull handle any sequence of input tokens </li>
 *  <li> BreakType can be associated with begin/end brackets (TODO: descript)</li>
 * </ul>
 * @author rssh
 */
public class PrettyPrintWriter {

    public PrettyPrintWriter(PrintWriter printWriter) {
        printWriter_ = printWriter;
    }

    public void putString(String value) {
        prettyPrint(PrettyPrintingToken.createString(value));
    }

    public void putBegin(PrettyPrintingBreakType breakType) {
        prettyPrint(PrettyPrintingToken.createBegin(breakType,0,2));
    }

    public void putBegin(PrettyPrintingBreakType breakType, int size, int offset) {
        prettyPrint(PrettyPrintingToken.createBegin(breakType,size,offset));
    }
    
    
    public void putEnd(PrettyPrintingBreakType breakType) {
        prettyPrint(PrettyPrintingToken.createEnd(breakType,0,0));
    }

    public void putEnd(PrettyPrintingBreakType breakType, int size, int offset) {
        prettyPrint(PrettyPrintingToken.createEnd(breakType,size,offset));
    }
    
    
    public void putConsistentBreak(int size) {
        prettyPrint(PrettyPrintingToken.createBreak(PrettyPrintingBreakType.CONSISTENT, size,2));
    }

    public void putInconsistentBreak(int size) {
        prettyPrint(PrettyPrintingToken.createBreak(PrettyPrintingBreakType.INCONSISTENT, size,2));
    }

    public void putLineBreak() {
        prettyPrint(PrettyPrintingToken.createBreak(PrettyPrintingBreakType.INCONSISTENT, lineWidth_,2));
    }
    
    public void putBreak(PrettyPrintingBreakType breakType, int size, int offset) {
        prettyPrint(PrettyPrintingToken.createBreak(breakType,size,offset));
    }
    
    
    public void putEof() {
        prettyPrint(PrettyPrintingToken.createEof());
    }

    public void flush() {
        putEof();
        printWriter_.flush();
    }

    public int  getLineWidth()
    {
        return lineWidth_;
    }
    
    public void setLineWidth(int lineWidth)
    {
        lineWidth_=lineWidth;
    }
    
    private static class TokenRecord {

        public TokenRecord(PrettyPrintingToken inToken, int inSize, int inNumber) {
            token = inToken;
            size = inSize;
            number = inNumber;
        }
        public PrettyPrintingToken token;
        public int size;
        public int number;

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            if (token != null) {
                sb.append(token.toString());
            } else {
                sb.append("null");
            }
            sb.append(',');
            sb.append(Integer.toString(size));
            sb.append(',');
            sb.append(Integer.toString(number));
            sb.append(')');
            return sb.toString();
        }
    }

    private static class IdentRecord {

        public IdentRecord(int inOffset, int inLineNumber, PrettyPrintingBreakType inBreakType) {
            offset = inOffset;
            lineNumber = inLineNumber;
            breakType = inBreakType;            
        }
        public int offset;
        public int lineNumber;
        public PrettyPrintingBreakType breakType;
    }

    private void prettyPrint(PrettyPrintingToken token) {
        switch (token.getKind()) {
            case EOF:
                if (!stack_.isEmpty()) {
                    checkStack(0);
                }
                checkStream(true);
                break;
            case BEGIN:
                if (stack_.isEmpty()) {
                    leftTotal_ = rightTotal_ = 0;
                }
                 {
                    TokenRecord e = new TokenRecord(token, -rightTotal_, ++nTokens_);
                    buffer_.addLast(e);
                    stack_.push(e);
                }
                break;
            case END:
                 {
                    TokenRecord e = new TokenRecord(token, 0, ++nTokens_);
                    buffer_.addLast(e);
                    stack_.push(e);
                    checkStack(0);
                }
                break;
            case BREAK:
                if (stack_.isEmpty()) {
                    leftTotal_ = rightTotal_ = 0;
                }
                 {
                    checkStack(0);
                    TokenRecord e = new TokenRecord(token, -rightTotal_, ++nTokens_);                  
                    buffer_.addLast(e);
                    stack_.push(e);
                    rightTotal_ += token.getLength();                    
                }
                break;
            case STRING:
                {
                    TokenRecord e = new TokenRecord(token, token.getLength(), ++nTokens_);
                    buffer_.addLast(e);
                    rightTotal_ += token.getLength();
                    checkStream(false);
                }
                break;
        }

    }

    private void checkStream(boolean inEof) {
        int n = 0;
        int bufferLen = buffer_.size();
        int prevBufferLen = bufferLen;
        do {
            if (rightTotal_ - leftTotal_ > lineWidth_ - offset_ || inEof) {
                if (!stack_.isEmpty()) {
                    TokenRecord bufferFirst = buffer_.getFirst();
                    TokenRecord stackLast = stack_.getLast();
                    if (bufferFirst == stackLast) {
                        stack_.removeLast().size = 900;
                    } else if (n > 0 && prevBufferLen == bufferLen) {
                        if (bufferFirst.number < stackLast.number) {
                            bufferFirst.size = 900;
                        } else {
                            stackLast.size = 900;
                            stack_.removeLast();
                        }
                    }
                } else if (n > 0 && prevBufferLen == bufferLen) {
                    // nothing to print now, must wait for next symbols
                    break;
                }
                prevBufferLen = bufferLen;
                advanceLeft(inEof);
                bufferLen = buffer_.size();
                ++n;
            } else {
                break;
            }
        } while (!buffer_.isEmpty());
    }

    private void advanceLeft(boolean inEof) {
        if (!buffer_.isEmpty()) {
            TokenRecord r = buffer_.removeFirst();
            do {
                if (r.size > 0 || inEof) {
                    print(r);
                    if (!buffer_.isEmpty()) {
                        r = buffer_.removeFirst();
                    } else {
                        break;
                    }
                } else {
                    buffer_.addFirst(r);
                    break;
                }
            } while (true);
        }
    }

    private void checkStack(int k) {
        TokenRecord x;
        if (!stack_.isEmpty()) {
            x = stack_.getFirst();
            switch (x.token.getKind()) {
                case BEGIN:
                    if (k > 0) {
                        stack_.removeFirst();
                        x.size += rightTotal_;
                        checkStack(k - 1);
                    }
                    break;
                case END:
                    stack_.removeFirst();
                    x.size++;
                    checkStack(k + 1);
                    break;
                default:
                    stack_.removeFirst();
                    x.size += rightTotal_;
                    if (k > 0) {
                        checkStack(k);
                    } else {
                        if (!stack_.isEmpty()) {
                            TokenRecord y = stack_.getFirst();
                            if (y.number - x.number == 1) {
                                checkStack(k);
                            }
                        }
                    }
                    break;
            }
        }
    }

    private void print(TokenRecord tr) {
        PrettyPrintingToken token = tr.token;
        int len = token.getLength();
        if (offset_>=lineWidth_) {
            printNewLine(0);
        }
        switch (token.getKind()) {
            case BEGIN:
                
                if (len > lineWidth_-offset_) {
                    identStack_.push(new IdentRecord(offset_,nLines_,
                            token.getBreakType() == PrettyPrintingBreakType.CONSISTENT ? PrettyPrintingBreakType.CONSISTENT
                            : PrettyPrintingBreakType.INCONSISTENT));
                } else {
                    identStack_.push(new IdentRecord(offset_, nLines_, PrettyPrintingBreakType.FITS));
                }
                  switch(token.getBreakType()) {
                    case CONSISTENT:
                       if (offset_!=0) {  
                         printNewLine(offset_+token.getOffset());
                       }
                       break;
                    case INCONSISTENT:
                       if (len > lineWidth_ - offset_) {
                           printNewLine(offset_+token.getOffset());
                       }else{
                           indent(len);
                           leftTotal_+=len;
                       } 
                       break;
                    case FITS:
                        indent(len);
                        leftTotal_+=len;
                  }
                break;                
            case END:
                 {
                    int identOffset = 0;
                    int identNLine = nLines_;
                    if (!identStack_.isEmpty()) {
                        IdentRecord ir = identStack_.pop();
                        if (!identStack_.isEmpty()) {
                          ir = identStack_.peek();  
                          identOffset = ir.offset;
                          identNLine = ir.lineNumber;
                        }
                    }                    
                    if (offset_ >= lineWidth_) {                      
                        printNewLine(identOffset+token.getOffset());
                    }else{
                      switch(token.getBreakType()) {
                          case CONSISTENT:                            
                              printNewLine(identOffset+token.getOffset());
                              break;
                          case INCONSISTENT:
                              if (identNLine!=nLines_) {
                                  printNewLine(identOffset+token.getOffset());
                              }
                              break;
                          case FITS:
                             /* do noting */
                              break;
                      }
                    }
                }                
                break;
            case BREAK:
                 {
                    int identOffset = 0;
                    if (!identStack_.isEmpty()) {
                        identOffset = identStack_.peek().offset;
                    }
                    switch (token.getBreakType()) {
                        case FITS:                        
                            indent(token.getLength());
                            leftTotal_+=token.getLength();
                            break;
                        case CONSISTENT:                       
                            printNewLine(identOffset+token.getOffset());
                            leftTotal_+=offset_;
                            break;
                        case INCONSISTENT:
                            if (len > lineWidth_ - offset_) {                           
                                printNewLine(identOffset+token.getOffset());
                                leftTotal_+=offset_;
                            } else {                              
                                indent(len);
                                leftTotal_+=len;
                            }
                    }
                }           
                break;
            case STRING:
                if (len > lineWidth_ - offset_) {
                    // line too long.
                    offset_=lineWidth_;
                } else {
                    offset_ += len;
                }
                printWriter_.print(token.getString());           
                leftTotal_ += token.getLength();
                break;
        }
    }

    private void indent(int amount) {
        if (amount > 0) {
          for (int i = 0; i < amount; ++i) {
            printWriter_.print(' ');
          }     
          offset_+=amount;
        }
    }

    private void printNewLine(int amount) {
        printWriter_.println();
        ++nLines_;
        offset_ = 0;
        if (amount > 0) {
           indent(amount);
        }
    }
    
    
    public static final int DEFAULT_LINE_WIDTH = 80;
    
    private int offset_ = 0;
    private int nLines_ = 0;
    
    private int lineWidth_ = DEFAULT_LINE_WIDTH;
    private int rightTotal_ = 0;
    private int leftTotal_ = 0;
    private int nTokens_ = 0;
    private PrintWriter printWriter_;
    private LinkedList<TokenRecord> buffer_ = new LinkedList<TokenRecord>();
    private LinkedList<TokenRecord> stack_ = new LinkedList<TokenRecord>();
    private LinkedList<IdentRecord> identStack_ = new LinkedList<IdentRecord>();
}
