import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by MrBretzel on 11/07/2015.
 */

public class AddParameter implements ActionListener {

    public static boolean b = true;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (b) {
            b = false;
            final JFrame frame = new JFrame("Choose new parameter");
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setSize(300, 150);
            frame.setIconImage(Main.icon);

            JLabel label = new JLabel("Parameter name: ");
            label.setSize(20, 25);
            label.setLocation(85, 150);
            frame.add(label);

            JTextField value = new JTextField(50);
            value.setSize(20, 25);
            value.setLocation(100, 150);

            frame.add(value);

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    b = true;
                    frame.setVisible(false);
                    frame.dispose();
                }
            });

            frame.setVisible(true);
        } else {
            final Runnable runnable = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
            if (runnable != null) runnable.run();
        }
    }
}
