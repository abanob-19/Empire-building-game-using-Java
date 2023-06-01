package GUI;

import GUI.Listeners.EndTurnListener;
import engine.City;
import engine.Game;
import engine.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MilitaryBuildingView implements ActionListener {

    private JPanel buttons, playerInfo;
    private JButton buyOrUpgrade, trainUnits, previous, endTurn;
    private JFrame frame;
    private City city;
    private Player player;
    private Game game;


    public MilitaryBuildingView(JFrame frame, City c, Player p, Game g, JPanel playerInfo){
        frame.setLayout(new GridLayout(10, 1, 20 , 20));          //Set the general layout of the whole frame
        playerInfo.setLayout(new GridLayout(1, 5,20,0));                         //Occupy the first row with the player information
        buttons = new JPanel();                                                         //Initialize the buttons JPanel
        //-----------------------------------//
        //---------CREATE BUTTONS------------//
        //-----------------------------------//
        buyOrUpgrade = new JButton("Buy or Upgrade buildings");   buyOrUpgrade.addActionListener(this);
        trainUnits = new JButton("Train units");                        trainUnits.addActionListener(this);
        previous = new JButton("Back to city view");                        previous.addActionListener(this);
        endTurn = new JButton("END TURN");                                  endTurn.addActionListener(new EndTurnListener(frame, g, playerInfo));
        endTurn.setBackground(Color.BLACK);
        endTurn.setForeground(Color.WHITE);
        buttons.add(buyOrUpgrade);  buttons.add(trainUnits);  buttons.add(previous);  buttons.add(endTurn);
        buttons.setLayout(new GridLayout(1, buttons.getComponentCount(),0,0)); //Let it occupy one row

        frame.getContentPane().add(playerInfo);
        frame.getContentPane().add(buttons);
        frame.validate();
        frame.repaint();

        this.frame = frame;
        this.playerInfo = playerInfo;
        player = p;
        city = c;
        game = g;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String src = e.getActionCommand();
        frame.getContentPane().removeAll();
        if(src.equals("Back to city view")){
            new CityView(city, game, frame, playerInfo);
        }
        else if(src.equals("Buy or Upgrade buildings")){
            new BuyOrUpgradeMilitary(frame, city, game.getPlayer(),game, playerInfo);
        }
        else{
            new TrainUnits(frame, city, game.getPlayer(),game, playerInfo);
        }
    }
}
