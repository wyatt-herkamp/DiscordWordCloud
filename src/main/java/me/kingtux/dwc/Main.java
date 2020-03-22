package me.kingtux.dwc;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        File file = new File("bot.properties");
        if (!file.exists()) {
            try {
                FileUtils.copyInputStreamToFile(Main.class.getResourceAsStream("/bot.properties"), file);
            } catch (IOException e) {
                DiscordWordCloud.LOGGER.error("Unable to copy bot.properties", e);
                return;
            }
        }
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(file));
        } catch (IOException e) {
            DiscordWordCloud.LOGGER.error("Unable to load bot.properties", e);
            return;
        }
        new DiscordWordCloud(properties);
    }

}
