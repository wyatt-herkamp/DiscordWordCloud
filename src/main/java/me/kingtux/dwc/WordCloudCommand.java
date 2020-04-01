package me.kingtux.dwc;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.filter.Filter;
import com.kennycason.kumo.palette.ColorPalette;
import com.kennycason.kumo.palette.LinearGradientColorPalette;
import dev.nitrocommand.core.annotations.*;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import static java.awt.Color.*;

@NitroCommand(command = {"wordcloud", "wc"}, format = "wordcloud", description = "Generates a Word Cloud")
public class WordCloudCommand {
    private DiscordWordCloud discordCloud;

    public WordCloudCommand(DiscordWordCloud discordWordCloud) {
        this.discordCloud = discordWordCloud;
    }

    @BaseCommand()
    public void baseCommand(Message message, Guild guild) {
        MessageEmbed messageEmbed = new MessageBuilder("Welcome to " + discordCloud.getJDA().getSelfUser().getName()).
                addField("To generate Square", "!wc square",false).
                addField("To generate Circle", "!wc Circle",false).build();


        message.getTextChannel().sendMessage(messageEmbed).queue();
    }

    @SubCommand(format = "square")
    public void square(Message message) {
        squareWithWidth("300", message);
    }

    @SubCommand(format = "square {width}")
    public void squareWithWidth(@CommandArgument("width") String square, Message message) {
        if (!Utils.isInt(square)) {
            MessageEmbed messageEmbed = new MessageBuilder("Failed to Pass Integer").build();
            message.getTextChannel().sendMessage(messageEmbed).queue();
            return;
        }
        generateSquare(Integer.parseInt(square), message);
    }

    @SubCommand(format = "circle")
    public void circle(Message message) {
        circleWithRadius("150", message);
    }

    @SubCommand(format = "circle {radius}")
    public void circleWithRadius(@CommandArgument("radius") String radius, Message message) {
        if (!Utils.isInt(radius)) {
            MessageEmbed messageEmbed = new MessageBuilder("Failed to Pass Integer").build();
            message.getTextChannel().sendMessage(messageEmbed).queue();
            return;
        }
        generateCircle(Integer.parseInt(radius), message);
    }

    @SubCommand(format = "ignore {potato}")
    public void removeWord(@CommandArgument("potato") String word, Message message) {
        if (discordCloud.addWord(message.getGuild(), word)) {
            message.getTextChannel().sendMessage("Added " + word).queue();
        } else {
            message.getTextChannel().sendMessage("Failed to add " + word).queue();

        }
    }

    public void generateCircle(int radius, Message message) {
        discordCloud.getExecutors().execute(() -> {
            FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
            frequencyAnalyzer.setFilter(new SimpleFilter(discordCloud.getGuildBannedWords(message.getGuild())));
            List<WordFrequency> wordFrequencies = null;
            try {
                wordFrequencies = frequencyAnalyzer.load(discordCloud.getGuildFiles().get(message.getGuild()));
            } catch (IOException e) {
                DiscordWordCloud.LOGGER.error("Failure", e);
                return;
            }

            Dimension dimension = new Dimension(radius * 2, radius * 2);
            WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
            wordCloud.setBackground(new CircleBackground(radius));
            wordCloud.setColorPalette(new ColorPalette(getColors()));
            wordCloud.setFontScalar(new LinearFontScalar(12, 50));
            wordCloud.setPadding(1);
            wordCloud.build(wordFrequencies);
            File file = new File(discordCloud.getTmpFolder(), System.currentTimeMillis() + ".png");
            wordCloud.writeToFile(file.getAbsolutePath());
            message.getTextChannel().sendMessage("Word Cloud Is Ready").addFile(file).queue();
        });
    }

    public void generateSquare(int squareHeight, Message message) {
        discordCloud.getExecutors().execute(() -> {
            FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
            frequencyAnalyzer.setFilter(new SimpleFilter(discordCloud.getGuildBannedWords(message.getGuild())));
            List<WordFrequency> wordFrequencies = null;
            try {
                wordFrequencies = frequencyAnalyzer.load(discordCloud.getGuildFiles().get(message.getGuild()));
            } catch (IOException e) {
                DiscordWordCloud.LOGGER.error("Failure", e);
                return;
            }

            Dimension dimension = new Dimension(squareHeight, squareHeight);
            WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
            wordCloud.setBackground(new RectangleBackground(dimension));
            wordCloud.setColorPalette(new ColorPalette(getColors()));
            wordCloud.setFontScalar(new LinearFontScalar(12, 50));
            wordCloud.setPadding(1);
            wordCloud.build(wordFrequencies);
            File file = new File(discordCloud.getTmpFolder(), System.currentTimeMillis() + ".png");
            wordCloud.writeToFile(file.getAbsolutePath());
            message.getTextChannel().sendMessage("Word Cloud Is Ready").addFile(file).queue();
        });
    }

    private Color[] getColors() {
        Random r = new Random();
        Color[] colors = new Color[100];
        for (int i = 0; i < 100; i++) {
            colors[i] = new Color(156 + r.nextInt(100), 156 + r.nextInt(100), 156 + r.nextInt(100));
        }
        return colors;
    }
}
