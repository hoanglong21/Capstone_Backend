package com.capstone.project.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class NotificationTest {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @ParameterizedTest(name = "index => userId={0}, created_date{1}, title{2}, content{3}, url{4}, is_read{5}")
    @CsvSource({
            "1,2023-4-7, study, new studyset is added, studyset13123,true ",
            "2,2023-4-8, assignment, new assignment is added, assignment23ws23,false  "
    })

    public void testNotification(int userId,String created_date,String title,String content,String url,boolean isRead) {
        try{
            Notification notification = Notification.builder()
                    .user(User.builder().id(userId).build())
                    .datetime(dateFormat.parse(created_date))
                    .title(title)
                    .content(content)
                    .url(url)
                    .is_read(isRead)
                    .build();

            assertThat(notification).isNotNull();
            assertThat(notification.getUser().getId()).isEqualTo(userId);
            assertThat(notification.getDatetime()).isEqualTo(created_date);
            assertThat(notification.getTitle()).isEqualTo(title);
            assertThat(notification.getContent()).isEqualTo(content);
            assertThat(notification.getUrl()).isEqualTo(url);
            assertThat(notification.is_read()).isEqualTo(isRead);
        }catch (ParseException e){
            throw  new RuntimeException(e);
        }

    }
}
