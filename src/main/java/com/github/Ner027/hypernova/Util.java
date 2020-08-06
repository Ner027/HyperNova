package com.github.Ner027.hypernova;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Util
{
    private static YouTube youtube;

    public static String stringBuilder(String[] args)
    {
        String temp = "";

        for (String s : args)
        {
            temp += s + " ";
        }

        return temp;
    }

    public static boolean isUrl(String x)
    {
        try
        {
            new URL(x);
            return true;
        } catch (MalformedURLException ignored)
        {
            return false;
        }
    }

    public static String searchYoutube(String input)
    {
        try
        {
            List<SearchResult> results = youtube.search()
                    .list("id,snippet")
                    .setQ(input)
                    .setMaxResults(1L)
                    .setType("video")
                    .setFields("items(id/videoId)")
                    .setKey(Constants.youtubeKey)
                    .execute()
                    .getItems();

            if (!results.isEmpty())
            {
                String videoId = results.get(0).getId().getVideoId();

                return "https://www.youtube.com/watch?v=" + videoId;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static void buildYoutube()
    {

        YouTube temp = null;

        try
        {
            temp = new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    null).setApplicationName("Ner0 Discord Bot").build();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        youtube = temp;
    }

    public static boolean validEmoji(String string)
    {

        switch (string)
        {
            case "\uD83C\uDDFE":
            case "\uD83C\uDDF3":
            case "\uD83D\uDD04":
                return true;
        }
        return false;
    }

}
