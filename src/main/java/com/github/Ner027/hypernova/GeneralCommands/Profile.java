package com.github.Ner027.hypernova.GeneralCommands;

import com.github.Ner027.hypernova.Command;
import com.github.Ner027.hypernova.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Profile implements Command
{
    @Override
    public void exec(String[] args, GuildMessageReceivedEvent event)
    {

        Member m;

        if (event.getMessage().getMentionedMembers().size() == 0)
        {
            m = event.getMember();
        } else m = event.getMessage().getMentionedMembers().get(0);


        if (m != null)
        {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle(m.getUser().getAsTag() + " Profile");
            builder.setColor(Color.CYAN);

            if (Constants.databaseUtil.getMembers(m) == 0)
            {
                builder.setImage(m.getUser().getAvatarUrl());
                builder.addField("Oooops!", "I could not find any info about " + m.getUser().getName(), false);
            } else
            {
                builder.setImage(m.getUser().getAvatarUrl());
                builder.addField("This is what I know about " + m.getUser().getName(), "Wins:" + Constants.databaseUtil.getWins(m), false);
            }
            Message message = event.getChannel().sendMessage(builder.build()).complete();
            if (message != null)
                message.delete().queueAfter(10, TimeUnit.SECONDS);
        }
    }

    @Override
    public String getHelp()
    {
        return "By executing this command if no other member is mentioned will show self profile,otherwise shows the mentioned member profile";
    }

    @Override
    public String getInvoke()
    {
        return "profile";
    }
}
