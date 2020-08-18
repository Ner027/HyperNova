package com.github.Ner027.hypernova.RandomCommands;

import com.github.Ner027.hypernova.Command;
import com.github.Ner027.hypernova.Constants;
import com.github.Ner027.hypernova.GuildVars;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class JoinRandom implements Command
{
    @Override
    public void exec(String[] args, GuildMessageReceivedEvent event)
    {
        GuildVars vars = Constants.databaseUtil.getGuildVars(event.getGuild());

        if (event.getMember() != null)
        {
            if (!vars.memberList.contains(event.getMember()))
            {
                vars.memberList.add(event.getMember());
                Constants.discordUtil.updateList(vars);
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
        return "join";
    }
}
