package org.example;


import javax.ws.rs.core.Application;
import java.util.Set;

public class RestEasyApplication extends Application {

    @Override
    public Set<Object> getSingletons() {
        return Set.of(
                new AsyncResource()
                // Comment out AsyncFilter to test that "/resource/async" works w/o it
                ,new AsyncFilter()
        );

    }

}
