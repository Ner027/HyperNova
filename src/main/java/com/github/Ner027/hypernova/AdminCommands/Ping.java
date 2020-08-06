package com.github.Ner027.hypernova.AdminCommands;

import com.github.Ner027.hypernova.Command;
import com.github.Ner027.hypernova.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Ping implements Command
{
    @Override
    public void exec(String[] args, GuildMessageReceivedEvent event)
    {
        long ping = Constants.jda.getGatewayPing();
        String p = "" + ping;


        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Pong!");
        builder.addField("Rest Ping:", Constants.jda.getRestPing().complete().toString(), false);
        builder.addField("Gateway Ping:", p, false);
        builder.setColor(Color.magenta);
        Message msg = event.getChannel().sendMessage(builder.build()).complete();
        msg.delete().queueAfter(5, TimeUnit.SECONDS);


    }

    @Override
    public String getHelp()
    {
        return "This command returns the bot ping";
    }

    @Override
    public String getInvoke()
    {
        return "ping";
    }
}
