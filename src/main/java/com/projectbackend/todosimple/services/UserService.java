package com.projectbackend.todosimple.services;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projectbackend.todosimple.models.User;
import com.projectbackend.todosimple.models.enums.ProfileEnum;
import com.projectbackend.todosimple.repositories.UserRepository;
import com.projectbackend.todosimple.services.exceptions.DataBindingViolationException;
import com.projectbackend.todosimple.services.exceptions.ObjectNotFoundException;

@Service
public class UserService {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;

	
	public User findById(Long id) {
		Optional<User> user = this.userRepository.findById(id);
		
		return user.orElseThrow(() -> new ObjectNotFoundException(
			"Usuário não Encontrado! Id: " + id + ", Tipo: " + User.class.getName()
		));
	}
	
	@Transactional
	public User create(User obj) {
		obj.setId(null);
		obj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
		obj.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
		
		obj = this.userRepository.save(obj);
				
		return obj;
	}
	
	@Transactional
	public User update(User obj) {
		User newObj = findById(obj.getId());

		newObj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
		
		return this.userRepository.save(newObj);
	}
	
	public void delete(Long id) {
		findById(id);
		
		try {
			this.userRepository.deleteById(id);
		} catch (Exception e) {
			throw new DataBindingViolationException("Não é Possível Excluir, Pois há Entidades Relacionadas!");
		}
	}
}
