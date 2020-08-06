package com.github.Ner027.hypernova.MusicCommands;

import com.github.Ner027.hypernova.Command;
import com.github.Ner027.hypernova.Constants;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Join implements Command
{
    @Override
    public void exec(String[] args, GuildMessageReceivedEvent event)
    {
        Constants.discordUtil.joinVoice(event.getGuild(), event.getMember());

    }

    @Override
    public String getHelp()
    {
        return null;
    }

    @Override
    public String getInvoke()
    {
        return "join";
    }
}
