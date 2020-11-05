package com.x13.viewer;

import javax.swing.*;

/**
 * Created by X13 on 05.11.2017.
 */
class Frame extends JFrame {

    //private static String dir = "D:/media/5.samsung/_tosort/20160117_120704.jpg";
    private static String file = "F:\\media\\wallpapers\\120 Ultra HD 4K Most Impressive Landscapes of the World Wallpapers [all][nico1899]\\Landscape, Waterscape, Nature, Mountain, Lake, Sunset, 92635872.jpg";

    public Frame(int maxWidth, int maxHeight) {
        setTitle("line");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(new Panel(maxWidth, maxHeight, file));
        setSize(maxWidth, maxHeight);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Frame bs = new Frame(1536, 1024);   //5/3
                bs.setVisible(true);
            }

        });

    }




}
