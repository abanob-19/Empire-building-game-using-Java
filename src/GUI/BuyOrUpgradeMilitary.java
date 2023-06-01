package GUI;

import GUI.Listeners.EndTurnListener;
import buildings.*;
import engine.City;
import engine.Game;
import engine.Player;
import exceptions.BuildingInCoolDownException;
import exceptions.MaxLevelException;
import exceptions.NotEnoughGoldException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BuyOrUpgradeMilitary implements ActionListener {

    private JPanel barracks, stable, archeryRange, playerInfo;
    private JButton buyBarracks, buyStable, buyRange, upgradeBarracks, upgradeStable, upgradeRange, previous, endTurn;
    private JFrame frame;
    private City city;
    private Player player;
    private Game game;

    public MilitaryBuilding getBuilding(String x){
        for (MilitaryBuilding t : city.getMilitaryBuildings()) {
            if(x.equals("Barracks")){
                if(t instanceof Barracks)
                    return t;
            }
            else if(x.equals("ArcheryRange")){
                if(t instanceof ArcheryRange)
                    return t;
            }
            else{
                if(t instanceof Stable)
                    return t;
            }
        }
        return null;
    }
    public boolean cityHas(String a) {
        for (MilitaryBuilding b : city.getMilitaryBuildings()) {
            if (getBuildingType(b).equals(a))
                return true;
        }
        return false;
    }
    public String getBuildingType(Building b){
        if(b instanceof ArcheryRange)
            return "ArcheryRange";
        if(b instanceof Barracks)
            return "Barracks";
        return "Stable";
    }

    public BuyOrUpgradeMilitary(JFrame frame, City c, Player p, Game g, JPanel playerInfo){
        this.playerInfo = playerInfo;
        player = p;
        city = c;
        game = g;
        frame.setLayout(new GridLayout(7, 1, 50 , 50));                        //Set the general layout of the whole frame
        playerInfo.setLayout(new GridLayout(1, 5,20,0));                         //Occupy the first row with the player information
        frame.add(playerInfo);
        //Initialize buttons
        previous = new JButton("Return to Military Building View");     previous.addActionListener(this);
        endTurn = new JButton("END TURN");                              endTurn.addActionListener(new EndTurnListener(frame, g, playerInfo));
        endTurn.setForeground(Color.WHITE);
        endTurn.setBackground(Color.BLACK);
        frame.add(previous);
        frame.add(endTurn);

        //-------------------
        //Initialize JPanels
        barracks = new JPanel();        barracks.setLayout(new GridLayout(1, 2,20,0));
        archeryRange = new JPanel();    archeryRange.setLayout(new GridLayout(1, 2,20,0));
        stable = new JPanel();          stable.setLayout(new GridLayout(1, 2,20,0));


        if(cityHas("Barracks")){
            upgradeBarracks = new JButton("UPGRADE BARRACKS");                                                                    upgradeBarracks.addActionListener(this);
            barracks.add(upgradeBarracks);
            barracks.add(new Label("Level: " + getBuilding("Barracks").getLevel() + "|| Upgrade Cost: " + getBuilding("Barracks").getUpgradeCost(), SwingConstants.CENTER));
        }else{
            buyBarracks = new JButton("BUY BARRACKS (2000)");                                                                     buyBarracks.addActionListener(this);
            barracks.add(new Label("", SwingConstants.CENTER));
            barracks.add(buyBarracks);
        }
        frame.add(barracks);

        if(cityHas("ArcheryRange")){
            upgradeRange = new JButton("UPGRADE ARCHERY RANGE");                                                                    upgradeRange.addActionListener(this);
            archeryRange.add(upgradeRange);
            archeryRange.add(new Label("Level: " + getBuilding("ArcheryRange").getLevel() + "|| Upgrade Cost: " + getBuilding("ArcheryRange").getUpgradeCost(), SwingConstants.CENTER));
        }else{
            buyRange = new JButton("BUY ARCHERY RANGE (1500)");                                                                   buyRange.addActionListener(this);
            archeryRange.add(buyRange);
            archeryRange.add(new Label(""), SwingConstants.CENTER);
        }
        frame.add(archeryRange);

        if(cityHas("Stable")){
            upgradeStable = new JButton("UPGRADE STABLE");                                                                          upgradeStable.addActionListener(this);
            stable.add(upgradeStable);
            stable.add(new Label("Level: " + getBuilding("Stable").getLevel() + "|| Upgrade Cost: " + getBuilding("Stable").getUpgradeCost(), SwingConstants.CENTER));
        }else{
            buyStable = new JButton("BUY STABLES (2500)");                                                                         buyStable.addActionListener(this);
            stable.add(buyStable);
            stable.add(new Label(""), SwingConstants.CENTER);
        }
        frame.add(stable);


        frame.revalidate();
        frame.repaint();
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String src = e.getActionCommand();

        switch (src) {
            case "UPGRADE STABLE":
                try {
                    player.upgradeBuilding(getBuilding("Stable"));
                } catch (NotEnoughGoldException | BuildingInCoolDownException | MaxLevelException a) {
                    JOptionPane.showMessageDialog(frame, a.getMessage());
                    return;
                }
                frame.getContentPane().removeAll();
                new BuyOrUpgradeMilitary(frame, city, player, game, playerInfo);
                break;
            case "UPGRADE ARCHERY RANGE":
                try {
                    player.upgradeBuilding(getBuilding("ArcheryRange"));
                } catch (NotEnoughGoldException | BuildingInCoolDownException | MaxLevelException a) {
                    JOptionPane.showMessageDialog(frame, a.getMessage());
                    return;
                }
                frame.getContentPane().removeAll();
                new BuyOrUpgradeMilitary(frame, city, player, game, playerInfo);
                break;
            case "UPGRADE BARRACKS":
                try {
                    player.upgradeBuilding(getBuilding("Barracks"));
                } catch (NotEnoughGoldException | BuildingInCoolDownException | MaxLevelException a) {
                    JOptionPane.showMessageDialog(frame, a.getMessage());
                    return;
                }
                frame.getContentPane().removeAll();
                new BuyOrUpgradeMilitary(frame, city, player, game, playerInfo);
                break;
            case "Return to Military Building View":
                frame.getContentPane().removeAll();
                new MilitaryBuildingView(frame, city, game.getPlayer(),game, playerInfo);
                break;
            case "BUY BARRACKS (2000)":
                try {
                    player.build("barracks", city.getName());
                } catch (NotEnoughGoldException notEnoughGoldException) {
                    JOptionPane.showMessageDialog(frame, notEnoughGoldException.getMessage());
                    return;
                }
                frame.getContentPane().removeAll();
                new BuyOrUpgradeMilitary(frame, city, game.getPlayer(), game, playerInfo);
                break;
            case "BUY ARCHERY RANGE (1500)":
                try {
                    player.build("archeryrange", city.getName());
                } catch (NotEnoughGoldException notEnoughGoldException) {
                    JOptionPane.showMessageDialog(frame, notEnoughGoldException.getMessage());
                    return;
                }
                frame.getContentPane().removeAll();
                new BuyOrUpgradeMilitary(frame, city, game.getPlayer(),game, playerInfo);
                break;
            case "BUY STABLES (2500)":
                try {
                    player.build("stable", city.getName());
                } catch (NotEnoughGoldException notEnoughGoldException) {
                    JOptionPane.showMessageDialog(frame, notEnoughGoldException.getMessage());
                    return;
                }
                frame.getContentPane().removeAll();
                new BuyOrUpgradeMilitary(frame, city, game.getPlayer(),game, playerInfo);
                break;
        }
        for (Component a : playerInfo.getComponents()) {
            if (a instanceof JLabel) {
                if (((JLabel) a).getText().contains("Treasury: "))
                    ((JLabel) a).setText("Treasury: " + player.getTreasury());

            }

        }
    }
}
