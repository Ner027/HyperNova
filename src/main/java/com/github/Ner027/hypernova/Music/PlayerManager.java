package com.github.Ner027.hypernova.Music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PlayerManager
{
    private static PlayerManager playerManager;
    private final AudioPlayerManager audioManager;
    public final Map<Long, GuildMusicManager> musicManagers;

    private PlayerManager()
    {
        this.musicManagers = new HashMap<>();
        this.audioManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(audioManager);
        AudioSourceManagers.registerLocalSource(audioManager);

    }

    public synchronized GuildMusicManager getGuildMusicManager(Guild guild)
    {
        long guildID = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildID);

        if (musicManager == null)
        {
            musicManager = new GuildMusicManager(audioManager, guild);
            musicManagers.put(guildID, musicManager);
        }
        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
        return musicManager;
    }

    public void loadAndPlay(TextChannel channel, String trackURl)
    {


        GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());
        audioManager.loadItemOrdered(musicManager, trackURl, new AudioLoadResultHandler()
        {
            @Override
            public void trackLoaded(AudioTrack track)
            {
                play(musicManager, track);
                musicManager.updateQueue(channel.getGuild());

            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist)
            {
                AudioTrack firstTrack = playlist.getSelectedTrack();
                if (firstTrack == null)
                {
                    firstTrack = playlist.getTracks().get(0);
                }
            }

            @Override
            public void noMatches()
            {

            }

            @Override
            public void loadFailed(FriendlyException exception)
            {
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("Ooops something is wrong");
                builder.addField("", "Something went wrong when loading your track, too bad!", false);
                builder.setColor(Color.magenta);
                Message msg;

                msg = channel.sendMessage(builder.build()).complete();
                msg.delete().queueAfter(5, TimeUnit.SECONDS);
            }
        });
    }

    private void play(GuildMusicManager musicManager, AudioTrack track)
    {
        musicManager.scheduler.queue(track);
    }

    public static synchronized PlayerManager getInstance()
    {
        if (playerManager == null)
        {
            playerManager = new PlayerManager();
        }
        return playerManager;
    }
}
