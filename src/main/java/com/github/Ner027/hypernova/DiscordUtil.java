package com.github.Ner027.hypernova;

import com.github.Ner027.hypernova.Music.GuildMusicManager;
import com.github.Ner027.hypernova.Music.PlayerManager;
import com.github.Ner027.hypernova.MusicCommands.Play;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DiscordUtil
{
    public void tempMessage(String message, TextChannel textChannel,Color c)
    {
        Message msg;

        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(c);
        builder.setTitle("Ooops something is wrong!");
        builder.addField("", message, true);
        msg = textChannel.sendMessage(builder.build()).complete();
        msg.delete().queueAfter(2, TimeUnit.SECONDS);
    }

    public void clear(String x, TextChannel textChannel)
    {
        int amt = 2;

        if (x != null)
        {
            try
            {
                amt = Integer.parseInt(x);
            } catch (NumberFormatException e)
            {
                e.printStackTrace();
            }

        }

        if (amt + 1 >= 100)
            amt = 100;

        List<Message> msgs = textChannel.getHistory().retrievePast(amt).complete();
        textChannel.purgeMessages(msgs);

    }

    public boolean hasPerm(Permission perm, Member member)
    {
        if (member != null)
        {
            return member.hasPermission(perm);
        }

        return false;
    }

    public boolean hasRole(Role role, Member member)
    {
        if (member != null)
        {
            return member.getRoles().contains(role);
        }

        return false;
    }

    public void SetupRandom(Guild guild, TextChannel textChannel)
    {
        clear("100", textChannel);
        GuildVars vars = Constants.databaseUtil.getGuildVars(guild);
        vars.teamEmbed = new EmbedBuilder();
        vars.teamEmbed.setTitle(":arrow_right:" + "Random Team Generator" + ":arrow_left:");
        vars.mainMessage = textChannel.sendMessage(vars.teamEmbed.build()).complete();
        vars.mainMessage.addReaction("\uD83D\uDD04").queue();
        vars.mainMessage.addReaction("\u274C").queue();
        vars.randomChannel = textChannel;
        vars.memberList = new ArrayList<>();
        vars.teamBlue = new ArrayList<>();
        vars.teamRed = new ArrayList<>();
    }


    //Team Generator stuff

    public void createBlue(int index, GuildVars vars)
    {
        for (int i = 0; i < index; i++)
        {
            vars.teamBlue.add(vars.memberList.get(i));
        }
    }


    public void createRed(int index, GuildVars vars)
    {
        for (int i = vars.memberList.size(); i > index; i--)
        {
            vars.teamRed.add(vars.memberList.get(i - 1));
        }
    }

    public void sendRed(GuildVars vars)
    {

        EmbedBuilder redTeam = new EmbedBuilder();
        redTeam.setTitle("Red Team");
        redTeam.setColor(Color.RED);
        for (Member m : vars.teamRed)
        {
            redTeam.addField("", m.getUser().getAsTag(), false);
        }
        vars.redMessage = vars.randomChannel.sendMessage(redTeam.build()).complete();
    }

    public void sendBlue(GuildVars vars)
    {
        EmbedBuilder blueTeam = new EmbedBuilder();
        blueTeam.setTitle("Blue Team");
        blueTeam.setColor(Color.blue);
        for (Member m : vars.teamBlue)
        {
            blueTeam.addField("", m.getUser().getAsTag(), false);
        }
        vars.blueMessage = vars.randomChannel.sendMessage(blueTeam.build()).complete();
    }

    public void genTeams(GuildVars vars, Member member, TextChannel textChannel)
    {
        if (Constants.discordUtil.hasRole(vars.managerRole, member))
        {
            Collections.shuffle(vars.memberList);

            if (vars.teamRed != null)
                vars.teamRed.clear();

            if (vars.teamBlue != null)
                vars.teamBlue.clear();

            if (vars.blueMessage != null)
                vars.blueMessage.delete().queue();

            if (vars.redMessage != null)
                vars.redMessage.delete().queue();


            if (vars.memberList.size() != 0 && vars.memberList.size() != 1)
            {
                if (vars.memberList.size() % 2 == 0)
                {
                    Constants.discordUtil.createBlue(vars.memberList.size() / 2, vars);
                    Constants.discordUtil.createRed(vars.memberList.size() / 2, vars);
                    Constants.discordUtil.sendBlue(vars);
                    Constants.discordUtil.sendRed(vars);
                }
                else
                {
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle("There is an odd number of player");
                    builder.setFooter("Do you wish to continue");
                    vars.confirmMessage = vars.randomChannel.sendMessage(builder.build()).complete();
                    vars.confirmMessage.addReaction("U+1F1FE").queue();
                    vars.confirmMessage.addReaction("U+1F1F3").queue();
                }
            }
        } else
        {
            tempMessage("You dont have the required role!", textChannel,Color.red);
        }
    }

    public void reset(GuildVars vars)
    {
        if (vars.redMessage != null)
        {
            vars.redMessage.delete().queue();
        }
        if (vars.blueMessage != null)
        {
            vars.blueMessage.delete().queue();
        }

        vars.blueMessage = null;
        vars.redMessage = null;


        vars.memberList.clear();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(":arrow_right:" + "Random Team Generator" + ":arrow_left:");
        vars.randomChannel.editMessageById(vars.mainMessage.getId(), embedBuilder.build()).queue();

    }

    public void joinVoice(Guild guild, Member member)
    {


        if (member != null)
        {
            if (member.getVoiceState() != null)
            {
                if (member.getVoiceState().inVoiceChannel())
                {
                    guild.getAudioManager().openAudioConnection(member.getVoiceState().getChannel());
                }
            }
        }
    }

    public void setupMusic(Guild guild, TextChannel textChannel)
    {

        clear("3", textChannel);
        GuildVars vars = Constants.databaseUtil.getGuildVars(guild);
        Constants.databaseUtil.editDatabase(textChannel.getIdLong(), guild, "musicChannelID");
        vars.musicChannel = textChannel;

        vars.musicMessageEmbed = new EmbedBuilder();
        vars.musicMessageEmbed.setTitle("Nothing playing at the moment");
        vars.musicMessageEmbed.setImage(Constants.imageURL);
        vars.musicMessageEmbed.setColor(Color.magenta);

        vars.musicMessage = vars.musicChannel.sendMessage(vars.musicMessageEmbed.build()).complete();
        vars.musicMessage.addReaction("\u25B6").queue();
        vars.musicMessage.addReaction("\u23F8").queue();
        vars.musicMessage.addReaction("\u23ED").queue();
        vars.musicMessage.addReaction("\uD83D\uDD07").queue();
        vars.musicMessage.addReaction("\uD83D\uDD04").queue();
        vars.musicMessage.addReaction("\u274C").queue();

        vars.musicChannel.getManager().setTopic("\u25B6 Resume player \u23F8 Pause player " +
                "\u23ED Skip \uD83D\uDD07 Mute \uD83D\uDD04 Shuffle \u274C Clear queue").queue();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Music Queue");
        embedBuilder.addField("", "Nothing else queued!", false);
        embedBuilder.setColor(Color.magenta);
        vars.musicQueue = vars.musicChannel.sendMessage(embedBuilder.build()).complete();


    }

    public boolean sameVoice(Member member, Guild guild)
    {
        if (member != null)
        {
            if (member.getVoiceState() != null)
            {
                return member.getVoiceState().getChannel() == guild.getAudioManager().getConnectedChannel();
            }
        }

        return false;
    }

    public void skip(Member member, Guild guild)
    {
        if (member != null)
        {
            if (member.getUser() != Constants.jda.getSelfUser())
            {
                if (Constants.discordUtil.sameVoice(member, guild))
                {
                    PlayerManager playerManager = PlayerManager.getInstance();
                    GuildMusicManager musicManager = playerManager.getGuildMusicManager(guild);

                    if (musicManager.player.getPlayingTrack() != null)
                    {
                        musicManager.scheduler.nextTrack();
                    }
                }
            }
        }
    }

    public void mute(Guild guild)
    {
        AudioManager manager = guild.getAudioManager();
        GuildMusicManager musicManager = PlayerManager.getInstance().getGuildMusicManager(guild);

        if(guild.getAudioManager().isSelfMuted())
        {
           manager.setSelfMuted(false);
           musicManager.player.setVolume(100);
        }
        else
        {
            manager.setSelfMuted(true);
            musicManager.player.setVolume(0);
        }

    }

    public void pause(Guild guild)
    {
        GuildMusicManager musicManager = PlayerManager.getInstance().getGuildMusicManager(guild);

        if(!musicManager.player.isPaused())
            musicManager.player.setPaused(true);
    }

    public void resume(Guild guild)
    {
        GuildMusicManager musicManager = PlayerManager.getInstance().getGuildMusicManager(guild);

        if(musicManager.player.isPaused())
            musicManager.player.setPaused(false);
    }

    public boolean isMusicMessage(GuildVars vars, GuildMessageReactionAddEvent event)
    {

        if (event.getUser() != Constants.jda.getSelfUser())
        {
            if(vars.musicChannel != null)
            {
                if(event.getChannel() == vars.musicChannel)
                {
                    return event.getMessageIdLong() == vars.musicMessage.getIdLong();
                }
            }
        }
        return false;
    }

    public void shuffle(Guild guild)
    {
        GuildMusicManager musicManager = PlayerManager.getInstance().getGuildMusicManager(guild);
        if(musicManager.scheduler.queue.size() >= 2)
        {
            Collections.shuffle(musicManager.scheduler.queue);
            musicManager.updateQueue();
        }
    }

    public void clearQueue(Guild guild)
    {
        GuildMusicManager musicManager = PlayerManager.getInstance().getGuildMusicManager(guild);
        musicManager.scheduler.queue.clear();
        musicManager.updateQueue();

    }

    public void toggleLoop(Guild guild)
    {
        GuildVars vars = Constants.databaseUtil.getGuildVars(guild);
        vars.isLooped = !vars.isLooped;
    }

    public void updateList(@NotNull GuildVars vars)
    {
        if (!vars.memberList.isEmpty())
        {
            vars.teamEmbed.clearFields();
            for (int i = 0; i < vars.memberList.size(); i++)
            {
                vars.teamEmbed.addField("", (i + 1) + "." + vars.memberList.get(i).getUser().getAsTag(), false);
            }
        }
        else vars.teamEmbed.clearFields();
        vars.mainMessage = vars.mainMessage.editMessage(vars.teamEmbed.build()).complete();
    }
}

