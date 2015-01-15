package sttts;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JLabel;

import java.awt.Font;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.swing.SwingConstants;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class AppWindow {

	private JFrame mainFrame;
	private JScrollPane scrollPane;
	private static JTextArea textArea;

	public static void main(String[] args) throws MalformedURLException, IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppWindow window = new AppWindow();
					window.mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		voce.SpeechInterface.init("lib", true, true, "", "");
		System.out.println("Test");
		
		while(true) {
			while(voce.SpeechInterface.getRecognizerQueueSize() > 0) {
				String s = voce.SpeechInterface.popRecognizedString();
				System.out.println(s);
				voce.SpeechInterface.synthesize(s);
			}
		}
	}

	public AppWindow() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		initialize();
	}

	private void initialize() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		mainFrame = new JFrame();
		mainFrame.setTitle("s-t-s");
		mainFrame.setResizable(false);
		mainFrame.setBounds(100, 100, 248, 300);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JLabel ttsttLabel = new JLabel("speech to text to speech");
		mainFrame.getContentPane().add(ttsttLabel, "2, 2");
		ttsttLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ttsttLabel.setFont(new Font("Georgia", Font.PLAIN, 20));
		
		JSeparator separator = new JSeparator();
		mainFrame.getContentPane().add(separator, "2, 4");
		
		scrollPane = new JScrollPane();
		mainFrame.getContentPane().add(scrollPane, "2, 6, fill, fill");
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
	}

}
