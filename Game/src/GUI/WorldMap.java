package GUI;

import GUI.Listeners.EndTurnListener;
import engine.City;
import engine.Game;
import units.Army;
import units.Status;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WorldMap implements ActionListener {
    private Game game;
    private JFrame frame, firstCanvas;
    private JPanel playerInfo, cities, idleBusy;
    private JButton cairo, sparta, rome, endTurn;
    private JLabel playerName, food, turnCount, playerCity, treasury, selectCity;
    private JTextArea busy, idle;
    private JScrollPane busyPane, idlePane;

    public WorldMap(JFrame frame, Game game) {      //RECEIVES AN EMPTY FRAME
        this.game = game;
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new GridLayout(3,1,0,5));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        idleBusy = new JPanel();
        idleBusy.setLayout(new GridLayout(1, 2,10,10));
        selectCity = new JLabel("Select A City", SwingConstants.CENTER);
        selectCity.setFont(new Font("", Font.BOLD, 40));
        {
            //-----------------------------------//
            //-----------PLAYER INFO-------------//
            //-----------------------------------//
            playerInfo = new JPanel();
            playerInfo.setLayout(new GridLayout(5, 1, 10, 10));
            playerName = new JLabel("Name: " + game.getPlayer().getName());
            food = new JLabel("Food: " + game.getPlayer().getFood());
            turnCount = new JLabel("Turn Count: " + game.getCurrentTurnCount());
            playerCity = new JLabel("Player city: " + game.getPlayer().getControlledCities().get(0).getName());
            food.setForeground(Color.YELLOW);
            food.setFont(new Font("", Font.BOLD, 20));
            playerCity.setForeground(Color.YELLOW);
            playerCity.setFont(new Font("", Font.BOLD, 20));
            playerName.setForeground(Color.YELLOW);
            playerName.setFont(new Font("", Font.BOLD, 20));
            turnCount.setForeground(Color.YELLOW);
            turnCount.setFont(new Font("", Font.BOLD, 20));
            playerInfo.setBackground(Color.DARK_GRAY);
            //-----------------------------------//
            //-------------TREASURY--------------//
            //-----------------------------------//
            treasury = new JLabel("Treasury: " + game.getPlayer().getTreasury());
            treasury.setForeground(Color.YELLOW);
            treasury.setFont(new Font("", Font.BOLD, 20));

            //-----------------------------------//
            //-------------END TURN--------------//
            //-----------------------------------//
            endTurn = new JButton("End Turn");
            endTurn.setFont(new Font("", Font.BOLD, 20));
            endTurn.setFocusable(false);
            endTurn.setBackground(Color.BLACK);
            endTurn.setForeground(Color.WHITE);
            endTurn.addActionListener(new EndTurnListener(frame, game, playerInfo));
        }
        {
            //-----------------------------------//
            //--------------CITIES---------------//
            //-----------------------------------//
            cities = new JPanel();
            cities.setLayout(new GridLayout(5, 1, 0, 10));
            cairo = new JButton("Cairo");
            cairo.setFocusable(false);
            sparta = new JButton("Sparta");
            sparta.setFocusable(false);
            rome = new JButton("Rome");
            rome.setFocusable(false);
            cairo.setFont(new Font("", Font.BOLD, 50));
            sparta.setFont(new Font("", Font.BOLD, 50));
            rome.setFont(new Font("", Font.BOLD, 50));
            cairo.setBackground(Color.GRAY);
            sparta.setBackground(Color.GRAY);
            rome.setBackground(Color.GRAY);
            cairo.addActionListener(this);
            sparta.addActionListener(this);
            rome.addActionListener(this);
        }
        //-----------------------------------//
        //------------ARMIES VIEW------------//
        //-----------------------------------//
        busy = new JTextArea();
        busy.setEditable(false);
        idle = new JTextArea();
        idle.setEditable(false);
        String sBusy = "BESIEGING OR MARCHING ARMIES:";
        String s = "IDLE ARMIES:";
        for(Army a : game.getPlayer().getControlledArmies()){
            if(a.getUnits().size() != 0) {
                if (a.getCurrentStatus() == Status.BESIEGING) {
                    sBusy += "\n\nAn army has been besieging " + a.getTarget() + " for " + getCity(a.getCurrentLocation()).getTurnsUnderSiege() + " turns.\n" + a.toString();
                }
                if (a.getCurrentStatus() == Status.MARCHING) {
                    sBusy += "\n\nAn army currently marching towards " + a.getTarget() + " and will arrive in " + a.getDistancetoTarget() + " turns.\n" + a.toString();
                }
                if (a.getCurrentStatus() == Status.IDLE) {
                    s += "\n\nAn army currently idle at " + a.getCurrentLocation() + ".\n" + a.toString();
                }
            }
        }
        busy.setText(sBusy);
        idle.setText(s);
        busyPane = new JScrollPane(busy);
        busyPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        busyPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        idlePane = new JScrollPane(idle);
        idlePane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        idlePane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        idleBusy.add(busyPane);
        idleBusy.add(idlePane);

        //-----------------------------------//
        //-------ADDING THE COMPONENTS-------//
        //-----------------------------------//
        playerInfo.add(playerCity);
        playerInfo.add(playerName);
        playerInfo.add(food);
        playerInfo.add(turnCount);
        contentPane.add(playerInfo, BorderLayout.NORTH);
        playerInfo.add(treasury);
        cities.add(selectCity);
        cities.add(cairo);
        cities.add(rome);
        cities.add(sparta);
        cities.add(endTurn);
        contentPane.add(cities, BorderLayout.CENTER);
        contentPane.add(idleBusy);
        frame.setContentPane(contentPane);

        frame.revalidate();
        frame.repaint();
        this.frame = frame;
    }

    private City getCity(String name){
        for(City c : game.getAvailableCities()){
            if(c.getName().equals(name))
                return c;
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        City c = getCity(e.getActionCommand());   //GET NAME OF CITY
        frame.getContentPane().removeAll();
        if((game.getPlayer()).getControlledCities().contains(c)) {
            //RESET THE FRAME TO PREPARE IT FOR CITY VIEW
            new CityView(c, game, frame, playerInfo);
        }
        else {
            JOptionPane.showMessageDialog(frame, "You Can't View a City That You Don't Control");
            new WorldMap(frame, game);
        }

    }
}
