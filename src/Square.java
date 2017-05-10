import java.awt.Color;

import javax.swing.*;

/**
* this where is sets up the Sqaure for the game board
*/
public class Square extends JPanel {
		JLabel label = new JLabel((Icon)null);
		Icon ic;
		boolean marked;

		public Square() {
			setBackground(new Color(0x3043ee));
			add(label);
			setIcon(new ImageIcon("b.png"));
			marked = false;
		}

		public void setIcon(Icon icon) {
			marked = true;
			ic = icon;
			label.setIcon(icon);
			label.setVerticalTextPosition(JLabel.CENTER);
			label.setHorizontalTextPosition(JLabel.CENTER);
			label.setVerticalAlignment(JLabel.CENTER);
			label.setHorizontalAlignment(JLabel.CENTER);
		}

		public boolean hasIcon(){
			return marked;
		}
	}