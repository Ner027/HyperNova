package com.github.Ner027.hypernova.AdminCommands;

import com.github.Ner027.hypernova.Command;
import com.github.Ner027.hypernova.Constants;
import com.github.Ner027.hypernova.GuildVars;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class RandomSetup implements Command
{

    @Override
    public void exec(String[] args, GuildMessageReceivedEvent event)
    {
        GuildVars vars = Constants.databaseUtil.getGuildVars(event.getGuild());

        Constants.databaseUtil.editDatabase(event.getChannel().getIdLong(), event.getGuild(), "randomChannelID");
        vars.randomChannel = event.getChannel();

        vars.teamBlue.clear();
        vars.teamRed.clear();
        vars.memberList.clear();

        if (vars.mainMessage != null)
            vars.mainMessage.delete().queue();

        if (vars.blueMessage != null)
            vars.blueMessage.delete().queue();

        if (vars.redMessage != null)
            vars.redMessage.delete().queue();

        vars.blueMessage = null;
        vars.redMessage = null;


        Constants.discordUtil.clear("100", event.getChannel());
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(":arrow_right:" + "Random Team Generator" + ":arrow_left:");

        vars.mainMessage = vars.randomChannel.sendMessage(embedBuilder.build()).complete();
        vars.mainMessage.addReaction("\uD83D\uDD04").queue();
        vars.mainMessage.addReaction("\u274C").queue();


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
