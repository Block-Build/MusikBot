package de.blockbuild.musikbot.core;

import java.io.IOException;
import java.nio.charset.Charset;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.DataFormatTools;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class ExtendYoutubeAudioSourceManager extends YoutubeAudioSourceManager {

	public String getYTAutoPlayNextVideoId(String videoId) throws IOException {
		try (CloseableHttpResponse response = getHttpInterface().execute(new HttpGet(videoId))) {

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				throw new IOException("Invalid status code for video page response: " + statusCode);
			}

			String html = IOUtils.toString(response.getEntity().getContent(), Charset.forName("UTF-8"));
			String part = DataFormatTools.extractBetween(html, "watch-sidebar-head", "stat attribution");
			String autoVideoId = getVideoIdFromString(part);
			if (autoVideoId == null) {
				return null;
			} else {
				return getWatchUrl(getVideoIdFromString(part));
			}
		}
	}

	public String getYTAutoPlayNextVideoId(AudioTrack track) throws IOException {
		try (CloseableHttpResponse response = getHttpInterface().execute(new HttpGet(track.getInfo().uri))) {

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				throw new IOException("Invalid status code for video page response: " + statusCode);
			}

			String html = IOUtils.toString(response.getEntity().getContent(), Charset.forName("UTF-8"));
			String part = DataFormatTools.extractBetween(html, "watch-sidebar-head", "stat attribution");
			String autoVideoId = getVideoIdFromString(part);
			if (autoVideoId == null) {
				return null;
			} else {
				return getWatchUrl(getVideoIdFromString(part));
			}
		}
	}

	private String getVideoIdFromString(String string) {
		if (string == null || string.length() < 20) {
			return null;
		}

		int startIndex = string.indexOf("/watch?v=");
		if (startIndex == 0) {
			return null;
		} else {
			startIndex += "/watch?v=".length();
		}
		return string.substring(startIndex, startIndex + 11);
	}

	private static String getWatchUrl(String videoId) {
		return "https://www.youtube.com/watch?v=" + videoId;
	}
}
