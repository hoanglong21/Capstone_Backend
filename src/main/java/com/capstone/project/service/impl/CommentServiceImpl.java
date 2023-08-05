package com.capstone.project.service.impl;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.*;
import com.capstone.project.repository.CommentRepository;
import com.capstone.project.service.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {


    @PersistenceContext
    private EntityManager em;
    private final CommentRepository commentRepository;

    private final UserService userService;
    private final CommentTypeService commentTypeService;
    private final PostService postService;
    private final TestService testService;
    private final StudySetService studySetService;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserService userService, CommentTypeService commentTypeService, PostService postService, TestService testService, StudySetService studySetService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.commentTypeService = commentTypeService;
        this.postService = postService;
        this.testService = testService;
        this.studySetService = studySetService;
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }


    @Override
    public List<Comment> getAllComment() {
        return commentRepository.findAll();
    }

    @Override
    public List<Comment> getAllCommentByPostId(int id) {
        return commentRepository.getCommentByPostId(id);
    }

    @Override
    public List<Comment> getAllCommentByRootId(int id) {
        return commentRepository.getCommentByRootId(id);
    }

    @Override
    public List<Comment> getAllCommentByStudySetId(int id) {
        return commentRepository.getCommentByStudySetId(id);
    }

    @Override
    public List<Comment> getAllCommentByTestId(int id) {
        return commentRepository.getCommentByTestId(id);
    }

    @Override
    public List<Comment> getAllCommentByAssignmentId(int id) {
        return commentRepository.getCommentByAssignmentId(id);
    }

    @Override
    public List<Comment> getAllCommentBySubmisionId(int id) {
        return commentRepository.getCommentBySubmissionId(id);
    }


    @Override
    public Comment getCommentById(int id) throws ResourceNotFroundException {
        Comment comment = commentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFroundException("Comment not exist with id:" + id));
        return comment;
    }

    @Override
    public Comment createComment(Comment comment) {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        Date date = localDateTimeToDate(localDateTime);
        comment.setCreated_date(date);
        return commentRepository.save(comment);
    }

    @Override
    public Comment updateComment(Comment comment, int id) throws ResourceNotFroundException {
        Comment comment_new = commentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFroundException("Comment not exist with id:" + id));
        comment_new.setContent(comment.getContent());
        return commentRepository.save(comment_new);
    }

    @Override
    public Boolean deleteComment(int id) throws ResourceNotFroundException {
        Comment comment = commentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFroundException("Comment not exist with id:" + id));
        commentRepository.delete(comment);
        return true;
    }

    @Override
    public Map<String, Object> getFilterComment( String search, String author,String direction, int typeid,int postid, int testid,int studysetid,int assignmentid,int submissionid, int rootid, int page, int size) throws ResourceNotFroundException {
        int offset = (page - 1) * size;

        String query = "SELECT comment.* FROM comment inner join user u on u.id = author_id WHERE 1=1 ";

        Map<String, Object> parameters = new HashMap<>();

        if (author != null && !author.isEmpty()) {
            query += " AND u.username LIKE :authorname";
            parameters.put("authorname", author);
        }

        if (search != null && !search.isEmpty()) {
            query += " AND content LIKE :search ";
            parameters.put("search", "%" + search + "%");
        }

        if (postid != 0) {
            query += " AND post_id = :postId";
            parameters.put("postId", postid);
        }

        if (studysetid != 0) {
            query += " AND studyset_id = :studysetId";
            parameters.put("studysetId",  studysetid);
        }


        if (testid != 0) {
            query += " AND test_id = :testId";
            parameters.put("testId", testid);
        }

        if (assignmentid != 0) {
            query += " AND assignment_id = :assignmentId";
            parameters.put("assignmentId", assignmentid);
        }

        if (submissionid != 0) {
            query += " AND submission_id = :submissionId";
            parameters.put("submissionId", submissionid);
        }

        if (rootid != 0) {
            query += " AND root_id = :rootId";
            parameters.put("rootId", rootid);
        }

        if (typeid != 0) {
            query += " AND type_id = :typeId";
            parameters.put("typeId", typeid);
        }

//        if (type != null && !type.isEmpty()) {
//            query += " AND type_id = :typeId";
//            CommentType commentType = commentTypeService.getCommentTypeByName(type);
//            parameters.put("typeId", commentType.getId());
//        }


        String direct = "desc";
        if(direction != null && !direction.isEmpty()){
            if (direction.equalsIgnoreCase("asc")) {
                direct = "asc";
            }
        }
        query += " ORDER BY created_date " + " " + direct;

        Query q = em.createNativeQuery(query, Comment.class);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }

        int totalItems = q.getResultList().size();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        q.setFirstResult(offset);
        q.setMaxResults(size);

        List<Comment> resultList = q.getResultList();

        Map<String, Object> response = new HashMap<>();
        response.put("list", resultList);
        response.put("currentPage", page);
        response.put("totalPages", totalPages);
        response.put("totalItems", totalItems);
        return response;
    }
}
