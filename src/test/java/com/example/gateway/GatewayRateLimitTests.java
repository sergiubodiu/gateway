package com.example.gateway;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.test.web.reactive.server.FluxExchangeResult;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GatewayRateLimitTests {

    @LocalServerPort
    int port;

//    @Test()
	public void rateLimiterWorks() {
        WebTestClient authClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port)
				.filter(basicAuthentication("user", "password"))
				.build();

		boolean wasLimited = false;

		for (int i = 0; i < 20; i++) {
			FluxExchangeResult<Map> result = authClient.get()
					.uri("/anything/1")
					.header("Host", "www.limited.org")
					.exchange()
					.returnResult(Map.class);
			if (result.getStatus().equals(HttpStatus.TOO_MANY_REQUESTS)) {
				System.out.println("Received result: "+result);
				wasLimited = true;
				break;
			}
		}

		assertThat(wasLimited)
				.as("A HTTP 429 TOO_MANY_REQUESTS was not received")
				.isTrue();

	}
}
