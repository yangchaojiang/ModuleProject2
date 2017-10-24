package com.translatemodule;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

/**
 * Created by yangc on 2017/9/18.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */

public class SSS {
    public static void main(String... args) throws Exception {
        // Instantiates a client
       // Translate translate = TranslateOptions.getDefaultInstance().getService();

        // The text to translate
        String text = "Hello, world!";
        //IlyT_G5GW6RWH_aAxoUI32
        // Translates some text into Russian
        TranslateOptions translateOptions =TranslateOptions.newBuilder().setApiKey("TranslateOptions").setProjectId("plasma-outcome-180302").build();
        Translate translate =translateOptions.getService();
        Translation translation =
                translate.translate(
                        text,
                        Translate.TranslateOption.sourceLanguage("en"),
                        Translate.TranslateOption.targetLanguage("ru"));


        System.out.printf("Text: %s%n", text);
        System.out.printf("Translation: %s%n", translation.getTranslatedText());
    }
}
