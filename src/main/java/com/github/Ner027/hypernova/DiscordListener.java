package com.github.Ner027.hypernova;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;


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
                    event.getMessage().delete().queue();
                    String cmd = args[0].substring(vars.prefix.length());

                    if (Constants.manager.adminCommands.containsKey(cmd))
                    {
                        if (Constants.discordUtil.hasPerm(Permission.ADMINISTRATOR, event.getMember()))
                        {
                            Constants.manager.adminCommands.get(cmd).exec(args, event);
                        } else
                        {
                            Constants.discordUtil.tempMessage("You dont have enough permissions!", event.getChannel());
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
                            event.getMessage().delete().queue();

                            if (Constants.manager.randomCommands.containsKey(args[0]))
                            {
                                Constants.manager.randomCommands.get(args[0]).exec(args, event);
                            }
                        }
                    }

                    if (vars.musicChannel != null)
                    {
                        if (event.getChannel() == vars.musicChannel)
                        {
                            event.getMessage().delete().queue();

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

        if (!Constants.databaseUtil.databaseGuilds.contains(event.getGuild()))
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
        GuildVars vars = Constants.databaseUtil.getGuildVars(event.getGuild());

        if (event.getMember().getUser() != Constants.jda.getSelfUser())
        {
            if (vars.musicChannel != null)
            {
                if (event.getChannel() == vars.musicChannel)
                {
                    if (event.getMessageIdLong() == vars.musicMessage.getIdLong())
                    {
                        switch (event.getReactionEmote().getName())
                        {
                            case "\uD83D\uDD07":
                                Constants.discordUtil.mute(event.getGuild());
                                break;
                            case "\u23EF":
                                Constants.discordUtil.skip(event.getMember(), event.getGuild());
                                break;
                        }

                        vars.musicMessage.removeReaction(event.getReactionEmote().getName(), event.getUser()).queue();
                    }
                }
            }

            if (vars.randomChannel != null)
            {
                if (event.getChannel() == vars.randomChannel)
                {
                    if (event.getMessageIdLong() == vars.mainMessage.getIdLong())
                    {
                        switch (event.getReactionEmote().getName())
                        {

                        }

                        vars.musicMessage.removeReaction(event.getReactionEmote().getName(), event.getUser()).queue();
                    }
                }
            }

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
}

