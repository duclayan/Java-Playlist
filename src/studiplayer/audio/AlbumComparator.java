package studiplayer.audio;

import java.util.Comparator;
// Attributes required for the comparison is not defined in AudioFile Class
// For the album name it is in TaggedFile
public class AlbumComparator implements Comparator<AudioFile>{

	
    @Override
    public int compare(AudioFile af1, AudioFile af2) {
        if (!(af1 instanceof TaggedFile) && af2 instanceof TaggedFile) {
            return -1;
        } else if (!(af2 instanceof TaggedFile) && af1 instanceof TaggedFile) {
            return 1;
        } else if (!(af1 instanceof TaggedFile) && !(af2 instanceof TaggedFile)) {
            return 0;
        } else {
            String album1 = ((TaggedFile) af1).getAlbum();
            String album2 = ((TaggedFile) af2).getAlbum();
            

            if (album1 == null && album2 == null) {
                return 0; // Both albums are null, consider them equal
            } else if (album1 == null) {
                return -1; // album1 is null, place it before album2
            } else if (album2 == null) {
                return 1; // album2 is null, place it before album1
            } else {
            	return album1.compareTo(album2);
            }
        }
    }
}


	


