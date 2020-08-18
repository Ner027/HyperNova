package com.github.Ner027.hypernova.GeneralCommands;

import com.github.Ner027.hypernova.Command;
import com.github.Ner027.hypernova.Constants;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.Date;

public class Daily implements Command
{
    @Override
    public void exec(String[] args, GuildMessageReceivedEvent event)
    {
        if (event.getMember() != null)
        {
            long time = Constants.databaseUtil.getNextTime(event.getMember());

            if (time == -1)
            {
                Constants.databaseUtil.insertMembers(event.getMember());
            }

            Date date = new Date();
            if(date.getTime() >= Constants.databaseUtil.getNextTime(event.getMember()))
            {
                Constants.databaseUtil.setNextTime(event.getMember(),date.getTime());
                Constants.databaseUtil.updateMemberCredits(event.getMember(),300);
            }
            else
            {
                long timeLeft = time - date.getTime();
                String formatedTime = "";
                Constants.discordUtil.tempMessage("Please try again in " , event.getChannel(), Color.green);
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
        return "daily";
    }
}
