package me.kingtux.dwc;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;


import java.awt.*;

public class MessageBuilder {

    private EmbedBuilder builder;

    public MessageBuilder(String description) {
        this.builder = new EmbedBuilder().setDescription(description);
    }

    public MessageBuilder setTitle(String title, String url) {
        builder.setTitle(title, url);
        return this;
    }

    public MessageBuilder setTitle(String title) {
        builder.setTitle(title);
        return this;
    }

    public MessageBuilder setColor(Color color) {
        builder.setColor(color);
        return this;
    }

    public MessageBuilder addField(String name, String value, boolean inLine) {
        builder.addField(name, value, inLine);
        return this;
    }

    public MessageBuilder addThumbnail(String url) {
        builder.setThumbnail(url);
        return this;
    }


    public MessageEmbed build() {
        return builder.build();
    }

    public MessageBuilder footer(String s, String iconURL) {
        builder.setFooter(s, iconURL);
        return this;
    }
}