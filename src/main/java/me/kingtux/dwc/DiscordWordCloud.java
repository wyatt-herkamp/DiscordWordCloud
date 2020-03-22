package me.kingtux.dwc;

import dev.nitrocommand.jda4.JDA4CommandCore;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiscordWordCloud {
    public static final Logger LOGGER = LoggerFactory.getLogger(DiscordWordCloud.class);
    private JDA jda;
    private final JDA4CommandCore commandCore;
    private final File tmpFolder = new File("tmp");
    private final File guilds = new File("guilds");
    private ExecutorService executors = Executors.newSingleThreadExecutor();
    private Map<Guild, File> guildFiles = new HashMap<>();

    public DiscordWordCloud(Properties properties) {
        try {
            jda = JDABuilder.createDefault(properties.getProperty("discord.token")).addEventListeners(new DWCEventListener(this)).build();
        } catch (LoginException e) {
            LOGGER.error("Unable to login into discord", e);
        }
        commandCore = new JDA4CommandCore(jda, "!");
        commandCore.registerCommand(new WordCloudCommand(this));
        if(!tmpFolder.exists())tmpFolder.mkdirs();
        tmpFolder.deleteOnExit();
    }

    public File getTmpFolder() {
        return tmpFolder;
    }

    public ExecutorService getExecutors() {
        return executors;
    }

    public File getGuilds() {
        return guilds;
    }

    public Map<Guild, File> getGuildFiles() {
        return guildFiles;
    }

    public void addGuild(Guild guild) {
        File guildFolder = new File(guilds, guild.getId());
        if (!guildFolder.exists())
            guildFolder.mkdirs();
        File messageFile = new File(guildFolder, "message.txt");
        try {
            if (!messageFile.exists())
                messageFile.createNewFile();
        } catch (IOException e) {
            LOGGER.error("Unable to create guild file", e);
        }
        guildFiles.put(guild, messageFile);
    }

    public void remove(Guild guild) {
        guildFiles.get(guild).delete();
        guildFiles.remove(guild);
    }

    public JDA getJDA() {
        return jda;
    }
}
