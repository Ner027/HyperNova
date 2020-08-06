package com.github.Ner027.hypernova;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface Command
{
    void exec(String[] args, GuildMessageReceivedEvent event);

    String getHelp();

    String getInvoke();

}
