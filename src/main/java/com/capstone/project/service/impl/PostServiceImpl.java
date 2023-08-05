package com.capstone.project.service.impl;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.*;
import com.capstone.project.model.Class;
import com.capstone.project.repository.AttachmentRepository;
import com.capstone.project.repository.CommentRepository;
import com.capstone.project.repository.PostRepository;
import com.capstone.project.service.ClassService;
import com.capstone.project.service.PostService;
import com.capstone.project.service.UserService;
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
public class PostServiceImpl implements PostService {

    @PersistenceContext
    private EntityManager em;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final AttachmentRepository attachmentRepository;
    private final UserService userService;
    private final ClassService classService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, CommentRepository commentRepository, AttachmentRepository attachmentRepository, UserService userService, ClassService classService) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.attachmentRepository = attachmentRepository;
        this.userService = userService;
        this.classService = classService;
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public List<Post> getAllPost() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> getAllPostByClassId(int id) {
        return postRepository.getPostByClassroomId(id);
    }

    @Override
    public Post getPostById(int id) throws ResourceNotFroundException {
        Post post = postRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFroundException("Post not exist with id:" + id));
             return post;
    }

    @Override
    public Post createPost(Post post) {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        Date date = localDateTimeToDate(localDateTime);
        post.setCreated_date(date);

        Post savedPost = postRepository.save(post);

//        if (file_names != null && urls != null && file_types != null && type != 0) {
//            int numOfAttachments = Math.min(file_names.size(), Math.min(urls.size(), file_types.size()));
//            for (int i = 0; i < numOfAttachments; i++) {
//                String file_name = file_names.get(i);
//                String url = urls.get(i);
//                String file_type = file_types.get(i);
//
//                Attachment attachment = new Attachment();
//                attachment.setFile_name(file_name);
//                attachment.setFile_url(url);
//                attachment.setFile_type(file_type);
//                AttachmentType attachmentType = new AttachmentType();
//                attachmentType.setId(type);
//                attachment.setAttachmentType(attachmentType);
//                attachment.setPost(savedPost);
//
//                attachmentRepository.save(attachment);
//            }
//        }

        return savedPost;
    }

    @Override
    public Post updatePost(Post post, int id) throws ResourceNotFroundException {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Post does not exist with id: " + id));
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        Date date = localDateTimeToDate(localDateTime);
        existingPost.setModified_date(date);
        existingPost.setContent(post.getContent());

        List<Attachment> attachments = attachmentRepository.getAttachmentByPostId(existingPost.getId());

        for (Attachment attachment : attachments) {
            attachmentRepository.delete(attachment);
        }

//        if (file_names != null && urls != null && file_types != null && type != 0) {
//            int numOfAttachments = Math.min(file_names.size(), Math.min(urls.size(), file_types.size()));
//            for (int i = 0; i < numOfAttachments; i++) {
//                String file_name = file_names.get(i);
//                String url = urls.get(i);
//                String file_type = file_types.get(i);
//
//                Attachment attachment = new Attachment();
//                attachment.setFile_name(file_name);
//                attachment.setFile_url(url);
//                attachment.setFile_type(file_type);
//                AttachmentType attachmentType = new AttachmentType();
//                attachmentType.setId(type);
//                attachment.setAttachmentType(attachmentType);
//                attachment.setPost(existingPost);
//
//                attachmentRepository.save(attachment);
//            }
//        }

        return postRepository.save(existingPost);
    }

    @Override
    public Boolean deletePost(int id) throws ResourceNotFroundException {
        Post post = postRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFroundException("Post not exist with id: " + id));
        for(Comment comment : commentRepository.getCommentByPostId(post.getId())){
            commentRepository.delete(comment);
        }
        for(Attachment attachment : attachmentRepository.getAttachmentByPostId(post.getId())){
            attachmentRepository.delete(attachment);
        }
        postRepository.delete(post);
        return true;
    }

    @Override
    public Map<String, Object> getFilterPost(String search, String author,String fromCreated,String toCreated,String sortBy,String direction, int classid, int page, int size) throws ResourceNotFroundException {
        int offset = (page - 1) * size;

        String query ="SELECT p.* FROM post p inner join user u on u.id = p.author_id where 1=1";

        Map<String, Object> parameters = new HashMap<>();

        if (author != null && !author.isEmpty()) {
            query += " AND u.username LIKE :authorname";
            parameters.put("authorname", author);
        }

        if (search != null && !search.isEmpty()) {
            query += " AND p.content LIKE :search ";
            parameters.put("search", "%" + search + "%");
        }

        if (classid != 0) {
            query += " AND p.class_id = :classId";
            parameters.put("classId", classid);
        }

        if (fromCreated != null && !fromCreated.equals("")) {
            query += " AND DATE(p.created_date) >= :fromCreated";
            parameters.put("fromCreated", fromCreated);
        }
        if (toCreated != null && !toCreated.equals("")) {
            query += " AND DATE(p.created_date) <= :toCreated";
            parameters.put("toCreated", toCreated);
        }

        query += " ORDER BY p." + sortBy + " " + direction;


        Query q = em.createNativeQuery(query, Post.class);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }

        int totalItems = q.getResultList().size();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        q.setFirstResult(offset);
        q.setMaxResults(size);

        List<Post> resultList = q.getResultList();

        Map<String, Object> response = new HashMap<>();
        response.put("list", resultList);
        response.put("currentPage", page);
        response.put("totalPages", totalPages);
        response.put("totalItems", totalItems);

        return response;

    }
}
