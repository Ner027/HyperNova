package com.github.Ner027.hypernova.MusicCommands;

import com.github.Ner027.hypernova.Command;
import com.github.Ner027.hypernova.Constants;
import com.github.Ner027.hypernova.Music.GuildMusicManager;
import com.github.Ner027.hypernova.Music.PlayerManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

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

                if (index <= musicManager.scheduler.queue.size() && index >= 0)
                {
                    musicManager.scheduler.queue.remove(index);
                    musicManager.updateQueue();
                }
                else Constants.discordUtil.tempMessage("Please provide a valid track index!", event.getChannel(), Color.yellow);
            }
            catch (NumberFormatException e)
            {
                Constants.discordUtil.tempMessage("Please enter a valid number!", event.getChannel(),Color.yellow);
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
