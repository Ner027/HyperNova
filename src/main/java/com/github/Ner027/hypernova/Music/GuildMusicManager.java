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

    public final Guild guild;

    /**
     * Creates a player and a track scheduler.
     *
     * @param manager Audio player manager to use for creating the player.
     */
    public GuildMusicManager(AudioPlayerManager manager, Guild guild)
    {
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player, this, Constants.databaseUtil.getGuildVars(guild));
        this.guild = guild;
        player.addListener(scheduler);
    }

    /**
     * @return Wrapper around AudioPlayer to use it as an AudioSendHandler.
     */
    public AudioPlayerSendHandler getSendHandler()
    {
        return new AudioPlayerSendHandler(player);
    }

    public void updateQueue()
    {

        GuildVars vars = Constants.databaseUtil.getGuildVars(guild);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Music Queue");
        embedBuilder.setColor(Color.magenta);


        if (!this.scheduler.queue.isEmpty())
        {
            if (this.scheduler.queue.size() >= 10)
            {
                for (int i = 0; i < 10; i++)
                {
                    embedBuilder.addField((i + 1) + ".", this.scheduler.queue.get(i).getInfo().title, false);
                }
                int left = (this.scheduler.queue.size() - 10);

                if(left > 0)
                {
                    embedBuilder.setFooter("And " + left + " more");
                }

            }
            else
            {
                int i = 0;
                for(AudioTrack t: this.scheduler.queue)
                {
                    i++;
                    embedBuilder.addField(i + ".",t.getInfo().title,false);
                }
                embedBuilder.setFooter("");
            }
        }
        else embedBuilder.addField("", "Nothing else queued!", false);

        if (this.player.getPlayingTrack() == null)
        {
            vars.musicMessageEmbed.setTitle("Nothing playing at the moment");
        } else vars.musicMessageEmbed.setTitle("Now playing: " + this.player.getPlayingTrack().getInfo().title);

        vars.musicMessage = vars.musicMessage.editMessage(vars.musicMessageEmbed.build()).complete();
        vars.musicQueue = vars.musicQueue.editMessage(embedBuilder.build()).complete();


    }
}