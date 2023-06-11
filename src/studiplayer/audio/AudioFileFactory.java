package studiplayer.audio;
import java.util.*;
import java.io.File;
import java.io.IOException;
public class AudioFileFactory {
	
	public static AudioFile getInstance(String pathname) throws NotPlayableException{
		Scanner scanner = null;
		try {
			int dotIndex = pathname.lastIndexOf('.');
			String fileType = pathname.substring(dotIndex+1);
			
			// convert to lowercase
			fileType = fileType.toLowerCase();

			if (fileType.equals("mp3") || fileType.equals("ogg")) {
				return new TaggedFile(pathname);
			} else if (fileType.equals("wav")) {
				return new WavFile(pathname);
			} else {
				throw new NotPlayableException(pathname,"Unknow suffix for AudioFile:");
			}
		}  catch (Exception e) {
			throw new NotPlayableException(pathname ,e);
		}		
	}

}
