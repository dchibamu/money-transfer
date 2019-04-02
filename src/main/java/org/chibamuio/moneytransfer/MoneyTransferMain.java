package org.chibamuio.moneytransfer;

import org.chibamuio.moneytransfer.config.JaxRsApplication;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import static org.chibamuio.moneytransfer.util.AppConstants.HOST;
import static org.chibamuio.moneytransfer.util.AppConstants.PORT;

public class MoneyTransferMain {
    private static URI baseUri;
    public static HttpServer bootstrapHttpServer(ResourceConfig resourceConfig) {
        baseUri = UriBuilder.fromUri(HOST).port(PORT).build();
        return GrizzlyHttpServerFactory.createHttpServer(baseUri, resourceConfig, false);
    }

    public static void main( String[] args ) throws IOException {
        final HttpServer server = bootstrapHttpServer(new JaxRsApplication());
        server.start();
        //System.out.println(String.format("Application wadl: %s", baseUri+"money-transfer/application.wadl"));
        System.out.println(String.format("Hit enter key to stop server"));
        System.in.read();
        server.shutdownNow();
    }
}
