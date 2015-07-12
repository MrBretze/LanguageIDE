
import com.sun.corba.se.impl.encoding.WrapperInputStream;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Enumeration;
import java.util.Properties;

public class Main {

    public static JFrame frame;
    public static JMenuBar menuBar;
    public static File choseFile = null;
    public static JLabel label = new JLabel();
    public static JFileChooser chooser = new JFileChooser();
    public static Properties properties = new Properties();
    public static GraphicsEnvironment environment;
    public static BufferedImage icon;
    public static Font ubuntuFount;
    public static Main main;
    public static SpringLayout layout = new SpringLayout();

    public Main() {

        Main.frame = new JFrame("Language IDE - 1.0");

        try {
            ubuntuFount = new Font("UbuntuMono-RI", Font.PLAIN, 8);
            if (ubuntuFount == null) {
                environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ubuntuFount = Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream("UbuntuMono-RI.ttf"));
                environment.registerFont(ubuntuFount);
            }
        } catch (IOException | FontFormatException e) {
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
        openFile.addActionListener(new ActionChooseFile());
        file.add(openFile);
        menuBar.add(file);

        chooser.setEnabled(true);
        chooser.setDragEnabled(true);
        chooser.setVisible(true);

        frame.add(chooser);
        frame.setJMenuBar(menuBar);
        frame.setPreferredSize(new Dimension(800, 600));
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
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }
        frame.pack();
        SwingUtilities.updateComponentTreeUI(frame);
        frame.setVisible(true);
    }


    public void openFile(File file) {
        if (file.toString().endsWith(".xml")) {
            try {
                Main.label.setEnabled(false);
                Main.label.setText("");
                BufferedReader bufRdr = new BufferedReader(
                        new InputStreamReader(new FileInputStream(file), "ISO-8859-1"));
                properties.loadFromXML(new ReaderInputStream(bufRdr));
                bufRdr.close();

                frame.setTitle("Language IDE - 1.0 {" + file.toString() + "}");

                Enumeration enuKeys = properties.keys();

                JPanel panel = new JPanel();
                panel.setLayout(layout);
                panel.setOpaque(true);
                while (enuKeys.hasMoreElements()) {
                    String key = (String) enuKeys.nextElement();
                    String value = properties.getProperty(key);
                    addJtextElement(key, value, panel);
                }
                JScrollPane pane = new JScrollPane(panel);
                frame.setContentPane(pane);
                frame.setLayout(new FlowLayout());
                frame.pack();
                SwingUtilities.updateComponentTreeUI(frame);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (file.toString().endsWith(".properties")) {
            try {
                Main.label.setEnabled(false);
                Main.label.setText("");
                BufferedReader bufRdr = new BufferedReader(new InputStreamReader(new FileInputStream(file), "ISO-8859-1"));
                properties.load(bufRdr);
                bufRdr.close();

                frame.setTitle("Language IDE - 1.0 {" + file.toString() + "}");

                Enumeration enuKeys = properties.keys();

                JPanel panel = new JPanel();
                panel.setLayout(layout);
                panel.setOpaque(true);
                while (enuKeys.hasMoreElements()) {
                    String key = (String) enuKeys.nextElement();
                    String value = properties.getProperty(key);
                    addJtextElement(key, value, panel);
                }
                JScrollPane pane = new JScrollPane();
                for(Component c : panel.getComponents()) {
                    pane.add(c);
                }
                pane.setPreferredSize(new Dimension(380, 100));
                frame.setContentPane(pane);
                frame.pack();
                SwingUtilities.updateComponentTreeUI(frame);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    int i = 0;

    public void addJtextElement(String keys, String value, JPanel panel) {
        i++;
        System.out.print("Register new JTextElement: value:" + value + "  keys: " + keys + "\n");
        JLabel l = new JLabel(keys, JLabel.TRAILING);
        panel.add(l);
        JTextField textField = new JTextField(value, 10);
        l.setLabelFor(textField);
        layout.putConstraint(SpringLayout.NORTH, panel, i * 5, SpringLayout.NORTH, l);
        layout.putConstraint(SpringLayout.SOUTH, panel, i * 5, SpringLayout.SOUTH, l);
        panel.add(textField);
    }

    public static ImageIcon getImageIcon(String img) {
        return new ImageIcon(Main.class.getResource(img));
    }


    public static void main(String[] args) {
        main = new Main();
    }
}
