package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.*;
import com.capstone.project.model.Class;
import com.capstone.project.repository.AnswerRepository;
import com.capstone.project.repository.CommentRepository;
import com.capstone.project.service.impl.AnswerServiceImpl;
import com.capstone.project.service.impl.CommentServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommentSerivceTest {


    @Mock
    private EntityManager em;
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentServiceImpl;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Order(1)
    @Test
    void testGetAllComment() {
        Comment comment = Comment.builder()
                .content("Hello guys")
                .build();
        List<Comment> comments = List.of(comment);
        when(commentRepository.findAll()).thenReturn(comments);
        assertThat(commentServiceImpl.getAllComment().size()).isGreaterThan(0);
    }

    @Order(2)
    @Test
    void testGetAllCommentByPostId() {
        List<Comment> comment = new ArrayList<>();
        Comment comment1 = Comment.builder().content("Hello guys").build();
        Comment comment2 = Comment.builder().content("Hello guys again").build();
        comment.add(comment1);
        comment.add(comment2);

        when(commentRepository.getCommentByPostId(any(Integer.class))).thenReturn(comment);
        List<Comment> retrievedComments = commentServiceImpl.getAllCommentByPostId(1);
        assertThat(retrievedComments).isEqualTo(comment);
    }

    @Order(3)
    @Test
    void testGetAllCommentByRootId() {
        List<Comment> comment = new ArrayList<>();
        Comment comment1 = Comment.builder().content("Hello guys").build();
        Comment comment2 = Comment.builder().content("Hello guys again").build();
        comment.add(comment1);
        comment.add(comment2);

        when(commentRepository.getCommentByRootId(any(Integer.class))).thenReturn(comment);
        List<Comment> retrievedComments = commentServiceImpl.getAllCommentByRootId(1);
        assertThat(retrievedComments).isEqualTo(comment);
    }

    @Order(4)
    @Test
    void testGetAllCommentByStudySetId() {
        List<Comment> comment = new ArrayList<>();
        Comment comment1 = Comment.builder().content("Hello guys").build();
        Comment comment2 = Comment.builder().content("Hello guys again").build();
        comment.add(comment1);
        comment.add(comment2);

        when(commentRepository.getCommentByStudySetId(any(Integer.class))).thenReturn(comment);
        List<Comment> retrievedComments = commentServiceImpl.getAllCommentByStudySetId(1);
        assertThat(retrievedComments).isEqualTo(comment);
    }

    @Order(5)
    @Test
    void testGetAllCommentByTestId() {
        List<Comment> comment = new ArrayList<>();
        Comment comment1 = Comment.builder().content("Hello guys").build();
        Comment comment2 = Comment.builder().content("Hello guys again").build();
        comment.add(comment1);
        comment.add(comment2);

        when(commentRepository.getCommentByTestId(any(Integer.class))).thenReturn(comment);
        List<Comment> retrievedComments = commentServiceImpl.getAllCommentByTestId(1);
        assertThat(retrievedComments).isEqualTo(comment);
    }

    @Order(6)
    @Test
    void testGetCommentById() {

        Comment comment = Comment.builder()
                .content("Hello guys")
                .build();
        when(commentRepository.findById(any())).thenReturn(Optional.ofNullable(comment));
        try{
            Comment getComment = commentServiceImpl.getCommentById(1);
            assertThat(getComment).isEqualTo(comment);
        }catch (ResourceNotFroundException e){
            e.printStackTrace();
        }
    }

    @Order(7)
    @ParameterizedTest(name = "index => userId={0}, testId={1},studysetId{2},rootId{3}, postId{4}, typeId{5},content{6},created_date{7}")
    @CsvSource({
            "1,1,1,1,1,1,Winter is coming,2023-3-5 ",
            "2,2,2,2,2,2,Summer time, 2023-3-5 "
    })
    public void testCreateComment(int userId, int testId,int studysetId,int rootId,int postId,int typeId,String content, String created_date){
        try{

            Comment comment = Comment.builder()
                    .user(User.builder().id(userId).build())
                    .test(com.capstone.project.model.Test.builder().id(testId).build())
                    .studySet(StudySet.builder().id(studysetId).build())
                    .root(Comment.builder().id(rootId).build())
                    .post(Post.builder().id(postId).build())
                    .commentType(CommentType.builder().id(typeId).build())
                    .content(content)
                    .created_date(dateFormat.parse(created_date))
                    .build();

            when(commentRepository.save(any())).thenReturn(comment);

            Comment createdcomment = commentServiceImpl.createComment(comment);
            assertThat(comment).isEqualTo(createdcomment);
        }catch (Exception e){
            e.printStackTrace();
        }


}

    @Order(8)
    @ParameterizedTest(name = "index => userId={0}, testId={1},studysetId{2},rootId{3}, postId{4}, typeId{5},content{6},created_date{7}")
    @CsvSource({
            "1,1,1,1,1,1,Winter is coming,2023-3-5 ",
            "2,2,2,2,2,2,Summer time, 2023-3-5 "
    })
    public void testUpdateComment(int userId, int testId,int studysetId,int rootId,int postId,int typeId,String content, String created_date){
        try{

            Comment comment_new = Comment.builder()
                    .commentType(CommentType.builder().id(typeId).build())
                    .content("Forcus")
                    .build();


            Comment comment = Comment.builder()
                    .user(User.builder().id(userId).build())
                    .test(com.capstone.project.model.Test.builder().id(testId).build())
                    .studySet(StudySet.builder().id(studysetId).build())
                    .root(Comment.builder().id(rootId).build())
                    .post(Post.builder().id(postId).build())
                    .commentType(CommentType.builder().id(typeId).build())
                    .content(content)
                    .created_date(dateFormat.parse(created_date))
                    .build();

            when(commentRepository.findById(any())).thenReturn(Optional.ofNullable(comment_new));
            when(commentRepository.save(any())).thenReturn(comment);

            Comment created_comment = commentServiceImpl.updateComment(comment,1);
            assertThat(created_comment).isEqualTo(comment);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Order(9)
    @Test
    void testDeleteComment() {
        Comment comment = Comment.builder()
                .id(1)
                .commentType(CommentType.builder().id(1).build())
                .content("Forcus")
                .build();

        when(commentRepository.findById(any())).thenReturn(Optional.ofNullable(comment));
        doNothing().when(commentRepository).delete(comment);
        try {
            commentServiceImpl.deleteComment(1);
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }
        verify(commentRepository, times(1)).delete(comment);
    }

    @Order(10)
    @ParameterizedTest(name = "index => search={0},author{1},direction{2}, typeId{3},postId{4},testId{5} ,studysetId{6},int assignmentid{7},int submissionid{8},rootId{9}, page{10}, size{11}")
    @CsvSource({
            "Hello,quantruong,DESC,1,2,1,2,1,1,1,1,5",
            "hello,ngocnguyen,DESC,1,2,1,2,1,1,1,1,5"
    })
    public void testGetFilterAssignment(String search, String author, String direction, int typeid,int postid,int testid, int studysetid,int assignmentid,int submissionid,int rootid, int page, int size) throws ResourceNotFroundException {

        MockitoAnnotations.openMocks(this);
        Comment comment = Comment.builder()
                .id(1)
                .commentType(CommentType.builder().id(1).build())
                .content("Forcus")
                .build();


        Query mockedQuery = mock(Query.class);
        when(em.createNativeQuery(anyString(),eq(Comment.class))).thenReturn(mockedQuery);
        when(mockedQuery.setParameter(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(List.of(comment));
        List<Comment> list = (List<Comment>) commentServiceImpl.getFilterComment(search, author, direction, typeid, postid,testid,studysetid,assignmentid,submissionid, rootid, page, size).get("list");
        assertThat(list.size()).isGreaterThan(0);

    }
}
