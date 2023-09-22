package org.example;

import org.jboss.resteasy.core.interception.jaxrs.SuspendableContainerRequestContext;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AsyncFilter implements ContainerRequestFilter {

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public void filter(ContainerRequestContext requestContext) {
        SuspendableContainerRequestContext suspendableCtx = (SuspendableContainerRequestContext) requestContext;
        suspendableCtx.suspend();
        executorService.submit(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                suspendableCtx.resume(e);
            }

            System.out.println("Resuming call from async filter");
            suspendableCtx.resume();
        });
    }
}
