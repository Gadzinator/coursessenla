package com.coursessenla.main.exception;

public class PlayListNotFoundException extends RuntimeException {

	public PlayListNotFoundException() {
		super("No playlists were found");
	}

	public PlayListNotFoundException(long id) {
		super(String.format("Playlist with id %d was not found", id));
	}
}
