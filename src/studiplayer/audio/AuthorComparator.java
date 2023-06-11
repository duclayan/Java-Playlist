package studiplayer.audio;

import java.util.Comparator;

public class AuthorComparator implements Comparator <AudioFile> {

	@Override
	public int compare(AudioFile o1, AudioFile o2) {
		// TODO Auto-generated method stub
		if(o1 == null || o2 == null) {
			throw new NullPointerException("Ein oder beide AudioFile-Objekte sind null.");
		}
        String author1 = o1.getAuthor();
        String author2 = o2.getAuthor();
        
		return author1.compareTo(author2);
	}


}
