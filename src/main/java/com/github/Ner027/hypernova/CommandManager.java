package com.github.Ner027.hypernova;

import com.github.Ner027.hypernova.AdminCommands.*;
import com.github.Ner027.hypernova.GeneralCommands.Help;
import com.github.Ner027.hypernova.GeneralCommands.Profile;
import com.github.Ner027.hypernova.MusicCommands.*;
import com.github.Ner027.hypernova.RandomCommands.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager
{
    public final Map<String, Command> randomCommands = new HashMap<>();
    public final Map<String, Command> adminCommands = new HashMap<>();
    public final Map<String, Command> musicCommands = new HashMap<>();
    public final Map<String, Command> generalCommands = new HashMap<>();
    public final List<String> adminInvokes = new ArrayList<>();
    public final List<String> musicInvokes = new ArrayList<>();
    public final List<String> teamInvokes = new ArrayList<>();
    public List<String> generalInvokes = new ArrayList<>();


    private void addCommand(Command command, Map<String, Command> map)
    {
        if (!map.containsKey(command.getInvoke()))
        {
            map.put(command.getInvoke(), command);

            if (map == adminCommands)
                adminInvokes.add(command.getInvoke());
            else if (map == musicCommands)
                musicInvokes.add(command.getInvoke());
            else if (map == randomCommands)
                teamInvokes.add(command.getInvoke());
            else if (map == generalCommands)
                generalInvokes.add(command.getInvoke());
        }


    }

    public CommandManager()
    {

        addCommand(new AddManager(), adminCommands);
        addCommand(new RandomSetup(), adminCommands);
        addCommand(new MainVoice(), adminCommands);
        addCommand(new EndGame(), randomCommands);
        addCommand(new SetManagerRole(), adminCommands);
        addCommand(new JoinRandom(), randomCommands);
        addCommand(new SplitMembers(), randomCommands);
        addCommand(new GenerateTeams(), randomCommands);
        addCommand(new LeaveRandom(), randomCommands);
        addCommand(new Reset(), randomCommands);
        addCommand(new Profile(), generalCommands);
        addCommand(new Play(), musicCommands);
        addCommand(new MusicSetup(), adminCommands);
        addCommand(new Join(), musicCommands);
        addCommand(new Clear(), adminCommands);
        addCommand(new Skip(), musicCommands);
        addCommand(new Ping(), adminCommands);
        addCommand(new Mute(), musicCommands);
        addCommand(new Remove(), musicCommands);
        addCommand(new SetPrefix(), adminCommands);
        addCommand(new Help(), generalCommands);
    }

}
