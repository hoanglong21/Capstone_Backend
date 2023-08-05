package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.*;
import com.capstone.project.model.Class;
import com.capstone.project.repository.NotificationRepository;
import com.capstone.project.service.impl.AssignmentServiceImpl;
import com.capstone.project.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Order(1)
    @Test
    void testGetAllNotification() {
        Notification notification = Notification.builder()
                .content("New studyset assigned")
                .title("Studyset").build();
        List<Notification> notifications = List.of(notification);
        when(notificationRepository.findAll()).thenReturn(notifications);
        assertThat(notificationService.getAllNotification().size()).isGreaterThan(0);
    }

    @Order(2)
    @Test
    void testGetNotificationById() {
        Notification notification = Notification.builder()
                .content("New studyset assigned")
                .title("Studyset").build();
        when(notificationRepository.findById(any())).thenReturn(Optional.ofNullable(notification));
        try{
            Notification getNotification = notificationService.getNotificationById(1);
            assertThat(getNotification).isEqualTo(notification);
        }catch (ResourceNotFroundException e){
            e.printStackTrace();
        }
    }

    @Order(3)
    @ParameterizedTest(name = "index => user_id{0}, content={1}, datetime={2},is_read{3},title{4},url{5}")
    @CsvSource({
            "1,new studyset added,2023-8-9,true, studyset, qweqw ",
            "2,new assignment added,2023-8-9,false, assignment,qweqdq "
    })
    public void testCreateNotification(int userId,String content, String datetime,boolean isRead,String title,String url){
        try {
            Notification notification = Notification.builder()
                    .user(User.builder().id(userId).build())
                    .content(content)
                    .datetime(dateFormat.parse(datetime))
                    .is_read(isRead)
                    .title(title)
                    .url(url)
                    .build();
            when(notificationRepository.save(any())).thenReturn(notification);
            Notification creatednoti = notificationService.createNotification(notification);
            assertThat(notification).isEqualTo(creatednoti);
        } catch (ParseException e){
            throw new RuntimeException(e);
        }
    }

    @Order(4)
    @ParameterizedTest(name = "index => user_id{0}, content={1}, datetime={2},is_read{3},title{4},url{5}")
    @CsvSource({
            "1,new studyset added,2023-8-9,true, studyset, qweqw ",
            "2,new assignment added,2023-8-9,false, assignment,qweqdq "
    })
    void testUpdateNotification(int userId,String content, String datetime,boolean isRead,String title,String url) {
        try{

            Notification notification_new = Notification.builder()
                    .content("New studyset assigned")
                    .title("Studyset").build();

            Notification notification = Notification.builder()
                    .user(User.builder().id(userId).build())
                    .content(content)
                    .datetime(dateFormat.parse(datetime))
                    .is_read(isRead)
                    .title(title)
                    .url(url)
                    .build();
            when(notificationRepository.findById(any())).thenReturn(Optional.ofNullable(notification_new));
            when(notificationRepository.save(any())).thenReturn(notification);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Order(5)
    @Test
    void testDeleteNotification() {

        Notification notification = Notification.builder()
                .id(1)
                .content("New studyset assigned")
                .title("Studyset").build();

        when(notificationRepository.findById(any())).thenReturn(Optional.ofNullable(notification));
        doNothing().when(notificationRepository).delete(notification);
        try {
            notificationService.deleteNotification(1);
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }
        verify(notificationRepository, times(1)).delete(notification);
    }

}

