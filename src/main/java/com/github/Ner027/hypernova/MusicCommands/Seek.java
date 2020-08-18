package com.github.Ner027.hypernova.MusicCommands;

import com.github.Ner027.hypernova.Command;
import com.github.Ner027.hypernova.Constants;
import com.github.Ner027.hypernova.Music.GuildMusicManager;
import com.github.Ner027.hypernova.Music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Seek implements Command
{
    @Override
    public void exec(String[] args, GuildMessageReceivedEvent event)
    {
        if (Constants.discordUtil.sameVoice(event.getMember(), event.getGuild()))
        {
            GuildMusicManager musicManager = PlayerManager.getInstance().getGuildMusicManager(event.getGuild());

            if (musicManager.player.getPlayingTrack() != null)
            {
                if (args.length > 1)
                {

                    String[] inp = args[1].split(":"); //We start by splitting the input string into an array on every ":"

                    //Initialize vars
                    List<Long> foo = new ArrayList<>();
                    long time = 0;

                    //For each part of the string we try to parse it into a long
                    //If the user is a dumbass and types something invalid
                    //A warning will be sent to the channel

                    for (String s : inp)
                    {
                        try
                        {
                            foo.add(Long.parseLong(s)); //we add the parsed element to a long list
                        }
                        catch (NumberFormatException e)
                        {
                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setColor(Color.orange);
                            embedBuilder.setTitle("Please provide a valid time!");
                            embedBuilder.addField("","Time should be provided in HH:MM:SS format!",false);
                            Message msg = event.getChannel().sendMessage(embedBuilder.build()).complete();
                            msg.delete().queueAfter(5, TimeUnit.SECONDS);
                            return;

                            //If something goes wrong after parsing one of the strings, a warning is sent and this thing returns
                        }
                    }

                    Collections.reverse(foo);//Reverse the list just to avoid more sheneningans

                    //The way that this thing works is by going trough each of the time units
                    //and multiply it by 60 powered to i
                    //Since we reversed that list the first one will be the seconds so we will
                    //have something like 60^0 * seconds.
                    //Since 60^0 == 1, the seconds remain the same, then for minutes we do
                    //60^1,so we will have something like 60 * minutes, which will convert it into seconds
                    //and this goes on and on forever

                    for(int i = 0; i < foo.size();i++)
                    {
                        time += (Math.pow(60D,i) * foo.get(i));
                    }

                    time *= 1000; //we just multiply it by 1000 bcs we need it in millis

                    if(time <= musicManager.player.getPlayingTrack().getDuration())
                    {
                        musicManager.player.getPlayingTrack().setPosition(time);
                    }else Constants.discordUtil.tempMessage("The time provided exceeds the duration of whats playing",event.getChannel(),Color.orange);
                }
            }else Constants.discordUtil.tempMessage("Nothing playing at the moment",event.getChannel(),Color.orange);
        }

    }

    @Override
    public String getHelp()
    {
        return "By providing a time in HH:MM:SS format will set the player into that position";
    }

    @Override
    public String getInvoke()
    {
        return "seek";
    }
}
