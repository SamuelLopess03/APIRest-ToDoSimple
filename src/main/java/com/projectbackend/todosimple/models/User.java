package com.projectbackend.todosimple.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.projectbackend.todosimple.models.enums.ProfileEnum;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = User.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
	public static final String TABLE_NAME = "user";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private Long id;
	
	@Column(name = "username", length = 100, nullable = false, unique = true)
	@NotBlank
	@Size(min = 2, max = 100)
	private String username;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(name = "password", length = 60, nullable = false)
	@NotBlank
	@Size(min = 8, max = 60)
	private String password;
	
	@OneToMany(mappedBy = "user")
	@JsonProperty(access = Access.WRITE_ONLY)
	private List<Task> tasks = new ArrayList<Task>();
	
	@ElementCollection(fetch = FetchType.EAGER)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@CollectionTable(name = "user_profiles")
	@Column(name = "profile", nullable = false)
	private Set<Integer> profiles = new HashSet<Integer>();
	
	public Set<ProfileEnum> getProfiles(){
		return this.profiles.stream()
				.map(x -> ProfileEnum.toEnum(x)).collect(Collectors.toSet());
	}
	
	public void addProfile(ProfileEnum profileEnum) {
		this.profiles.add(profileEnum.getCode());
	}
		
}
