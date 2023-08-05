package com.capstone.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String username;

    private String first_name;

    private String last_name;

//    private boolean gender; // 0 male; 1 female

    private String gender;

    @Column
    @Temporal(TemporalType.DATE)
    private Date dob;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true)
    private String phone;

    private String password;

    private String role;

    @Column(columnDefinition="TEXT")
    private String address;

    private String bio;

    private String status;

    @Column(columnDefinition="TEXT")
    private String avatar;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date banned_date;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleted_date;

    @Column(unique = true)
    private String token; // for verify

    private String pin; // for reset password

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_date;
}