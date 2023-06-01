package GUI;

import GUI.Listeners.EndTurnListener;
import engine.City;
import engine.Game;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Flow;

public class CityView implements ActionListener{
    private City city;
    private Game game;
    private JFrame frame;
    private JPanel buttons, playerInfo;
    private JButton militaryBuilding, economicBuilding, armies, previous, endTurn, units;

    public CityView(City city, Game game, JFrame frame, JPanel playerInfo){
        frame.setLayout(new GridLayout(10,1,20,20));
        playerInfo.setLayout(new GridLayout(1, 5,20,0));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        //-----------------------------------//
        //---------CREATE BUTTONS------------//
        //-----------------------------------//
        endTurn = new JButton("End Turn");
        endTurn.setFont(new Font("", Font.BOLD, 20));
        endTurn.setBackground(Color.BLACK);
        endTurn.setForeground(Color.WHITE);
        endTurn.addActionListener(new EndTurnListener(frame, game, playerInfo));
        buttons = new JPanel();
        militaryBuilding = new JButton("Military Buildings");   militaryBuilding.addActionListener(this);   militaryBuilding.setFont(new Font("",Font.BOLD,15));
        economicBuilding = new JButton("Economic Buildings");   economicBuilding.addActionListener(this);   economicBuilding.setFont(new Font("",Font.BOLD,15));
        armies = new JButton("View armies");                    armies.addActionListener(this);             armies.setFont(new Font("",Font.BOLD,15));
        previous = new JButton("Back to world map");            previous.addActionListener(this);           previous.setFont(new Font("",Font.BOLD,15));
        units = new JButton("View All Units");            units.addActionListener(this);           units.setFont(new Font("",Font.BOLD,15));
        buttons.add(militaryBuilding);
        buttons.add(economicBuilding);
        buttons.add(armies);
        buttons.add(previous);
        buttons.add(units);
        buttons.add(endTurn);
        buttons.setLayout(new GridLayout(1, buttons.getComponentCount(), 0, 0));

        frame.getContentPane().add(playerInfo);
        frame.getContentPane().add(buttons);
        frame.validate();
        frame.repaint();
       
        this.frame = frame;
        this.city = city;
        this.game = game;
        this.playerInfo = playerInfo;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
        String src = e.getActionCommand();
        switch (src) {
            case "Back to world map":
                frame.getContentPane().removeAll();
                frame.setLayout(new BorderLayout());
                new WorldMap(frame, game);
                break;
            case "View armies":
                frame.getContentPane().removeAll();
                new ArmyView(frame, city, game.getPlayer(), game, playerInfo);
                break;
            case "Military Buildings":
                frame.getContentPane().removeAll();
                new MilitaryBuildingView(frame, city, game.getPlayer(), game, playerInfo);
                break;
            case "Economic Buildings":
                frame.getContentPane().removeAll();
                new BuyOrUpgradeEco(frame, city, game.getPlayer(), game, playerInfo);
                break;
            default:
                frame.getContentPane().removeAll();
                new UnitsView(frame, city, game.getPlayer(),game, playerInfo);
                break;
        }
	}
}
