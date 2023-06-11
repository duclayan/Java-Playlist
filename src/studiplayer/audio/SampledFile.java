package studiplayer.audio;
import java.util.Objects;

import studiplayer.basic.BasicPlayer;

public abstract class SampledFile extends AudioFile {
	protected long duration;
	
	public SampledFile() {}
	
	public SampledFile(String x) throws NotPlayableException{
			super(x);
	}

	
	public void togglePause() {
		BasicPlayer.togglePause();
	}
	
	public void stop() {
		BasicPlayer.stop();
	}
	
	public void play() throws NotPlayableException {
		try {
			BasicPlayer.play(this.getPathname());
		} catch (Exception e) {
			throw new NotPlayableException(pathname,e);
		}
		
		
	}
	
	public String getFormattedDuration() { 
		if (Objects.isNull(duration)) {
			return "";
		}else {
			return timeFormatter(duration);
		}
	}
	
	public String getFormattedPosition() {
		long position = BasicPlayer.getPosition();
		return timeFormatter(position);
	}

	public static String timeFormatter(long microtime) {
		// Convert MacroSeconds -> Seconds
		long toSeconds = (microtime/1000000);
		// Get Minute and Seconds Conversion
		long mm = toSeconds/60;
		long ss = toSeconds - mm*60;
		
		if (toSeconds < 0 || microtime < 0) {
			throw new RuntimeException("Negative time value provided");
		} else if (mm > 99 || ss >= 60) {
			throw new RuntimeException("Time value exceed allowed format");
		} else {
			String timeString = String.format("%02d:%02d", mm, ss);
			return timeString;
		}
	}
	
	

}
