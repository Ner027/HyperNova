package com.github.Ner027.hypernova.AdminCommands;

import com.github.Ner027.hypernova.Command;
import com.github.Ner027.hypernova.Constants;
import com.github.Ner027.hypernova.GuildVars;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class AddManager implements Command
{

    @Override
    public void exec(String[] args, GuildMessageReceivedEvent event)
    {
        GuildVars vars = Constants.databaseUtil.getGuildVars(event.getGuild());

        if (vars.managerRole != null)
        {
            if (!args[1].isEmpty())
            {
                for (Member m : event.getMessage().getMentionedMembers())
                {
                    event.getGuild().addRoleToMember(m, vars.managerRole).queue();
                }
            }
        } else
        {
            Constants.discordUtil.tempMessage("There is no manager role set for this server", event.getChannel());
        }
    }


    @Override
    public String getHelp()
    {
        return "If there is a manager role set to this server you can add it to a user by using this command and mentioning one or more users";
    }

    @Override
    public String getInvoke()
    {
        return "addmanager";
    }
}
