package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.telegrambot.model.NotificationTask;
import java.util.Collection;

public interface NotificationRepository extends JpaRepository<NotificationTask, Long> {
    @Query(value= "select * from notification where notification_date = current_timestamp",
            nativeQuery = true)
    Collection<NotificationTask> getScheduleNotification();
}
