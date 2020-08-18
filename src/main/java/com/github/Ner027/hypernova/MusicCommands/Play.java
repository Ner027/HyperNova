package com.github.Ner027.hypernova.MusicCommands;

import com.github.Ner027.hypernova.Command;
import com.github.Ner027.hypernova.Constants;
import com.github.Ner027.hypernova.Music.PlayerManager;
import com.github.Ner027.hypernova.Util;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class Play implements Command
{
    @Override
    public void exec(String[] args, GuildMessageReceivedEvent event)
    {

        PlayerManager playerManager = PlayerManager.getInstance();


        if (!event.getGuild().getAudioManager().isConnected())
        {
            Constants.discordUtil.joinVoice(event.getGuild(), event.getMember());
            play(event, args, playerManager);
        } else if (Constants.discordUtil.sameVoice(event.getMember(), event.getGuild()))
        {
            play(event, args, playerManager);
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
        return "play";
    }

    private void play(GuildMessageReceivedEvent event, String[] args, PlayerManager playerManager)
    {

        String name = Util.stringBuilder(args);

        if (Util.isUrl(args[0]))
        {
            playerManager.loadAndPlay(event.getChannel(), args[0]);
        } else
        {
            if (Util.searchYoutube(name) != null)
            {
                playerManager.loadAndPlay(event.getChannel(), Util.searchYoutube(name));
            } else
            {
                Constants.discordUtil.tempMessage("Couldn't find what you searched for!", event.getChannel(), Color.yellow);
            }
        }

    }
}
