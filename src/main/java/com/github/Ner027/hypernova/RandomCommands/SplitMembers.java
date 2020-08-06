package com.github.Ner027.hypernova.RandomCommands;

import com.github.Ner027.hypernova.Command;
import com.github.Ner027.hypernova.Constants;
import com.github.Ner027.hypernova.GuildVars;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class SplitMembers implements Command
{
    @Override
    public void exec(String[] args, GuildMessageReceivedEvent event)
    {
        GuildVars vars = Constants.databaseUtil.getGuildVars(event.getGuild());

        vars.voiceRed = event.getGuild().createVoiceChannel("Red Team").complete();
        vars.voiceBlue = event.getGuild().createVoiceChannel("Blue Team").complete();
        vars.voiceBlue.getManager().setUserLimit(vars.teamBlue.size()).queue();
        vars.voiceRed.getManager().setUserLimit(vars.teamRed.size()).queue();


        if (!vars.teamBlue.isEmpty())
        {
            for (Member m : vars.teamBlue)
            {
                if (m.getVoiceState() != null)
                {
                    if (m.getVoiceState().inVoiceChannel())
                    {
                        event.getGuild().moveVoiceMember(m, vars.voiceBlue).complete();
                    }
                }
            }
        }

        if (!vars.teamRed.isEmpty())
        {
            for (Member m : vars.teamRed)
            {
                if (m.getVoiceState() != null)
                {
                    if (m.getVoiceState().inVoiceChannel())
                    {
                        event.getGuild().moveVoiceMember(m, vars.voiceRed).complete();
                    }
                }
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
        return "split";
    }
}
