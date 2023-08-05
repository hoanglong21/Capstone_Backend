package com.capstone.project.dto;

import com.capstone.project.model.Class;
import com.capstone.project.model.StudySetType;
import com.capstone.project.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudySetResponse {

    private int id;
    private String title;
    private String description;
    private boolean is_deleted;
    private boolean is_public;
    private boolean is_draft;
    private int type_id;
    private int author_id;
    private Date deleted_date;
    private int count;

    private String author;
    private String avatar;
    private Date created_date;

    private String author_firstname;
    private String author_lastname;
}

