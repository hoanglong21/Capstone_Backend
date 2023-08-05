package com.capstone.project.dto;

import com.capstone.project.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassLearnerRequest {

    private int id;

    private User user;

    private Class classroom;


    private String username;

    private String avatar;

    private Boolean accepted;

    private Date created_date;

}
