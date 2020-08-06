package com.github.Ner027.hypernova;

import java.io.*;
import java.util.Properties;


public class Config
{
    public Config()
    {
        File configFile = new File("config.properties");

        try
        {
            Properties p = new Properties();
            if (configFile.createNewFile())
            {
                OutputStream os = new FileOutputStream("config.properties");
                p.setProperty("BotToken", "");
                p.setProperty("YoutubeToken", "");
                p.setProperty("DefaultPrefix", "");
                p.setProperty("DatabaseURL", "");
                p.setProperty("DatabaseUser", "");
                p.setProperty("DatabasePsw", "");
                p.setProperty("OwnerID", "");
                p.store(os, null);
                System.out.println("A new config file has been created, please configure your bot and then restart it");
                try
                {
                    Thread.sleep(3000L);
                    System.exit(0);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            } else
            {
                InputStream is = new FileInputStream("config.properties");
                p.load(is);
                Constants.token = p.getProperty("BotToken");
                Constants.youtubeKey = p.getProperty("YoutubeToken");
                Constants.prefix = p.getProperty("DefaultPrefix");
                Constants.dbURL = p.getProperty("DatabaseURL");
                Constants.username = p.getProperty("DatabaseUser");
                Constants.psw = p.getProperty("DatabasePsw");
                Constants.ownerID = Long.parseLong(p.getProperty("OwnerID"));

            }


        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
