package com.example.WizardShopBot.util;

import lombok.Getter;

public enum Emoji {
    PILL("\uD83D\uDC8A"),

    IDOL("\uD83D\uDDFF"),

    PIG("\uD83D\uDC37"),

    FOX_FACE("\uD83E\uDD8A"),

    UNICORN_FACE("\uD83E\uDD84"),

    GRIN("\uD83D\uDE01"),

    CLOWN("\uD83E\uDD21"),

    GHOST("\uD83D\uDC7B"),

    ALIEN("\uD83D\uDC7D"),

    SCULL("☠️"),

    COWBOY("\uD83E\uDD20"),

    SPROUT("\uD83E\uDD91"),

    DRAGON("\uD83D\uDC09"),

    RAT("\uD83D\uDC00"),

    BLACK_MAN("\uD83D\uDC66\uD83C\uDFFF"),

    CRIPPLE("\uD83D\uDC68\uD83C\uDFFC\u200D\uD83E\uDDBD"),

    BOT_FACE("\uD83E\uDD16"),

    PAPER_ROLL("\uD83E\uDDFB");

    @Getter
    final String value;
    Emoji(String value) {
        this.value = value;
    }

    public static String getRandom(){
        int i = (int) (Math.random()*18);
        for (Emoji s : Emoji.values()) {
            if (s.ordinal() == i) {
                return s.getValue();
            }
        }
        return null;
    }

}
