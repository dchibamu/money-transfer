package org.chibamuio.moneytransfer;

import org.chibamuio.moneytransfer.config.JaxRsApplication;
import org.chibamuio.moneytransfer.dao.AccountDao;
import org.chibamuio.moneytransfer.dao.CustomerDao;
import org.chibamuio.moneytransfer.dao.TransactionDao;
import org.chibamuio.moneytransfer.dao.impl.AccountDaoImpl;
import org.chibamuio.moneytransfer.dao.impl.CustomerDaoImpl;
import org.chibamuio.moneytransfer.dao.impl.TransactionDaoImpl;
import org.chibamuio.moneytransfer.services.AccountService;
import org.chibamuio.moneytransfer.services.CustomerService;
import org.chibamuio.moneytransfer.services.impl.AccountServiceImpl;
import org.chibamuio.moneytransfer.services.impl.CustomerServiceImpl;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;

public class MoneyTransferMain {
    private static final String HOST = "http://localhost/";
    private static final int PORT = 8888;

    private static ResourceConfig getConfig(){
        ResourceConfig config = new ResourceConfig().packages(true, "org.chibamuio.moneytransfer");
        //tell jersey to use the jackson libraries for JSON parsing and serialization
        config.register(JacksonFeature.class);
        //config.register(JacksonJsonProvider.class);
        config.register(new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bind(AccountServiceImpl.class).to(AccountService.class).in(RequestScoped.class);
                        bind(CustomerServiceImpl.class).to(CustomerService.class).in(RequestScoped.class);
                        bind(AccountDaoImpl.class).to(AccountDao.class).in(RequestScoped.class);
                        bind(CustomerDaoImpl.class).to(CustomerDao.class).in(RequestScoped.class);
                        bind(TransactionDaoImpl.class).to(TransactionDao.class).in(RequestScoped.class);

                    }
                });
        return config;
    }

    public static HttpServer bootstrapHttpServer(ResourceConfig resourceConfig) {
        URI baseUri = UriBuilder.fromUri(HOST).port(PORT).build();
        return GrizzlyHttpServerFactory.createHttpServer(baseUri, resourceConfig, false);
    }

    public static void main( String[] args ) throws IOException {
        final HttpServer server = bootstrapHttpServer(new JaxRsApplication());
        server.start();
        System.out.println(String.format("Hit enter key to stop server"));
        System.in.read();
        server.shutdownNow();
    }
}
