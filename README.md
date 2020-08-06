# HyperNova
HyperNova is a Music/Util/Fun Discord Bot built using JDA and a lot of coffee.


**DISCLAIMER: THIS PROJECT HAS NO DOCUMENTATION, AND NEEDS STUFF THAT REQUIRES SOME KNOWLADGE**

Commands:

HyperNova commands are divided in two main groups:

**Prefix commands:**

This commands can be executed anywhere on the server where the bot has permission to read, write and delete messages.
Inside this group there are other sub groups:

-Admin:
Commands that help with the server administration and allow to setup the bot.

-General:
Commands that everyone can use as the name says they can be a lot of stuff.

**Channel specific commands**

As the name implies this commands can only be executed on specific text channels and they dont need to start with a prefix.

-Music:
Commands that control the music player, if you want to play something you dont need to type any command at all just go to
the text channel defined as the music channel and write the title of what you want to play, or paste an URL.

-Teams:
Commands that control the "Team Generator" part of the bot.


**More about HyperNova**

**Music:**

HyperNova is equiped with a fully capable music player based on LavaPlayer and Youtube Data v3 API.

After making it join your server you can setup a text channel as the main music channel,you can achieve this by 
executing the command "setupmusic", this command is considered an admin command so you need the permissions to run
it and it has to start with the prefix.

**Team Generator**

This part of the bot was built for people who like to play coustum games with their friends and just want a quick and 
easy way of generating random teams based on the players that joined the generator.

Similar to the music player, "Team Generator" also needs a dedicated text channel, which should be setted using the command
"teamsetup", after executing this command the bot will set up the channel with all you need.

For joining the generator all you need to do is go to the designated text channel and type "run",no prefix needed,after that 
your name will be added to the player list.As soon as everyon as joined a "Manager" can run the command "generate" and the teams
will then be generated, if you are not happy with the teams you can just execute "generate" again and it will cook yourself some
freshly generated teams!

The "Manager" needs to be identified with a role, you can create the role yourself and it doesn't need any special permissions or anything
it is just a way to make sure that the person who will handle it is not some crazy lunatic whose gonna screw up everything.

After creating a role, or using an already existing one, you can execute "setmanager @Role". For it to work you can't just type the role
name you need to mention it.

To set a server member as "Manager" you can add the role yourself or you can execute "addmanager @Member".As the "setmanager" command it
needs to mention at least one member, if you want to you can mention more than one member.

**Both of these commands fall into the admin commands so they need to be executed with a prefix and you need the permissions"




**How to run the bot yourself**

If you want to you can start by compiling the project yourself, the project uses Gradle so I sugest that if you want to compile it 
yourself you know at least the basics of Java and how to use Gradle.

If you really just want to get the bot up and running, just go to the releases tab and download the jar file.

(Ofc, you need to have Java JRE installed to run it)

For HyperNova to work you will also need to have MySQL database.This database needs to have specific tables and collumns so you will need
to run the following queries:

```
CREATE TABLE  guildManager(
    guildID BIGINT NOT NULL,
    musicChannelID BIGINT,
    managerRoleID BIGINT,
    mainVoice BIGINT,
    randomChannelID BIGINT,
    guildPrefix VARCHAR(10)
);

CREATE TABLE memberData(
    memberID BIGINT NOT NULL,
    memberWins INT,
    PRIMARY KEY (memberID)
);
```

If you dont know anything about databases, you will need to run this queries in something like [MySQL Workbench](https://www.mysql.com/products/workbench/) or [JetBrains Datagrip](https://www.jetbrains.com/datagrip/).

After you have all of this done, you can finally run the jar, on first time setup a config file will be generated, after that you can just close the program and open the config file with any text editor, inside of that file you will have to fill all the fields, otherwise the bot might go boom.

**About the config file**

For the config file you will need to have:

-Discord bot token.
-Google API key (Make sure the key's project has Youtube Data API v3 on it).
-Database URL.
-Database User.
-Database Password.
-Bot default prefix.
-Discord Owner ID.


**This bot is a WIP, still has a lot of stuff that needs work on, and features that are only half done**
.













