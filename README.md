Teamspeak Moderator Bot v0.0.1
==============================

Written in Java 1.8.0_201 by Ian Cowan

*Please note the stability of this bot is currently unknown*

Prerequisites
--------------
- Java 1.8.0_201 Required

Installation
------------

1. You can clone the entire repository, but the only necessary file is `ts3Bot-X.X.X.jar` under `jarfile/ts3Bot-X.X.X.jar`

2. Run the jar file
    - `java -jar ts3Bot-X.X.X.jar`

3. If you do not have a config file in the same directory as the bot, one will be created. Complete this as appropiate following `jar-file/config.properties.example`

4. Run the jar file again, and the bot will start up. If everything is configured correctly the bot will begin working immediately.

5. If you close the window, the bot will stop running. It is recommended to create a service so the bot will run all of the time. The steps to do this on CentOS will be below.

Creating a Service (CentOS 7)
-----------------------------
1. Navigate to `/lib/systemd/system`.
2. Create a file called `ts3Bot.service` and include the following contents:
```python
[Unit]
Description=Teamspeak Moderator Bot
After=network.target

[Service]
WorkingDirectory=/home/path/to/ts3Bot # Update with the appropiate path
ExecStart=/bin/bash -c "java -jar /home/path/to/ts3Bot/ts3Bot-X.X.X.jar" # Update with the appropiate path
User=ts # Update with the appropiate user
Group=ts # Update with the appropiate group

[Install]
WantedBy=multi-user.target
```
3. Close out of the file and save it
4. Run the command `systemctl start ts3Bot` to start the bot
    - To stop the bot, you can use `systemctl stop ts3Bot`
5. Enable the service so it will begin running on startup withe the command `systemctl enable ts3Bot`

Troubleshooting
---------------
All questions can be directed to [ian@cowanemail.com](mailto:ian@cowanemail.com).

Usage
-----
This was created for the use by the ZTL ARTCC and has been made available to the public. This is open source so feel free to make whatever changes you may wish!

Version History
---------------
- 4/15/2019 -> Version 0.0.1 Released -> Initial release
