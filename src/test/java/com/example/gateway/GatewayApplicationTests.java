package com.example.gateway;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GatewayApplicationTests {

	@LocalServerPort
	int port;
	private WebTestClient client;

	@Before
	public void setup() {
		client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void pathRouteWorks() {
		client.get().uri("/get")
				.exchange()
				.expectStatus().isOk()
				.expectBody(Map.class)
				.consumeWith(result -> {
					assertThat(result.getResponseBody()).isNotEmpty();
				});
	}

	@Test
	@SuppressWarnings("unchecked")
	public void hostRouteWorks() {
		client.get().uri("/headers")
				.header("Host", "www.myhost.org")
				.exchange()
				.expectStatus().isOk()
				.expectBody(Map.class)
				.consumeWith(result -> {
					assertThat(result.getResponseBody()).isNotEmpty();
				});
	}

	@Test
	@SuppressWarnings("unchecked")
	public void rewriteRouteWorks() {
		client.get().uri("/foo/get")
				.header("Host", "www.rewrite.org")
				.exchange()
				.expectStatus().isOk()
				.expectBody(Map.class)
				.consumeWith(result -> {
					assertThat(result.getResponseBody()).isNotEmpty();
				});
	}

	@Test
	@SuppressWarnings("unchecked")
	public void hystrixRouteWorks() {
		client.get().uri("/delay/5")
				.header("Host", "www.hystrix.org")
				.exchange()
				.expectStatus().isEqualTo(HttpStatus.GATEWAY_TIMEOUT);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void hystrixFallbackRouteWorks() {
		client.get().uri("/delay/5")
				.header("Host", "www.hystrixfallback.org")
				.exchange()
				.expectStatus().isOk()
				.expectBody(String.class).isEqualTo("This is a fallback");
	}

}
