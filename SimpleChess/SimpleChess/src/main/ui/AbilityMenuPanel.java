package main.ui;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;


public class AbilityMenuPanel extends JPanel {
    private static final long serialVersionUID = 1L;

	public AbilityMenuPanel(Runnable onTeleport) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JButton tp = new JButton("Teleport");
        tp.addActionListener(e -> onTeleport.run());
        add(tp);
        setVisible(false);
    }
}
