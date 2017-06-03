package com.chompa.pong;

import com.sun.javafx.geom.*;
import javafx.scene.shape.Ellipse;

import javax.swing.*;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.*;
import java.awt.geom.Ellipse2D;
import java.util.HashSet;

/**
 * Created by Antony on 02/06/2017.
 */
public class Pong extends JPanel implements KeyListener, ActionListener
{
    private int width, height;
    private Timer timer = new Timer(5, this);

    private HashSet<String> keys = new HashSet<>();

    // Pads
    private final int SPEED = 2;
    private int padWidth, padHeight;
    private int padding;
    private int leftPadY, rightPadY;

    // Ball
    private double ballSize, ballX, ballY, ballVelX = SPEED * 3, ballVelY = 0.5;

    private boolean first;

    public Pong(){
        first = true;
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setBackground(Color.black);
        timer.setInitialDelay(100);
        timer.start();
    }

    private void init(){
        width = getWidth();
        height = getHeight();
        // Scale elements based on screen width/height
        leftPadY = height / 2;
        rightPadY = height / 2;
        padWidth = width / 48;
        padHeight = height / 6;
        padding = width / 96;
        ballSize = width / 36;
        ballX = width / 2 - (ballSize / 2);
        ballY = height / 2 - (ballSize / 2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if(first){
            init();
            first = false;
        }

        g2d.setColor(Color.white);

        /* Draw the paddles and ball */

        // Left Pad
        Rectangle2D leftPad = new Rectangle(padding, leftPadY - (padHeight / 2), padWidth, padHeight);
        g2d.fill(leftPad);

        // Right Pad
        Rectangle2D rightPad = new Rectangle(width - padding - padWidth, rightPadY - (padHeight / 2), padWidth, padHeight);
        g2d.fill(rightPad);

        // Ball
        Ellipse2D ball = new Ellipse2D.Double(ballX, ballY, ballSize, ballSize);
        g2d.fill(ball);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /* Check the hash set for the keys */
        if(keys.size() == 1)
        {
            if(keys.contains("UP")){
                leftPadY -= (leftPadY - (padHeight / 2) > padding) ? SPEED : 0;
            }
            else if(keys.contains("DOWN")){
                leftPadY += (leftPadY + (padHeight / 2)  < height - padding) ? SPEED : 0;
            }
        }

        /* Pad Collisions */

        // Create 'hit boxes' for the paddles and the ball
        Rectangle2D ballBox = new Rectangle((int)ballX, (int)ballY, (int)ballSize, (int)ballSize);
        Rectangle2D leftPadBox = new Rectangle(padding, leftPadY - (padHeight / 2), padWidth, padHeight);
        Rectangle2D rightPadBox = new Rectangle(width - padding - padWidth, rightPadY - (padHeight / 2),
                padWidth, padHeight);

        // Check to see if the ball's hit box intersects with either one of the pads
        if(ballBox.getBounds().intersects(leftPadBox.getBounds()) ||
                ballBox.getBounds().intersects(rightPadBox.getBounds())){
            System.out.println("Ball is colliding with paddle!");

            ballVelX = -ballVelX; // Reverse the X direction of the ball
            ballVelY += Math.random() * ((0.5 - -0.5) + 1) + -0.5; // Randomize the Y velocity (from -.5 to .5)
        }

        ballX += ballVelX;
        ballY += ballVelY;

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_UP:
                keys.add("UP");
                break;
            case KeyEvent.VK_DOWN:
                keys.add("DOWN");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_UP:
                keys.remove("UP");
                break;
            case KeyEvent.VK_DOWN:
                keys.remove("DOWN");
        }
    }

    public static void main(String[] args){
        JFrame gameFrame = new JFrame();
        gameFrame.setTitle("Pong!");
        Pong pong = new Pong();
        gameFrame.setContentPane(pong);
        gameFrame.setSize(1280, 720);
        gameFrame.setResizable(false);
        gameFrame.setVisible(true);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
