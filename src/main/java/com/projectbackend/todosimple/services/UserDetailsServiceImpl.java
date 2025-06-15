package com.projectbackend.todosimple.services;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.projectbackend.todosimple.models.User;
import com.projectbackend.todosimple.repositories.UserRepository;
import com.projectbackend.todosimple.security.UserSpringSecurity;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.userRepository.findByUsername(username);
				
		if(Objects.isNull(user)) 
			throw new UsernameNotFoundException("Usuário Não Encontrado: " + username);
		
		return new UserSpringSecurity(
				user.getId(), user.getUsername(),
				user.getPassword(), user.getProfiles());
	}

}
