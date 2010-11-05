package sr.projectx.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.apache.log4j.Logger;

@ManagedBean(name = "propertyService", eager = true)
@ApplicationScoped
public class PropertyServiceImpl implements PropertyService {

    /** Properties file. */
    private static final String PROPERTIES_FILE = "/application.properties";

    /** Log. */
    private static final Logger LOG = Logger.getLogger(PropertyServiceImpl.class);

    /** Properties. */
    private final Properties properties;

    /**
     * Constructor.
     */
    public PropertyServiceImpl() {
        properties = new Properties();
        InputStream is = getClass().getResourceAsStream(PROPERTIES_FILE);
        if (is != null) {
            try {
                properties.load(is);
            } catch (IOException e) {
                LOG.error("Could not read properties file", e);
            }
        } else {
            LOG.error("Application properties file not found");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see sr.projectx.services.PropertyService#getProperty(java.lang.String)
     */
    @Override
    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            String msg = String.format("Application property with key '%s' not found", key);
            LOG.error(msg);
        }
        return value;
    }

}
