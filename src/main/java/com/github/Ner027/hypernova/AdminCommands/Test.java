package com.github.Ner027.hypernova.AdminCommands;

import com.github.Ner027.hypernova.Command;
import com.github.Ner027.hypernova.Util;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Test implements Command
{
    @Override
    public void exec(String[] args, GuildMessageReceivedEvent event)
    {

    }

    @Override
    public String getHelp()
    {
        return null;
    }

    @Override
    public String getInvoke()
    {
        return "test";
    }
}
