import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
* this where is sets up the Sqaure for the game board
*/
public class Square extends JPanel {
		JLabel label = new JLabel((Icon)null);

		public Square() {
			setBackground(Color.white);
			add(label);
		}

		public void setIcon(Icon icon) {
			label.setIcon(icon);
		}
	}