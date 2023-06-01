package GUI;

import engine.City;
import engine.Game;
import units.Army;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BusyArmies implements ActionListener {
    JPanel playerInfo;
    JFrame frame;
    Game game;
    City city;

    public BusyArmies(City c, Game g, JFrame frame, JPanel playerInfo){
        game = g;
        city = c;
        this.playerInfo = playerInfo;
        frame.setLayout(new GridLayout(7,1,10,10));
        frame.add(playerInfo);
        for(Army a : game.getPlayer().getControlledArmies()){

        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
