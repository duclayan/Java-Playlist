package studiplayer.audio;
import studiplayer.basic.WavParamReader;
import java.util.Arrays;  
public class WavFile extends SampledFile{
	
	public WavFile() {}
	
	public WavFile(String x) throws NotPlayableException {
		super(x);
		readAndSetDurationFromFile(pathname);
	};

	public String toString() {
		return author + " - " + title + " - " + getFormattedDuration();
	};
	
	public String[] fields() {
		
		String[] returnFields = {author, title, "", getFormattedDuration()};
		
		return returnFields;
	};
	public static long computeDuration(long numberOfFrames, float frameRate) {
		
		long duration = (long) ((numberOfFrames / frameRate ) * (1e6));
		
		return Math.round(duration);
	};
	
	public void readAndSetDurationFromFile(String pathname) throws NotPlayableException {
		try {
			WavParamReader.readParams(pathname);
			
			long numberOfFrames = WavParamReader.getNumberOfFrames();
			float frameRate = WavParamReader.getFrameRate();
			
			duration =  computeDuration(numberOfFrames, frameRate);
		
		} catch (Exception e) {
			throw new NotPlayableException(pathname,e);
		}
	}

}
