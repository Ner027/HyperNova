package com.github.Ner027.hypernova;

import com.github.Ner027.hypernova.Music.GuildMusicManager;
import com.github.Ner027.hypernova.Music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DiscordUtil
{
    public void tempMessage(String message, TextChannel textChannel)
    {
        Message msg;

        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.red);
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
            if (member.getRoles().contains(role))
            {
                return true;
            }
        }

        return false;
    }

    public void SetupRandom(Guild guild, TextChannel textChannel)
    {
        clear("100", textChannel);
        GuildVars vars = Constants.databaseUtil.getGuildVars(guild);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(":arrow_right:" + "Random Team Generator" + ":arrow_left:");
        vars.mainMessage = textChannel.sendMessage(embedBuilder.build()).complete();
        vars.mainMessage.addReaction("\uD83D\uDD04").queue();
        vars.mainMessage.addReaction("\u274C").queue();
        vars.randomChannel = textChannel;
        vars.teamRed = null;
        vars.teamBlue = null;
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

                } else
                {
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle("There is an odd number of player");
                    builder.setFooter("Do you wish to continue");
                    vars.confirmMessage = vars.randomChannel.sendMessage(builder.build()).complete();
                    vars.confirmMessage.addReaction("U+1F1FE").queue();
                    vars.confirmMessage.addReaction("U+1F1F3").queue();
                    vars.canUpdate = true;
                }
            }
        } else
        {
            tempMessage("You dont have the required role!", textChannel);
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
        vars.musicMessageEmbed.setImage("https://i.imgur.com/y8WEApe.png");
        vars.musicMessageEmbed.setColor(Color.magenta);

        vars.musicMessage = vars.musicChannel.sendMessage(vars.musicMessageEmbed.build()).complete();
        vars.musicMessage.addReaction("\u23EF").queue();
        vars.musicMessage.addReaction("\uD83D\uDD07").queue();

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
        guild.getAudioManager().setSelfDeafened(!guild.getAudioManager().isSelfDeafened());
    }
}
