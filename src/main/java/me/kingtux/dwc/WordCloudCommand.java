package me.kingtux.dwc;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;
import com.kennycason.kumo.palette.LinearGradientColorPalette;
import dev.nitrocommand.core.annotations.BaseCommand;
import dev.nitrocommand.core.annotations.NitroCommand;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

@NitroCommand(command = {"wordcloud", "wc"}, format = "wordcloud", description = "Generates a Word Cloud")
public class WordCloudCommand {
    private DiscordWordCloud discordCloud;

    public WordCloudCommand(DiscordWordCloud discordWordCloud) {
        this.discordCloud = discordWordCloud;
    }

    @BaseCommand
    public void baseCommand(Message message) {
        //Now I need to learn the Kumo Library
        discordCloud.getExecutors().execute(() -> {
            TextChannel channel = message.getTextChannel();
            FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
            List<WordFrequency> wordFrequencies = null;
            try {
                wordFrequencies = frequencyAnalyzer.load(discordCloud.getGuildFiles().get(channel.getGuild()));
            } catch (IOException e) {
                DiscordWordCloud.LOGGER.error("Failure", e);
                return;
            }
            Dimension dimension = new Dimension(600, 600);
            WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
            wordCloud.setPadding(0);
            wordCloud.setBackground(new RectangleBackground(dimension));
            wordCloud.setColorPalette(new ColorPalette(Color.white));
            wordCloud.setFontScalar(new LinearFontScalar(10, 40));
            wordCloud.build(wordFrequencies);
            File file = new File(discordCloud.getTmpFolder(), System.currentTimeMillis() + ".png");
            wordCloud.writeToFile(file.getAbsolutePath());
            channel.sendMessage("Word Cloud Is Ready").addFile(file).queue();
        });

    }
}
