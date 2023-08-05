package com.capstone.project.service.impl;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.*;
import com.capstone.project.model.Class;
import com.capstone.project.repository.*;
import com.capstone.project.service.ClassService;
import com.capstone.project.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import net.loomchild.segment.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.Query;

import java.util.*;

@Service
public class ClassServiceImpl implements ClassService {

    @PersistenceContext
    private EntityManager em;

    private final ClassRepository classRepository;
    private final PostRepository postRepository;
    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final CommentRepository commentRepository;
    private final AssignmentRepository assignmentRepository;
    private final AttachmentRepository attachmentRepository;
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final ClassLearnerRepository classLearnerRepository;

    private final UserService userService;

    @Autowired
    public ClassServiceImpl(ClassRepository classRepository, PostRepository postRepository, TestRepository testRepository, QuestionRepository questionRepository, AnswerRepository answerRepository, CommentRepository commentRepository, AssignmentRepository assignmentRepository, AttachmentRepository attachmentRepository, SubmissionRepository submissionRepository, UserRepository userRepository, ClassLearnerRepository classLearnerRepository, UserService userService) {
        this.classRepository = classRepository;
        this.postRepository = postRepository;
        this.testRepository = testRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.commentRepository = commentRepository;
        this.assignmentRepository = assignmentRepository;
        this.attachmentRepository = attachmentRepository;
        this.submissionRepository = submissionRepository;
        this.userRepository = userRepository;
        this.classLearnerRepository = classLearnerRepository;
        this.userService = userService;
    }

    @Override
    public List<Class> getAllClass() {
        return classRepository.findAll();
    }

    @Override
    public Class getClassroomById(int id) throws ResourceNotFroundException {
        Class classroom = classRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFroundException("Class not exist with id:" + id));
        return classroom;
    }

    @Override
    public Class createClassroom(Class classroom){
        String classCode = generateClassCode();
        classroom.setClasscode(classCode);
        classroom.setCreated_date(new Date());
        return classRepository.save(classroom);
    }
    private String generateClassCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int codeLength = 7;
        Random random = new Random();

        StringBuilder classCode = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            classCode.append(randomChar);
        }

        return classCode.toString();
    }

    @Override
    public Class updateClassroom(Class classrooms, int id) throws ResourceNotFroundException {
        Class classroom = classRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFroundException("Class not exist with id:" + id));
        classroom.setClass_name(classrooms.getClass_name());
        classroom.setDescription(classrooms.getDescription());
        classroom.set_deleted(classrooms.is_deleted());
        return classRepository.save(classroom);
    }

    @Override
    public Boolean deleteClass(int id) throws ResourceNotFroundException {
        Class classroom = classRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFroundException("Class not exist with id:" + id));
        classroom.set_deleted(true);
        classroom.setDeleted_date(new Date());
        classRepository.save(classroom);
        return true;
    }

    @Override
    public Boolean deleteHardClass(int id) throws ResourceNotFroundException {
        Class classroom = classRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Class not exist with id:" + id));
        for(Post post : postRepository.getPostByClassroomId(classroom.getId())){
            for(Comment comment : commentRepository.getCommentByPostId(post.getId())){
                commentRepository.delete(comment);
            }
            for(Attachment attachment : attachmentRepository.getAttachmentByPostId(post.getId())){
                attachmentRepository.delete(attachment);
            }
            postRepository.delete(post);
        }
        for(Test test : testRepository.getTestByClassroomId(classroom.getId())){
            for(Question question: questionRepository.getQuestionByTestId(test.getId())){
                for(Answer answer : answerRepository.getAnswerByQuestionId(question.getId())){
                    answerRepository.delete(answer);
                }
                questionRepository.delete(question);
            }
            for(Comment comment : commentRepository.getCommentByTestId(test.getId())){
                commentRepository.delete(comment);
            }
            testRepository.delete(test);
        }
        for(Assignment assignment : assignmentRepository.getAssignmentByClassroomId(classroom.getId())){
            for(Submission submission : submissionRepository.getSubmissionByAssignmentId(assignment.getId())){
                for(Attachment attachment : attachmentRepository.getAttachmentBySubmissionId(submission.getId())){
                    attachmentRepository.delete(attachment);
                }
                for(Comment comment : commentRepository.getCommentBySubmissionId(submission.getId())){
                    commentRepository.delete(comment);
                }
                submissionRepository.delete(submission);
            }
            for(Attachment attachment : attachmentRepository.getAttachmentByAssignmentId(assignment.getId())){
                attachmentRepository.delete(attachment);
            }
            for(Comment comment : commentRepository.getCommentByAssignmentId(assignment.getId())){
                commentRepository.delete(comment);
            }
            assignmentRepository.delete(assignment);
        }
        classRepository.delete(classroom);
        return true;
    }

    @Override
    public Map<String, Object> getFilterClass(int classid,Boolean isDeleted, String search, String author, String fromDeleted, String toDeleted,
                                              String fromCreated, String toCreated,String sortBy,String direction, int page, int size) throws ResourceNotFroundException {
        int offset = (page - 1) * size;

//        String query ="SELECT * FROM class WHERE 1=1";

        String query = "SELECT c.*, COUNT(cl.user_id) AS member, COUNT(cs.sudyset_id) AS studyset,u.avatar,u.username as author " +
                "FROM class c " +
                "LEFT JOIN class_learner cl ON c.id = cl.class_id " +
                "LEFT JOIN class_studyset cs ON c.id = cs.class_id " +
                "LEFT JOIN user u ON u.id = c.author_id " +
                "GROUP BY c.id HAVING 1=1 ";

        Map<String, Object> parameters = new HashMap<>();

        if (classid != 0) {
            query += " AND c.id = :classId";
            parameters.put("classId", classid);
        }

        if (isDeleted != null) {
            query += " AND is_deleted = :isDeleted";
            parameters.put("isDeleted", isDeleted);

        }

        if (author != null && !author.isEmpty()) {
            query += " AND u.username LIKE :authorname OR EXISTS (SELECT * FROM class_learner cl LEFT JOIN user r ON cl.user_id = r.id WHERE cl.class_id = c.id AND r.username LIKE :authorname)";
            parameters.put("authorname", author);
        }

        if (search != null && !search.isEmpty()) {
            query += " AND (class_name LIKE :search OR description LIKE :search)";
            parameters.put("search", "%" + search + "%");
        }

        if ((isDeleted == null || isDeleted)) {
            if (fromDeleted != null) {
                query += " AND DATE(deleted_date) >= :from";
                parameters.put("from", fromDeleted);
            }
            if (toDeleted != null) {
                query += " AND DATE(deleted_date) <= :to";
                parameters.put("to", toDeleted);
            }
        }

        if (fromCreated != null && !fromCreated.equals("")) {
            query += " AND DATE(created_date) >= :fromCreated";
            parameters.put("fromCreated", fromCreated);
        }
        if (toCreated != null && !toCreated.equals("")) {
            query += " AND DATE(created_date) <= :toCreated";
            parameters.put("toCreated", toCreated);
        }

        query += " ORDER BY " + sortBy + " " + direction;

//        String direct = "desc";
//        if(direction != null && !direction.isEmpty()){
//            if (direction.equalsIgnoreCase("asc")) {
//                direct = "asc";
//            }
//        }
//        query += " ORDER BY created_date " + " " + direct;

        Query q = em.createNativeQuery(query, "ClassCustomListMapping");
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }

        int totalItems = q.getResultList().size();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        q.setFirstResult(offset);
        q.setMaxResults(size);

        List<Class> resultList = q.getResultList();

        Map<String, Object> response = new HashMap<>();
        response.put("list", resultList);
        response.put("currentPage", page);
        response.put("totalPages", totalPages);
        response.put("totalItems", totalItems);

        return response;
    }

    @Override
    public Class joinClass(String classCode, String username) throws ResourceNotFroundException {
        Class classroom = classRepository.findByClasscode(classCode);
        if (classroom == null) {
            throw new ResourceNotFroundException("Class not exist with code: " +classCode);
        }


        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new ResourceNotFroundException("User not exist with username: " +username);
        }

        if(classroom.getUser().getUsername().contains(username)){
            throw new ResourceNotFroundException("You cannot join your class !");
        }

        ClassLearner classLearner = classLearnerRepository.findByUserIdAndClassroomId(user.getId(),classroom.getId());
        if (classLearner != null) {
            throw new ResourceNotFroundException("You are already in the class !");
        }

        if(classroom.is_deleted() == true) {
            throw new ResourceNotFroundException("Class is not exist !");
        }

        classLearner = new ClassLearner();
        classLearner.setUser(user);
        classLearner.setClassroom(classroom);
        classLearner.setCreated_date(new Date());
        classLearner.set_accepted(false);
        classLearnerRepository.save(classLearner);
        // add user to classroom
        return classRepository.save(classroom);
    }

    @Override
    public Class ResetClassCode(int id) throws ResourceNotFroundException {
        Class classroom = classRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Class not exist with id:" + id));
        String newCode = generateClassCode();
        classroom.setClasscode(newCode);
        return classRepository.save(classroom);
    }

    @Override
    public Boolean CheckUserClass(int userId, int classId) throws ResourceNotFroundException {
        Class classroom = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not exist with id:" + classId));


        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ResourceNotFroundException("User not exist with id: " + userId);
        }

        ClassLearner classLearner = classLearnerRepository.findByUserIdAndClassroomId(user.getId(),classroom.getId());
        if (classLearner!= null) {
            if(classLearner.getUser().getId() == userId && classLearner.is_accepted()){
                return true;}
        }

        if (user.getUsername().equals(classroom.getUser().getUsername())) {
            return true;
        }


        return false;
    }

    @Override
    public Boolean CheckUserClassWaiting(int userId, int classId) throws ResourceNotFroundException {
        Class classroom = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not exist with id:" + classId));


        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ResourceNotFroundException("User not exist with id: " + userId);
        }

        ClassLearner classLearner = classLearnerRepository.findByUserIdAndClassroomId(user.getId(),classroom.getId());
        if (classLearner!= null) {
            if(classLearner.getUser().getId() == userId && !classLearner.is_accepted()){
                return true;}
        }
        return false;
    }


}

