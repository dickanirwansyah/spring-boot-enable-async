package com.dicka.springbootasync.service;

import com.dicka.springbootasync.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class GithubLookUpService {

    private static final Logger logger = LoggerFactory.getLogger(GithubLookUpService.class);

    private final RestTemplate restTemplate;

    public GithubLookUpService(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = restTemplateBuilder.build();
    }

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<User> findUser(String user) throws InterruptedException{
        logger.info("Looking up "+user);
        String url = String.format("https://api.github.com/users/%s", user);
        User result = restTemplate.getForObject(url, User.class);
        // Artificial delay of 1s for demonstration purposes
        Thread.sleep(1000L);
        return CompletableFuture.completedFuture(result);
    }
}
