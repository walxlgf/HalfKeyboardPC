import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
	private static AppFrame f;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		f = new AppFrame();
		f.setSize(300, 80);
		f.setLocation(200, 200);
		f.setVisible(true);
		f.setResizable(false);
		f.setTitle("Half Keyboard");

		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				f.setVisible(false);
				f.dispose();
				System.exit(0);
			}
		});
	}

	
}
