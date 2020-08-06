package com.github.Ner027.hypernova.AdminCommands;

import com.github.Ner027.hypernova.Command;
import com.github.Ner027.hypernova.Constants;
import com.github.Ner027.hypernova.GuildVars;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class MainVoice implements Command
{
    @Override
    public void exec(String[] args, GuildMessageReceivedEvent event)
    {
        GuildVars vars = Constants.databaseUtil.getGuildVars(event.getGuild());

        if (event.getMember() != null)
        {
            if (event.getMember().getVoiceState() != null)
            {
                if (event.getMember().getVoiceState().inVoiceChannel())
                {
                    vars.mainChannel = event.getMember().getVoiceState().getChannel();
                    if (vars.mainChannel != null)
                    {
                        Constants.databaseUtil.editDatabase(vars.mainChannel.getIdLong(), event.getGuild(), "mainVoice");
                    }
                }
            }
        }
    }

    @Override
    public String getHelp()
    {
        return "By joining a voice channel and executing this command you will set that as the main voice channel for this server";
    }

    @Override
    public String getInvoke()
    {
        return "setvoice";
    }
}
