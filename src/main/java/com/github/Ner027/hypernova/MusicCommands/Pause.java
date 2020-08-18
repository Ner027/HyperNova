package com.github.Ner027.hypernova.MusicCommands;

import com.github.Ner027.hypernova.Command;
import com.github.Ner027.hypernova.Constants;
import com.github.Ner027.hypernova.Music.GuildMusicManager;
import com.github.Ner027.hypernova.Music.PlayerManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Pause implements Command
{

    @Override
    public void exec(String[] args, GuildMessageReceivedEvent event)
    {
        Constants.discordUtil.pause(event.getGuild());
    }

    @Override
    public String getHelp()
    {
        return null;
    }

    @Override
    public String getInvoke()
    {
        return "pause";
    }
}
