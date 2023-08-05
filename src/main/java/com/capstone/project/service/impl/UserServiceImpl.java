package com.capstone.project.service.impl;

import com.capstone.project.exception.DuplicateValueException;
import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.User;
import com.capstone.project.repository.UserRepository;
import com.capstone.project.service.UserService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.sql.SQLSyntaxErrorException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, JavaMailSender mailSender) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    @Override
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateValueException("Username already registered");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateValueException("Email already registered");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String uniqueToken;
        while(true) {
            uniqueToken = UUID.randomUUID().toString();
            if(!userRepository.existsByToken(uniqueToken)) {
                break;
            }
        }
        user.setToken(uniqueToken);
        user.setStatus("pending");

        // new
        user.setCreated_date(new Date());
        // end of new

        return userRepository.save(user);
    }

    @Override
    public List<User> findAllNameExcept(String username, String except) {
        return userRepository.findAllNameExcept(username, except);
    }

    @Override
    public User getUserByUsername(String username) throws ResourceNotFroundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new ResourceNotFroundException("User not exist with username: " + username);
        }
        return user;
    }

    @Override
    public User updateUser(String username, User userDetails) throws ResourceNotFroundException, DuplicateValueException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new ResourceNotFroundException("User not exist with username: " + username);
        }
        
        if (userDetails.getPhone()!=null &&  !userDetails.getPhone().equals("") && !userDetails.getPhone().equals(user.getPhone()) && userRepository.existsByPhone(userDetails.getPhone())) {
            throw new DuplicateValueException("Phone already registered");
        }

        user.setBio(userDetails.getBio());
        user.setDob(userDetails.getDob());
        user.setAvatar(userDetails.getAvatar());
        user.setAddress(userDetails.getAddress());
        user.setFirst_name(userDetails.getFirst_name());
        user.setLast_name(userDetails.getLast_name());
        user.setGender(userDetails.getGender());
        user.setPhone(userDetails.getPhone());

//        user.setStatus(userDetails.getStatus());
//        user.setRole(userDetails.getRole());

        User updateUser = userRepository.save(user);
        return updateUser;
    }

    @Override
    public Boolean banUser(String username) throws ResourceNotFroundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new ResourceNotFroundException("User not exist with username: " + username);
        }
        user.setStatus("banned");
        user.setBanned_date(new Date());
        userRepository.save(user);
        return true;
    }

    @Override
    public Boolean deleteUser(String username) throws ResourceNotFroundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new ResourceNotFroundException("User not exist with username: " + username);
        }
        user.setStatus("deleted");
        user.setDeleted_date(new Date());
        userRepository.save(user);
        return true;
    }

    @Override
    public Boolean recoverUser(String username) throws ResourceNotFroundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new ResourceNotFroundException("User not exist with username: " + username);
        }
        Date today = new Date();
        Date banned_date = user.getBanned_date();
        if(banned_date != null) {
            long diffInMillis = today.getTime() - banned_date.getTime();
            long diffInDays = diffInMillis / (24 * 60 * 60 * 1000);
            if(diffInDays<=7) {
                user.setStatus("banned");
                userRepository.save(user);
                return false;
            } else {
                user.setStatus("active");
                userRepository.save(user);
                return true;
            }
        } else {
            user.setStatus("active");
            userRepository.save(user);
            return true;
        }
    }

    @Override
    public Boolean verifyAccount(String token) throws ResourceNotFroundException {
        User user = userRepository.findUserByToken(token);
        if (user == null) {
            throw new ResourceNotFroundException("User not exist with token: " + token);
        }
        user.setStatus("active");
        userRepository.save(user);
        return true;
    }

    @Override
    public Boolean sendVerificationEmail(String username) throws ResourceNotFroundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new ResourceNotFroundException("User not exist with token: " + username);
        }
        try {
            // for current version only
            String siteURL = "http://localhost:8080/api/v1/";

            // end of current version
            String toAddress = user.getEmail();
            String fromAddress = "nihongolevelup.box@gmail.com";
            String senderName = "NihongoLevelUp";
//            String subject = "Please verify your registration";
//            String content = "Dear [[name]],<br>"
//                    + "We hope this message finds you well.<br>"
//                    + "We would like to kindly request that you please follow the link provided below to verify your registration: "
//                    + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
//                    + "Thank you for taking the time to complete this important step. If you have any questions or concerns, please do not hesitate to contact us.:<br>"
//                    + "Thank you,<br>"
//                    + "The NihongoLevelUp Team";

            String subject = "Confirm Your Registration with NihongoLevelUp";
            String content = "Dear [[name]],<br><br>"
                    + "Thank you for registering for a NihongoLevelUp account. To complete your registration, please click the button below to verify your email address: "
                    + "<a href=\"[[URL]]\" style=\"display:inline-block;background-color:#3399FF;color:#FFF;padding:10px 20px;text-decoration:none;border-radius:5px;font-weight:bold;\" target=\"_blank\">Verify Email</a><br><br>"
                    + "Thank you for choosing NihongoLevelUp! If you have any questions or concerns, please do not hesitate to contact us.<br><br>"
                    + "Best regards,<br>"
                    + "NihongoLevelUp Team";

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);

            content = content.replace("[[name]]", user.getFirst_name() + " " + user.getLast_name());

            String verifyURL = siteURL + "verify?token=" + user.getToken();
            content = content.replace("[[URL]]", verifyURL);

            helper.setText(content, true);

            mailSender.send(message);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public Boolean resetPassword(String username, String pin, String password) throws ResourceNotFroundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new ResourceNotFroundException("User not exist with username: " + username);
        }
        if (user.getPin().equals(pin)) {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String sendResetPasswordEmail(String username) throws ResourceNotFroundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            user = userRepository.findUserByEmail(username);
            if(user == null) {
                throw new ResourceNotFroundException("User not exist with username or email: " + username);
            }
        }
        try {
            // for current version only
            String siteURL = "http://localhost:3000/";

            // end of current version
            String toAddress = user.getEmail();
            String fromAddress = "nihongolevelup.box@gmail.com";
            String senderName = "NihongoLevelUp";

            String subject = "Reset Your Password with NihongoLevelUp";
            String content = "Dear [[name]],<br><br>"
                    + "<p>We have received a request to reset your password for your NihongoLevelUp account. To proceed with resetting your password, please click the button below:</p>"
                    + "<a href=\"[[URL]]\" style=\"display:inline-block;background-color:#3399FF;color:#FFF;padding:10px 20px;text-decoration:none;border-radius:5px;font-weight:bold;\" target=\"_blank\">Reset Password</a><br><br>"
                    + "If you did not initiate this request, please ignore this email. Otherwise, please use the link above to update your password.<br><br>"
                    + "Thank you,<br>"
                    + "NihongoLevelUp Team";

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);


            content = content.replace("[[name]]", user.getFirst_name() + " " + user.getLast_name());

            // pin update
            String pin = UUID.randomUUID().toString();
            user.setPin(pin);
            userRepository.save(user);
            // end of pin update

            String resetURL = siteURL + "reset-password?username=" + username +  "&pin=" + user.getPin();
            content = content.replace("[[URL]]", resetURL);

            helper.setText(content, true);

            mailSender.send(message);
            return hideEmail(user.getEmail());
        } catch (Exception e) {
            return "Error occur";
        }
    }

    public static String hideEmail(String email) {
        String[] strings = email.split("@");

        int part1 = (int) Math.floor((double) strings[0].length()/3);
        int part2 = (int) Math.ceil((double) strings[0].length()/2);
        int domain1 = (int) Math.floor((double) strings[1].length()/3);
        int domain2 = (int) Math.ceil((double) strings[1].length()/2);
        StringBuilder hiddenPart1 = new StringBuilder(strings[0]);
        for(int i = part1; i <= part2; i++) {
            hiddenPart1.setCharAt(i, '*');
        }
        StringBuilder hiddenPart2 = new StringBuilder(strings[1]);
        for(int i = domain1; i <= domain2; i++) {
            hiddenPart2.setCharAt(i, '*');
        }
        return hiddenPart1.toString() + "@" + hiddenPart2.toString();
    }

    @Override
    public Boolean checkMatchPassword(String username, String checkPassword) throws ResourceNotFroundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new ResourceNotFroundException("User not exist with username:" + username);
        }
        if(passwordEncoder.matches(checkPassword, user.getPassword())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean changePassword(String username, String password) throws ResourceNotFroundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new ResourceNotFroundException("User not exist with username: " + username);
        }
        try {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Map<String, Object> filterUser(String name, String username, String email,  String gender, String phone, String[] role, String address, String bio, String[] status,
                                 String fromDob, String toDob, String fromBanned, String toBanned, String fromDeleted, String toDeleted, String fromCreated, String toCreated,
                                          String sortBy, String direction, int page, int size) {
        String sql = "SELECT * FROM user WHERE 1=1";
        List<Object> params = new ArrayList<>();

        if(name!=null && !name.equals("")) {
            sql += " AND (first_name LIKE ? OR last_name LIKE ? OR concat(first_name, ' ', last_name) LIKE ?)";
            String nameParam = "%" + name + "%";
            params.add(nameParam);
            params.add(nameParam);
            params.add(nameParam);
        }
        if (username != null && !username.equals("")) {
            sql += " AND username LIKE ?";
            params.add("%" + username + "%");
        }
        if (email != null && !email.equals("")) {
            sql += " AND email LIKE ?";
            params.add("%" + email + "%");
        }
        if (gender != null && !gender.equals("")) {
            sql += " AND gender LIKE ?";
            params.add("%" + gender + "%");
        }
        if (phone != null && !phone.equals("")) {
            sql += " AND phone LIKE ?";
            params.add("%" + phone + "%");
        }
        if (role != null && role.length > 0) {
            sql += " AND (";
            for (int i = 0; i < role.length; i++) {
                sql += "role LIKE ?";
                params.add("%" + role[i] + "%");
                if (i != role.length - 1) {
                    sql += " OR ";
                }
            }
            sql += ")";
        }
        if (address != null && !address.equals("")) {
            sql += " AND address LIKE ?";
            params.add("%" + address + "%");
        }
        if (bio != null && !bio.equals("")) {
            sql += " AND bio LIKE ?";
            params.add("%" + bio + "%");
        }
        if (status != null && status.length > 0) {
            sql += " AND (";
            for (int i = 0; i < status.length; i++) {
                sql += "status LIKE ?";
                params.add("%" + status[i] + "%");
                if (i != status.length - 1) {
                    sql += " OR ";
                }
            }
            sql += ")";
        }
        if (fromDob != null && !fromDob.equals("")) {
            sql += " AND dob >= ?";
            params.add(fromDob);
        }
        if (toDob != null && !toDob.equals("")) {
            sql += " AND dob <= ?";
            params.add(toDob);
        }
        if (fromBanned != null && !fromBanned.equals("")) {
            sql += " AND DATE(banned_date) >= ?";
            params.add(fromBanned);
        }
        if (toBanned != null && !toBanned.equals("")) {
            sql += " AND DATE(banned_date) <= ?";
            params.add(toBanned);
        }
        if (fromDeleted != null && !fromDeleted.equals("")) {
            sql += " AND DATE(deleted_date) >= ?";
            params.add(fromDeleted);
        }
        if (toDeleted != null && !toDeleted.equals("")) {
            sql += " AND DATE(deleted_date) <= ?";
            params.add(toDeleted);
        }
        if (fromCreated != null && !fromCreated.equals("")) {
            sql += " AND DATE(created_date) >= ?";
            params.add(fromCreated);
        }
        if (toCreated != null && !toCreated.equals("")) {
            sql += " AND DATE(created_date) <= ?";
            params.add(toCreated);
        }

        if(sortBy != null && !sortBy.equals("") && direction != null && !direction.equals("")) {
            sql += " ORDER BY " + sortBy + " " + direction;
        }
        
        // Count total items
        String countSql = "SELECT COUNT(*) FROM (" + sql + ") AS countQuery";
        long totalItems = jdbcTemplate.queryForObject(countSql, Long.class, params.toArray());

        // Apply pagination
        int offset = (page - 1) * size;
        sql += " LIMIT ? OFFSET ?";
        params.add(size);
        params.add(offset);

        List<User> userList = jdbcTemplate.query(sql, params.toArray(), new BeanPropertyRowMapper<>(User.class));
        int totalPages = (int) Math.ceil((double) totalItems / size);

        Map<String, Object> response = new HashMap<>();
        response.put("list", userList);
        response.put("currentPage", page);
        response.put("totalItems", totalItems);
        response.put("totalPages", totalPages);

        return response;
    }

}
