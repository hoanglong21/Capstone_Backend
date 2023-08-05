package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Class;
import com.capstone.project.model.Notification;

import java.text.ParseException;
import java.util.List;

public interface NotificationService {
    List<Notification> getAllNotification();

    Notification getNotificationById(int id) throws ResourceNotFroundException;

    Notification createNotification(Notification noti);

    Notification updateNotification( Notification noti,  int id) throws ResourceNotFroundException;
    Boolean deleteNotification( int id) throws ResourceNotFroundException;
}
