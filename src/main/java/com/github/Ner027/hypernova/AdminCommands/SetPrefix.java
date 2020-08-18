package com.github.Ner027.hypernova.AdminCommands;

import com.github.Ner027.hypernova.Command;
import com.github.Ner027.hypernova.Constants;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class SetPrefix implements Command
{
    @Override
    public void exec(String[] args, GuildMessageReceivedEvent event)
    {
        if (args[1].length() <= 10)
        {
            Constants.databaseUtil.getGuildVars(event.getGuild()).prefix = args[1];
            Constants.databaseUtil.addPrefix(event.getGuild(), args[1]);
        } else Constants.discordUtil.tempMessage("Prefixes must be shorter than 10 characters", event.getChannel(), Color.yellow);
    }

    @Override
    public String getHelp()
    {
        return "By executing this command and providing a valid string shorter than 10 characters the bot prefix will be changed to that";
    }

    @Override
    public String getInvoke()
    {
        return "setprefix";
    }
}
