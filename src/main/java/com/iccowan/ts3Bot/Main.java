package com.iccowan.ts3Bot;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

public class Main {
    public static Dictionary getConfig() {
        // Check for the config file
        // If it doesn't exist, create it and close the program for the user to write to it
        File configFile = new File("config.properties");
        try {
            if(configFile.createNewFile()) {
                System.out.println("Thank you for using Bitchin' Betty! We are creating a config file for your use...");
                createConfigFile();
                System.out.println("The config file has been created.");
                System.out.println("Ending program...");
                System.exit(0);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        // Read through the file and get all of the important values
        Dictionary config = new Hashtable();
        try {
            BufferedReader file = new BufferedReader(new FileReader(configFile));
            String line = file.readLine();
            while (line != null) {
                if(line.length() > 0) {
                    if(line.charAt(0) != '#') {
                        String[] splitString = line.split("=", 0);
                        if(1 < splitString.length) {
                            config.put(splitString[0], splitString[1].trim());
                        } else {
                            config.put(splitString[0], 0);
                        }
                    }
                }
                line = file.readLine();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        return config;
    }

    // Creates a config file
    public static void createConfigFile() {
        try {
            File config = new File("config.properties");
            FileWriter writeConfig = new FileWriter(config);
            writeConfig.write("# Teamspeak Moderator Bot: BITCHIN BETTY\n");
            writeConfig.write("# Written by Ian Cowan\n");
            writeConfig.write("\n");
            writeConfig.write("# Host name to connect to teamspeak (i.e. ts.yourdomain.com)\n");
            writeConfig.write("HOST_NAME=\n");
            writeConfig.write("\n");
            writeConfig.write("# Servery query username\n");
            writeConfig.write("SERVER_QUERY_USERNAME=serveradmin\n");
            writeConfig.write("\n");
            writeConfig.write("# Servery query password\n");
            writeConfig.write("SERVER_QUERY_PASSWORD=\n");
            writeConfig.write("\n");
            writeConfig.write("# Number of the virtual server you want the bot on\n");
            writeConfig.write("VIRTUAL_SERVER=1\n");
            writeConfig.write("\n");
            writeConfig.write("# Bot Name\n");
            writeConfig.write("BOT_NAME=BITCHIN BETTY\n");
            writeConfig.write("\n");
            writeConfig.write("# Max Idle Time Before Moving in Minutes\n");
            writeConfig.write("MAX_IDLE_TIME_MOVE=\n");
            writeConfig.write("\n");
            writeConfig.write("# Away Channel Number\n");
            writeConfig.write("AWAY_CHANNEL=\n");
            writeConfig.write("\n");
            writeConfig.write("# Max Idle Time Before Warning\n");
            writeConfig.write("IDLE_TIME_WARN=\n");
            writeConfig.write("\n");
            writeConfig.write("# Max Idle Time Before Kicking\n");
            writeConfig.write("IDLE_TIME_KICK=\n");
            writeConfig.write("\n");
            writeConfig.write("# Server Groups to Ignore (separate with a comma)\n");
            writeConfig.write("IGNORE_GROUP=\n");
            writeConfig.write("\n");
            writeConfig.write("# Kick or Move for recording? K => Kick, M => Move, Blank => Neither\n");
            writeConfig.write("RECORDING=\n");
            writeConfig.write("\n");
            writeConfig.write("# Move for muting input? Y => yes, N => no\n");
            writeConfig.write("MUTE_INPUT=\n");
            writeConfig.write("\n");
            writeConfig.write("# Move for muting output? Y => yes, N => no\n");
            writeConfig.write("MUTE_OUTPUT=\n");
            writeConfig.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Get the config
        Dictionary config = getConfig();

        // Get a bot going
        Bot tsBot = new Bot(config);

        // Have the bot connect to the server
        tsBot.botConnect();

        // Have the bot do its thing
        String[] emptyArray = {};
        tsBot.main(emptyArray, tsBot, config);
    }
}
