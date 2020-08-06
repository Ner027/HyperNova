package com.github.Ner027.hypernova.RandomCommands;

import com.github.Ner027.hypernova.Command;
import com.github.Ner027.hypernova.Constants;
import com.github.Ner027.hypernova.GuildVars;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class EndGame implements Command
{
    @Override
    public void exec(String[] args, GuildMessageReceivedEvent event)
    {
        GuildVars vars = Constants.databaseUtil.getGuildVars(event.getGuild());

        if (vars.teamBlue != null && vars.teamRed != null)
        {
            if (Constants.discordUtil.hasRole(vars.managerRole, event.getMember()))
            {
                if (args[1].equalsIgnoreCase("blue") || args[1].equals("red"))
                {
                    if (vars.mainChannel != null)
                    {
                        for (Member m : vars.memberList)
                        {
                            if (m.getVoiceState() != null)
                            {
                                if (m.getVoiceState().inVoiceChannel())
                                {
                                    event.getGuild().moveVoiceMember(m, vars.mainChannel).complete();
                                }
                            }
                        }
                    }

                    List<Member> temp = null;

                    if (args[1].equalsIgnoreCase("blue"))
                    {
                        temp = vars.teamBlue;
                    } else if (args[1].equalsIgnoreCase("red"))
                        temp = vars.teamRed;

                    if (temp != null)
                    {
                        if (!temp.isEmpty())
                        {
                            for (Member m : temp)
                            {
                                if (Constants.databaseUtil.getMembers(m) == 0)
                                {
                                    Constants.databaseUtil.insertMembers(m);
                                }
                                Constants.databaseUtil.addWin(m);
                            }

                        }
                    }
                } else
                {
                    Constants.discordUtil.tempMessage("Please provide a valid team!", event.getChannel());
                }

                if (vars.voiceBlue != null)
                    vars.voiceBlue.delete().queue();
                if (vars.voiceRed != null)
                    vars.voiceRed.delete().queue();

                Constants.discordUtil.SetupRandom(event.getGuild(), vars.randomChannel);

            } else
            {
                Constants.discordUtil.tempMessage("You dont have the required role!", event.getChannel());
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
        return "endgame";
    }
}
