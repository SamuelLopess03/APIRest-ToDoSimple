package com.projectbackend.todosimple.services;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projectbackend.todosimple.models.Task;
import com.projectbackend.todosimple.models.User;
import com.projectbackend.todosimple.models.enums.ProfileEnum;
import com.projectbackend.todosimple.repositories.TaskRepository;
import com.projectbackend.todosimple.security.UserSpringSecurity;
import com.projectbackend.todosimple.services.exceptions.AuthorizationException;
import com.projectbackend.todosimple.services.exceptions.DataBindingViolationException;
import com.projectbackend.todosimple.services.exceptions.ObjectNotFoundException;

@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private UserService userService;
	
	public Task findById(Long id) {
		Task task = this.taskRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(
			"Task não Encontrada! Id: " + id + ", Tipo: " + Task.class.getName()
		));
		
		UserSpringSecurity userSpringSecurity = UserService.authenticated();
		
		if(Objects.isNull(userSpringSecurity) || !userSpringSecurity.hasRole(ProfileEnum.ADMIN)
				&& !userHasTask(userSpringSecurity, task))
			throw new AuthorizationException("Acesso Negado!");
		
		return task;
	}
	
	public List<Task> findAllByUser() {
		UserSpringSecurity userSpringSecurity = UserService.authenticated();
		
		if(Objects.isNull(userSpringSecurity))
			throw new AuthorizationException("Acesso Negado!");
		
		List<Task> tasks = this.taskRepository.findByUser_Id(userSpringSecurity.getId());
		
		return tasks;
	}
	
	@Transactional
	public Task create(Task obj) {
		UserSpringSecurity userSpringSecurity = UserService.authenticated();
		
		if(Objects.isNull(userSpringSecurity))
			throw new AuthorizationException("Acesso Negado!");
		
		User user = this.userService.findById(userSpringSecurity.getId());
		
		obj.setId(null);
		obj.setUser(user);
		
		return this.taskRepository.save(obj);
	}
	
	@Transactional
	public Task update(Task obj) {
		Task newObj = findById(obj.getId());
		
		newObj.setDescription(obj.getDescription());
		
		return this.taskRepository.save(obj);
	}
	
	public void delete(Long id) {
		findById(id);
		
		try {
			this.taskRepository.deleteById(id);
		} catch (Exception e) {
			throw new DataBindingViolationException("Não é Possível Excluir, Pois há Entidades Relacionadas!");
		}
	}
	
	private Boolean userHasTask(UserSpringSecurity userSpringSecurity, Task task) {
		return task.getUser().getId().equals(userSpringSecurity.getId());
	}
}
