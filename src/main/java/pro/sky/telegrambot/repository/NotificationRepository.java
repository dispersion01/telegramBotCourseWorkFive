package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.management.Notification;
import java.util.Collection;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("from NotificationTask where notification_date = current_timestamp")
    Collection<Notification> getScheduleNotification();
}
