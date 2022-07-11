package pro.sky.telegrambot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationRepository;

import javax.management.Notification;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotificationService {
    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private static final String REGEX_BOT_MESSAGE = "/^([A-Z]{2}[0-9]{6})?$|[0-9]{8}[\\s\\-]?[0-9]{5}?$/";
    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public NotificationTask schedule(NotificationTask task, Long chatId) {
        task.setChatId(chatId);
        NotificationTask storedTask = repository.save(task);
        logger.info("NotificatonTask" + storedTask);
        return storedTask;
    }
    public Optional<NotificationTask> parse(String notificationBotMessage) {
        Pattern pattern = Pattern.compile(REGEX_BOT_MESSAGE);
        Matcher matcher = pattern.matcher((notificationBotMessage));
        NotificationTask result = null;
        try {
            if (matcher.find()) {
                LocalDateTime notificationDateTime = LocalDateTime.parse(matcher.group(1), DATE_TIME_FORMATTER);
                String notification = matcher.group(3);
                result = new NotificationTask(notification, notificationDateTime);
            }
        } catch (Exception e) {
            logger.error("Fail time: " + notificationBotMessage + "with pattern" +  DATE_TIME_FORMATTER+e);
        } catch (DateTimeException e) {
            logger.error("Fail notification: " + notificationBotMessage, e);
        }
        return Optional.ofNullable(result);
    }

    public void notifyAllScheduledTask(Consumer<NotificationTask> notifier) {
        logger.info("Send notification");
        Collection<Notification> notifications = repository.getScheduleNotification();
        notifications.forEach(task -> {
            notifier.accept(task);
            task.markAsSent(notifications);
        });
        repository.saveAll(notifications);

    }
}
