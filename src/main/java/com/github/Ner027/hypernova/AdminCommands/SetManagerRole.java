package com.github.Ner027.hypernova.AdminCommands;


import com.github.Ner027.hypernova.Command;
import com.github.Ner027.hypernova.Constants;
import com.github.Ner027.hypernova.GuildVars;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class SetManagerRole implements Command
{

    @Override
    public void exec(String[] args, GuildMessageReceivedEvent event)
    {
        GuildVars vars = Constants.databaseUtil.getGuildVars(event.getGuild());


        if (!args[1].isEmpty())
        {
            if (!event.getMessage().getMentionedRoles().isEmpty())
            {
                vars.managerRole = event.getMessage().getMentionedRoles().get(0);
                Constants.databaseUtil.editDatabase(vars.managerRole.getIdLong(), event.getGuild(), "managerRoleID");
            } else
            {
                Constants.discordUtil.tempMessage("Please mention a valid role!", event.getChannel(), Color.yellow);
            }
        }
    }


    @Override
    public String getHelp()
    {
        return "With this command you can define a role to be the server's manager role";
    }

    @Override
    public String getInvoke()
    {
        return "setmanager";
    }
}
