package com.dicka.springbootasync;

import com.dicka.springbootasync.model.User;
import com.dicka.springbootasync.service.GithubLookUpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.CompletableFuture;

@SpringBootApplication
@EnableAsync
public class SpringBootAsyncApplication implements CommandLineRunner{

	private static final Logger logger = LoggerFactory.getLogger(SpringBootAsyncApplication.class);

	@Autowired
	private GithubLookUpService githubLookUpService;

	@Bean("threadPoolTaskExecutor")
	public TaskExecutor getAsyncExecutor(){
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(20);
		executor.setMaxPoolSize(1000);
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setThreadNamePrefix("Async-");
		return executor;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAsyncApplication.class, args);
	}

	@Override
	public void run(String...args) throws Exception {

		// Start the clock

		long start = System.currentTimeMillis();

		// Kick of multiple, asynchronous lookups

		CompletableFuture<User> page1 = githubLookUpService.findUser("PivotalSoftware");

		CompletableFuture<User> page2 = githubLookUpService.findUser("CloudFoundry");

		CompletableFuture<User> page3 = githubLookUpService.findUser("Spring-Projects");

		CompletableFuture< User> page4 = githubLookUpService.findUser("RameshMF");

		// Wait until they are all done

		CompletableFuture.allOf(page1, page2, page3, page4).join();

		// Print results, including elapsed time

		logger.info("Elapsed time: " + (System.currentTimeMillis() - start));

		logger.info("--> " + page1.get());

		logger.info("--> " + page2.get());

		logger.info("--> " + page3.get());

		logger.info("--> " + page4.get());

	}

}
