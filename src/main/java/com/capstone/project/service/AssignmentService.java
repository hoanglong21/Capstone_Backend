package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Assignment;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface AssignmentService {

    List<Assignment> getAllAssignment();
    List<Assignment> getAllAssignmentByClassId(int id);

//    Assignment createAssignment(Assignment assignment, List<String> files_name, int type, List<String> urls, List<String> file_types);

    Assignment createAssignment(Assignment assignment) throws ResourceNotFroundException;

    Assignment getAssignmentById (int id) throws ResourceNotFroundException;

//    Assignment updateAssignment(int id, Assignment assignment, List<String> file_names, int type, List<String> urls, List<String> file_types) throws ResourceNotFroundException;

    Assignment updateAssignment(int id, Assignment assignment) throws ResourceNotFroundException;

    Boolean deleteAssignment(int id) throws ResourceNotFroundException;

    Map<String, Object> getFilterAssignment(String search, String author, String fromStart, String toStart, String fromCreated, String toCreated,
                                            Boolean isDraft, String direction, String sortBy, int classid, int page, int size) throws ResourceNotFroundException;

    Map<String, Object> getNumSubmitAssignment(int assignmentid, int classid) throws ResourceNotFroundException;
}
