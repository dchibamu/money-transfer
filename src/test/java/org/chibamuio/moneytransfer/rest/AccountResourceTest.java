package org.chibamuio.moneytransfer.rest;

import org.chibamuio.moneytransfer.services.AccountService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.ws.rs.core.Application;
@DisplayName("<=== AccountResource ===>")
class AccountResourceTest extends JerseyTest {

    @Mock
    private AccountService accountServiceMock;
    @Override
    protected Application configure() {
        ResourceConfig config = new ResourceConfig(AccountResource.class);
        config.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(accountServiceMock).to(AccountService.class);
            }
        });
        return config;
    }

    @Test
    void testOpenAccount() {

    }
}
