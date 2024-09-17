package com.coursessenla.main.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class CharacterInfoId implements Serializable {

	@Column(name = "movie_id")
	private Long movieId;

	@Column(name = "actor_id")
	private Long actorId;
}
