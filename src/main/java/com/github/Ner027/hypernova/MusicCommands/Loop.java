package com.github.Ner027.hypernova.MusicCommands;

import com.github.Ner027.hypernova.Command;
import com.github.Ner027.hypernova.Constants;
import com.github.Ner027.hypernova.Music.GuildMusicManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Loop implements Command
{
    @Override
    public void exec(String[] args, GuildMessageReceivedEvent event)
    {
        Constants.discordUtil.toggleLoop(event.getGuild());
    }

    @Override
    public String getHelp()
    {
        return "Not implemented !!!!";
    }

    @Override
    public String getInvoke()
    {
        return "loop";
    }
}
