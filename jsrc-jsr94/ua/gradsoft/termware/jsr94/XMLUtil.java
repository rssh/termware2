/*
 * XMLUtil.java
 *
 * Created on 23, 08, 2005, 1:53
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termware.jsr94;

import java.io.PrintStream;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Notation;
import org.w3c.dom.ProcessingInstruction;

/**
 *
 * @author Ruslan Shevchenko
 */
public class XMLUtil {
    

    public static void print(Node node,PrintStream out, int level) throws DOMException
    {
        switch(node.getNodeType()) {
            case Node.ATTRIBUTE_NODE:
            {
                out.print(node.getNodeName());
                out.print("=");
                out.print("\"");
                printInQuote(node.getNodeValue(),out);
                out.print("\"");
            }
            break;
            case Node.CDATA_SECTION_NODE:
            {
                out.print("<![CDATA[");
                out.print(node.getNodeValue());
                out.print("]]>");
            }
            break;
            case Node.COMMENT_NODE:
            {
                out.print("<!--");
                out.print(node.getNodeValue());
                out.print("-->");
            }
            break;
            case Node.DOCUMENT_FRAGMENT_NODE:              
            {
                NodeList childNodes=node.getChildNodes();            
                for(int i=0; i<childNodes.getLength(); ++i){
                    Node current = childNodes.item(i);
                    print(current,out,level+1);
                    if (current.getNodeType()==Node.TEXT_NODE) {                        
                       out.print(" ");
                    }else{
                      out.println();  
                      printIndent(out,level);
                    }                                        
                }              
            }
            break;
            case Node.DOCUMENT_NODE:
            {
                out.print("<?xml version=\"1.0\" encoding=\"UTF-8\" >");
                NodeList childNodes=node.getChildNodes();            
                for(int i=0; i<childNodes.getLength(); ++i){
                    if (i!=0) {
                      out.println();
                    }
                    printIndent(out,level);
                    print(childNodes.item(i),out,level+1);
                }                              
            }
            break;
            case Node.DOCUMENT_TYPE_NODE:
            {
                out.print("<!DOCTYPE ");
                out.print(node.getNodeName());
                out.print(" PUBLIC \"");
                DocumentType documentType = (DocumentType)node;
                out.print(documentType.getPublicId());
                out.print("\" \"");
                out.print(documentType.getSystemId());
                out.print("\">");                
            }
            break;
            case Node.ELEMENT_NODE:
            {                          
                printIndent(out,level);
                out.print("<");
                out.print(node.getNodeName());
                NamedNodeMap attrs = node.getAttributes();
                if (attrs!=null) {
                    for(int i=0; i<attrs.getLength(); ++i){
                        out.print(" ");
                        XMLUtil.print(attrs.item(i),out,level);
                    }
                }
                out.println(">");
                NodeList childNodes=node.getChildNodes();
                for(int i=0; i<childNodes.getLength(); ++i) {                    
                    XMLUtil.print(childNodes.item(i),out,level+1);                    
                    out.println();
                }
                out.print("</");
                out.print(node.getNodeName());
                out.print(">");
            }
            break;
            case Node.ENTITY_NODE:
            {
                Entity entity=(Entity)node;
                out.print("<!ENTITY ");
                out.print(entity.getNotationName());
                out.print(" \"");
                out.print(entity.getPublicId());
                out.print("\" \"");
                out.print(entity.getSystemId());
                out.print("\">");
            }
            break;
            case Node.ENTITY_REFERENCE_NODE:
            {
                out.print("&");
                out.print(node.getNodeName());
                out.print(";");
            }
            break;
            case Node.NOTATION_NODE:
            {
                Notation notation = (Notation)node;
                out.print("<!NOTATION ");
                out.print(notation.getNodeName());
                out.print(" \"");
                out.print(notation.getPublicId());
                out.print("\" \"");
                out.print(notation.getSystemId());
                out.print("\">");
            }
            break;
            case Node.PROCESSING_INSTRUCTION_NODE:
            {
                ProcessingInstruction pi = (ProcessingInstruction)node;
                out.print("<?");
                out.print(pi.getTarget());
                out.print(" ");
                out.print(pi.getData());
                out.print(">");
            }
            break;
            case Node.TEXT_NODE:
            {
                printText(node.getNodeValue(),out,level);                
            }
            break;
            default:
                throw new DOMException(DOMException.SYNTAX_ERR, "Invalid node type");                            
        }
    }
    
    
    public static void printIndent(PrintStream out, int level)
    {
        for(int i=0; i<level; ++i) {
            out.print(" ");
        }
    }
    
    public static void printInQuote(String value, PrintStream out)
    {
      for(int i=0; i<value.length(); ++i) {
          char ch = value.charAt(i);
          if (ch=='"') {
              out.print("\\\"");
          }else{
              out.print(ch);
          }
      }  
    }
    
    public static void printText(String text, PrintStream out, int level)
    {
      char[] chText = text.toCharArray();  
      for(int i=0; i<chText.length; ++i) {
          switch(chText[i]) {
              case '<': 
                  out.print("&lt;");
                  break;
              case '>':
                  out.print("&gt;");
                  break;
              case '&':
                  out.print("&amp;");
                  break;
              default:
                  out.print(chText[i]);
                  break;
          }
      }
    }
    
}
