package de.blockbuild.musikbot.commands.radio;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.commands.MBCommand;
import de.blockbuild.musikbot.core.TrackScheduler;

public class RautemusikCommand extends MBCommand {

	public RautemusikCommand(Bot bot) {
		super(bot);
		this.name = "rautemusik";
		this.aliases = new String[] { "raute", "main", "#" };
		this.help = "Plays Rautemusik!";
		this.arguments = "<12punks | 90s | BigCityBeats | BreakZ.FM | ChartHits | Christmas | Club | DAS Coachingradio | Country | Deutschrap | Goldies | Globalize Yourself Stereo/gyr | Happy | HappyHardcore | HardeR | House | JaM | Kids | Klassik | Lounge | LoveHits | Main | Oriental | PartyHits | Rock | Salsa | Schlager | Sex | Solo Piano | Study | TechHouse | Top40 | Trance | Trap | Traurig | Volksmusik | Wacken Radio | Weihnachten | Workout>";
		this.joinOnCommand = true;
		this.category = MUSIC;
	}

	@Override
	protected void doGuildCommand(CommandEvent event) {
		TrackScheduler trackScheduler = musicManager.getTrackScheduler();
		AudioPlayerManager playerManager = bot.getPlayerManager();
		if (args.isEmpty()) {
			if (event.getMessage().getContentDisplay().trim().toLowerCase().startsWith("main", 1)) {
				playerManager.loadItemOrdered(musicManager, "http://main-high.rautemusik.fm/listen.mp3",
						new ResultHandler(trackScheduler, event));
			} else {
				StringBuilder builder = new StringBuilder(event.getClient().getWarning());
				builder.append(" ").append(this.arguments);
				event.reply(builder.toString());
			}
		} else {
			String link;

			switch (args.toLowerCase()) {
			case "12punks":
				link = "http://12punks-high.rautemusik.fm/listen.mp3";
				break;
			case "90s":
				link = "http://90s-high.rautemusik.fm/listen.mp3";
				break;
			case "bigcitybeats":
				link = "http://bcb-high.rautemusik.fm/listen.mp3";
				break;
			case "breakz.fm":
				link = "http://breakz-high.rautemusik.fm/listen.mp3";
				break;
			case "charthits":
				link = "http://charthits-high.rautemusik.fm/listen.mp3";
				break;
			case "christmas":
				link = "http://christmas-high.rautemusik.fm/listen.mp3";
				break;
			case "club":
				link = "http://club-high.rautemusik.fm/listen.mp3";
				break;
			case "das coachingradio":
				link = "http://coaching-high.rautemusik.fm/listen.mp3";
				break;
			case "country":
				link = "http://country-high.rautemusik.fm/listen.mp3";
				break;
			case "deutschrap":
				link = "http://deutschrap-high.rautemusik.fm/listen.mp3";
				break;
			case "goldies":
				link = "http://goldies-high.rautemusik.fm/listen.mp3";
				break;
			case "globalize yourself stereo":
				link = "http://gys-high.rautemusik.fm/listen.mp3";
				break;
			case "gys":
				link = "http://gys-high.rautemusik.fm/listen.mp3";
				break;
			case "happy":
				link = "http://happy-high.rautemusik.fm/listen.mp3";
				break;
			case "happyhardcore":
				link = "http://happyhardcore-high.rautemusik.fm/listen.mp3";
				break;
			case "harder":
				link = "http://harder-high.rautemusik.fm/listen.mp3";
				break;
			case "house":
				link = "http://house-high.rautemusik.fm/listen.mp3";
				break;
			case "jak":
				link = "http://jam-high.rautemusik.fm/listen.mp3";
				break;
			case "kids":
				link = "http://kids-high.rautemusik.fm/listen.mp3";
				break;
			case "klassik":
				link = "http://klassik-high.rautemusik.fm/listen.mp3";
				break;
			case "lounge":
				link = "http://lounge-high.rautemusik.fm/listen.mp3";
				break;
			case "lovehits":
				link = "http://lovehits-high.rautemusik.fm//listen.mp3";
				break;
			case "main":
				link = "http://main-high.rautemusik.fm/listen.mp3";
				break;
			case "oriental":
				link = "http://oriental-high.rautemusik.fm/listen.mp3";
				break;
			case "partyhits":
				link = "http://partyhits-high.rautemusik.fm/listen.mp3";
				break;
			case "rock":
				link = "http://rock-high.rautemusik.fm/listen.mp3";
				break;
			case "salsa":
				link = "http://salsa-high.rautemusik.fm/listen.mp3";
				break;
			case "schlager":
				link = "http://schlager-high.rautemusik.fm/listen.mp3";
				break;
			case "sex":
				link = "http://sex-high.rautemusik.fm/listen.mp3";
				break;
			case "solo piano":
				link = "http://solopiano-high.rautemusik.fm/listen.mp3";
				break;
			case "study":
				link = "http://study-high.rautemusik.fm/listen.mp3";
				break;
			case "techHouse":
				link = "http://techhouse-high.rautemusik.fm/listen.mp3";
				break;
			case "top40":
				link = "http://top40-high.rautemusik.fm/listen.mp3";
				break;
			case "trance":
				link = "http://trance-high.rautemusik.fm/listen.mp3";
				break;
			case "trap":
				link = "http://trap-high.rautemusik.fm/listen.mp3";
				break;
			case "traurig":
				link = "http://traurig-high.rautemusik.fm/listen.mp3";
				break;
			case "volksmusik":
				link = "http://volksmusik-high.rautemusik.fm/listen.mp3";
				break;
			case "wacken radio":
				link = "http://wackenradio-high.rautemusik.fm/listen.mp3";
				break;
			case "weihnachten":
				link = "http://weihnachten-high.rautemusik.fm/listen.mp3";
				break;
			case "workout":
				link = "http://workout-high.rautemusik.fm/listen.mp3";
				break;

			default:
				link = null;
				break;
			}

			if (!(link == null)) {
				playerManager.loadItemOrdered(playerManager, link, new ResultHandler(trackScheduler, event));
			} else {
				StringBuilder builder = new StringBuilder(event.getClient().getWarning());
				builder.append(" `").append(args).append("` Isn't a vaild radio station");
				event.reply(builder.toString());
			}
		}
	}

	@Override
	protected void doPrivateCommand(CommandEvent event) {
		event.reply(event.getClient().getError() + " This command cannot be used in Direct messages.");

		StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());

		builder.append(" **MusikBot** ").append("by Block-Build\n");
		builder.append("SpigotMC: `https://www.spigotmc.org/resources/the-discord-musikbot-on-minecraft.64277/`\n");
		builder.append("GitHub: `https://github.com/Block-Build/MusikBot`\n");
		builder.append("Version: `").append(bot.getMain().getDescription().getVersion()).append("`\n");
		builder.append("Do you have any problem or suggestion? Open an Issue on GitHub.");

		event.reply(builder.toString());
	}

	private class ResultHandler implements AudioLoadResultHandler {

		private TrackScheduler trackScheduler;
		private CommandEvent event;

		public ResultHandler(TrackScheduler trackScheduler, CommandEvent event) {
			this.trackScheduler = trackScheduler;
			this.event = event;
		}

		@Override
		public void trackLoaded(AudioTrack track) {
			trackScheduler.playTrack(track, event);
		}

		@Override
		public void playlistLoaded(AudioPlaylist playlist) {
			// should never called
		}

		@Override
		public void noMatches() {
			StringBuilder builder = new StringBuilder(event.getClient().getError());
			builder.append(" No result found: ").append(args);
			event.reply(builder.toString());
		}

		@Override
		public void loadFailed(FriendlyException throwable) {
			StringBuilder builder = new StringBuilder(event.getClient().getError());
			builder.append(" faild to load ").append(args);
			event.reply(builder.toString());
		}
	}
}
