package com.coursessenla.main.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Playlist {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToMany(mappedBy = "playlists", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<Movie> movies;

	@Override
	public String toString() {
		return "Playlist{" +
				"id=" + id +
				", name='" + name + '\'' +
				", user=" + user +
				", movies=" + movies +
				'}';
	}
}
