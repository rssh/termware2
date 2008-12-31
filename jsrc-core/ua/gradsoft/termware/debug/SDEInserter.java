package ua.gradsoft.termware.debug;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * This helper class inserts SDE (JSR 45 Software Debug Extension attribute) into class bytes.
 * Code is derived from Apache Jasper project.
 * SMAPUtils.SDEInstaller
 * <pre>
 * //*********************************************************************
 * // Installation logic (from Robert Field, JSR-045 spec lead)
 * </pre>
 */
public  class SDEInserter {
    
    
    static final String nameSDE = "SourceDebugExtension";
    
    byte[] orig;
    byte[] sdeAttr;
    byte[] gen;
    
    int origPos = 0;
    int genPos = 0;
    
    int sdeIndex;
    boolean isDebugEnabled=false;
    
    
    
    
    SDEInserter(byte[] originBytes, byte[] sdeAttr) throws UnsupportedEncodingException, IOException
    {        
        this.sdeAttr = sdeAttr;
        // get the bytes
        orig = originBytes;
        gen = new byte[orig.length + sdeAttr.length + 100];
        
        // do it
        addSDE();
        
        byte[] newGen = new byte[genPos];
        System.arraycopy(gen,0,newGen,0,genPos);
        gen=newGen;
        
    }

    byte[] getResult()
    { return gen; }
    
    void   debug(String line)
    {
      System.err.println(line);  
    }
        
    void addSDE() throws UnsupportedEncodingException, IOException {
        int i;
        copy(4 + 2 + 2); // magic min/maj version
        int constantPoolCountPos = genPos;
        int constantPoolCount = readU2();
        if (isDebugEnabled) {
            System.err.println("constant pool count: " + constantPoolCount);
        }
        writeU2(constantPoolCount);
        
        // copy old constant pool return index of SDE symbol, if found
        sdeIndex = copyConstantPool(constantPoolCount);
        if (sdeIndex < 0) {
            // if "SourceDebugExtension" symbol not there add it
            writeUtf8ForSDE();
            
            // increment the countantPoolCount
            sdeIndex = constantPoolCount;
            ++constantPoolCount;
            randomAccessWriteU2(constantPoolCountPos, constantPoolCount);
            
            if (isDebugEnabled) {
                debug("SourceDebugExtension not found, installed at: " + sdeIndex);
            }
                
        } else {
            if (isDebugEnabled) {
                debug("SourceDebugExtension found at: " + sdeIndex);
            }
        }
        copy(2 + 2 + 2); // access, this, super
        int interfaceCount = readU2();
        writeU2(interfaceCount);
        if (isDebugEnabled)
            debug("interfaceCount: " + interfaceCount);
        copy(interfaceCount * 2);
        copyMembers(); // fields
        copyMembers(); // methods
        int attrCountPos = genPos;
        int attrCount = readU2();
        writeU2(attrCount);
        if (isDebugEnabled)
            debug("class attrCount: " + attrCount);
        // copy the class attributes, return true if SDE attr found (not copied)
        if (!copyAttrs(attrCount)) {
            // we will be adding SDE and it isn't already counted
            ++attrCount;
            randomAccessWriteU2(attrCountPos, attrCount);
            if (isDebugEnabled)
                debug("class attrCount incremented");
        }
        writeAttrForSDE(sdeIndex);
    }
    
    void copyMembers() {
        int count = readU2();
        writeU2(count);
        if (isDebugEnabled)
            debug("members count: " + count);
        for (int i = 0; i < count; ++i) {
            copy(6); // access, name, descriptor
            int attrCount = readU2();
            writeU2(attrCount);
            if (isDebugEnabled)
                debug("member attr count: " + attrCount);
            copyAttrs(attrCount);
        }
    }
    
    boolean copyAttrs(int attrCount) {
        boolean sdeFound = false;
        for (int i = 0; i < attrCount; ++i) {
            int nameIndex = readU2();
            // don't write old SDE
            if (nameIndex == sdeIndex) {
                sdeFound = true;
                if (isDebugEnabled)
                    debug("SDE attr found");
            } else {
                writeU2(nameIndex); // name
                int len = readU4();
                writeU4(len);
                copy(len);
                if (isDebugEnabled)
                    debug("attr len: " + len);
            }
        }
        return sdeFound;
    }
    
    void writeAttrForSDE(int index) {
        writeU2(index);
        writeU4(sdeAttr.length);
        for (int i = 0; i < sdeAttr.length; ++i) {
            writeU1(sdeAttr[i]);
        }
    }
    
    void randomAccessWriteU2(int pos, int val) {
        int savePos = genPos;
        genPos = pos;
        writeU2(val);
        genPos = savePos;
    }
    
    int readU1() {
        return ((int)orig[origPos++]) & 0xFF;
    }
    
    int readU2() {
        int res = readU1();
        return (res << 8) + readU1();
    }
    
    int readU4() {
        int res = readU2();
        return (res << 16) + readU2();
    }
    
    void writeU1(int val) {
        gen[genPos++] = (byte)val;
    }
    
    void writeU2(int val) {
        writeU1(val >> 8);
        writeU1(val & 0xFF);
    }
    
    void writeU4(int val) {
        writeU2(val >> 16);
        writeU2(val & 0xFFFF);
    }
    
    void copy(int count) {
        for (int i = 0; i < count; ++i) {
            gen[genPos++] = orig[origPos++];
        }
    }
    
    byte[] readBytes(int count) {
        byte[] bytes = new byte[count];
        for (int i = 0; i < count; ++i) {
            bytes[i] = orig[origPos++];
        }
        return bytes;
    }
    
    void writeBytes(byte[] bytes) {
        for (int i = 0; i < bytes.length; ++i) {
            gen[genPos++] = bytes[i];
        }
    }
    
    int copyConstantPool(int constantPoolCount)
    throws UnsupportedEncodingException, IOException {
        int sdeIndex = -1;
        // copy const pool index zero not in class file
        for (int i = 1; i < constantPoolCount; ++i) {
            int tag = readU1();
            writeU1(tag);
            switch (tag) {
                case 7 : // Class
                case 8 : // String
                    if (isDebugEnabled)
                        debug(i + " copying 2 bytes");
                    copy(2);
                    break;
                case 9 : // Field
                case 10 : // Method
                case 11 : // InterfaceMethod
                case 3 : // Integer
                case 4 : // Float
                case 12 : // NameAndType
                    if (isDebugEnabled)
                        debug(i + " copying 4 bytes");
                    copy(4);
                    break;
                case 5 : // Long
                case 6 : // Double
                    if (isDebugEnabled)
                        debug(i + " copying 8 bytes");
                    copy(8);
                    i++;
                    break;
                case 1 : // Utf8
                    int len = readU2();
                    writeU2(len);
                    byte[] utf8 = readBytes(len);
                    String str = new String(utf8, "UTF-8");
                    if (isDebugEnabled)
                        debug(i + " read class attr -- '" + str + "'");
                    if (str.equals(nameSDE)) {
                        sdeIndex = i;
                    }
                    writeBytes(utf8);
                    break;
                default :
                    throw new IOException("unexpected tag: " + tag);
            }
        }
        return sdeIndex;
    }
    
    void writeUtf8ForSDE() {
        int len = nameSDE.length();
        writeU1(1); // Utf8 tag
        writeU2(len);
        for (int i = 0; i < len; ++i) {
            writeU1(nameSDE.charAt(i));
        }
    }
}

