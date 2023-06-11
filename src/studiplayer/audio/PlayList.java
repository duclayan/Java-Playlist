package studiplayer.audio;
import java.io.*;
import java.util.*;
public class PlayList extends LinkedList<AudioFile> {
	
	private int currentPosition;
	private boolean randomOrder;
	//	Parameter-less Constructor
	public PlayList() {
		super();	
		currentPosition = 0;
	}
	
	// Constructor with Parameter
	public PlayList(String pathname){
		this();
		try {
			loadFromM3U(pathname);
		} catch (Exception e) {
			if (this.size() > 0) {
				//
			} else {
				throw new RuntimeException("File not found", e);
			}
			//
		}
		
	}
	// getCurrent()
	public int getCurrent() {
		return currentPosition;
	}
	
	// setCurrent()
	public void setCurrent(int current) {
		currentPosition = current;
		
	}
	
	// getCurrentAudioFile
	public AudioFile getCurrentAudioFile() {
		
		if ( isEmpty() || currentPosition < 0 || currentPosition >= size()) {
			return null;
		} else {
			return get(currentPosition);
		}
	}
	
	//changeCurrent
	public void changeCurrent() {
		// Current Position is counted as 0,1,2 
		// Size returns the length, counts as 1,2,3
		if(isEmpty() || currentPosition < 0 || currentPosition + 1 >= size()) {
			currentPosition = 0;
		} else {
			currentPosition += 1;	
		}
		
		if (currentPosition == 0 && randomOrder) {
			Collections.shuffle(this);
		}
	}
	
	// setRandomOrder
	public void setRandomOrder(Boolean randomOrder) {
		
		this.randomOrder = randomOrder;
		
		if (randomOrder) {
			Collections.shuffle(this);
			// random playback
		} else {
			// sequential playback
			
		}
	}
	
	// saveAsM3U - WRITE
	public void saveAsM3U(String pathname) {
	    try (PrintWriter writer = new PrintWriter(pathname)) {
	        writer.println("#EXTM3U");
	        for ( int i = 0; i < this.size(); i++) {
	        	AudioFile newFile = AudioFileFactory.getInstance(this.get(i).pathname);
	        	writer.println("#EXTINF:" + newFile.getFilename());
	            writer.println(newFile.getPathname());
	        }
	    } catch (Exception e) {
//	    	throw new NotPlayableException(pathname,e);
//	        System.err.println("Unable to save playlist: " + e.getMessage());
	    }
	}
	// loadFromM3U- READS
	public void loadFromM3U(String pathname) throws NotPlayableException {
		String fname = pathname;
		Scanner scanner = null;
		String line;
		
		try {
			// Create a Scanner
			scanner = new Scanner(new File(fname));
			// Since this worked out we know that the file is readable
			// Read line by line
			
			while (scanner.hasNextLine()) {
				line = scanner.nextLine();
				System.out.println(line);
				try {
					if (line.charAt(0) == '#' || line.isEmpty() || line.isBlank()) {
						// ignore
					} else {
						AudioFile newFile =AudioFileFactory.getInstance(line);
						this.add(newFile);
					}
				} catch (Exception e) {
					// Just let go
				}

			}
		} catch (Exception e) {
			 e.printStackTrace();
			System.out.println(e.getMessage());
			throw new NotPlayableException(pathname,e.getMessage());		
		}  
		
		scanner.close();
		

	}
	// Sort Method
	public void sort(SortCriterion order) {
		System.out.println(this);

        switch (order) {
        	
            case AUTHOR:
            	Collections.sort(this, new AuthorComparator());
                break;
            case TITLE:
            	Collections.sort(this,  new TitleComparator());;
                break;
            case ALBUM:
            	Collections.sort(this,  new AlbumComparator());;
                break;
            case DURATION:
            	Collections.sort(this,  new DurationComparator());;
            	break;
            default:
                throw new IllegalArgumentException("Invalid sorting criterion.");
        }
    }
}
