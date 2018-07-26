/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jdbc2;

import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *
 * @author Daniel
 */
public class Main {
    public static void main(String[] args) {
        MyFrame myFrame = new MyFrame();
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setSize(500, 250);
        myFrame.setLocationRelativeTo(null);
        myFrame.setVisible(true);
    }
}
