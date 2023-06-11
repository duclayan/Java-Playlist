package studiplayer.ui;

import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import studiplayer.audio.AudioFile;
import studiplayer.audio.NotPlayableException;
import studiplayer.audio.PlayList;
import studiplayer.basic.BasicPlayer;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
public class Player extends Application {
	
	private PlayList playList;
	public static final String DEFAULT_PLAYLIST = "playlists/DefaultPlayList.m3u" ;

	private AudioFile audioFile;
	private volatile boolean stopped = false;
	
	//Constant 
	private String titleTextPrefix = "Current song: ";
	
	//Label
    private Label songDescription;
    private Label playTime;
    
    private Stage mainWindow;
    
	//Buttons
	private Button playButton = createButton("play.png");
	private Button pauseButton = createButton("pause.png");
	private Button stopButton = createButton("stop.png");
	private Button nextButton = createButton("next.png");
	private Button editorButton = createButton("pl_editor.png");
			
	//PlayerEditor
	private PlayListEditor playListEditor;
	private boolean editorVisible;
	
	public Player() {
		super();
	}
	
	public String getPlayListPathname() {
		return audioFile.getPathname();
	}
	
    public void setPlayList(String playListPath) {
        if (playListPath == null || playListPath.isEmpty()) {
            // Use default playlist
            playList = new PlayList();
        } else {
            // Load playlist from the specified path
           try {
			playList.loadFromM3U(playListPath);
		} catch (NotPlayableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }
        refreshUI();
    }


	
	public void setEditorVisible(boolean visible) {
        this.editorVisible = visible;
    }
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		mainWindow = primaryStage;
		// Modify to be an object of play list
		playList = new PlayList();
		playListEditor = new PlayListEditor(this, this.playList);
		editorVisible= false;
		
        // Create labels for song description and play time
        songDescription = new Label();
        playTime = new Label();
       
        mainWindow.setTitle("no current song");
        songDescription.setText("no current song");
        setButtonStates(true,false,false,true,true);
        
        editorButton.setOnAction(e -> {
        	if(editorVisible) {
        		editorVisible = false;
        		playListEditor.hide();
        	} else {
        		editorVisible = true;
        		playListEditor.show();
        	}
        	
        });
        
		List<String> parameters = getParameters().getRaw();	
		
		if(parameters.isEmpty()) {
//			If empty play-list, we will load default
			playTime.setText("--:--");
			playList.loadFromM3U(DEFAULT_PLAYLIST);
		} else {
			// If non-empty play list is passed
			playList.loadFromM3U(parameters.get(0));
			audioFile=  playList.getCurrentAudioFile();
			songDescription.setText(audioFile.toString());;
		}
		
		// BorderPane
		BorderPane mainPane = new BorderPane();
		Scene scene = new Scene(mainPane,700,90);
	
		// HorizontalBox
		HBox hbox = new HBox(playTime, playButton, pauseButton, stopButton, nextButton, editorButton);
		
		// Center all elements of HBOX
		hbox.setAlignment(Pos.CENTER);
		
		// Button triggers

		playButton.setOnAction(e -> {
			playCurrentSong();
			setButtonStates(false,true,true,true,true);
		});
		
		pauseButton.setOnAction(e -> {
		    if (!playList.isEmpty()) {
		        AudioFile currentSong = playList.getCurrentAudioFile();
		        currentSong.togglePause();
		    }
			setButtonStates(false,true,true,true,true);   
		});
		
		stopButton.setOnAction(e -> {
			stopCurrentSong();
			setButtonStates(true,false,false,true,true);
		        
		});
		
		nextButton.setOnAction(e -> {
			stopCurrentSong();
			playList.setCurrent(playList.getCurrent() + 1);
			playCurrentSong();
			setButtonStates(false,true,true,true,true);
		        
			
		});
		
		editorButton.setOnAction(e -> {
			System.out.println("editorButton");
		});
		

		mainPane.setTop(songDescription);
		mainPane.setLeft(hbox);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	//Button Creator
	private Button createButton(String iconfile) {
		Button button = null; 
		
		try {
			URL url = getClass().getResource("/icons/"+ iconfile);

			Image icon = new Image(url.toString());
			ImageView imageView = new ImageView(icon);
			
			imageView.setFitHeight(48);
			imageView.setFitWidth(48);
			
			button=new Button("", imageView);
			button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			
		} catch (Exception e) {
			System.out.println("Image:" + "icons/" + iconfile + " not found!");
			System.exit(-1);
		}
		return button;
	}
	
	
	private void updateSongInfo(AudioFile af){
		if (af != null) {
			mainWindow.setTitle(titleTextPrefix + af.toString());
			songDescription.setText(af.toString());
			playTime.setText("00:00");;	
		} else {
			mainWindow.setTitle("no current song");
			songDescription.setText("no current song");;
			playTime.setText("--:--");;
		}
	};
	
	public void playCurrentSong() {
		this.stopped = false;
		AudioFile af = playList.getCurrentAudioFile();
		
		if(af !=  null) {
			// start threads
			(new TimerThread()).start();
			(new PlayerThread()).start();
		}
		updateSongInfo(playList.getCurrentAudioFile());
	}
	
	private void stopCurrentSong() {
	    this.stopped = true;
	    if (!playList.isEmpty()) {
	        AudioFile currentSong = playList.getCurrentAudioFile();
	        currentSong.stop();
	        updateSongInfo(playList.getCurrentAudioFile());
	    }
	    
	}
	
	private void refreshUI() {
		Platform.runLater(() -> {
			if (playList != null && playList.size() > 0) {
				updateSongInfo(playList.getCurrentAudioFile());
				setButtonStates(true,false,false,true,false);
			}else {
				updateSongInfo(null);
				setButtonStates(true,true,true,true,false);
			}
		});
	}
	
	
	public void setButtonStates(boolean playButtonState, boolean pauseButtonState, boolean stopButtonState, boolean nextButtonState, boolean editorButtonState) {
		playButton.setDisable(!playButtonState); // false // input: false
		// play button should be enabled, so setDisable(false) -> playButtonState should be true
		pauseButton.setDisable(!pauseButtonState);
		stopButton.setDisable(!stopButtonState);
		nextButton.setDisable(!nextButtonState);
		editorButton.setDisable(!editorButtonState);
	}
	
	private class TimerThread extends Thread {
		
		public void run() {
			AudioFile af = playList.getCurrentAudioFile();
			while (!stopped && !playList.isEmpty()) {
				
				try {
					String formattedPosition = af.getFormattedPosition();	
					 Platform.runLater(() -> playTime.setText(formattedPosition));
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		
			
		}
	}
	
	private class PlayerThread extends Thread {
		public void run() {
			while(!stopped && !playList.isEmpty()) {
				try {
					playList.getCurrentAudioFile().play();
					
				} catch (NotPlayableException e) {
					e.printStackTrace();
				}
				
				if(!stopped) {
					playList.changeCurrent();
					updateSongInfo(playList.getCurrentAudioFile());
				}
			}
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
	
}
