package studiplayer.audio;
import java.util.Map;

import studiplayer.basic.BasicPlayer;
import studiplayer.basic.TagReader;
public class TaggedFile extends SampledFile {
	

	protected String album;
	
	public TaggedFile() {}
	
	public TaggedFile(String s) throws NotPlayableException{
		super(s);
		readAndStoreTags();
	}

	public String getAlbum() {
		return album;
	}
	
	
	public String toString() {
		
		String returnString = "";
		
		if (author != null && !author.isEmpty()) {
			returnString += author + " - ";
		} 
		
		if (title != null  && !title.isEmpty() ) {
			returnString += title + " - ";
		}
		
		if (getAlbum() != null  && !getAlbum().isEmpty()) {
			returnString += getAlbum() + " - ";
		}
		
		if (getFormattedDuration() != null && !getFormattedDuration().isEmpty()) {
			returnString += getFormattedDuration();
		}
		
		return returnString;
	};
	

	public void readAndStoreTags() throws NotPlayableException{
		
		try {
			Map<String, Object> tags = TagReader.readTags(this.getPathname());
			Object readAuthor = tags.get("author"); 
			Object readTitle = tags.get("title");
			Object readAlbum = tags.get("album");
			Object readDuration = tags.get("duration");
	
			if (TagReader.readTags(this.getPathname()).get("duration") != null) {
				readDuration = (Integer.parseInt(TagReader.readTags(this.getPathname()).get("duration").toString()));	
			}
			
			if ( readAuthor != null) {
				author =  readAuthor.toString().strip();
				
			}
			if (readTitle != null) {
				title = readTitle.toString().strip();		
			}
			if (readAlbum != null) {
				album = readAlbum.toString().strip();
			}
			if (readDuration !=null) {
				duration =  Integer.parseInt(readDuration.toString());
			} 
		}catch (Exception e) {
			throw new NotPlayableException("Error reading tags from audio file.", getPathname(), e);
		}
	}
	
	public String[] fields() {
		
		String[] returnFields = {author, title, album, getFormattedDuration()};
		
		return returnFields;
	};
	
}
