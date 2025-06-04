package com.projectbackend.todosimple.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.projectbackend.todosimple.models.Task;
import com.projectbackend.todosimple.models.User;
import com.projectbackend.todosimple.repositories.TaskRepository;

public class TaskService {
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private UserService userService;
	
	public Task findById(Long id) {
		Optional<Task> task = this.taskRepository.findById(id);
		
		return task.orElseThrow(() -> new RuntimeException(
			"Task não Encontrada! Id: " + id + ", Tipo: " + Task.class.getName()
		));
	}
	
	@Transactional
	public Task create(Task obj) {
		User user = this.userService.findById(obj.getUser().getId());
		
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
			throw new RuntimeException("Não é Possível Excluir, Pois há Entidades Relacionadas!");
		}
	}
}
