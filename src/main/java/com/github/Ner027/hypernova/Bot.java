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

    //Entry point

    public static void main(String[] args)
    {
        new Config();

        try
        {
            new Bot();
        }
        catch (LoginException e)
        {
            e.printStackTrace();
            System.out.println("Something went wrong while tying to log in");
        }

        System.out.println("Startup Completed in:" + delta + "ms");

    }

    private Bot() throws LoginException
    {
        long startTime = System.currentTimeMillis();
        listener = new DiscordListener();

        //JDA Object creation
        jda = JDABuilder.createDefault(Constants.token)
                .addEventListeners(listener)
                .build();
        Constants.jda = jda;

        //Youtube object creation
        Util.buildYoutube();


        Constants.manager = new CommandManager();
        Constants.databaseUtil = new DatabaseUtil();
        Constants.discordUtil = new DiscordUtil();


        long endTime = System.currentTimeMillis();


        //Calculate the time the bot took to initiate
        delta = endTime - startTime;

        //Send the time , as a private message to the Bot Owner
        PrivateChannel txt = jda.openPrivateChannelById(Constants.ownerID).complete();
        if(txt != null)
            txt.sendMessage("Startup Completed in: " + delta + "ms").complete();
        //If no ID or a invalid ID was provided nothing will happen
    }

}


