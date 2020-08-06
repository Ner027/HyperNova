package com.github.Ner027.hypernova.Music;

import com.github.Ner027.hypernova.Constants;
import com.github.Ner027.hypernova.GuildVars;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;

import java.awt.*;

/**
 * Holder for both the player and a track scheduler for one guild.
 */
public class GuildMusicManager
{
    /**
     * Audio player for the guild.
     */
    public final AudioPlayer player;
    /**
     * Track scheduler for the player.
     */
    public final TrackScheduler scheduler;

    /**
     * Creates a player and a track scheduler.
     *
     * @param manager Audio player manager to use for creating the player.
     */
    public GuildMusicManager(AudioPlayerManager manager, Guild guild)
    {
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player, this, guild);
        player.addListener(scheduler);
    }

    /**
     * @return Wrapper around AudioPlayer to use it as an AudioSendHandler.
     */
    public AudioPlayerSendHandler getSendHandler()
    {
        return new AudioPlayerSendHandler(player);
    }

    public void updateQueue(Guild guild)
    {
        GuildVars vars = Constants.databaseUtil.getGuildVars(guild);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Music Queue");
        embedBuilder.setColor(Color.magenta);


        if (!this.scheduler.queue.isEmpty())
        {
            int i = 0;

            for (AudioTrack t : this.scheduler.queue)
            {
                i++;
                embedBuilder.addField(i + ".", t.getInfo().title, false);
            }
        } else embedBuilder.addField("", "Nothing else queued!", false);


        if (this.player.getPlayingTrack() == null)
        {
            vars.musicMessageEmbed.setTitle("Nothing playing at the moment");
        } else vars.musicMessageEmbed.setTitle("Now playing: " + this.player.getPlayingTrack().getInfo().title);

        vars.musicMessage = vars.musicMessage.editMessage(vars.musicMessageEmbed.build()).complete();
        vars.musicQueue = vars.musicQueue.editMessage(embedBuilder.build()).complete();


    }
}