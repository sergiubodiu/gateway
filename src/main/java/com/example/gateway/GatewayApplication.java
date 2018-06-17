package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

	@Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
	    return builder.routes()
            .route("path_route", r -> r.path("/get")
                .filters(f ->
                    f.addRequestHeader("X-SpringOne", "Awesome!"))
                .uri("http://httpbin.org:80"))
            .route("host_route", r -> r.host("*.myhost.org")
                .filters(f ->
                    f.addRequestHeader("AnotherOne", "bitesthedust"))
                .uri("http://httpbin.org:80"))
			.route(r -> r.host("*.rewrite.org").and().path("/foo/**")
                .filters(f ->
                    f.rewritePath("/foo/(?<segment>.*)", "/${segment}"))
                .uri("http://httpbin.org:80"))
            .route(r -> r.host("*.setpath.org").and().path("/foo/{segment}")
                .filters(f ->
                    f.setPath("/{segment}"))
                .uri("http://httpbin.org:80"))
            .route("hystrix_route", r -> r.host("*.hystrix.org")
                .filters(f ->
                    f.hystrix(config -> config.setName("slowcmd")))
                .uri("http://httpbin.org:80"))
            .route("hystrix_fallback_route", r -> r.host("*.hystrixfallback.org")
                .filters(f ->
                    f.hystrix(config ->
                            config.setName("fallbackcmd").setFallbackUri("forward:/hystrixfallback")))
                .uri("http://httpbin.org:80"))
//                .uri("lb://myservice")) //ribbon
//			.route("limit_route", r -> r
//					.host("*.limited.org").and().path("/anything/**")
//					.filters(f -> f.requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter())))
//					.uri("http://httpbin.org"))
//            .route("websocket_route", r -> r.path("/echo")
//                .uri("ws://localhost:9000"))
            .build();
    }

//	@Bean
//	RedisRateLimiter redisRateLimiter() {
//		return new RedisRateLimiter(1, 2);
//	}

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}
}
