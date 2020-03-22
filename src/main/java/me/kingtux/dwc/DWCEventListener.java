package me.kingtux.dwc;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.io.*;

public class DWCEventListener extends ListenerAdapter {
    private DiscordWordCloud wordCloud;

    public DWCEventListener(DiscordWordCloud discordWordCloud) {
        this.wordCloud = discordWordCloud;
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        //TODO ignore code messages ``` and `
        String[] message = event.getMessage().getContentStripped().split(" ");
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(wordCloud.getGuildFiles().get(event.getGuild()),true));
            for (String s : message) {
                String newString = s.replaceAll("[^a-zA-Z]","");
                bufferedWriter.newLine();
                bufferedWriter.write(newString);
            }
            bufferedWriter.close();
            //Cant remember how to do new lines

        } catch (IOException e) {
            DiscordWordCloud.LOGGER.error("Unable to print", e);
        }
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        System.out.println(wordCloud.getJDA().getInviteUrl(Permission.MESSAGE_HISTORY, Permission.MESSAGE_READ, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_WRITE));
    }

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event) {
        if (!wordCloud.getGuildFiles().containsKey(event.getGuild())) {
            wordCloud.addGuild(event.getGuild());
        }
    }

    @Override
    public void onGuildReady(@Nonnull GuildReadyEvent event) {
        if (!wordCloud.getGuildFiles().containsKey(event.getGuild())) {
            wordCloud.addGuild(event.getGuild());
        }
    }

    @Override
    public void onGuildLeave(@Nonnull GuildLeaveEvent event) {
        wordCloud.remove(event.getGuild());
    }
}
