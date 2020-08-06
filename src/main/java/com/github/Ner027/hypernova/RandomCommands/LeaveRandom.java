package com.github.Ner027.hypernova.RandomCommands;

import com.github.Ner027.hypernova.Command;
import com.github.Ner027.hypernova.Constants;
import com.github.Ner027.hypernova.GuildVars;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class LeaveRandom implements Command
{
    @Override
    public void exec(String[] args, GuildMessageReceivedEvent event)
    {
        GuildVars vars = Constants.databaseUtil.getGuildVars(event.getGuild());

        vars.memberList.remove(event.getMember());
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(":arrow_right:" + "Random Team Generator" + ":arrow_left:");

        for (int i = 0; i < vars.memberList.size(); i++)
        {
            embedBuilder.addField("", (i + 1) + "." + vars.memberList.get(i).getUser().getAsTag(), false);
        }

        vars.randomChannel.editMessageById(vars.mainMessage.getId(), embedBuilder.build()).queue();

    }

    @Override
    public String getHelp()
    {
        return null;
    }

    @Override
    public String getInvoke()
    {
        return "leave";
    }
}
