package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.example.demo.model.ProductoModel;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.config.client.ConfigClientFailFastException;
import org.springframework.context.ConfigurableApplicationContext;

class MsProductoFApplicationTests {

    @Test
    void startsWithRemoteConfiguration() throws IOException {
        List<String> requests = new CopyOnWriteArrayList<>();
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        try (var input = getClass().getResourceAsStream("/config-server-response.json")) {
            assertThat(input).isNotNull();
            byte[] response = input.readAllBytes();
            server.createContext("/", exchange -> {
                requests.add(exchange.getRequestURI().getPath());
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.length);
                try (var output = exchange.getResponseBody()) {
                    output.write(response);
                }
            });
        }
        server.start();

        try (ConfigurableApplicationContext context = SpringApplication.run(
                MsProductoFApplication.class,
                "--spring.main.web-application-type=none",
                "--CONFIG_SERVER_URL=http://localhost:" + server.getAddress().getPort(),
                "--CONFIG_GIT_LABEL=main")) {
            assertThat(requests).contains("/ms-producto-facturador-electronico/default/main");
            assertThat(context.getEnvironment().getProperty("config.integration.message")).isEqualTo("producto-desde-config-server");
            assertThat(context.getEnvironment().getProperty("spring.datasource.url")).startsWith("jdbc:h2:mem:");
            assertThat(context.getBean(ProductoModel.class)).isNotNull();
        } finally {
            server.stop(0);
        }
    }

    @Test
    void failsWhenRequiredRemoteConfigurationIsMissing() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        server.createContext("/", exchange -> {
            exchange.sendResponseHeaders(404, -1);
            exchange.close();
        });
        server.start();

        try {
            assertThatThrownBy(() -> {
                try (ConfigurableApplicationContext ignored = SpringApplication.run(
                        MsProductoFApplication.class,
                        "--spring.main.web-application-type=none",
                        "--CONFIG_SERVER_URL=http://localhost:" + server.getAddress().getPort(),
                        "--CONFIG_GIT_LABEL=main")) {
                    // La configuracion remota es obligatoria.
                }
            }).isInstanceOf(ConfigClientFailFastException.class);
        } finally {
            server.stop(0);
        }
    }
}
