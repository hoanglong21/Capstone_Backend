package com.capstone.project.repository;

import com.capstone.project.model.Comment;
import com.capstone.project.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    List<Comment> getCommentByPostId(int id);
    List<Comment> getCommentByRootId(int id);
    List<Comment> getCommentByStudySetId(int id);
    List<Comment> getCommentByTestId(int id);

    List<Comment> getCommentByAssignmentId(int id);

    List<Comment> getCommentBySubmissionId(int id);
}
