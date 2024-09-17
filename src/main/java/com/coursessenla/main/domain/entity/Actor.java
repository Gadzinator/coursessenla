package com.coursessenla.main.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Actor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(name = "birth_date")
	private LocalDate birthDate;

	private String nationality;

	private String awards;

	@OneToMany(mappedBy = "actor", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CharacterInfo> characterInfos;

	@Override
	public String toString() {
		return "Actor{" +
				"id=" + id +
				", name='" + name + '\'' +
				", birthDate=" + birthDate +
				", nationality='" + nationality + '\'' +
				", awards='" + awards + '\'' +
				", characterInfos=" + characterInfos +
				'}';
	}
}
