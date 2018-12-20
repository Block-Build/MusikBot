package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.blockbuild.musikbot.Main;
import de.blockbuild.musikbot.core.MBCommand;
import de.blockbuild.musikbot.core.TrackScheduler;

public class RautemusikCommand extends MBCommand {

	public RautemusikCommand(Main main) {
		super(main);
		this.name = "rautemusik";
		this.aliases = new String[] { "raute", "main" };
		this.help = "Plays Rautemusik!";
		this.arguments = "<12punks | 90s | BigCityBeats | BreakZ.FM | ChartHits | Christmas | Club | DAS Coachingradio | Country | Deutschrap | Goldies | Globalize Yourself Stereo/gyr | Happy | HappyHardcore | HardeR | House | JaM | Kids | Klassik | Lounge | LoveHits | Main | Oriental | PartyHits | Rock | Salsa | Schlager | Sex | Solo Piano | Study | TechHouse | Top40 | Trance | Trap | Traurig | Volksmusik | Wacken Radio | Weihnachten | Workout>";
	}

	@Override
	protected void doCommand(CommandEvent event) {
		TrackScheduler trackScheduler = main.getBot().getScheduler();
		AudioPlayerManager playerManager = main.getBot().getPlayerManager();
		if (event.getArgs().isEmpty()) {
			if (event.getMessage().getContentDisplay().toLowerCase().startsWith("main", 1)) {
				playerManager.loadItem("http://main-high.rautemusik.fm/listen.mp3",
						new ResultHandler(trackScheduler, event));
			} else {
				StringBuilder builder = new StringBuilder(event.getClient().getWarning());
				builder.append(" ").append(this.arguments);
				event.reply(builder.toString());
			}
		} else {
			String link;

			switch (event.getArgs().toLowerCase()) {
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
				playerManager.loadItem(link, new ResultHandler(trackScheduler, event));
				// StringBuilder builder = new StringBuilder(event.getClient().getSuccess());
				// builder.append(" Stream started");
				// event.reply(builder.toString());
			} else {
				StringBuilder builder = new StringBuilder(event.getClient().getWarning());
				builder.append(" `").append(event.getArgs()).append("` Isn't a vaild Stram");
				event.reply(builder.toString());
			}
		}
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
			//should never called
		}

		@Override
		public void noMatches() {
			StringBuilder builder = new StringBuilder(event.getClient().getError());
			builder.append(" No result found: ").append(event.getArgs());
			event.reply(builder.toString());
			System.out.println("no results found: " + event.getArgs());
		}

		@Override
		public void loadFailed(FriendlyException throwable) {
			StringBuilder builder = new StringBuilder(event.getClient().getError());
			builder.append(" faild to load ").append(event.getArgs());
			event.reply(builder.toString());
			System.out.println("faild to load: " + event.getArgs());
		}
	}

}
