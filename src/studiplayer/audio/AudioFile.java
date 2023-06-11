package studiplayer.audio;
/**
 * @author dominiqueduclayan
 *
 */
import java.io.File;
public abstract class AudioFile {
// getters
// ParsePathname , parseFilename
	/**
	 * @param args
	 */
	protected String pathname,filename, title, author;
	
	//Konstruktor
	public AudioFile(){}
	
	// Weiteren Konstruktor
	public AudioFile(String pathname) throws NotPlayableException{

			File file = new File(pathname);
			parsePathname(pathname);
			parseFilename(getFilename());
			file = new File(this.pathname);
			
			if (!file.canRead()) {
				throw new  NotPlayableException(pathname, "Die Datei kann nicht gelesen werden.");
		    }
	}
	
	private boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().indexOf("win") >= 0;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getPathname() {
		return pathname;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public void parseFilename(String filename) {
		
	 if ( filename.contains(" - " )) {
		 // " - " separates author and title
		 // if only " - ", then it is empty
			if (filename.equals(" - ")){
				this.author = "";
				this.title = "";
			} else {
				// posMinus: position " - "
				int posMinus = filename.lastIndexOf(" - ");
				// author : from first character until " - " from filename
				this.author = filename.substring(0, posMinus).trim();
				// title : from " - " until last character from filename
				this.title = filename.substring(posMinus+3).trim();
			}
		} else {
			this.author = "";
			this.title = filename.trim();
		}
		
	 	// remove extensions (z.b: .mp3)
		if (title.contains(".")) {
			this.title = this.title.substring(0, title.lastIndexOf(".")).trim();
		}
	}

	public void parsePathname(String pathname) {

		String pathnameTrim = pathname.trim();
		Boolean laufwerk = false;
		
		
		if (pathname.length() > 1 && pathnameTrim.length()>0) {
			
			int lastSlash;
			
			char char1 = pathname.charAt(0);
			char char2 = pathname.charAt(1);
			boolean isLetter = (char1>= 'A' && char1<= 'Z' || char1>= 'a' && char1<= 'z' );
			
			
			if (isLetter && char2 == ':') { // zb: d:
				laufwerk = true;
			}
			
			if (pathname.contains("\\")) {
				pathname  = pathname.replace("\\","/");
				while(pathname.indexOf("//") != -1) {
					pathname = pathname.replace("//", "/");
				}
			}
			
			if (pathname.contains("/")&& pathname.contains("//")) {
				while(pathname.indexOf("//") != -1 ) {
					pathname = pathname.replace("//", "/");
				}
			}
			
			if (isWindows()) {
				pathname = pathname.replace("/", "\\");
				this.pathname = pathname;
				int lastslash = pathname.lastIndexOf("\\");
				this.filename =  pathname.substring(lastslash + 1 , pathname.length());
			} else {
				if(laufwerk) {
					pathname = pathname.substring(2);
					pathname = "/" + char1 + pathname;
				}
				this.pathname = pathname;
				
				lastSlash = pathname.lastIndexOf("/");
				this.filename = pathname.substring(lastSlash+1, pathname.length());

			}
			
		} else {
			this.pathname = pathname;
			this.filename = pathname;
		}
	
	}  

	public String toString() {
		if (getAuthor().isEmpty()) {
			return getTitle();
		} else {
			return author + " - " + title;
		}
	}
	
	public abstract void play() throws NotPlayableException;
	
	public abstract void togglePause();
	
	public abstract void stop();
	
	public abstract String getFormattedDuration();;
		
	public abstract String getFormattedPosition();;
		
	public abstract String[] fields();
}
