package com.x13.viewer;

/**
 * Created by X13 on 05.11.2017.
 */

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.Graphics.*;

import java.awt.Graphics2D.*;

import javax.swing.JFrame;

import javax.swing.JPanel;

import java.awt.Event.*;

import java.awt.Component.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.SwingUtilities;


public class Panel extends JPanel {


    private BufferedImage img;
    private int maxWidth, maxHeight;

    Panel(int maxWidth, int maxHeight, String file) {
        super();

        try {

            File f = new File(file);

            URL url = f.toURI().toURL();
            img = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (img.getWidth() > img.getHeight()) {
            this.maxWidth = maxWidth;
            this.maxHeight = (int)((double)maxWidth*img.getHeight()/img.getWidth());
        } else {
            this.maxHeight = maxHeight;
            this.maxWidth = (int)((double)maxHeight * img.getWidth() / img.getHeight());
        }

        setPreferredSize(new Dimension(this.maxWidth, this.maxHeight));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this.maxWidth, this.maxHeight, null);
    }


}

