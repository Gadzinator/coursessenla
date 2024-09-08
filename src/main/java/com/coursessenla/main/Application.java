package com.coursessenla.main;

import com.coursessenla.main.controller.ReviewController;
import com.coursessenla.main.domain.dto.MovieDto;
import com.coursessenla.main.domain.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@ComponentScan
public class Application {

	public static void main(String[] args) throws JsonProcessingException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
		final ReviewController reviewController = context.getBean(ReviewController.class);
		final MovieDto movieDtoFirst = DtoFactory.createMovieDtoFirst();
		final MovieDto movieDtoSecond = DtoFactory.createMovieDtoSecond();
		final UserDto userDtoFirst = DtoFactory.createUserDtoFirst();
		final UserDto userDtoSecond = DtoFactory.createUserDtoSecond();

		ExecutorService executorService = Executors.newFixedThreadPool(2);
		try {
			executorService.submit(() -> reviewController.save(DtoFactory.createReviewDtoFirst(userDtoFirst, movieDtoFirst)));
			executorService.submit(() -> reviewController.save(DtoFactory.createReviewDtoSecond(userDtoSecond, movieDtoSecond)));
		} finally {
			executorService.shutdown();

			try {
				if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
					executorService.shutdownNow();
				}
			} catch (InterruptedException e) {
				executorService.shutdownNow();
				Thread.currentThread().interrupt();
			}
		}
	}
}
