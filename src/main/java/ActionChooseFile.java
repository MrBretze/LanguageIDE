import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by MrBretzel on 11/07/2015.
 */

public class ActionChooseFile implements ActionListener {

    private JFileChooser chooser = Main.chooser;

    @Override
    public void actionPerformed(ActionEvent e) {
        chooser.updateUI();
        chooser.showOpenDialog(null);
        Main.choseFile = chooser.getSelectedFile();
        if (Main.choseFile != null && Main.choseFile.isFile())
           Main.main.openFile(Main.choseFile);
    }
}
