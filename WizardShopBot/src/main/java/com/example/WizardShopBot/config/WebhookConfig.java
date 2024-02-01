package com.example.WizardShopBot.config;

import com.example.WizardShopBot.service.WizardShopBot;
import com.example.WizardShopBot.service.handlers.CommandHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Configuration
@AllArgsConstructor
public class WebhookConfig {
    private final BotConfig botConfig;

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(botConfig.getPath()).build();
    }

    @Bean
    public WizardShopBot springWebhookBot(SetWebhook setWebhook, CommandHandler commandHandler) {
        WizardShopBot bot = new WizardShopBot(setWebhook, commandHandler);

        bot.setBotPath(botConfig.getPath());
        bot.setBotUsername(botConfig.getUsername());
        bot.setBotToken(botConfig.getToken());

        return bot;
    }
}


