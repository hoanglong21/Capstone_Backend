package com.capstone.project.repository;

import com.capstone.project.model.Content;
import com.capstone.project.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Integer> {
    List<Post> getPostByClassroomId(int id);
}
