package sttts_v2;

import java.awt.EventQueue;

import javax.speech.EngineCreate;
import javax.speech.EngineList;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.speech.synthesis.Voice;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JLabel;

import java.awt.Font;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Locale;

import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import com.sun.speech.freetts.jsapi.FreeTTSEngineCentral;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;

public class MainWindow {

	private JFrame mainFrame;
	private JScrollPane scrollPane;
	private static JTextArea textArea;
	
	private static final String ACOUSTIC_MODEL = 
			"resource:/WSJ_8gau_13dCep_16k_40mel_130Hz_6800Hz";
	private static final String DICTIONARY_PATH = 
			"resource:/WSJ_8gau_13dCep_16k_40mel_130Hz_6800Hz/dict/cmudict.0.6d";
	private static final String LANGUAGE_MODEL =
			"file:resources/en-us.lm.dmp";
	
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
		
		Configuration configuration = new Configuration();
		configuration.setAcousticModelPath(ACOUSTIC_MODEL);
		configuration.setDictionaryPath(DICTIONARY_PATH);
		configuration.setLanguageModelPath(LANGUAGE_MODEL);
		LiveSpeechRecognizer liveSpeechRecognizer = new LiveSpeechRecognizer(configuration);
		

		Voice kevinHQ = new Voice("kevin16", Voice.GENDER_DONT_CARE, Voice.AGE_DONT_CARE, null);
		
		try {
			Synthesizer synthesizer = null;
			SynthesizerModeDesc generalDesc = new SynthesizerModeDesc(
					null,	// engine name
					"general",	// mode name
					Locale.US,	// locale
					null, // Running
					null); // voice
			
			FreeTTSEngineCentral central = new FreeTTSEngineCentral();
			EngineList list = central.createEngineList(generalDesc);
			
			if (list.size() > 0) { 
                EngineCreate creator = (EngineCreate) list.get(0); 
                synthesizer = (Synthesizer) creator.createEngine(); 
            } 

			if(synthesizer == null) {
				System.err.println("cannot create synthesizer");
				System.exit(1);
			}
			synthesizer.allocate();
			synthesizer.getSynthesizerProperties().setVoice(kevinHQ);
			synthesizer.resume();
			
			liveSpeechRecognizer.startRecognition(true);
			textArea.insert("ready!", 0);
			
			while(true) {
				String utterance = liveSpeechRecognizer.getResult().getHypothesis();
				textArea.insert(utterance + "\n", 0);
				synthesizer.speakPlainText(utterance, null);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}

	public MainWindow() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		initialize();
	}

	private void initialize() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		mainFrame = new JFrame();
		mainFrame.setTitle("sttts");
		mainFrame.setResizable(false);
		mainFrame.setBounds(100, 100, 248, 300);
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

}
