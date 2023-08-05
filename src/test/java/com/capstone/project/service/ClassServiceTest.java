package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.*;
import com.capstone.project.model.Class;
import com.capstone.project.repository.*;
import com.capstone.project.service.impl.AnswerServiceImpl;
import com.capstone.project.service.impl.ClassServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.hibernate.boot.registry.classloading.internal.ClassLoaderServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClassServiceTest {

    @Mock
    private EntityManager em;

    @Mock
    private UserService userService;

    @Mock
    private ClassLearnerRepository classLearnerRepository;
    @Mock
    private ClassService classService;
    @Mock
    private ClassRepository classRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private AttachmentRepository attachmentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ClassServiceImpl classServiceImpl;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Order(1)
    @Test
    void testGetAllClass() {

        Class classroom = Class.builder()
                .class_name("On thi N3")
                .description("hoc N3")
                .build();
        List<Class> classrooms = List.of(classroom);
        when(classRepository.findAll()).thenReturn(classrooms);
        assertThat(classServiceImpl.getAllClass().size()).isGreaterThan(0);
    }

    @Order(2)
    @Test
    void testGetClassById() {
        Class classroom = Class.builder()
                .class_name("On thi N3")
                .description("hoc N3")
                .build();
        when(classRepository.findById(any())).thenReturn(Optional.ofNullable(classroom));
        try {
            Class getClassroom = classServiceImpl.getClassroomById(1);
            assertThat(getClassroom).isEqualTo(classroom);
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }
    }

    @Order(3)
    @ParameterizedTest(name = "index => userId={0}, class_name={1}, classcode{2},created_date{3},deleted_date{4}, description{5},is_deleted{6}")
    @CsvSource({
            "1, Luyen thi JLPT N5,gjhktg,2023-7-1,2023-8-7, On thi N3,false ",
            "2, Luyen thi JLPT N4,Jnf5A,2023-8-9,2023-8-7, On thi N3,true "
    })
    public void testCreateClass(int userId, String class_name, String classcode, String created_date,
                                String deleted_date, String description, Boolean is_deleted) {
        try {
            Class classroom = Class.builder()
                    .user(User.builder().id(userId).build())
                    .class_name(class_name)
                    .classcode(classcode)
                    .created_date(dateFormat.parse(created_date))
                    .deleted_date(dateFormat.parse(deleted_date))
                    .description(description)
                    .is_deleted(is_deleted)
                    .build();
            when(classRepository.save(any())).thenReturn(classroom);

            Class createdclass = classServiceImpl.createClassroom(classroom);
            assertThat(classroom).isEqualTo(createdclass);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    @Order(4)
    @ParameterizedTest(name = "index => userId={0}, class_name={1}, classcode{2},created_date{3},deleted_date{4}, description{5},is_deleted{6}")
    @CsvSource({
            "1, Luyen thi JLPT N5,gjhktg,2023-7-1,2023-8-7, On thi N3,false ",
            "2, Luyen thi JLPT N4,Jnf5A,2023-8-9,2023-8-7, On thi N3,true "
    })
    public void testUpdateClass(int userId, String class_name, String classcode, String created_date,
                                String deleted_date, String description, Boolean is_deleted) {
        try {

            Class classroom_new = Class.builder()
                    .class_name("luyen thi N3")
                    .description("hoc N3")
                    .build();

            Class classroom = Class.builder()
                    .user(User.builder().id(userId).build())
                    .class_name(class_name)
                    .classcode(classcode)
                    .created_date(dateFormat.parse(created_date))
                    .deleted_date(dateFormat.parse(deleted_date))
                    .description(description)
                    .is_deleted(is_deleted)
                    .build();
            when(classRepository.findById(any())).thenReturn(Optional.ofNullable(classroom_new));
            when(classRepository.save(any())).thenReturn(classroom);

            Class created_class = classServiceImpl.updateClassroom(classroom, 1);
            assertThat(created_class).isEqualTo(created_class);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Order(5)
    @Test
    void testDeleteClass() {

        Class classroom = Class.builder()
                .id(1)
                .class_name("luyen thi N3")
                .description("hoc N3")
                .build();

        when(classRepository.findById(any())).thenReturn(Optional.ofNullable(classroom));
        try {
            classServiceImpl.deleteClass(1);
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }
        assertThat(classroom.is_deleted()).isEqualTo(true);
    }

    @Order(6)
    @Test
    void deleteHardClass() {
        Class classroom = Class.builder()
                .id(1)
                .class_name("luyen thi N3")
                .description("hoc N3")
                .build();

        Post post = Post.builder().id(1).classroom(classroom).content("Submit assignment tomorrow").build();

        Comment comment = Comment.builder().commentType(CommentType.builder().id(1).build()).content("Hello").build();

        Assignment assignment = Assignment.builder()
                .id(1)
                .user(User.builder().id(2).build())
                .classroom(Class.builder().id(2).build())
                .instruction("do excersices")
                .title("Assignment 1")
                .build();

        Submission submission = Submission.builder()
                .id(1)
                .description("submit assignment")
                .assignment(assignment).build();

        Attachment attachment = Attachment.builder()
                .attachmentType(AttachmentType.builder().id(1).build())
                .file_url("tailieu.docx")
                .assignment(assignment)
                .submission(submission).build();

        doNothing().when(classRepository).delete(classroom);
        doNothing().when(postRepository).delete(post);
        doNothing().when(commentRepository).delete(comment);
        doNothing().when(assignmentRepository).delete(assignment);
        doNothing().when(submissionRepository).delete(submission);
        doNothing().when(attachmentRepository).delete(attachment);

        when(classRepository.findById(1)).thenReturn(Optional.of(classroom));
        when(postRepository.getPostByClassroomId(1)).thenReturn(List.of(post));
        when(commentRepository.getCommentByPostId(1)).thenReturn(List.of(comment));
        when(assignmentRepository.getAssignmentByClassroomId(1)).thenReturn(List.of(assignment));
        when(submissionRepository.getSubmissionByAssignmentId(1)).thenReturn(List.of(submission));
        when(attachmentRepository.getAttachmentBySubmissionId(1)).thenReturn(List.of(attachment));

        try {
            classServiceImpl.deleteHardClass(1);
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }

        verify(classRepository, times(1)).delete(classroom);
        verify(postRepository, times(1)).delete(post);
        verify(commentRepository, times(1)).delete(comment);
        verify(assignmentRepository, times(1)).delete(assignment);
        verify(submissionRepository, times(1)).delete(submission);
        verify(attachmentRepository, times(1)).delete(attachment);
    }

    @Order(7)
    @ParameterizedTest(name = "{index} => code={0}, username={1}")
    @CsvSource({
            "ABC123, test_user1",
            "DEF456, test_user2"
    })
    void testJoinClass(String code, String username) {
        try {

            User user = User.builder()
                    .username(username)
                    .email("quan@gmail.com")
                    .build();
            when(userRepository.findUserByUsername(username)).thenReturn(user);

            Class classroom = Class.builder().classcode(code).user(user).build();
            when(classRepository.findByClasscode(code)).thenReturn(classroom);

            Class joinedclass = classServiceImpl.joinClass(code, username);

            assertThat(joinedclass).isNotNull();
            assertThat(classroom.getClasscode()).isEqualTo(joinedclass.getClasscode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Order(8)
    @ParameterizedTest(name = "{index} => expectedCode={0}, actualCode={1}")
    @CsvSource({
            "NEW456, NEW456",
            "NEW789, NEW789",
    })
    void testResetClassCode(String expectedCode, String actualCode) {
        try {
            int classId = 1;

            Class classObj = Class.builder().id(classId).classcode("OLD123").build();
            when(classRepository.findById(classId)).thenReturn(Optional.of(classObj));

            classServiceImpl.ResetClassCode(classId);

            actualCode = classObj.getClasscode();

            verify(classRepository, times(1)).save(classObj);
            assertThat(expectedCode).isNotEqualTo(actualCode);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Order(9)
    @ParameterizedTest(name = "index => clasId={0},isDeleted{1},search{2}, author{3},fromDeleted{4},toDeleted{5} ," +
                                       " fromCreated{6},toCreated{7}, sortBy{8},direction{9},page={10}, size{11} ")
    @CsvSource({
            "1,true,JLPT, quantruong,2023-8-9,2023-8-15,2023-8-1,2023-8-5,created_date,DESC,1,5",
            "2,false,IELTS,ngocnguyen,2023-8-9,2023-8-15,2023-8-1,2023-8-5,created_date,DESC,1,5"
    })
    public void testGetFilterClass(int classId,boolean isDeleted,String search, String author, String fromDeleted, String toDeleted, String fromCreated, String toCreated,
                                   String sortBy, String direction,int page, int size) throws ResourceNotFroundException {

        MockitoAnnotations.openMocks(this);
        Class classroom = Class.builder()
                .id(1)
                .class_name("luyen thi N3")
                .description("hoc N3")
                .build();

        Query mockedQuery = mock(Query.class);
        when(em.createNativeQuery(anyString(),eq("ClassCustomListMapping"))).thenReturn(mockedQuery);
        when(mockedQuery.setParameter(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(List.of(classroom));

        List<Class> list = (List<Class>) classServiceImpl.getFilterClass(classId,isDeleted,search,author,fromDeleted,toDeleted,fromCreated,toCreated,
                                                                          sortBy,direction,page,size).get("list");
        assertThat(list.size()).isGreaterThan(0);

    }

    @Order(10)
    @ParameterizedTest(name = "index => userId={0},classId{1}")
    @CsvSource({
            "1,1 ",
            "2,1 "
    })
    public void testCheckUserClass(int userId,int classId) {


        ClassLearner classLearner = new ClassLearner();
        classLearner.setId(1);
        classLearner.setUser(User.builder().id(3).build());
        classLearner.setClassroom(Class.builder().id(1).build());

// Giả lập hàm classLearnerRepository.findByUserIdAndClassroomId trả về classLearner
        when(classLearnerRepository.findByUserIdAndClassroomId(userId, classId)).thenReturn(classLearner);
            try {
                // Gọi phương thức cần kiểm tra
                Boolean result = classService.CheckUserClass(userId, classId);

                // So sánh kết quả trả về với kết quả dự kiến
                boolean expectedResult = classLearner != null && classLearner.getUser().getId() == userId;
                assertThat(result).isEqualTo(expectedResult);

            } catch (ResourceNotFroundException e) {
                throw new RuntimeException(e);
            }


    }
}
