package eu.cloudopting.tosca.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

public class coNamespaceContext implements NamespaceContext
{
    private final Logger log = LoggerFactory.getLogger(coNamespaceContext.class);

    public String getNamespaceURI(String prefix)
    {
//    	System.out.println("getNamespaceURI:"+prefix);
        if (prefix.equals("co")){
//        	log.debug("coNamespaceContext.getNamespaceURI beccato prefisso");
            return "http://docs.oasis-open.org/tosca/ns/2011/12/CloudOptingTypes";
        }
        else if (prefix.equals("ns")) {
//        	log.debug("coNamespaceContext.getNamespaceURI prefisso vuoto");
        	return "http://docs.oasis-open.org/tosca/ns/2011/12";
		}
        else{
            log.debug("coNamespaceContext.getNamespaceURI null prefix.");
        	return "http://docs.oasis-open.org/tosca/ns/2011/12";
//            return XMLConstants.NULL_NS_URI;
        }
    }
    
    public String getPrefix(String namespace)
    {
//    	System.out.println("getPrefix"+namespace);
        if (namespace.equals("http://docs.oasis-open.org/tosca/ns/2011/12/CloudOptingTypes"))
            return "co";
        else if (namespace.equals("http://docs.oasis-open.org/tosca/ns/2011/12"))
        	return "ns";
        else
            return null;
    }

    public Iterator getPrefixes(String namespace)
    {
        return null;
    }
}  