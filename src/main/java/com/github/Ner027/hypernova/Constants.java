package com.github.Ner027.hypernova;

import net.dv8tion.jda.api.JDA;

import java.sql.Connection;

public class Constants
{

    //This vars get loaded from the config file for ease of use
    public static JDA jda;
    public static String token;
    public static String prefix;
    public static String youtubeKey;
    public static String dbURL;
    public static String username;
    public static String psw;
    public static long ownerID;
    public static String imageURL;

    //Objects that are used through out the project
    public static CommandManager manager;
    public static DatabaseUtil databaseUtil;
    public static DiscordUtil discordUtil;
    public static Util util;


}
