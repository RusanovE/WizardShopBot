package com.example.WizardShopBot.service.handlers;

import com.example.WizardShopBot.entity.CustOrder;
import com.example.WizardShopBot.entity.Customer;
import com.example.WizardShopBot.entity.Question;
import com.example.WizardShopBot.repos.CustomerRepository;
import com.example.WizardShopBot.repos.OrderRepository;
import com.example.WizardShopBot.repos.QuestionRepository;
import com.example.WizardShopBot.util.Emoji;
import com.example.WizardShopBot.util.Parser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommandHandler {

    private final CustomerRepository customerRepository;

    private final OrderRepository orderRepository;

    private final QuestionRepository questionRepository;

    public BotApiMethod<?> answer(Update  update) {
        String command = update.getMessage().getText();
        String tgNick   = update.getMessage().getChat().getFirstName();
        String username = update.getMessage().getChat().getUserName();
        long chatId     = update.getMessage().getChat().getId();
        int messageId   = update.getMessage().getMessageId();

        if (command.contains("@wizards_shop_bot")) {command = command.replace("@tournament_chess_bot", "");}

        if (!command.contains("/") && command.contains("#newOrder")) {
            return createOrder(messageId,chatId,command,tgNick,username);
        } //TODO Выделить в отдельный метод, UPD: Done

        if (!command.contains("/") && command.contains("#newQuestion")){
            return createQuestion(messageId,chatId,command,tgNick,username);
        }//TODO Выделить в отдельный метод, UPD: Done

        // Спец команда для продавца
        if (command.contains("/answerQ")) return receivingAnswerQuestionCommand(command);

        switch (command) {
            case ("/start") -> {
                return receivingStartCommand(chatId, messageId, tgNick);
            }
            case ("/help") -> {
                return receivingHelpCommand(chatId, messageId);
            }
            case ("/price_list") -> {
                return receivingPriceCommand(chatId, messageId);
            }
            case ("/create_order") -> {
                return receivingCreateOrderCommand(chatId, messageId);
            }
            case ("/ask_question") -> {
                return receivingAskQuestionCommand(chatId, messageId);
            }
            default -> {
                return null;
            }
        }
    }
    
    public BotApiMethod<?> receivingStartCommand(long chatId, int messageId, String name){
        String answer = "Здравствуй, " + name + ", я начал работать. Используй команду /help чтобы понять что к чему";

        return  SendMessage.builder()
                .chatId(chatId)
                .text(answer)
                .replyToMessageId(messageId)
                .build();
    }

    public BotApiMethod<?> receivingHelpCommand(long chatId, int messageId){
        String mainText;
        mainText = """
                Вы можете оперировать такими командами:

                Системные команды:
                /start - для запуска бота
                /help - для помощи в понимании управления ботом

                Основные команды:
                /price_list - узнать актуальные цены
                /create_order - для создания заказа
                /ask_question - хочешь задать вопрос - задавай
                """;

       return SendMessage.builder()
               .chatId(chatId)
               .text(mainText)
               .replyToMessageId(messageId)
               .build();
    }

    public BotApiMethod<?> receivingPriceCommand(long chatId, int messageId){
        String answer = "Сейчас расценки такие: \n"
                + Emoji.ALIEN.getValue() + " Товар №1 - 150 - 200 грн \n"
                + Emoji.BOT_FACE.getValue() + " Товар №2 - 250 - 300 грн \n"
                + Emoji.SCULL.getValue() + " Товар №3 - 800 - 900 грн";

        return  SendMessage.builder()
                .chatId(chatId)
                .text(answer)
                .replyToMessageId(messageId)
                .build();
    }

    public BotApiMethod<?> receivingCreateOrderCommand(long chatId, int messageId){
        String answer = """
                Хорошо, теперь четко опиши:
                
                1) Что тебе нужно
                2) В каком количестве
                3) Какой крайний срок доставки
                4) Твой адрес
                5) Другая дополнительная информация, которая может быть мне полезна
                Потом жди ответа.
                
                При формулировании заказа обязательно укажите этот хештег -- #newOrder
                """;

        return  SendMessage.builder()
                .chatId(chatId)
                .text(answer)
                .replyToMessageId(messageId)
                .build();
    }

    public BotApiMethod<?> receivingAskQuestionCommand(long chatId, int messageId){
        String answer = """
                Хорошо, я внимательно слушаю твой вопрос, как смогу сразу отвечу""" + Emoji.COWBOY.getValue() + "\n\nПри формулировании вопроса обязательно укажите этот хештег -- #newQuestion";

        return  SendMessage.builder()
                .chatId(chatId)
                .text(answer)
                .replyToMessageId(messageId)
                .build();
    }

    // Спец команда для продавца
    public BotApiMethod<?> receivingAnswerQuestionCommand(String message){
        String[] parsedCommand = Parser.parseCommand(message);
        String answer = parsedCommand[0].replace("/answerQ", "");

        return  SendMessage.builder()
                .chatId(parsedCommand[1])
                .text(answer)
                .build();
    }

    public BotApiMethod<?> messageForward(int messageId, long senderChatId){

        // Указываем chatId получателя (продавца)
        long receiverChatId = 000000000;

        // Пересылаем сообщение от одного пользователя продавцу
        return ForwardMessage.builder()
                .chatId(receiverChatId)
                .fromChatId(senderChatId)
                .messageId(messageId)
                .build();
    }

    public BotApiMethod<?> createOrder(int messageId, long chatId, String command, String tgNick, String username){
        Customer existingCustomer = customerRepository.findByChatId(chatId);
        if (existingCustomer != null){
            CustOrder newCustOrder =  new CustOrder(command,existingCustomer,false);
            if (existingCustomer.getOrders() != null){
                existingCustomer.getOrders().add(newCustOrder);
            }else {
                existingCustomer.setOrders(new ArrayList<>(Collections.singletonList(newCustOrder)));
            }
            orderRepository.save(newCustOrder);
            log.info("New order save");
        }else {
            Customer newCustomer = new Customer(tgNick,username,chatId);
            CustOrder newCustOrder =  new CustOrder(command,newCustomer,false);
            if (newCustomer.getOrders() != null){
                newCustomer.getOrders().add(newCustOrder);
            }else {
                newCustomer.setOrders(new ArrayList<>(Collections.singletonList(newCustOrder)));
            }
            customerRepository.save(newCustomer);
            orderRepository.save(newCustOrder);
            log.info("New order save");
        }
        return messageForward(messageId,chatId);
    }


    public BotApiMethod<?> createQuestion(int messageId, long chatId, String command, String tgNick, String username){
        Customer existingCustomer = customerRepository.findByChatId(chatId);
        if (existingCustomer != null){
            Question newQuestion =  new Question(command,existingCustomer,false);
            if (existingCustomer.getQuestions() != null){
                existingCustomer.getQuestions().add(newQuestion);
            }else {
                existingCustomer.setQuestions(new ArrayList<>(Collections.singletonList(newQuestion)));
            }
            questionRepository.save(newQuestion);
            log.info("New question save");
        }else {
            Customer newCustomer = new Customer(tgNick,username,chatId);
            Question newQuestion =  new Question(command,newCustomer,false);
            if (newCustomer.getQuestions() != null){
                newCustomer.getQuestions().add(newQuestion);
            }else {
                newCustomer.setQuestions(new ArrayList<>(Collections.singletonList(newQuestion)));
            }
            customerRepository.save(newCustomer);
            questionRepository.save(newQuestion);
            log.info("New order save");
        }
        return messageForward(messageId,chatId);
    }
}


