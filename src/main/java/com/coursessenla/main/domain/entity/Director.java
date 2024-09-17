package com.coursessenla.main.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@NamedEntityGraph(name = "graph.Director",
		attributeNodes = @NamedAttributeNode("movies"))
public class Director {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(name = "birth_date")
	private LocalDate birthDate;

	private String nationality;

	private String awards;

	@ManyToMany(mappedBy = "directors")
	private List<Movie> movies;

	@Override
	public String toString() {
		return "Director{" +
				"id=" + id +
				", name='" + name + '\'' +
				", birthDate=" + birthDate +
				", nationality='" + nationality + '\'' +
				", awards='" + awards + '\'' +
				", movies=" + movies +
				'}';
	}
}
