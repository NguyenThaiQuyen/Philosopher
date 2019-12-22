package v1;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Dinner extends JFrame {
    private final static int NUM_PHILOSOPHER = 5;
    private final int w = 1000, h = 700;
    private Philosopher[] philosophers;
    private Chopstick[] chopsticks;

    private Point[] philPoints = new Point[NUM_PHILOSOPHER];
    private Point[] chopPoints = new Point[NUM_PHILOSOPHER];

    private Toolkit tool;
    private Image[] images = new Image[7];

    JButton btnStart, btnReset, btnDeadlock;
    JScrollPane scrollPane;
    JTextArea textArea;
    JLabel lbResult, lbTitle;

    double alpha = 2*Math.PI/NUM_PHILOSOPHER;
    double beta = alpha/2;
    int tableCoordinate = 145;
    Point center = new Point(w/2 - 50,h/2 - 50);

    public void DinnerGUI() {
        this.setTitle("Dinner Philosophers");
        this.setSize(w, h);
        this.setLayout(null);
        this.setLocation(450, 200);
        this.setDefaultCloseOperation(3);

        btnStart = new JButton("Start");
        btnStart.setFont(new Font("Ubuntu Mono", 1, 18));
        btnReset = new JButton("Reset");
        btnReset.setEnabled(false);
        btnReset.setFont(new Font("Ubuntu Mono", 1, 18));
        btnDeadlock = new JButton("Deadlock");
        btnDeadlock.setFont(new Font("Ubuntu Mono", 1, 18));
        lbResult = new JLabel("RESULT: ");
        lbResult.setFont(new Font("Ubuntu Mono", 1, 18));
        lbTitle = new JLabel("DINNER OF PHILOSOPHERS");
        lbTitle.setFont(new Font("Ubuntu Mono", 1, 30));
        lbTitle.setForeground(new java.awt.Color(204, 43, 43));

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBounds(20, 100, 300, 300);
        scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(20,100,300,300);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        btnStart.setBounds(400, 600, 150, 40);
        btnReset.setBounds(600, 600, 150, 40);
        btnDeadlock.setBounds(800, 600, 150, 40);
        lbResult.setBounds(20, 50, 120, 40);
        lbTitle.setBounds(300, 10, 350, 40);

        this.add(lbResult);
        this.add(lbTitle);
        this.add(btnStart);
        this.add(btnReset);
        this.add(btnDeadlock);
        this.add(scrollPane);
        this.setResizable(false);
        this.setVisible(true);

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnStartAction(e);
            }
        });

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnResetAction(e);
            }
        });

        btnDeadlock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnDeadlockAction(e);
            }
        });
    }

    private void btnResetAction(ActionEvent e) {
        btnReset.setEnabled(false);
        btnStart.setEnabled(true);
        btnStart.setText("Start");
        btnDeadlock.setEnabled(true);
        textArea.setText("");
        detroy();
        this.repaint();
        InitChopsticks();
        InitPhilosophers();
    }

    private void btnDeadlockAction(ActionEvent e) {
        btnDeadlock.setEnabled(false);
        btnStart.setEnabled(false);
        btnReset.setEnabled(true);
        for(int i = 0 ; i < NUM_PHILOSOPHER ; i++) {
            philosophers[i] = new Philosopher(i, chopsticks[(i - 1 + NUM_PHILOSOPHER) % NUM_PHILOSOPHER], chopsticks[i], true);
            philosophers[i].start();
            philosophers[i].setView(this);
            setTextArea("Philosopher " + i + " is thinking!");
        }
    }

    private void btnStartAction(ActionEvent e) {
        if (btnStart.getText() == "Start") {
            btnStart.setText("Pause");
            for(int i = 0 ; i < NUM_PHILOSOPHER ; i++) {
                philosophers[i] = new Philosopher(i, chopsticks[(i - 1 + NUM_PHILOSOPHER) % NUM_PHILOSOPHER], chopsticks[i]);
                philosophers[i].start();
                philosophers[i].setView(this);
                setTextArea("Philosopher " + i + " is thinking!");
            }
        } else {
            pause();
            btnStart.setText("Pause");
        }
        btnDeadlock.setEnabled(false);
        btnReset.setEnabled(true);
    }

    public void setPoint() {
        for (int i = 0 ; i < 5 ; i++) {
            int philCoordinate = 220;
            int chopCoordinate = 110;
            Point phil = new Point((int)(center.x+philCoordinate*Math.cos(-i*alpha)) + 100,(int)(center.y+philCoordinate*Math.sin(-i*alpha)) -50);
            philPoints[i] = phil;
            Point chop = new Point((int)(center.x+chopCoordinate*Math.cos(-(i*alpha + beta))) + 110,(int)(center.y+chopCoordinate*Math.sin(-(i*alpha + beta))) -40 );
            chopPoints[i] = chop;
        }
    }

    public void setImages() {
        try {
            tool=Toolkit.getDefaultToolkit();
            images[0] = tool.getImage(getClass().getResource("../image/eat.png"));
            images[1] = tool.getImage(getClass().getResource("../image/think.png"));
            images[2] = tool.getImage(getClass().getResource("../image/hungry.png"));
            images[3] = tool.getImage(getClass().getResource("../image/getRight.png"));
            images[4] = tool.getImage(getClass().getResource("../image/getLeft.png"));
            images[5] = tool.getImage(getClass().getResource("../image/eatingIcon.png"));
            images[6] = tool.getImage(getClass().getResource("../image/food.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void InitPhilosophers() {
        philosophers = new Philosopher[NUM_PHILOSOPHER];
        for(int i = 0; i < NUM_PHILOSOPHER; i++) {
            philosophers[i] = new Philosopher(i, chopsticks[(i - 1 + NUM_PHILOSOPHER) % NUM_PHILOSOPHER], chopsticks[i]);
        }
    }

    public void InitChopsticks() {
        chopsticks = new Chopstick[NUM_PHILOSOPHER];
        for(int i = 0; i < NUM_PHILOSOPHER; i++) {
            chopsticks[i] = new Chopstick(i);
        }
    }

    public Dinner() {
        DinnerGUI();
        setPoint();
        setImages();
        InitChopsticks();
        InitPhilosophers();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK);
        Font myFont = new Font("Arial", Font.ITALIC, 20);
        g.setFont(myFont);

        g.drawImage(images[5], 20, 460, 64, 64, this);
        g.drawString("EATING", 90, 500);
        g.drawImage(images[1], 20, 530, 64, 64, this);
        g.drawString("THINKING", 90, 570);
        g.drawImage(images[2], 20, 600, 64, 64, this);
        g.drawString("HUNGRY", 90, 640);

        g.setColor(Color.DARK_GRAY);
        g.fillOval(center.x - tableCoordinate + 160, center.y - tableCoordinate + 15, 2 * tableCoordinate, 2* tableCoordinate);
        g.drawImage(images[6],w/2 + 30,h/2 - 115, 150, 150, this);

        Image img;
        for (int i = 0 ; i < NUM_PHILOSOPHER ; i++) {
            if (philosophers[i].isRunning()) {
                if (philosophers[i].status == Philosopher.STATUS.THINKING) {
                    img = images[1];
                } else if (philosophers[i].status == Philosopher.STATUS.HUNGRY) {
                    img = images[2];
                } else if (philosophers[i].status == Philosopher.STATUS.GETRIGHT) {
                    img = images[3];
                } else if (philosophers[i].status == Philosopher.STATUS.GETLEFT) {
                    img = images[4];
                } else {
                    img = images[0];
                }
            } else {
                img = images[1];
            }

            g.drawImage(img, philPoints[i].x, philPoints[i].y, this);

            g.setColor(Color.BLACK);
            myFont = new Font("Arial", Font.BOLD, 20);
            g.setFont(myFont);
            g.drawString("P" + i, philPoints[i].x + 54, philPoints[i].y + 135);

            if(!chopsticks[i].isTaken()) {
                g.setColor(Color.RED);
                g.fillRect(chopPoints[i].x + 55, chopPoints[i].y + 40, 7, 50);
            }
        }
    }

    public void updatePhilosophers(int philID) {
        repaint(philPoints[philID].x, philPoints[philID].y, images[0].getWidth(null), images[0].getHeight(null));
        try {
            philosophers[philID].sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateChopstick(int chopstickID) {
        repaint(chopPoints[chopstickID].x + 55, chopPoints[chopstickID].y + 40, 7, 50);
    }

    public void setTextArea(String str) {
        textArea.setText(textArea.getText() + str + "\n");
    }

    public void pause() {
        try {
            for(int i = 0; i < NUM_PHILOSOPHER; i++) {
                philosophers[i].sleep(3000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void detroy() {
        try {
            for (int i = 0 ; i < NUM_PHILOSOPHER ; i++) {
                philosophers[i].destroyPhil();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Dinner();
    }

}