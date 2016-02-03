package eu.cloudopting.domain.util;

import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.hibernate4.encryptor.HibernatePBEEncryptorRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class DatabaseEncryptionConfiguration  implements EnvironmentAware{
	
	private final Logger log = LoggerFactory.getLogger(DatabaseEncryptionConfiguration.class);
	
	public static final String STRING_ENCRYPTOR_NAME= "HibernateStringEncryptor";
	private PBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
    private HibernatePBEEncryptorRegistry registry = HibernatePBEEncryptorRegistry.getInstance();
    private RelaxedPropertyResolver propertyResolver;
    private Environment env;
    
    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
        this.propertyResolver = new RelaxedPropertyResolver(env, "jasypt.encriptor.");
        this.encryptor();
    }
    
    /**
     * Configure the String Encryptor with the password 
     * that SHOULD be retrieved from a System VARIABLE
     * See the file "application.yml"
     * 
     * At the moment, the password is always null.
     * @return
     */
    public PBEStringEncryptor encryptor(){
    	String pwd = propertyResolver.getProperty("password");
    	if (pwd!=null){
    		encryptor.setPassword(pwd);
    	}else{
    		log.error("No Encryption Password defined!");
    	}
    	
        registry.registerPBEStringEncryptor(DatabaseEncryptionConfiguration.STRING_ENCRYPTOR_NAME, encryptor);
        return registry.getPBEStringEncryptor(STRING_ENCRYPTOR_NAME);
    }
    
}
