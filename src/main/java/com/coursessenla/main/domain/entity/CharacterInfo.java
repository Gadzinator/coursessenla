package com.coursessenla.main.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "character_info")
public class CharacterInfo {

	@EmbeddedId
	private CharacterInfoId id;

	@ManyToOne
	@MapsId("movieId")
	@JoinColumn(name = "movie_id", nullable = false)
	private Movie movie;

	@ManyToOne
	@MapsId("actorId")
	@JoinColumn(name = "actor_id", nullable = false)
	private Actor actor;

	@Column(name = "character_name")
	private String characterName;

	@Override
	public String toString() {
		return "CharacterInfoService{" +
				"id=" + id +
				", movie=" + movie +
				", actor=" + actor +
				", characterName='" + characterName + '\'' +
				'}';
	}
}
