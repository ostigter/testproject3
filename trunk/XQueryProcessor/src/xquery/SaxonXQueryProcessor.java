package xquery;


import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import net.sf.saxon.Configuration;
import net.sf.saxon.query.DynamicQueryContext;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.trans.XPathException;


/**
 * XQuery processor implementation with Saxon.
 * 
 * @author Oscar Stigter
 */
public class SaxonXQueryProcessor extends AbstractXQueryProcessor {


    public OutputStream executeFunction(
            String namespace, String function,  Object ... params)
            throws XQueryException {
        String location = modules.get(namespace);
        if (location == null) {
            throw new XQueryException(
                    "Namespace " + namespace + " is not supported");
        }
        String query = createQuery(namespace, location, function, params);
        System.out.println("Query: \n" + query);

        try {
            Configuration config = new Configuration();
            StaticQueryContext sqc = new StaticQueryContext(config);
            XQueryExpression expr = sqc.compileQuery(query);
            DynamicQueryContext dqc = new DynamicQueryContext(config);
            for(int i = 0; i < params.length; i++) {
                dqc.setParameter("par" + String.valueOf(i + 1), params[i]);
            }
            OutputStream os = new ByteArrayOutputStream();
            Result result = new StreamResult(os);
            expr.run(dqc, result, null);
            return os;
        } catch (XPathException xpe) {
            throw new XQueryException(xpe.getMessage());
        }

    }


}
