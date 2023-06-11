package studiplayer.audio;

import java.util.Comparator;

public class TitleComparator implements Comparator <AudioFile> {
	@Override 
	
	public int compare(AudioFile obj1, AudioFile obj2) {
		
		if (obj1 == null || obj2 == null) {
			throw new NullPointerException ("Ein oder Beides Pointer sind Null");
		}
		
		String title1 = obj1.getTitle();
		String title2 = obj2.getTitle();
		return title1.compareTo(title2);
	}
}
