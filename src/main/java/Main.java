
import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

public class Main {

    public static JFrame frame;
    public static JMenuBar menuBar;
    public static File choseFile = null;
    public static int z = 50;
    public static JLabel label = new JLabel();
    public static JFileChooser chooser = new JFileChooser();
    public static Properties properties = new Properties();
    public static GraphicsEnvironment environment;
    public static Font ubuntuFount;

    public Main() {

        Main.frame = new JFrame("Language IDE - 1.0");

        try {
            ubuntuFount = new Font("UbuntuMono-RI", Font.PLAIN, 8);
            if(ubuntuFount == null) {
                environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ubuntuFount = Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream("UbuntuMono-RI.ttf"));
                environment.registerFont(ubuntuFount);
            }
        } catch (IOException|FontFormatException e) {
            //Handle exception
        }



        menuBar = new JMenuBar();

        JMenu option = new JMenu("Option");
        option.setMnemonic(KeyEvent.VK_F);

        JMenuItem exitItem = new JMenuItem("Exit", getImageIcon("exit.png"));
        exitItem.setMnemonic(KeyEvent.VK_E);
        exitItem.setToolTipText("Exit application");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        option.add(exitItem);
        menuBar.add(option);

        JMenu file = new JMenu("File");

        JMenuItem openFile = new JMenuItem("Open file", getImageIcon("open_file.png"));
        openFile.setToolTipText("Open a file");
        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                chooser.updateUI();
                chooser.showOpenDialog(null);
                Main.choseFile = chooser.getSelectedFile();
                if (choseFile != null && choseFile.isFile())
                    openFile(choseFile);
            }
        });

        file.add(openFile);
        menuBar.add(file);

        DragDropListener listener = new DragDropListener();

        new DropTarget(chooser, listener);

        chooser.setEnabled(true);
        chooser.setDragEnabled(true);
        chooser.setVisible(true);

        frame.add(chooser);
        frame.setJMenuBar(menuBar);
        frame.setSize(800, 600);
        frame.setResizable(false);
        if (Main.choseFile == null) {
            label.setText("                       Choose a file or drop this !"); //#Yolo
            label.setVisible(true);
            label.setFont(new Font("UbuntuMono-RI", Font.PLAIN, 36));
            label.setVerticalAlignment(JLabel.CENTER);
            frame.getContentPane().add(label);
        }
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        try {
            frame.setIconImage(ImageIO.read(Main.class.getResourceAsStream("icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }
        SwingUtilities.updateComponentTreeUI(frame);
        frame.setVisible(true);
    }


    public void openFile(File file) {
        if (file.toString().endsWith(".xml")) {
            try {
                Main.label.setEnabled(false);
                Main.label.setText("");
                FileInputStream stream = new FileInputStream(file);
                properties.loadFromXML(stream);
                stream.close();
                Enumeration enuKeys = properties.keys();
                while (enuKeys.hasMoreElements()) {
                    String key = (String) enuKeys.nextElement();
                    String value = properties.getProperty(key);
                    addJtextElement(key, value);
                }
                SwingUtilities.updateComponentTreeUI(frame);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("The file is not a good file !");
        }
    }


    public void addJtextElement(String keys, String value) {
        JTextArea area = new JTextArea(1, 1);
        area.setSize(50, 20);
        area.setLocation(100, z);
        area.setFont(new Font("UbuntuMono-RI", Font.PLAIN, 16));
        z += 2;
        area.setText(value);
        area.setVisible(true);
        frame.add(area);
    }

    public static ImageIcon getImageIcon(String img) {
        return new ImageIcon(Main.class.getResource(img));
    }


    public static void main(String[] args) {
        new Main();
    }

    private static void refreshUI(JComponent c, boolean includeParent) {
        if (includeParent)
            c.updateUI();

        for (int i = 0; i < c.getComponentCount(); i++) {
            Component child = c.getComponent(i);
            if (child instanceof JComponent) {
                refreshUI((JComponent) child, true);
            }
        }
    }

}
