package com.example.WizardShopBot.service;

import com.example.WizardShopBot.service.handlers.CommandHandler;
import com.example.WizardShopBot.util.WordRecordeder;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateDispatcher {
    final CommandHandler commandHandler;


    @Autowired
    public UpdateDispatcher( CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public BotApiMethod<?> distribute(Update update) {
        if (update.hasMessage() ) {
            WordRecordeder.writeFile("\n\n" + update.getMessage().getChat().getFirstName().toUpperCase() + "  "
                    + update.getMessage().getChat().getId().toString().toUpperCase()
                    +"\n" +update.getMessage().getText());

            if (update.getMessage().hasText() && update.getMessage().getText().charAt(0) == '/' ) {
                return commandHandler.answer(update);
            }
        }
        return commandHandler.answer(update);
    }

}