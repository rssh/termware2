/*
 * XMLTermWareEntityResolver.java
 *
 * Created 9, 08, 2005, 2:55
 *
 * Copyright (c) 2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termware.parsers.xml.terms;

import java.io.InputStream;
import java.net.URL;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 *
 * @author Ruslan Shevchenko
 */
public class XMLTermWareEntityResolver implements EntityResolver
{
			
	public InputSource resolveEntity (String publicId, String systemId) {
		if ( systemId!=null && systemId.startsWith(GRADSOFT_URL_) ) {
			ClassLoader classLoader = this.getClass().getClassLoader();
                        String resourceName = "ua/gradsoft/termware/dtd/" + systemId.substring( GRADSOFT_URL_.length()+"termware/".length() );
			InputStream dtdStream = classLoader.getResourceAsStream(resourceName);
			if (dtdStream==null) {
                                //System.err.println("resource not found:"+resourceName);
				return null;
                        } else {				
				InputSource source = new InputSource(dtdStream);
				source.setPublicId(publicId);
				source.setSystemId(systemId);
				return source;
			}
		}
		else {
			// use the default behaviour
			return null;
		}
	}
	
        private static final String GRADSOFT_URL_ = "http://www.gradsoft.ua/";
        
}

