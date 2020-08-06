package com.github.Ner027.hypernova.RandomCommands;

import com.github.Ner027.hypernova.Command;
import com.github.Ner027.hypernova.Constants;
import com.github.Ner027.hypernova.GuildVars;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class GenerateTeams implements Command
{
    GuildVars vars;

    @Override
    public void exec(String[] args, GuildMessageReceivedEvent event)
    {
        vars = Constants.databaseUtil.getGuildVars(event.getGuild());
        Constants.discordUtil.genTeams(vars, event.getMember(), event.getChannel());
    }

    @Override
    public String getHelp()
    {
        return null;
    }

    @Override
    public String getInvoke()
    {
        return "generate";
    }


}
