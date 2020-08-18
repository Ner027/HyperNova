package com.github.Ner027.hypernova.Music;

import com.github.Ner027.hypernova.GuildVars;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.ArrayList;
import java.util.List;

/**
 * This class schedules tracks for the audio player. It contains the queue of tracks.
 */
public class TrackScheduler extends AudioEventAdapter
{
    private final AudioPlayer player;
    public final List<AudioTrack> queue;
    private final GuildMusicManager musicManager;
    private final GuildVars vars;
    public AudioTrack loopTrack;

    /**
     * @param player The audio player this scheduler uses
     */
    public TrackScheduler(AudioPlayer player, GuildMusicManager manager, GuildVars v)
    {
        this.player = player;
        this.queue = new ArrayList<>();
        this.musicManager = manager;
        this.vars = v;
    }

    /**
     * Add the next track to queue or play right away if nothing is in the queue.
     *
     * @param track The track to play or add to queue.
     */
    public void queue(AudioTrack track)
    {
        // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
        // something is playing, it returns false and does nothing. In that case the player was already playing so this
        // track goes to the queue instead.
        if (!player.startTrack(track, true))
        {
            queue.add(track);
        }
    }

    /**
     * Start the next track, stopping the current one if it is playing.
     */
    public void nextTrack()
    {
        AudioTrack track = null;

        if (queue.size() > 0)
            track = queue.get(0);

        player.startTrack(track, false);

        musicManager.updateQueue();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason)
    {
        if (endReason.mayStartNext)
        {
            nextTrack();
        }
        queue.remove(0);
        musicManager.updateQueue();
    }
}