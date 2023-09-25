package org.example;

import org.jboss.resteasy.core.interception.jaxrs.SuspendableContainerRequestContext;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AsyncFilter implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AsyncFilter.class);

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public void filter(ContainerRequestContext requestContext) {
        SuspendableContainerRequestContext suspendableCtx = (SuspendableContainerRequestContext) requestContext;
        suspendableCtx.suspend();
        executorService.submit(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                logger.error("Error while waiting", e);
                suspendableCtx.resume(e);
            }

            logger.debug("Resuming call from async filter");
            suspendableCtx.resume();
        });
    }
}
