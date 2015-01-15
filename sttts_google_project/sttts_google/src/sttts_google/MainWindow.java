package sttts_google;

import java.io.IOException;
import java.net.MalformedURLException;
import java.awt.EventQueue;
import java.awt.Font;

import javaFlacEncoder.FLACFileWriter;

import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JLabel;

import javazoom.jl.player.Player;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.*;
import com.darkprograms.speech.synthesiser.Synthesiser;

public class MainWindow {

	private static final String YOUR_API_KEY = "AIzaSyBYzYZhTogQW_D8jMVgm2qxblVUtZgEdN8";
	private static final String YOUR_LANGUAGE = "auto"; // better: "de-DE"
	private JFrame mainFrame;
	private JScrollPane scrollPane;
	private static JTextArea textArea;

	public static void main(String[] args) throws MalformedURLException, IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainWindow() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		initialize();

		GSpeechDuplex dup = new GSpeechDuplex(YOUR_API_KEY);
		@SuppressWarnings("resource")
		Microphone mic = new Microphone(FLACFileWriter.FLAC);
		
		mic.open();
		dup.setLanguage(YOUR_LANGUAGE);
		dup.addResponseListener(new GSpeechResponseListener(){// Adds the listener
	        public void onResponse(GoogleResponse gr){
	        	textArea.insert(gr.getResponse() + " (confidence: " + Double.parseDouble(gr.getConfidence())*100 + ")\n", 0);
	        	new Thread(new PlayMP3Thread(gr.getResponse())).run();
	        }
	    });
			
		try {
			dup.recognize(mic.getTargetDataLine(), mic.getAudioFormat());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initialize() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		mainFrame = new JFrame();
		mainFrame.setTitle("sttts");
		mainFrame.setResizable(true);
		mainFrame.setBounds(100, 100, 300, 500);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(new BoxLayout(mainFrame.getContentPane(), BoxLayout.Y_AXIS));
		
		JLabel ttsttLabel = new JLabel("speech to text to speech");
		mainFrame.getContentPane().add(ttsttLabel);
		ttsttLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ttsttLabel.setFont(new Font("Georgia", Font.PLAIN, 20));
		
		scrollPane = new JScrollPane();
		mainFrame.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
	}
	
	protected class PlayMP3Thread implements Runnable {
		private String synthText;

        public PlayMP3Thread(String synth) {
			super();
			synthText = synth;
		}

		@Override
        public void run() {
            Synthesiser synthesiser = new Synthesiser();

            try {
            	Player player = new Player(synthesiser.getMP3Data(synthText));
                player.play();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
