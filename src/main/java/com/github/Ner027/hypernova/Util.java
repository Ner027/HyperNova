package com.github.Ner027.hypernova;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Util
{
    private YouTube.PlaylistItems.List playlistItemRequest;
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

    public void returnPlaylist(String inp)
    {
        List<PlaylistItem> playlistItemList = new ArrayList<>();


        try
        {
            playlistItemRequest = youtube.playlistItems().list("snippet,contentDetails");

            playlistItemRequest.setPlaylistId("PLcTITYXmnAxZjOzPfSt3QkP7V7ybttoQL");

            playlistItemRequest.setFields("items(contentDetails/videoId),nextPageToken,pageInfo");

            playlistItemRequest.setKey(Constants.youtubeKey);

            String nextToken = "";

            do {
                playlistItemRequest.setPageToken(nextToken);
                PlaylistItemListResponse playlistItemResult = playlistItemRequest.execute();

                playlistItemList.addAll(playlistItemResult.getItems());

                nextToken = playlistItemResult.getNextPageToken();

            } while (nextToken != null);

            System.out.println(playlistItemList.get(0).getContentDetails().getVideoId());
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
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

    //Not used atm will be used to optimize the Emote actions

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
