package com.project.my;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static com.intellij.uiDesigner.core.GridConstraints.*;

public class Window extends JFrame {
    private JPanel pnlMain;
    private JLabel numberGenerations;
    private JLabel livingTime;
    private JPanel pnlScreen;
    private JTextField tfDelay;
    private JButton btnOkDelay;
    private JButton btnWriteFile;
    private JButton btnReadFile;
    private JButton btnGraph;

    private Logic logic = new Logic();
    private int[][] field = new int[logic.HEIGHT][logic.WIDTH];
    private Image imgFood;
    private Image imgPoison;
    private Image imgYoungCeil;
    private Image imgOldCeil;
    private Image imgStone;
    private Image imgBackground;
    private int delay;
    private long start;
    private long finish;
    private long timeLiving;
    private boolean onGraph;

    Window() throws IOException {
        super("Live points");
        onGraph = true;
        super.setBounds(300, 90, 1400, 850);
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setImages();
        delay = 100;
        Font font = new Font("Monaco", Font.BOLD, 20);
        livingTime.setFont(font);
        numberGenerations.setFont(font);
        logic.createField(field);
        numberGenerations.setText("Поколение №: " + logic.getNumberGeneration());
        final JPanel drawPanel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                for (int i = 0; i < logic.HEIGHT; i++) {
                    for (int j = 0; j < logic.WIDTH; j++) {
                        switch (field[i][j]) {
                            case 0:
                                g.drawImage(imgBackground, j * 25, i * 25, null);
                                break;
                            case 1:
                                g.drawImage(imgStone, j * 25, i * 25, null);
                                break;
                            case 2:
                                g.drawImage(imgFood, j * 25, i * 25, null);
                                break;
                            case 3:
                                g.drawImage(imgPoison, j * 25, i * 25, null);
                                break;
                            case 4:
                                g.drawImage(imgYoungCeil, j * 25, i * 25, null);
                                break;
                            case 5:
                                g.drawImage(imgOldCeil, j * 25, i * 25, null);
                                break;
                        }
                    }
                }
            }
        };
        drawPanel.setDoubleBuffered(true);
        drawPanel.setMinimumSize(new Dimension(1125, 625));
        drawPanel.setBackground(Color.getHSBColor(200, 200, 200));
        pnlScreen.add(drawPanel, new GridConstraints(0, 0, 1, 1, ANCHOR_CENTER, FILL_BOTH, SIZEPOLICY_WANT_GROW, SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null));
        super.add(pnlMain);
        final Timer timer = new Timer(delay, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Window.super.setVisible(true);
                if (!logic.isEnd()) {
                    logic.nextStep(field);
                } else {
                    try {
                        finish = System.currentTimeMillis();
                        timeLiving = finish - start;
                        livingTime.setText("Время жизни: " + (timeLiving / (delay + 1)));
                        logic.createNewGenerationsAndRefresh(field);
                        numberGenerations.setText("Поколение №: " + logic.getNumberGeneration());
                        start = System.currentTimeMillis();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                if (onGraph) {
                    drawPanel.repaint();
                }
            }
        });
        timer.start();
        btnOkDelay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    delay = Integer.parseInt(tfDelay.getText());
                    timer.setDelay(delay);
                } catch (Exception ex) {
                    System.out.println("Введите число");
                }
            }
        });
        btnWriteFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StringBuilder str = new StringBuilder();
                int[] c = logic.getDnk();
                int[] d = new int[64];
                System.arraycopy(c, 0, d, 0, 64);
                for (int i = 0; i < 64; i++) {
                    str.append(d[i]).append(" ");
                }
                FileWork fw = new FileWork("src/main/resources/Files/best.txt");
                try {
                    fw.writeValue(str.toString());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        btnReadFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FileWork fw = new FileWork("src/main/resources/Files/best.txt");
                int[] d = new int[64];
                try {
                    d = fw.readValue();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                logic.setGenom(d, field);
            }
        });
        btnGraph.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onGraph = !onGraph;
            }
        });
    }

    private void setImages() throws IOException {
        String pathForPoison = "/Files/Images/Poison.png";
        String pathForFood = "/Files/Images/Food.png";
        String pathForOldCeil = "/Files/Images/OldCeil.png";
        String pathForYoungCeil = "/Files/Images/YoungCeil.png";
        String pathForStone = "/Files/Images/Stone.png";
        String pathForBackground = "/Files/Images/Background.png";
        imgFood = ImageIO.read(getClass().getResourceAsStream(pathForFood));
        imgOldCeil = ImageIO.read(getClass().getResourceAsStream(pathForOldCeil));
        imgYoungCeil = ImageIO.read(getClass().getResourceAsStream(pathForYoungCeil));
        imgPoison = ImageIO.read(getClass().getResourceAsStream(pathForPoison));
        imgStone = ImageIO.read(getClass().getResourceAsStream(pathForStone));
        imgBackground = ImageIO.read(getClass().getResourceAsStream(pathForBackground));
    }
}