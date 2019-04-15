package com.iccowan.ts3Bot;
import com.github.theholywaffle.teamspeak3.*;

import com.github.theholywaffle.teamspeak3.api.wrapper.*;

import java.util.List;
import java.util.Date;
import java.util.Dictionary;
import java.text.SimpleDateFormat;

/**
 * Hello world!
 *
 */
public class Bot {
    // Init attributes
    final TS3Config config = new TS3Config();
    final TS3Query query;
    final TS3Api api;
    final Dictionary botConfiguration;

    // Constructor
    public Bot(Dictionary bConf) {
        config.setHost((String) bConf.get("HOST_NAME"));
        query = new TS3Query(config);
        query.connect();
        api = query.getApi();
        botConfiguration = bConf;
    }

    // The bot connects to the server
    public void botConnect() {
        System.out.println("Bot connecting to server...");
        api.login((String) botConfiguration.get("SERVER_QUERY_USERNAME"), (String) botConfiguration.get("SERVER_QUERY_PASSWORD"));
        api.selectVirtualServerById(Integer.parseInt((String) botConfiguration.get("VIRTUAL_SERVER")));
        api.setNickname((String) botConfiguration.get("BOT_NAME"));
        System.out.println("Bot connected successfully!");
    }

    // Checks idle times on clients
    public void checkIdleTimes() {
        List<Client> clients = api.getClients();
        for(Client c : clients) {
            long idleTime = c.getIdleTime() / 60000;

            // Decide whether the user should be kicked or moved
            this.doSomethingWithIdleTime(c, idleTime);
        }
    }

    // Do something to update the client depending on their idle time
    public void doSomethingWithIdleTime(Client client, long idleTime) {
        // Ignore clients with certain server groups
        int[] serverGroups = client.getServerGroups();
        String groupIgnores = (String) botConfiguration.get("IGNORE_GROUP");
        String[] groups = groupIgnores.split(",", 0);
        boolean ignore = false;
        for(String g : groups) {
            int gInt = Integer.parseInt(g);
            for(int cG : serverGroups) {
                if(gInt == cG) {
                    ignore = true;
                }
            }
        }

        // Ignore if they have the specific server group or are a server query client
        if(!ignore && !client.isServerQueryClient()) {
            // Get configuration variables
            int maxIdleBeforeWarn = Integer.parseInt((String) botConfiguration.get("IDLE_TIME_WARN"));
            int maxIdleBeforeMove = Integer.parseInt((String) botConfiguration.get("MAX_IDLE_TIME_MOVE"));
            int maxIdleBeforeKick = Integer.parseInt((String) botConfiguration.get("IDLE_TIME_KICK"));
            int awayChannelId = Integer.parseInt((String) botConfiguration.get("AWAY_CHANNEL"));
            int clientId = client.getId();

            // Idle moves/kicks
            if(idleTime >= maxIdleBeforeKick && maxIdleBeforeKick != 0) {
                api.sendPrivateMessage(clientId, "You have been kicked for " + Integer.toString(maxIdleBeforeKick) + " minutes of inactivity!");
                api.kickClientFromServer("Inactivity", clientId);
            } else if(idleTime >= (maxIdleBeforeKick - 5) && maxIdleBeforeKick != 0) {
                api.sendPrivateMessage(clientId, "You are about to be kicked for " + Integer.toString(maxIdleBeforeKick) + " minutes of inactivity!");
            } else if(idleTime >= maxIdleBeforeMove && maxIdleBeforeMove != 0 && client.getChannelId() != awayChannelId) {
                api.sendPrivateMessage(clientId, "You have been moved for " + Integer.toString(maxIdleBeforeMove) + " minutes of inactivity!");
                api.moveClient(clientId, awayChannelId);
            } else if(idleTime >= maxIdleBeforeWarn && maxIdleBeforeWarn != 0 && client.getChannelId() != awayChannelId) {
                api.sendPrivateMessage(clientId, "You are about to be moved for " + Integer.toString(maxIdleBeforeMove) + " minutes of inactivity!");
            }

            // Get configuration for recording and muting
            String recording = ((String) botConfiguration.get("RECORDING")).toUpperCase();
            String muteInput = ((String) botConfiguration.get("MUTE_INPUT")).toUpperCase();
            String muteOutput = ((String) botConfiguration.get("MUTE_OUTPUT")).toUpperCase();

            // Recording moves
            if(client.isRecording() && recording.charAt(0) == 'K') {
                api.kickClientFromServer("Recording is not permitted in the teamspeak.", clientId);
            } else if(client.isRecording() && recording.charAt(0) == 'M') {
                api.moveClient(clientId, awayChannelId);
                api.sendPrivateMessage(clientId, "Recording is not permitted in the teamspeak.");
            }

            // Headphones muted moving
            if(client.isOutputMuted() && muteOutput.charAt(0) == 'Y' && idleTime >= 1 && client.getChannelId() != awayChannelId) {
                api.moveClient(clientId, awayChannelId);
                api.sendPrivateMessage(clientId, "You have been moved for muting your output!");
            }

            // Mic muted moving
            if(client.isInputMuted() && muteInput.charAt(0) == 'Y' && idleTime >= 1 && client.getChannelId() != awayChannelId) {
                api.moveClient(clientId, awayChannelId);
                api.sendPrivateMessage(clientId, "You have been moved for muting your input!");
            }

            // Finally, help them out and move them if they are set to away
            if(client.isAway() && client.getChannelId() != awayChannelId) {
                api.moveClient(clientId, awayChannelId);
            }
        }
    }

    public static void main(String[] args, Bot tsBot, Dictionary config) {
        // Over a long period of time, let the bot do its thing
        while(true) {
            // Check for the idle times
            tsBot.checkIdleTimes();
        }
    }
}
