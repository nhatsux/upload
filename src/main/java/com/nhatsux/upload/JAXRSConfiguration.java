package com.nhatsux.upload;

import com.nhatsux.upload.controllers.FilesControllers;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;

/**
 * Configures JAX-RS for the application.
 * @author Juneau
 */
@ApplicationPath("upload")
public class JAXRSConfiguration extends Application {
    private Set<Object> singletons = new HashSet<Object>();
    
    public JAXRSConfiguration(){
        singletons.add(new FilesControllers());
        CorsFilter cors = new CorsFilter();
        cors.getAllowedOrigins().add("*");
       
        singletons.add(cors);
    }
    
    @Override
    public Set<Object> getSingletons(){
        return singletons;
    }
}
