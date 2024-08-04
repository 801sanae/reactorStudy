package com.kmy.study.mono;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

/*
 * Mono는 0개 또는 1개를 Emit
 * Http req / resp 사용하기 적합한 publisher 타입.
 */
public class MonoExample01 {

    public static void main(String[] args) {
        URI workTimeUri = UriComponentsBuilder.newInstance().scheme("http")
                .host("worldtimeapi.org")
                .port(80)
                .path("/api/timezone/Asia/Seoul")
                .build()
                .encode()
                .toUri();

        ObjectMapper objectMapper = new ObjectMapper();

        WebClient webClient = WebClient.builder()
                        .build();

        Mono.just(webClient
                        .get()
                        .uri(workTimeUri)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block())
                .map(res -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(res);
                        System.out.println(jsonNode.toString());
                        return jsonNode.path("datetime");
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .subscribe(
                        data -> {
                            System.out.println("onNext emit:: " + data);
                        },
                        error -> {
                            System.out.println("error :: " + error.getMessage());
                        },
                        () ->{
                            System.out.println("onComplete emit ");
                        }
                );






    }
}
