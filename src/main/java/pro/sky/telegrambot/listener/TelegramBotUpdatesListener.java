package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.service.NotificationService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private static final String START_COMMAND = "/start";
    private static final String HELLO_TEXT = "Hello!";
    private static final String INVALID_TEXT_OR_COMMAND = "Invalid text or command";

    @Autowired
    private TelegramBot telegramBot;
    private final NotificationService notificationService;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    private TelegramBotUpdatesListener(TelegramBot telegramBot, NotificationService notificationService) {
        this.telegramBot = telegramBot;
        this.notificationService = notificationService;
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            // Process your updates here
            Message message = update.message();
            if (message.text().startsWith(START_COMMAND)) {
                logger.info(START_COMMAND + "received");
                sendMessage(extractChatId(message), HELLO_TEXT);
            } else {

                notificationService.parse(message.text()).ifPresentOrElse(
                        task -> scheduledNotification(extractChatId(message), task),
                        () -> sendMessage(extractChatId(message), INVALID_TEXT_OR_COMMAND)

                );
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void scheduledNotification(Long chatId, NotificationTask task) {
        notificationService.schedule(task, chatId);
        sendMessage(chatId, "Notification scheduled");
    }

    private void sendMessage(NotificationTask task) {
        sendMessage(task.getId(), task.getNotification_message());
    }

    private void sendMessage(Long chatId, String messageText) {
        SendMessage sendMessage = new SendMessage(chatId, messageText);
        telegramBot.execute(sendMessage);
    }

    private Long extractChatId(Message message) {
        return message.chat().id();
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void notifyScheduledTask() {
        notificationService.notifyAllScheduledTask(this::sendMessage);
    }

}
