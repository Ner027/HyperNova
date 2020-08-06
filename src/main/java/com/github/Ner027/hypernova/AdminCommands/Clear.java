package com.github.Ner027.hypernova.AdminCommands;

import com.github.Ner027.hypernova.Command;
import com.github.Ner027.hypernova.Constants;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Clear implements Command
{
    @Override
    public void exec(String[] args, GuildMessageReceivedEvent event)
    {
        Constants.discordUtil.clear(args[1], event.getChannel());
    }

    @Override
    public String getHelp()
    {
        return "By providing a valid integer after the command, the bot will wipe all messages in that range";
    }

    @Override
    public String getInvoke()
    {
        return "clear";
    }
}
