package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Class;
import com.capstone.project.model.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface PostService {

    List<Post> getAllPost();

    List<Post> getAllPostByClassId(int id);

    Post getPostById(int id) throws ResourceNotFroundException;

    Post createPost(Post post);

//    Post createPost(Post post, List<String> file_names, int type, List<String> urls, List<String> file_types);

//    Post updatePost(Post posts, int id, List<String> files, int type, List<String> urls, List<String> file_types) throws ResourceNotFroundException;

    Post updatePost(Post post, int id) throws ResourceNotFroundException;

    Boolean deletePost(int id) throws ResourceNotFroundException;

    Map<String, Object> getFilterPost(String search, String author,String fromCreated,String toCreated,String sortBy, String direction, int classid, int page, int size) throws ResourceNotFroundException;
}
