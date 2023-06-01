package GUI.Listeners;

import GUI.WorldMap;
import engine.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Starter implements ActionListener {
    JFrame oldFrame;
    private String startingCity;
    private JTextField name;

    public Starter(JFrame oldFrame, JTextField name, String startingCity) {
        this.oldFrame = oldFrame;
        this.name = name;
        this.startingCity = startingCity;
    }

    public void actionPerformed(ActionEvent args) {
        Game g = null;
        if(name.getText().length() == 0){
            JOptionPane.showMessageDialog(oldFrame, "Please Enter Your Name");
            return;
        } else{
            try {
                g = new Game(name.getText(), startingCity);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(oldFrame, e.getMessage());
            }
        }
        //INITIATE THE VERY FIRST FRAME OF THE WORLD MAP
        JFrame newFrame = new JFrame("World Map");
        newFrame.setSize(oldFrame.getSize());
        newFrame.setVisible(true);
        newFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        oldFrame.dispose();
        new WorldMap(newFrame, g);
    }
}
