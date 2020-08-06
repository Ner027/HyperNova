package com.github.Ner027.hypernova.MusicCommands;

import com.github.Ner027.hypernova.Command;
import com.github.Ner027.hypernova.Constants;
import com.github.Ner027.hypernova.Music.GuildMusicManager;
import com.github.Ner027.hypernova.Music.PlayerManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Remove implements Command
{

    @Override
    public void exec(String[] args, GuildMessageReceivedEvent event)
    {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());

        if (!musicManager.scheduler.queue.isEmpty())
        {
            int index;
            try
            {
                index = Integer.parseInt(args[1]) - 1;


                System.out.println(musicManager.scheduler.queue.size());
                if (index <= musicManager.scheduler.queue.size() && index >= 0)
                {
                    System.out.println(index);
                    musicManager.scheduler.queue.remove(index);
                    musicManager.updateQueue(event.getGuild());
                } else Constants.discordUtil.tempMessage("Please provide a valid track index!", event.getChannel());
            } catch (NumberFormatException e)
            {
                Constants.discordUtil.tempMessage("Please enter a valid number!", event.getChannel());
            }
        }
    }

    @Override
    public String getHelp()
    {
        return null;
    }

    @Override
    public String getInvoke()
    {
        return "remove";
    }
}
