package com.capstone.project.service.impl;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Notification;
import com.capstone.project.repository.NotificationRepository;
import com.capstone.project.service.NotificationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @PersistenceContext
    private EntityManager em;

    private final NotificationRepository notificationRepository;


    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public List<Notification> getAllNotification() {
        return notificationRepository.findAll();
    }

    @Override
    public Notification getNotificationById(int id) throws ResourceNotFroundException {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Notification not exist with id:" + id));
        return notification;
    }

    @Override
    public Notification createNotification(Notification notification) {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        Date date = localDateTimeToDate(localDateTime);
        notification.setDatetime(date);
        return notificationRepository.save(notification);
    }

    @Override
    public Notification updateNotification(Notification noti, int id) throws ResourceNotFroundException {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Notification not exist with id:" + id));
        notification.setContent(noti.getContent());
        notification.set_read(noti.is_read());
        notification.setTitle(noti.getTitle());
        notification.setUrl(noti.getUrl());
        return notificationRepository.save(notification);
    }

    @Override
    public Boolean deleteNotification(int id) throws ResourceNotFroundException {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Notification not exist with id:" + id));
        notificationRepository.delete(notification);
        return true;
    }
}
