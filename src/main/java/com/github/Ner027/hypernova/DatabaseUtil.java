package com.github.Ner027.hypernova;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DatabaseUtil
{
    public Map<Guild, GuildVars> guildVarsList = new HashMap<>();
    public List<Guild> guilds = new ArrayList<>();

    //There is a need to create a connection object on every request because it might time out, might change to a
    //Connection pool later on

    public void databaseStartup()
    {
        guilds = Constants.jda.getGuilds();

        for (Guild g : guilds)
        {
            if(retrieveFromDatabase(g,"guildID") == 0)
            {
                addGuildToDatabase(g);
            }

            GuildVars temp = new GuildVars();

            temp.guildID = g.getIdLong();

            long randomChannelID = retrieveFromDatabase(g, "randomChannelID");
            TextChannel textChannel = g.getTextChannelById(randomChannelID);

            if (randomChannelID != 0)
            {
                temp.randomChannel = textChannel;
            }

            long musicID = retrieveFromDatabase(g, "musicChannelID");
            TextChannel musicChannel = g.getTextChannelById(musicID);

            if (musicID != 0)
            {
                temp.musicChannel = musicChannel;
            }


            long managerRole = retrieveFromDatabase(g, "managerRoleID");
            if (managerRole != 0)
            {

                temp.managerRole = g.getRoleById(managerRole);

            }

            long mainVoice = retrieveFromDatabase(g, "mainVoice");
            {
                if (mainVoice != 0)
                {
                    temp.mainChannel = g.getVoiceChannelById(mainVoice);
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



    }

    public void addGuildToDatabase(Guild guild)
    {
        String query = "INSERT INTO guildManager (guildID) VALUE (?)";

        try
        {
            PreparedStatement statement = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).prepareStatement(query);
            statement.setLong(1,guild.getIdLong());
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
        String query = "SELECT " + column +" FROM guildManager WHERE guildID = ?";
        try
        {
            PreparedStatement st = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).prepareStatement(query);
            st.setLong(1,guild.getIdLong());
            ResultSet set = st.executeQuery();

            set.next();
            return set.getLong(1);
        } catch (SQLException e)
        {
            e.printStackTrace();
            return 0;
        }
    }

    public void editDatabase(long id, Guild guild, String column)
    {
        String query = "UPDATE guildManager SET " + column + " = ? WHERE guildID = ?";
        try
        {
            PreparedStatement statement = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).prepareStatement(query);
            statement.setLong(1,id);
            statement.setLong(2,guild.getIdLong());
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void removeGuild(Guild guild)
    {
        String query = "DELETE FROM guildManager WHERE guildID = ?";
        try
        {
            PreparedStatement statement = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).prepareStatement(query);
            statement.setLong(1,guild.getIdLong());
            statement.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void addWin(Member member)
    {
        String query = "UPDATE memberData SET memberWins = memberWins + 1 WHERE memberID = ?";
        try
        {
            PreparedStatement statement = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).prepareStatement(query);
            statement.setLong(1,member.getIdLong());
            statement.executeUpdate();

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private void addMember(Member member)
    {
        if (member != null)
        {
            String query = "INSERT INTO memberData (memberID) VALUE(?)";
            String anotherQuery = "UPDATE memberData SET memberWins = 0 WHERE memberID = ?";
            try
            {
                PreparedStatement statement = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).prepareStatement(query);
                PreparedStatement anotherStatement = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).prepareStatement(anotherQuery);
                statement.setLong(1,member.getIdLong());
                anotherStatement.setLong(1,member.getIdLong());
                statement.executeUpdate();
                anotherStatement.executeUpdate();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
    }

    public long getMembers(Member member)
    {
        String query = "SELECT * FROM memberData WHERE memberID = ?";
        try
        {
            PreparedStatement st = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).prepareStatement(query);
            st.setLong(1,member.getIdLong());
            ResultSet set = st.executeQuery();
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
        String query = "DELETE FROM memberData WHERE memberID = ?";
        try
        {
            PreparedStatement statement = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).prepareStatement(query);
            statement.setLong(1,m.getIdLong());
            statement.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public String getPrefix(Guild guild)
    {
        String query = "SELECT guildPrefix FROM guildManager WHERE guildID = ?";
        try
        {
            PreparedStatement statement = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).prepareStatement(query);
            statement.setLong(1,guild.getIdLong());
            ResultSet set = statement.executeQuery();

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
        String query = "UPDATE guildManager SET guildPrefix = (?) WHERE guildID = ?";

        try
        {
            PreparedStatement statement = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).prepareStatement(query);
            statement.setString(1, prefix);
            statement.setLong(2,guild.getIdLong());
            statement.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    public int getWins(Member member)
    {
        String query = "SELECT memberWins FROM memberData WHERE memberID = ?";
        try
        {
            PreparedStatement st = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).prepareStatement(query);
            st.setLong(1,member.getIdLong());
            ResultSet set = st.executeQuery();

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

    public void updateMemberCredits(Member m ,int amt)
    {
        String query = "UPDATE memberData SET memberCredits = memberCredits + ? WHERE memberID = ?";
        try
        {
            PreparedStatement st = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).prepareStatement(query);
            st.setInt(1,amt);
            st.setLong(2,m.getIdLong());
            st.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public int getMemberCredits(Member m)
    {
        String query = "SELECT memberCredits FROM memberData WHERE memberID = ?";
        try
        {
            PreparedStatement st = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).prepareStatement(query);
            st.setLong(1,m.getIdLong());
            ResultSet set = st.executeQuery();
            set.next();
            return set.getInt(1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return 0;
        }
    }

    public long getNextTime(Member m)
    {
        String query = "SELECT nextTime FROM memberData WHERE memberID = ?";
        try
        {
            PreparedStatement st = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).prepareStatement(query);
            st.setLong(1,m.getIdLong());
            ResultSet set = st.executeQuery();
            set.next();
            return set.getLong(1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public void setNextTime(Member m, long next)
    {
        String query = "UPDATE memberData SET nextTime = ? WHERE memberID = ?";
        try
        {
            PreparedStatement st = DriverManager.getConnection(Constants.dbURL, Constants.username, Constants.psw).prepareStatement(query);
            st.setLong(1,next + 86400L);
            st.setLong(2,m.getIdLong());
            st.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


}
