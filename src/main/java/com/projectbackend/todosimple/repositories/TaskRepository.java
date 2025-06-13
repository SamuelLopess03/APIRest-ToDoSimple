package com.projectbackend.todosimple.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projectbackend.todosimple.models.Task;
import com.projectbackend.todosimple.models.projections.TaskProjection;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
	
	//@Query(value = "SELECT t FROM Task t WHERE t.user.id = :id")
	//List<Task> findByUser_Id(@Param("id") Long id);
	
	//@Query(value = "SELECT * FROM Task t WHERE t.user_id = :id", nativeQuery = true)
	//List<Task> findByUser_Id(@Param("id") Long id);
	
	List<TaskProjection> findByUser_Id(Long id);
}
