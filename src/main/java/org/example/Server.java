package org.example;

import org.jboss.resteasy.core.AsynchronousDispatcher;
import org.jboss.resteasy.core.ResourceMethodRegistry;
import org.jboss.resteasy.core.ResteasyDeploymentImpl;
import org.jboss.resteasy.core.providerfactory.ResteasyProviderFactoryImpl;
import org.jboss.resteasy.plugins.server.netty.NettyJaxrsServer;

public class Server {

    private final NettyJaxrsServer instance;

    Server(String hostname, int port) {
        ResteasyProviderFactoryImpl providerFactory = new ResteasyProviderFactoryImpl();
        ResourceMethodRegistry registry = new ResourceMethodRegistry(providerFactory);
        AsynchronousDispatcher dispatcher = new AsynchronousDispatcher(providerFactory, registry);
        ResteasyDeploymentImpl deployment = new ResteasyDeploymentImpl();

        instance = new NettyJaxrsServer();
        instance.setHostname(hostname);
        instance.setPort(port);
        instance.setDeployment(deployment);

        deployment.setApplication(new RestEasyApplication());
        deployment.setProviderFactory(providerFactory);
        deployment.setRegistry(registry);
        deployment.setDispatcher(dispatcher);
        deployment.start();
    }

    public void start() {
        instance.start();
    }

    public static void main(String[] args) {
        new Server("0.0.0.0", 9999).start();
    }

}


