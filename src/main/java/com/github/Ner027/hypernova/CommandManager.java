package com.github.Ner027.hypernova;

import com.github.Ner027.hypernova.AdminCommands.*;
import com.github.Ner027.hypernova.GeneralCommands.Daily;
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
    //This class manages and registers all the commands

    //This maps store the commands in groups, if you want to you can add more
    public final Map<String, Command> randomCommands = new HashMap<>();
    public final Map<String, Command> adminCommands = new HashMap<>();
    public final Map<String, Command> musicCommands = new HashMap<>();
    public final Map<String, Command> generalCommands = new HashMap<>();

    //This lists store the command invokes, they are atm, just used for
    //the help command
    public final List<String> adminInvokes = new ArrayList<>();
    public final List<String> musicInvokes = new ArrayList<>();
    public final List<String> teamInvokes = new ArrayList<>();
    public final List<String> generalInvokes = new ArrayList<>();


    private void addCommand(Command command, Map<String, Command> map)
    {
        //When adding a command we need to check if that command isn't already registered
        //If it is we just ignore it

        if (!map.containsKey(command.getInvoke()))
        {
            map.put(command.getInvoke(), command); //Add the command to the specified map

            //Add the command invoke to the right list depending on the map
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

        //For adding a new command just call the addCommand method
        //The command class needs to implement the Command interface
        //Remember that the group that you add it will change the way the
        //command works.
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
        addCommand(new Resume(), musicCommands);
        addCommand(new Pause(),musicCommands);
        addCommand(new Shuffle(),musicCommands);
        addCommand(new Loop(),musicCommands);
        addCommand(new Test(),adminCommands);
        addCommand(new Seek(),musicCommands);
        addCommand(new Daily(),generalCommands);
    }

}
