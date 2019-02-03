[![Release](https://img.shields.io/github/release/Block-Build/MusikBot.svg)](https://github.com/Block-Build/MusikBot/releases/latest)
# MusikBot ![GitHub Logo](/src/main/resources/64.png)  

Discord MusikBot on Minecraft
The Discord MusikBot is a Music Bot you can host on your Minecraft Server as a Plugin! It's unimportant which MC version you are using, MusikBot supports nearly all of them! MusikBot is easy and fast to setup, have FUN!

### Features:
* You can add songs from YouTube, SoundCloud, Bandcamp, Vimeo, Twitch streams, Local files or just URLs!
* Plays playlists from YouTube
* You can search for YouTube videos
* Listen to choosen Radio streams!
* Supported formats: MP3, FLAC, WAV, AAC, Opus, Vorbis, MP4/M4A, OGG, Stream playlists (M3U and PLS)
* Adding songs to queue
* Shuffle the playlist
* YouTube autoplay
* Get information about currend song
* You are able to skip tracks
* Able to change the volume of the Bot
* Save your playlist to load and listen to it next time
* Join the Bot to as much Servers as you want! They will act independently
* Many things can be configured: Should the bot disconnect if it is alone? How about automatically joining a channel? And many more!
* White- and blacklists for users and other bots
* Change the command trigger to whatever you want
* And a lot of more cool stuff! Just have a look to !help and config files;)
* Much more is planned! Suggestions and feedback are welcome! Please open an issue

### Basic Commands:
* !play [URL | title]
* !queue [URL | title]
* !next
* !skip [amount]
* !volume [0-100]
* !info
* !join [channel name]
* !stop
* !leave
* !pause / !resume
* ...
* For more commands and information type !help

### Requirements:
 * Java 8 is Required!

### How to setup:
* Copy paste Plugin in the 'plugins' folder of your Minecraft server
* Start server once to generate the default config
* You have to insert the token to the BotConfig.yml
* To get a Bot Token you have to create a Discord Bot at this site:
  * https://discordapp.com/developers/applications/
  * Create a new Application
  * Navigate to Bot and click 'add Bot'
  * Get the Token
* Also fill in your Discord user ID
  * You could easy get Channel and User ID's from discord by right click after enableing the Developer Mode
* After the server started successfully there will be an invite URL in console and config to join the Bot to your Discord Server.

### Support/Suggestions/Bugs:
I've create this Bot to warm up my java knowledge. I hope you will enjoy it.
If you have any problems, suggestions or bugs feel free to create an issue. Every feedback is welcome, thanks.:giggle:


This project would not be possible without JDA the DiscodJavaApi and lavaplayer for the audio mechanic.
