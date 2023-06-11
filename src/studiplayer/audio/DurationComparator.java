package studiplayer.audio;
import java.util.Comparator;

public class DurationComparator implements Comparator<AudioFile> {

	
	@Override
	public int compare(AudioFile af1, AudioFile af2) {
		
		String duration1 = af1.getFormattedDuration();
		String duration2 = af2.getFormattedDuration();
		
        if (duration1 == null && duration2 == null) {
            return 0; // Both duration are null, consider them equal
        } else if (duration1 == null) {
            return -1; // duration1 is null, place it before duration2
        } else if (duration2 == null) {
            return 1; // duration2 is null, place it before duration1
        } else {
        	return duration1.compareTo(duration2);
        }
	}
}
