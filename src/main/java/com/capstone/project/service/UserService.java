package com.capstone.project.service;

import com.capstone.project.exception.DuplicateValueException;
import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.User;

import java.sql.SQLSyntaxErrorException;
import java.util.List;
import java.util.Map;

public interface UserService {
    User createUser(User User);

    List<User> findAllNameExcept(String username, String except);

    User getUserByUsername(String username) throws ResourceNotFroundException;

    User updateUser(String username, User userDetails) throws ResourceNotFroundException, DuplicateValueException;

    Boolean banUser(String username) throws ResourceNotFroundException;

    Boolean deleteUser(String username) throws ResourceNotFroundException;

    Boolean recoverUser(String username) throws ResourceNotFroundException;

    Boolean verifyAccount(String token) throws ResourceNotFroundException;

    Boolean sendVerificationEmail(String username) throws ResourceNotFroundException;

    Boolean resetPassword(String username, String pin, String password) throws ResourceNotFroundException;

    String sendResetPasswordEmail(String username) throws ResourceNotFroundException;

    Boolean checkMatchPassword(String username, String checkPassword) throws ResourceNotFroundException;

    Boolean changePassword(String username, String password) throws ResourceNotFroundException;

    Map<String, Object> filterUser(String name, String username, String email, String gender, String phone, String[] role, String address, String bio, String[] status,
                                   String fromDob, String toDob, String fromBanned, String toBanned, String fromDeleted, String toDeleted, String fromCreated, String toCreated,
                                   String sortBy, String direction, int page, int size);
}
