package com.github.Ner027.hypernova.GeneralCommands;

import com.github.Ner027.hypernova.Command;
import com.github.Ner027.hypernova.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Help implements Command
{
    @Override
    public void exec(String[] args, GuildMessageReceivedEvent event)
    {

        if (args.length == 1)
        {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(Color.magenta);
            builder.setTitle("Run this command together with one of the types below!");
            builder.addField("1. ", "admin", false);
            builder.addField("2. ", "general", false);
            builder.addField("3. ", "music", false);
            builder.addField("4. ", "teams", false);
            builder.setFooter("Example: " + Constants.prefix + "help " + "general");
            Message msg = event.getChannel().sendMessage(builder.build()).complete();
            msg.delete().queueAfter(15, TimeUnit.SECONDS);
        } else
        {
            switch (args[1].toLowerCase())
            {
                case "admin":
                    sendHelp(event.getChannel(), Constants.manager.adminInvokes, Constants.manager.adminCommands);
                    break;
                case "general":
                    sendHelp(event.getChannel(), Constants.manager.generalInvokes, Constants.manager.generalCommands);
                    break;
                case "music":
                    sendHelp(event.getChannel(), Constants.manager.musicInvokes, Constants.manager.musicCommands);
                    break;
                case "teams":
                    sendHelp(event.getChannel(), Constants.manager.teamInvokes, Constants.manager.randomCommands);
                    break;
                default:
                    Constants.discordUtil.tempMessage("Please provide a valid group of commands", event.getChannel());
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
        return "help";
    }

    private void sendHelp(TextChannel channel, List<String> invokes, Map<String, Command> cmds)
    {

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.magenta);
        embedBuilder.setTitle("Here are your commands!");

        for (String s : invokes)
        {
            if (cmds.get(s).getHelp() != null)
                embedBuilder.addField(s, cmds.get(s).getHelp(), false);
            else embedBuilder.addField(s, "No help available", false);
        }

        if (invokes == Constants.manager.musicInvokes)
            embedBuilder.setFooter("This only work on music channel!");
        else if (invokes == Constants.manager.teamInvokes)
            embedBuilder.setFooter("This only work on the teams channel!");

        Message msg = channel.sendMessage(embedBuilder.build()).complete();
        msg.delete().queueAfter(15, TimeUnit.SECONDS);
    }
}
