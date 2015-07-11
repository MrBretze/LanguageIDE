
import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

public class Main {

    public static JFrame frame;
    public static JMenuBar menuBar;
    public static File choseFile = null;
    public static int y = 50;
    public static JLabel label = new JLabel();
    public static JFileChooser chooser = new JFileChooser();
    public static Properties properties = new Properties();
    public static GraphicsEnvironment environment;
    public static BufferedImage icon;
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

        JButton addButton = new JButton("New");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Choose new parameter");
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.setSize(300, 150);
                frame.setIconImage(icon);
                frame.setVisible(true);

                JLabel label = new JLabel("Parameter name: ");
                label.setSize(20, 25);
                label.setLocation(85, 150);
                label.setVisible(true);

                frame.add(label);

                JTextField value = new JTextField(1);
                value.setSize(20, 25);
                value.setLocation(100, 150);
                value.setVisible(true);

                frame.add(value);
            }
        });
        addButton.setLocation(695, 501);
        addButton.setSize(50, 25);
        addButton.setVisible(true);

        frame.add(addButton);

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
            icon = ImageIO.read(Main.class.getResourceAsStream("icon.png"));
            frame.setIconImage(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
        frame.setLayout(new BorderLayout());
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

                frame.setTitle("Language IDE - 1.0 {" + file.toString() + "}");

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
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(100, 100));
        panel.setMaximumSize(new Dimension(800, 600));
        panel.setLocation(300, y);
        JTextField field = new JTextField(1);
        field.setSize(20, 10);
        field.setLocation(0, 20);
        field.setFont(new Font("UbuntuMono-RI", Font.PLAIN, 16));
        y += 20;
        field.setText(value);
        field.setVisible(true);
        panel.add(field);
        frame.add(panel, BorderLayout.CENTER);
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
