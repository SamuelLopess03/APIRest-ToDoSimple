package com.projectbackend.todosimple.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.projectbackend.todosimple.models.Task;
import com.projectbackend.todosimple.models.projections.TaskProjection;
import com.projectbackend.todosimple.services.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/task")
@Validated
public class TaskController {
	
	@Autowired
	private TaskService taskService;
	
	@GetMapping("/{id}")
	public ResponseEntity<Task> findById(@PathVariable Long id) {
		Task obj = this.taskService.findById(id);
		
		return ResponseEntity.ok(obj);
	}
	
	@GetMapping("/user")
	public ResponseEntity<List<TaskProjection>> findAllByUserId() {
		List<TaskProjection> objs = this.taskService.findAllByUser();
		
		return ResponseEntity.ok().body(objs);
	}
	
	@PostMapping
	@Validated
	public ResponseEntity<Void> create(@Valid @RequestBody Task obj) {
		this.taskService.create(obj);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				  .path("/{id}").buildAndExpand(obj.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping("/{id}")
	@Validated
	public ResponseEntity<Void> update(@Valid @RequestBody Task obj, @PathVariable Long id){
		obj.setId(id);
		
		this.taskService.update(obj);
		
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		this.taskService.delete(id);
		
		return ResponseEntity.noContent().build();
	}
}
