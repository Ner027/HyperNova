package com.github.Ner027.hypernova;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.PrivateChannel;

import javax.security.auth.login.LoginException;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Bot
{
    private JDA jda;
    private DiscordListener listener;
    private static long delta;

    public static void main(String[] args)
    {
        try
        {
            new Config();
            new Bot();
        } catch (LoginException e)
        {
            e.printStackTrace();
        }

        System.out.println("Startup Completed in:" + delta + "ms");

    }

    private Bot() throws LoginException
    {
        long startTime = System.currentTimeMillis();
        listener = new DiscordListener();
        jda = JDABuilder.createDefault(Constants.token)
                .addEventListeners(listener)
                .build();
        Constants.jda = jda;

        Util.buildYoutube();


        Constants.manager = new CommandManager();
        Constants.databaseUtil = new DatabaseUtil();
        Constants.discordUtil = new DiscordUtil();

        try
        {
            Constants.connection = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw);

            System.out.println("Connected to database with success!");
        } catch (SQLException e)
        {

            System.out.println("Error while connecting to database!");
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();

        delta = endTime - startTime;

        PrivateChannel txt = jda.openPrivateChannelById(Constants.ownerID).complete();
        txt.sendMessage("Startup Completed in: " + delta + "ms").complete();
    }

}


