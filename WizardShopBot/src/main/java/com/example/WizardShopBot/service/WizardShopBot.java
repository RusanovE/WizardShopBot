package com.example.WizardShopBot.service;

import com.example.WizardShopBot.service.handlers.CommandHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
@Getter
@Setter
public class WizardShopBot extends TelegramWebhookBot {
    String botPath;
    String botUsername;
    String botToken;

    final CommandHandler commandHandler;
    final UpdateDispatcher updateDispatcher;

    public WizardShopBot(SetWebhook setWebhook, CommandHandler commandHandler) {
        super(String.valueOf(setWebhook));
        this.commandHandler = commandHandler;
        this.updateDispatcher = new UpdateDispatcher( commandHandler);
        List<BotCommand> listOfBotCommands = new ArrayList<>();
        listOfBotCommands.add(new BotCommand("/help","for description all bot`s methods"));
        listOfBotCommands.add(new BotCommand("/price_list","for get a list of prices for products or services provided by the bot"));
        listOfBotCommands.add(new BotCommand("/create_order","for create a new order using this command"));
        listOfBotCommands.add(new BotCommand("/ask_question","for send feedback to the author"));
        try {
            execute(new SetMyCommands(listOfBotCommands, new BotCommandScopeDefault(),null));
        }catch (TelegramApiException e){
            log.error("error with list of command" + e.getMessage());
        }
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            File file = new File("your path to file");
            if (update.hasMessage() && update.getMessage().getText().contains("/sendDoc")) {

                    SendDocument sendDocument = new SendDocument();
                    sendDocument.setChatId(update.getMessage().getChatId().toString());
                    sendDocument.setDocument(new InputFile(file));

                    try {
                    execute(sendDocument);
                } catch (TelegramApiException e) {
                   log.error(e.getMessage());
                }
            }
            return updateDispatcher.distribute(update);
        } catch (IllegalArgumentException e) {
            log.debug("EXCEPTION_ILLEGAL_MESSAGE " + e.getMessage());
            return null;
        } catch (Exception e) {
            log.debug("EXCEPTION_WHAT_THE_FUCK " + e.getMessage());
            return null;
        }
    }
}