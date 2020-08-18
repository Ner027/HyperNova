package com.github.Ner027.hypernova.RandomCommands;

import com.github.Ner027.hypernova.Command;
import com.github.Ner027.hypernova.Constants;
import com.github.Ner027.hypernova.GuildVars;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

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

                if (vars.voiceBlue != null)
                    vars.voiceBlue.delete().queue();
                if (vars.voiceRed != null)
                    vars.voiceRed.delete().queue();

                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("Which team won?");
                builder.addField("", "Please react with one of the emotes", false);
                builder.setFooter("Blue | Red");
                builder.setColor(Color.magenta);
                vars.endGameMessage = event.getChannel().sendMessage(builder.build()).complete();
                vars.endGameMessage.addReaction("\uD83D\uDD35").queue();
                vars.endGameMessage.addReaction("\uD83D\uDD34").queue();

            }
        }
        else
        {
            Constants.discordUtil.tempMessage("You dont have the required role!", event.getChannel(),Color.red);
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
