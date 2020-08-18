package com.github.Ner027.hypernova;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;


@SuppressWarnings("ALL")
public class DiscordListener extends ListenerAdapter
{

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event)
    {
        handleCommand(event.getMessage().getContentRaw().split("\\s+"), event);

    }

    private void handleCommand(String[] args, GuildMessageReceivedEvent event)
    {
        GuildVars vars = Constants.databaseUtil.getGuildVars(event.getGuild());
        if (event.getMember() != null)
        {
            if ((event.getMember()).getUser() != Constants.jda.getSelfUser())
            {
                if (args[0].substring(0, vars.prefix.length()).equals(vars.prefix))
                {
                    event.getMessage().delete().queueAfter(500, TimeUnit.MILLISECONDS);
                    String cmd = args[0].substring(vars.prefix.length());

                    if (Constants.manager.adminCommands.containsKey(cmd))
                    {
                        if (Constants.discordUtil.hasPerm(Permission.ADMINISTRATOR, event.getMember()))
                        {
                            Constants.manager.adminCommands.get(cmd).exec(args, event);
                        } else
                        {
                            Constants.discordUtil.tempMessage("You dont have enough permissions!", event.getChannel(), Color.red);
                        }
                    } else if (Constants.manager.generalCommands.containsKey(cmd))
                    {
                        Constants.manager.generalCommands.get(cmd).exec(args, event);
                    }
                } else
                {
                    if (vars.randomChannel != null)
                    {
                        if (event.getChannel() == vars.randomChannel)
                        {
                            event.getMessage().delete().queueAfter(500, TimeUnit.MILLISECONDS);

                            if (Constants.manager.randomCommands.containsKey(args[0]))
                            {
                                Constants.manager.randomCommands.get(args[0]).exec(args, event);
                            }
                            return;
                        }
                    }

                    if (vars.musicChannel != null)
                    {
                        if (event.getChannel() == vars.musicChannel)
                        {
                            event.getMessage().delete().queueAfter(500, TimeUnit.MILLISECONDS);

                            if (Constants.manager.musicCommands.containsKey(args[0]))
                            {
                                Constants.manager.musicCommands.get(args[0]).exec(args, event);
                            } else
                            {
                                Constants.manager.musicCommands.get("play").exec(args, event);
                            }
                        }
                    }

                }
            }

        }
    }


    @Override
    public void onReady(@Nonnull ReadyEvent event)
    {
        Constants.databaseUtil.databaseStartup();
    }

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event)
    {
        //For the sake of this thing not blowing up, this should not happen
        //Except in the case where the bot leaves a guild while its offline

        if (Constants.databaseUtil.retrieveFromDatabase(event.getGuild(), "guildID") == 0)
        {
            Constants.databaseUtil.addGuildToDatabase(event.getGuild());
        }
    }

    @Override
    public void onGuildLeave(@Nonnull GuildLeaveEvent event)
    {
        Constants.databaseUtil.removeGuild(event.getGuild());
    }

    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event)
    {
        Constants.databaseUtil.insertMembers(event.getMember());
    }

    @Override
    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event)
    {
        if (event.getUser() != Constants.jda.getSelfUser())
        {
            GuildVars vars = Constants.databaseUtil.getGuildVars(event.getGuild());

            if (event.getChannel() == vars.randomChannel || event.getChannel() == vars.musicChannel)
            {
                if (Constants.discordUtil.isMusicMessage(vars, event))
                {
                    handleMusicEmote(event);
                }
                else if (event.getChannel() == vars.randomChannel)
                {
                    handleTeamEmote(event);
                }

                event.getChannel().retrieveMessageById(event.getMessageIdLong()).complete().removeReaction(event.getReactionEmote().getName(), event.getUser());
            }
        }

    }

    private void handleTeamEmote(GuildMessageReactionAddEvent event)
    {

        GuildVars vars = Constants.databaseUtil.getGuildVars(event.getGuild());

        switch (event.getReactionEmote().getName())
        {
            case "\uD83C\uDDFE":
                genTeams(event, vars);
                break;
            case "\uD83C\uDDF3":
                deleteMsg(event, vars);
                break;
            case "\uD83D\uDD35":
                endGame(vars, "blue", event);
                break;
            case "\uD83D\uDD34":
                endGame(vars, "red", event);
                break;
            case "\uD83D\uDC4D":
                nextGame(true, event, vars);
                break;
            case "\uD83D\uDC4E":
                nextGame(false, event, vars);
                break;

        }
    }


    private void nextGame(boolean next, GuildMessageReactionAddEvent event, GuildVars vars)
    {
        if (event.getMessageIdLong() == vars.nextGame.getIdLong())
        {
            if (!next)
            {
                vars.memberList.clear();
                Constants.discordUtil.updateList(vars);
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("Ok,see you later!");
                builder.setColor(Color.magenta);
                Message message = vars.randomChannel.sendMessage(builder.build()).complete();
                message.delete().queueAfter(2, TimeUnit.SECONDS);
            }

            if(vars.redMessage != null)
                vars.redMessage.delete().queue();

            if(vars.blueMessage != null)
                vars.blueMessage.delete().queue();

            vars.blueMessage = null;
            vars.redMessage = null;

            vars.nextGame.delete().queue();
            vars.nextGame = null;
        }
    }


    private void deleteMsg(GuildMessageReactionAddEvent event, GuildVars vars)
    {
        if (vars.confirmMessage != null)
        {
            if (vars.confirmMessage.getIdLong() == event.getMessageIdLong())
            {
                vars.confirmMessage.delete().queue();
                vars.confirmMessage = null;
            }
        }
    }

    private void handleMusicEmote(GuildMessageReactionAddEvent event)
    {
        GuildVars vars = Constants.databaseUtil.getGuildVars(event.getGuild());

        switch (event.getReactionEmote().getName())
        {
            case "\u23F8":
                Constants.discordUtil.pause(event.getGuild());
                break;
            case "\u25B6":
                Constants.discordUtil.resume(event.getGuild());
                break;
            case "\uD83D\uDD07":
                Constants.discordUtil.mute(event.getGuild());
                break;
            case "\uD83D\uDD04":
                Constants.discordUtil.shuffle(event.getGuild());
                break;
            case "\u274C":
                Constants.discordUtil.clearQueue(event.getGuild());
                break;
            case "\u23ED":
                Constants.discordUtil.skip(event.getMember(), event.getGuild());
                break;
            case "\u267E":
                Constants.discordUtil.toggleLoop(event.getGuild());
                break;
        }

    }


    @Override
    public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event)
    {
        if (event.getAuthor().getIdLong() == Constants.ownerID)
        {
            String[] args = event.getMessage().getContentRaw().split("\\s+");
            if (args[0].equalsIgnoreCase("shutdown"))
            {
                System.out.println("Shuting down");
                System.exit(0);
            }
        }
    }

    private void genTeams(GuildMessageReactionAddEvent event, GuildVars vars)
    {
        if (vars.confirmMessage != null)
        {
            if (vars.confirmMessage.getIdLong() == event.getMessageIdLong())
            {
                Constants.discordUtil.createBlue((vars.memberList.size() / 2) + 1, vars);
                Constants.discordUtil.createRed((vars.memberList.size() / 2) + 1, vars);
                Constants.discordUtil.sendBlue(vars);
                Constants.discordUtil.sendRed(vars);
                vars.confirmMessage.delete().queue();
                vars.confirmMessage = null;
            }
        }
    }

    private void endGame(GuildVars vars, String team, GuildMessageReactionAddEvent event)
    {

        if (event.getMessageIdLong() == vars.endGameMessage.getIdLong())
        {
            if (Constants.discordUtil.hasRole(vars.managerRole, event.getMember()))
            {
                List<Member> temp = null;

                if (team.equalsIgnoreCase("blue"))
                    temp = vars.teamBlue;
                else if (team.equalsIgnoreCase("red"))
                    temp = vars.teamRed;

                if (!temp.isEmpty())
                {
                    for (Member m : temp)
                    {
                        Constants.databaseUtil.insertMembers(m);
                        Constants.databaseUtil.addWin(m);
                    }

                }

                vars.endGameMessage.delete().queue();
                vars.endGameMessage = null;
                vars.teamRed.clear();
                vars.teamBlue.clear();
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("Do you want to play again?");
                builder.addField("", "Please react with one of the emotes", false);
                builder.setFooter("Yes | No");
                builder.setColor(Color.magenta);
                vars.nextGame = vars.randomChannel.sendMessage(builder.build()).complete();
                vars.nextGame.addReaction("\uD83D\uDC4D").queue();
                vars.nextGame.addReaction("\uD83D\uDC4E").queue();

            } else
            {
                Constants.discordUtil.tempMessage("You dont have the required role", vars.randomChannel, Color.red);
            }

        }

    }
}

