package com.github.Ner027.hypernova;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DatabaseUtil
{
    public Map<Guild, GuildVars> guildVarsList = new HashMap<>();
    public List<Long> databaseGuilds = new ArrayList<>();
    public List<Guild> guilds = new ArrayList<>();


    public void databaseStartup()
    {
        guilds = Constants.jda.getGuilds();

        String query = "SELECT guildID FROM guildManager";
        try
        {
            ResultSet set = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).createStatement().executeQuery(query);
            while (set.next())
            {
                databaseGuilds.add(set.getLong(1));
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        for (Guild guild : guilds)
        {
            if (!databaseGuilds.contains(guild.getIdLong()))
            {
                addGuildToDatabase(guild);
            }
        }

        for (Guild g : guilds)
        {
            GuildVars temp = new GuildVars();

            temp.guildID = g.getIdLong();

            long randomChannelID = retrieveFromDatabase(g, "randomChannelID");
            TextChannel textChannel = g.getTextChannelById(randomChannelID);

            if (randomChannelID != 0)
            {
                if (textChannel != null)
                {
                    temp.randomChannel = textChannel;
                }
            }

            long musicID = retrieveFromDatabase(g, "musicChannelID");
            TextChannel musicChannel = g.getTextChannelById(musicID);

            if (musicID != 0)
            {
                if (musicChannel != null)
                {
                    temp.musicChannel = musicChannel;
                }
            }


            long managerRole = retrieveFromDatabase(g, "managerRoleID");
            if (managerRole != 0)
            {
                if (g.getRoleById(managerRole) != null)
                {
                    temp.managerRole = g.getRoleById(managerRole);
                }
            }

            long mainVoice = retrieveFromDatabase(g, "mainVoice");
            {
                if (mainVoice != 0)
                {
                    if (g.getVoiceChannelById(mainVoice) != null)
                    {
                        temp.mainChannel = g.getVoiceChannelById(mainVoice);
                    }
                }
            }

            String prefix = getPrefix(g);
            if (prefix != null)
            {

                temp.prefix = prefix;
            } else temp.prefix = Constants.prefix;

            guildVarsList.put(g, temp);

            if (textChannel != null)
            {
                Constants.discordUtil.SetupRandom(g, textChannel);
            }

            if (musicChannel != null)
            {
                Constants.discordUtil.setupMusic(g, musicChannel);
            }

        }

        databaseGuilds.clear();

    }

    public void addGuildToDatabase(Guild guild)
    {
        String query = "INSERT INTO guildManager (guildID) VALUE (" + guild.getIdLong() + ")";

        try
        {
            PreparedStatement statement = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).prepareStatement(query);
            statement.executeUpdate();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public GuildVars getGuildVars(Guild guild)
    {
        if (guildVarsList.containsKey(guild))
        {
            return guildVarsList.get(guild);
        }
        return null;
    }

    public long retrieveFromDatabase(Guild guild, String column)
    {
        String query = "SELECT " + column + " FROM guildManager WHERE guildID = " + guild.getIdLong();
        try
        {
            ResultSet set = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).createStatement().executeQuery(query);
            set.next();
            return set.getLong(1);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    public void editDatabase(long id, Guild guild, String column)
    {
        String query = "UPDATE guildManager SET " + column + " = " + id + " WHERE guildID = " + guild.getIdLong();
        try
        {
            PreparedStatement statement = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void removeGuild(Guild guild)
    {
        String query = "DELETE FROM guildManager WHERE guildID = " + guild.getIdLong();
        try
        {
            PreparedStatement statement = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void addWin(Member member)
    {
        String query = "UPDATE memberData SET memberWins = memberWins + 1 WHERE memberID = " + member.getIdLong();
        try
        {
            PreparedStatement statement = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private void addMember(Member member)
    {
        if (member != null)
        {
            String query = "INSERT INTO memberData (memberID) VALUE(" + member.getIdLong() + ")";
            String anotherQuery = "UPDATE memberData SET memberWins = 0 WHERE memberID = " + member.getIdLong();
            try
            {
                PreparedStatement statement = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).prepareStatement(query);
                statement.executeUpdate();
                PreparedStatement anotherStatement = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).prepareStatement(anotherQuery);
                anotherStatement.executeUpdate();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
    }

    public long getMembers(Member member)
    {
        String query = "SELECT * FROM memberData WHERE memberID = " + member.getIdLong();
        try
        {
            ResultSet set = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).createStatement().executeQuery(query);
            if (set.next())
            {
                return set.getLong(1);
            } else return 0;
        } catch (SQLException e)
        {
            e.printStackTrace();
            return 0;
        }
    }

    public void insertMembers(Member m)
    {
        if (getMembers(m) == 0)
        {
            addMember(m);
        }
    }

    public void removeMember(Member m)
    {
        String query = "DELETE FROM memberData WHERE memberID = " + m.getIdLong();
        try
        {
            PreparedStatement statement = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public String getPrefix(Guild guild)
    {
        String query = "SELECT guildPrefix FROM guildManager WHERE guildID = " + guild.getIdLong();
        try
        {

            ResultSet set = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).createStatement().executeQuery(query);
            if (set.next())
                return set.getString(1);


        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }


    public void addPrefix(Guild guild, String prefix)
    {
        String query = "UPDATE guildManager SET guildPrefix = (?) WHERE guildID = " + guild.getIdLong();

        try
        {
            PreparedStatement statement = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).prepareStatement(query);
            statement.setString(1, prefix);
            statement.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    public int getWins(Member member)
    {
        String query = "SELECT memberWins FROM memberData WHERE memberID = " + member.getIdLong();
        try
        {
            ResultSet set = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).createStatement().executeQuery(query);
            if (set.next())
            {
                return set.getInt(1);
            } else return 0;
        } catch (SQLException e)
        {
            e.printStackTrace();
            return 0;
        }
    }

}
