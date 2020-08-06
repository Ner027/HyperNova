package com.github.Ner027.hypernova;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.util.ArrayList;
import java.util.List;

public class GuildVars
{
    public List<Member> memberList = new ArrayList<Member>();
    public Message blueMessage, redMessage, confirmMessage, mainMessage, musicMessage, musicQueue;
    public boolean canUpdate;
    public VoiceChannel voiceBlue, voiceRed, mainChannel;
    public Role managerRole;
    public TextChannel randomChannel, musicChannel;
    public List<Member> teamBlue, teamRed;
    public Long guildID;
    public EmbedBuilder musicMessageEmbed;
    public String prefix;


}
