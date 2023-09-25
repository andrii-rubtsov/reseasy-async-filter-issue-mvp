package org.example;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


@Path("/resource")
public class AsyncResource {

    private static final Logger logger = LoggerFactory.getLogger(AsyncResource.class);

    private final AtomicLong counter = new AtomicLong();

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public AsyncResource() {
        logger.info("Resource created");
    }

    /**
     * Works correctly, when {@link AsyncFilter} is disabled.
     * Raises 400 BAD REQUEST, when the async filter is enabled.
     */
    @Path("/async")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public void async(@Suspended final AsyncResponse response) {
        logger.info("Async handler called");
        executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                response.resume(e);
            }
            response.resume(String.format("Async response %s\n", counter.getAndIncrement()));
        });
        logger.info("Async handler ended");
    }

    @Path("/sync")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response sync() throws ExecutionException, InterruptedException {
        logger.info("Sync handler started");
        return executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                return Response.serverError().build();
            }
            return Response.ok(
                    String.format("Sync response %s\n", counter.getAndIncrement())
            ).build();
        }).get();
    }

}
