package com.github.Ner027.hypernova.AdminCommands;
import com.github.Ner027.hypernova.Command;
import com.github.Ner027.hypernova.Constants;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class RandomSetup implements Command
{

    @Override
    public void exec(String[] args, GuildMessageReceivedEvent event)
    {
        Constants.discordUtil.SetupRandom(event.getGuild(),event.getChannel());
    }

    @Override
    public String getHelp()
    {
        return "By executing this command on a text channel the bot will set it up as the random team generator";
    }

    @Override
    public String getInvoke()
    {
        return "teamsetup";
    }
}
