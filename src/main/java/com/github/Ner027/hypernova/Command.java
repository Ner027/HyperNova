package com.github.Ner027.hypernova;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface Command
{
    //Command interface, if you want to add commands to the bot you should use this

    void exec(String[] args, GuildMessageReceivedEvent event);

    String getHelp();

    String getInvoke();

}
