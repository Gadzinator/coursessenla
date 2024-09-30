package com.coursessenla.main.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		final String stackTrace = getStackTrace(e);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(stackTrace);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception e) {
		final String stackTrace = getStackTrace(e);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(stackTrace);
	}

	@ExceptionHandler(ActorNotFoundException.class)
	public ResponseEntity<?> handleActorNotFoundException(ActorNotFoundException exception, WebRequest request) {
		return handleGlobal(exception, request, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(CharacterInfoNotFoundException.class)
	public ResponseEntity<?> handleCharacterInfoNotFoundException(CharacterInfoNotFoundException exception, WebRequest request) {
		return handleGlobal(exception, request, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DirectorNotFoundException.class)
	public ResponseEntity<?> handleDirectorNotFoundException(DirectorNotFoundException exception, WebRequest request) {
		return handleGlobal(exception, request, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(GenreNotFoundException.class)
	public ResponseEntity<?> handleGenreNotFoundException(GenreNotFoundException exception, WebRequest request) {
		return handleGlobal(exception, request, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MovieNotFoundException.class)
	public ResponseEntity<?> handleMovieNotFoundException(MovieNotFoundException exception, WebRequest request) {
		return handleGlobal(exception, request, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(PlayListNotFoundException.class)
	public ResponseEntity<?> handlePlayListNotFoundException(PlayListNotFoundException exception, WebRequest request) {
		return handleGlobal(exception, request, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ReviewNotFoundException.class)
	public ResponseEntity<?> handleReviewNotFoundException(ReviewNotFoundException exception, WebRequest request) {
		return handleGlobal(exception, request, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException exception, WebRequest request) {
		return handleGlobal(exception, request, HttpStatus.NOT_FOUND);
	}

	private ResponseEntity<?> handleGlobal(Exception exception, WebRequest request, HttpStatus httpStatus) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));

		return new ResponseEntity<>(errorDetails, httpStatus);
	}

	private String getStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);

		return sw.toString();
	}
}
