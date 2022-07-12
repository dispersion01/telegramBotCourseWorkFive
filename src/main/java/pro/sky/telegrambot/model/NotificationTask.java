package pro.sky.telegrambot.model;

import org.springframework.data.annotation.Id;
import pro.sky.telegrambot.repository.NotificationRepository;
import pro.sky.telegrambot.service.NotificationService;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class NotificationTask {


    public enum NotificationStatus {
        SCHEDULED,
        SENT,
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime notification_date;
    private String notification_message;
    private Long chatId;
    private LocalDateTime sentDate;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status = NotificationStatus.SCHEDULED;
    public NotificationTask() {

    }
    public NotificationTask(String notification_message, LocalDateTime sentDate) {
        this.notification_message = notification_message;
        this.sentDate = sentDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getNotification_date() {
        return notification_date;
    }

    public void setNotification_date(LocalDateTime notification_date) {
        this.notification_date = notification_date;
    }

    public String getNotification_message() {
        return notification_message;
    }

    public void setNotification_message(String notification_message) {
        this.notification_message = notification_message;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public LocalDateTime getSentDate() {
        return sentDate;
    }

    public void setSentDate(LocalDateTime sentDate) {
        this.sentDate = sentDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return Objects.equals(id, that.id) && Objects.equals(notification_date, that.notification_date) && Objects.equals(notification_message, that.notification_message) && Objects.equals(chatId, that.chatId) && Objects.equals(sentDate, that.sentDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, notification_date, notification_message, chatId, sentDate);
    }

    @Override
    public String toString() {
        return "NotificationTask{" +
                "id=" + id +
                ", notification_date=" + notification_date +
                ", notification_message='" + notification_message + '\'' +
                ", chatId=" + chatId +
                ", sentDate=" + sentDate +
                '}';
    }

    public void markAsSent() {
        this.status= NotificationStatus.SENT;
        this.sentDate = LocalDateTime.now();
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }
}
