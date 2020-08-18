package com.github.Ner027.hypernova;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.util.List;

public class GuildVars
{
    public List<Member> memberList;
    public Message blueMessage, redMessage, confirmMessage, mainMessage, musicMessage, musicQueue,endGameMessage,nextGame;
    public VoiceChannel voiceBlue, voiceRed, mainChannel;
    public Role managerRole;
    public TextChannel randomChannel, musicChannel;
    public List<Member> teamBlue, teamRed;
    public Long guildID;
    public EmbedBuilder musicMessageEmbed,teamEmbed;
    public String prefix;
    public boolean isLooped = false;


}
