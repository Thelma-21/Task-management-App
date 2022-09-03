package com.example.week8_task_manager.repository;

import com.example.week8_task_manager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(value = "SELECT * FROM tasks WHERE status = ?1 AND user_id" , nativeQuery = true)
    List<Task> listOfTasksByStatus(String Status, Long id);

    @Query(value = "UPDATE tasks SET status = ?1 where  id = ?2" , nativeQuery = true)
    boolean updateTaskByIdAndStatus(String status , Long id);

    @Query(value = "SELECT * FROM tasks WHERE user_id =?1", nativeQuery = true)
    List<Task> listOfTasksByUserId(Long id);
}
