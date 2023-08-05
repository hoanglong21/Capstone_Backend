package com.capstone.project.service.impl;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Feedback;
import com.capstone.project.model.FeedbackType;
import com.capstone.project.model.User;
import com.capstone.project.repository.FeedbackRepository;
import com.capstone.project.repository.FeedbackTypeRepository;
import com.capstone.project.repository.UserRepository;
import com.capstone.project.service.FeedbackService;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private FeedbackTypeRepository feedbackTypeRepository;
    private final JavaMailSender mailSender;

    @Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, UserRepository userRepository, FeedbackTypeRepository feedbackTypeRepository, JavaMailSender mailSender) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
        this.feedbackTypeRepository = feedbackTypeRepository;
        this.mailSender = mailSender;
    }

    @Override
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public Feedback createFeedback(Feedback feedback) {
        sendFeedbackEmail(feedback);
        feedback.setCreated_date(new Date());
        return feedbackRepository.save(feedback);
    }

    @Override
    public Feedback getFeedbackById(int id) throws ResourceNotFroundException {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Feedback not exist with id: " + id));
        return feedback;
    }

    @Override
    public Feedback updateFeedback(int id, Feedback feedbackDetails) throws ResourceNotFroundException {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Feedback not exist with id: " + id));
        feedback.setTitle(feedbackDetails.getTitle());
        feedback.setContent(feedbackDetails.getContent());

        Feedback updateFeedback = feedbackRepository.save(feedback);
        return updateFeedback;
    }

    @Override
    public Boolean deleteFeedback(int id) throws ResourceNotFroundException {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Feedback not exist with id: " + id));
        feedbackRepository.delete(feedback);
        return true;
    }

    @Autowired
    private EntityManager entityManager;

    @Override
    public Map<String, Object> filterFeedback(String search, int type, int authorId, String authorName, String destination,
                                              String fromCreated, String toCreated, String sortBy, String direction,
                                              int page, int size) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String jpql = "FROM Feedback f LEFT JOIN FETCH f.user WHERE 1=1 ";
        Map<String, Object> params = new HashMap<>();

        if (search != null && !search.isEmpty()) {
            jpql += " AND (f.content LIKE :search OR f.title LIKE :search) ";
            params.put("search", "%" + search + "%");
        }

        if (destination != null && !destination.isEmpty()) {
            jpql += " AND f.destination LIKE :destination ";
            params.put("destination", "%" + destination + "%");
        }

        if (authorId != 0) {
            jpql += " AND f.user.id = :authorId ";
            params.put("authorId", authorId);
        }

        if (authorId == 0 && authorName != null && !authorName.isEmpty()) {
            jpql += " AND (f.user.username LIKE :authorName OR CONCAT(f.user.first_name, ' ', f.user.last_name) LIKE :authorName) ";
            params.put("authorName", "%" + authorName + "%");
        }

        if (type != 0) {
            jpql += " AND f.feedbackType.id = :type ";
            params.put("type", type);
        }

        if (fromCreated != null && !fromCreated.isEmpty()) {
            jpql += " AND f.created_date >= :fromCreated ";
            params.put("fromCreated", formatter.parse(fromCreated));
        }

        if (toCreated != null && !toCreated.isEmpty()) {
            jpql += " AND f.created_date <= :toCreated ";
            params.put("toCreated", formatter.parse(toCreated));
        }

        if(sortBy != null && !sortBy.equals("") && direction != null && !direction.equals("")) {
            sortBy = "f." + sortBy;
            jpql += " ORDER BY " + sortBy + " " + direction;
        }

        TypedQuery<Feedback> query = entityManager.createQuery(jpql, Feedback.class);

        // Set parameters
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        // Apply pagination
        int offset = (page - 1) * size;
        query.setFirstResult(offset);
        query.setMaxResults(size);

        List<Feedback> feedbackList = query.getResultList();

        int totalItems = getTotalFeedbackCount(jpql, params);
        int totalPages = (int) Math.ceil((double) totalItems / size);

        Map<String, Object> response = new HashMap<>();
        response.put("list", feedbackList);
        response.put("currentPage", page);
        response.put("totalItems", totalItems);
        response.put("totalPages", totalPages);

        return response;
    }

    @Override
    public String replyFeedback(int feedbackId, String subject, String content) throws Exception {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new ResourceNotFroundException("Feedback not exist with id: " + feedbackId));
        subject = "[" + feedback.getTitle() + "] " + subject;
        String toAddress = feedback.getUser().getEmail();
        String fromAddress = "nihongolevelup.box@gmail.com";
        String senderName = "NihongoLevelUp";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
        return "Send reply successfully";
    }

    private int getTotalFeedbackCount(String jpql, Map<String, Object> params) {
        // Remove the ORDER BY clause from the original JPQL query
        int orderByIndex = jpql.toUpperCase().lastIndexOf("ORDER BY");
        if (orderByIndex != -1) {
            jpql = jpql.substring(0, orderByIndex);
        }

        // Construct the count query by adding the COUNT function and removing the FETCH JOIN
        String countJpql = "SELECT COUNT(f) " + jpql.replace("LEFT JOIN FETCH f.user", "");

        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);

        // Set parameters for the count query
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }

        Long countResult = countQuery.getSingleResult();
        return countResult != null ? countResult.intValue() : 0;
    }

    private void sendFeedbackEmail(Feedback feedback) {
        try {
            User user = userRepository.findUserById(feedback.getUser().getId());
            if (user == null) {
                throw new ResourceNotFroundException("User not found with id: " + feedback.getUser().getId());
            }
            FeedbackType feedbackType = feedbackTypeRepository.findById(feedback.getFeedbackType().getId())
                    .orElseThrow(() -> new ResourceNotFroundException("Feedback Type not found with id: " + feedback.getFeedbackType().getId()));

            String toAddress = user.getEmail();
            String fromAddress = "nihongolevelup.box@gmail.com";
            String senderName = "NihongoLevelUp";

            String subject = "Thank You for Your Feedback!";
            String content = "";
            if(feedback.getDestination().contains("/")) {
                content = "Dear [[name]],<br><br>"
                        + "Thank you for taking the time to provide us with your feedback. We really appreciate it!<br><br>"
                        + "We will carefully review your comments and suggestions regarding " + feedbackType.getName() + " to see how we can improve our services.<br><br>"
                        + "Here are the details of your feedback:<br><br>"
                        + "Title: " + feedback.getTitle() + "<br>"
                        + "Type: " + feedbackType.getName() + "<br>"
                        + "Content: " + feedback.getContent() + "<br>"
                        + "To: " + feedback.getDestination() + "<br><br>"
                        + "If you have any additional feedback or questions, please do not hesitate to contact us at nihongolevelup.box@gmail.com.<br><br>"
                        + "Best regards,<br>"
                        + "NihongoLevelUp Team";
            } else {
                content = "Dear [[name]],<br><br>"
                        + "Thank you for taking the time to provide us with your feedback. We really appreciate it!<br><br>"
                        + "We will carefully review your comments and suggestions to see how we can improve our services.<br><br>"
                        + "If you have any additional feedback or questions, please do not hesitate to contact us at nihongolevelup.box@gmail.com.<br><br>"
                        + "Best regards,<br>"
                        + "NihongoLevelUp Team";
            }

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);

            content = content.replace("[[name]]", user.getFirst_name() + " " + user.getLast_name());

            helper.setText(content, true);

            mailSender.send(message);
        } catch (Exception e) {

        }
    }
}
