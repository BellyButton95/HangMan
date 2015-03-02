package View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Logic.FourPictures;

public class GameWindow extends JFrame {

	private JLabel blanks;
	private JLabel gameOverLabel;
	private JPanel jp3;
	private ArrayList<JButton> jbArray;
	private static ArrayList<FourPictures> levels;
	private static int currentLevel;
	private ImageIcon img1;
	private ImageIcon img2;
	private ImageIcon img3;
	private ImageIcon img4;
	private URL url;
	private URL url1;
	private URL url2;
	private URL url3;
	private static String[] urls;
	private static ArrayList<String> lines;
	private static int maxLevel;

	public GameWindow(int z) {
		currentLevel = z;
		System.out.println(currentLevel);
		GameWindow gw = new GameWindow(levels.get(z));
	}

	public GameWindow(final FourPictures game) {
		super("Hangman");
		JPanel jp = new JPanel();
		jp.setLayout(new BorderLayout());
		setSize(860, 640);
		setPreferredSize(new Dimension(700, 500));
		add(jp);
		setVisible(true);

		jbArray = new ArrayList<JButton>();

		// adding the labels
		JPanel jp1 = new JPanel();
		jp1.setLayout(new FlowLayout());
		jp.add(jp1, BorderLayout.NORTH);

		// labels
		JLabel guess = new JLabel("Guessed:");
		JLabel wrong = new JLabel("");
		blanks = new JLabel();

		blanks.setText(game.getProgress());

		jp1.add(guess);
		jp1.add(wrong);
		jp1.add(blanks);

		// pictures
		JPanel jp2 = new JPanel();
		jp2.setLayout(new GridLayout(2, 2));

		try {
			url = GameWindow.class.getResource(game.getFile()[0]);
			url1 = GameWindow.class.getResource(game.getFile()[1]);
			url2 = GameWindow.class.getResource(game.getFile()[2]);
			url3 = GameWindow.class.getResource(game.getFile()[3]);

			img1 = new ImageIcon(url);
			img2 = new ImageIcon(url1);
			img3 = new ImageIcon(url2);
			img4 = new ImageIcon(url3);
		} catch (NullPointerException e) {
			try {
				url = new URL(game.getFile()[0]);
				url1 = new URL(game.getFile()[1]);
				url2 = new URL(game.getFile()[2]);
				url3 = new URL(game.getFile()[3]);
			} catch (MalformedURLException e1) {
				System.out.println("url-to ni staa");
			}

			img1 = new ImageIcon(url);
			img2 = new ImageIcon(url1);
			img3 = new ImageIcon(url2);
			img4 = new ImageIcon(url3);
		}
		JLabel jl1 = new JLabel(img1);
		JLabel jl2 = new JLabel(img2);
		JLabel jl3 = new JLabel(img3);
		JLabel jl4 = new JLabel(img4);

		jp2.add(jl1);
		jp2.add(jl2);
		jp2.add(jl3);
		jp2.add(jl4);

		jp.add(jp2);

		// buttons
		jp3 = new JPanel();
		jp3.setLayout(new GridLayout(2, game.getCharLen() / 2));
		JPanel jpBottom = new JPanel();
		jpBottom.setLayout(new BoxLayout(jpBottom, BoxLayout.Y_AXIS));

		for (int j = 0; j < game.getCharLen(); j++) {
			final JButton btn = new JButton();
			btn.setText(game.getChar().charAt(j) + "");
			jp3.add(btn);
			jbArray.add(btn);
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!game.isGameOver()) {
						game.tryProgress(btn.getText().charAt(0));
						blanks.setText(game.getProgress());
						if (game.isGameOver()) {
							gameOver();
						}
					} else {
						gameOver();
					}
					btn.setVisible(false);
				}
			});
		}
		// The shit for the hint button
		JPanel hintPanel = new JPanel();
		hintPanel.setLayout(new FlowLayout());
		final JButton hintButton = new JButton("Hint?");
		jbArray.add(hintButton);
		hintPanel.add(hintButton);
		hintButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.hint();
				hintButton.setVisible(false);
				blanks.setText(game.getProgress());
//				System.out.println(game.getHintChar());
				for (JButton jb : jbArray) {
					if (jb.getText().charAt(0) == game.getHintChar()) {
						jb.setVisible(false);
						break;
					}
				}
				if (game.isGameOver()) {
					gameOver();
				}
			}
		});

		jpBottom.add(jp3);
		jpBottom.add(hintPanel);
		jp.add(jpBottom, BorderLayout.SOUTH);

		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	private void gameOver() {
		System.out.println("Game Over");
		changeVis();
	}

	private void changeVis() {
		for (JButton jb : jbArray) {
			jb.setVisible(false);
		}
		jp3.setLayout(new FlowLayout());
		gameOverLabel = new JLabel(FourPictures.getWinLose());
		JButton nextButton = new JButton("Next Level");
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				setVisible(false);
				currentLevel += 1;
				if(currentLevel<maxLevel){
					//does it work? Idk
				dispose();
				GameWindow gw1 = new GameWindow(currentLevel);
				}
				else{
					JFrame victoryScreen = new JFrame("No more levels");
					victoryScreen.setLayout(new BorderLayout());
					JLabel sorryLabel = new JLabel("Sorry, but there are no more levels :(", SwingConstants.CENTER);
					victoryScreen.add(sorryLabel, BorderLayout.CENTER);
					victoryScreen.setSize(400, 400);
					victoryScreen.setDefaultCloseOperation(EXIT_ON_CLOSE);
					victoryScreen.setVisible(true);
					}
			}
		});
		jp3.add(gameOverLabel);
		jp3.add(nextButton);
	}

	public static void generateLevels() {
		currentLevel = 0;
		try {
			URL gamesURL = new URL(
					"http://www.inf.kcl.ac.uk/staff/andrew/fourpictures/two-levels.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					gamesURL.openStream()));
			levels = new ArrayList<FourPictures>();
			String inputLine;
			// paris
			String filenames[] = { "Paris1.jpg", "Paris2.jpg", "Paris3.jpg",
					"Paris4.jpg" };
			FourPictures game = new FourPictures("paris", "pghabrkist",
					filenames);
			levels.add(game);
			urls = new String[4];
			lines = new ArrayList<String>();
			int k = -1;
			// for loop ot stranicata
			while((inputLine = br.readLine())!=null) {
				k++;
				System.out.println(k);
				System.out.println(inputLine);
				lines.add(inputLine);
				if(k>=2){
					urls[k - 2] = lines.get(k);
				}
				if(k>4){
					FourPictures newGame = new FourPictures(lines.get(0),lines.get(1), urls);					
					System.out.println(newGame);
					levels.add(newGame);
					urls = new String[4];
					lines = new ArrayList<String>();
					k=-1;
				}
//				for (int i = 1; i < 6; i++) {
//					if ((inputLine = br.readLine()) != null) {
//						System.out.println(inputLine);
//						lines.add(inputLine);
//						if (i >= 2) {
//							urls[i - 2] = lines.get(i);
//						}
//					}
//				}

			}
			maxLevel = levels.size() - 1;
			br.close();
		} catch (MalformedURLException e) {
			System.out.println("url-to ni staa");
		} catch (IOException e) {
			System.out.println("Ni staa ne6o");
		}

	}

	public static void main(String[] args) {
		GameWindow.generateLevels();
		GameWindow gw1 = new GameWindow(0);
	}
}
