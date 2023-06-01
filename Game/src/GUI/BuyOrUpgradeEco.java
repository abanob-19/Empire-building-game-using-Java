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

public class BuyOrUpgradeEco implements ActionListener {

    private JPanel farm, market, playerInfo;
    private JButton buyFarm, upgradeFarm, buyMarket, upgradeMarket, previous, endTurn;
    private JFrame frame;
    private City city;
    private Player player;
    private Game game;

    public EconomicBuilding getBuilding(String x){
        for (EconomicBuilding t : city.getEconomicalBuildings()) {
            if(x.equals("Market") && (t instanceof Market)){
                    return t;
            }
            if(x.equals("Farm") && (t instanceof Farm)){
                return t;
            }
        }
        return null;
    }
    public boolean cityHas(String a) {
        for (EconomicBuilding b : city.getEconomicalBuildings()) {
            if (getBuildingType(b).equals(a))
                return true;
        }
        return false;
    }
    public String getBuildingType(Building b){
        if(b instanceof Market)
            return "Market";
        return "Farm";
    }

    public BuyOrUpgradeEco(JFrame frame, City c, Player p, Game g, JPanel playerInfo){
        this.playerInfo = playerInfo;
        player = p;
        city = c;
        game = g;
        frame.setLayout(new GridLayout(7, 1, 50 , 50));                        //Set the general layout of the whole frame
        playerInfo.setLayout(new GridLayout(1, 5,20,0));                         //Occupy the first row with the player information
        frame.add(playerInfo);
        //Initialize buttons
        previous = new JButton("Return to City View");                  previous.addActionListener(this);
        endTurn = new JButton("END TURN");                              endTurn.addActionListener(new EndTurnListener(frame, g, playerInfo));
        endTurn.setForeground(Color.WHITE);
        endTurn.setBackground(Color.BLACK);
        frame.add(previous);
        frame.add(endTurn);

        //-------------------
        //Initialize JPanels
        farm = new JPanel();        farm.setLayout(new GridLayout(1, 2,20,0));
        market = new JPanel();    market.setLayout(new GridLayout(1, 2,20,0));


        if(cityHas("Market")){
            upgradeMarket = new JButton("UPGRADE MARKET");                                                                    upgradeMarket.addActionListener(this);
            market.add(upgradeMarket);
            market.add(new Label("Level: " + getBuilding("Market").getLevel() + "|| Upgrade Cost: " + getBuilding("Market").getUpgradeCost(), SwingConstants.CENTER));
        }else{
            buyMarket = new JButton("BUY MARKET (1500)");                                                                     buyMarket.addActionListener(this);
            market.add(new Label("", SwingConstants.CENTER));
            market.add(buyMarket);
        }
        frame.add(market);

        if(cityHas("Farm")){
            upgradeFarm = new JButton("UPGRADE FARM");                                                                    upgradeFarm.addActionListener(this);
            farm.add(upgradeFarm);
            farm.add(new Label("Level: " + getBuilding("Farm").getLevel() + "|| Upgrade Cost: " + getBuilding("Farm").getUpgradeCost(), SwingConstants.CENTER));
        }else{
            buyFarm = new JButton("BUY FARM (1000)");                                                                   buyFarm.addActionListener(this);
            farm.add(buyFarm);
            farm.add(new Label(""), SwingConstants.CENTER);
        }
        frame.add(farm);


        frame.revalidate();
        frame.repaint();
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String src = e.getActionCommand();

        switch (src) {
            case "UPGRADE MARKET":
                try {
                    player.upgradeBuilding(getBuilding("Market"));
                } catch (NotEnoughGoldException | BuildingInCoolDownException | MaxLevelException a) {
                    JOptionPane.showMessageDialog(frame, a.getMessage());
                    return;
                }
                frame.getContentPane().removeAll();
                new BuyOrUpgradeEco(frame, city, player, game, playerInfo);
                break;
            case "UPGRADE FARM":
                try {
                    player.upgradeBuilding(getBuilding("Farm"));
                } catch (NotEnoughGoldException | BuildingInCoolDownException | MaxLevelException a) {
                    JOptionPane.showMessageDialog(frame, a.getMessage());
                    return;
                }
                frame.getContentPane().removeAll();
                new BuyOrUpgradeEco(frame, city, player, game, playerInfo);
                break;
            case "Return to City View":
                frame.getContentPane().removeAll();
                new CityView(city, game, frame, playerInfo);
                break;
            case "BUY MARKET (1500)":
                try {
                    player.build("market", city.getName());
                } catch (NotEnoughGoldException notEnoughGoldException) {
                    JOptionPane.showMessageDialog(frame, notEnoughGoldException.getMessage());
                    return;
                }
                frame.getContentPane().removeAll();
                new BuyOrUpgradeEco(frame, city, game.getPlayer(), game, playerInfo);
                break;
            case "BUY FARM (1000)":
                try {
                    player.build("farm", city.getName());
                } catch (NotEnoughGoldException notEnoughGoldException) {
                    JOptionPane.showMessageDialog(frame, notEnoughGoldException.getMessage());
                    return;
                }
                frame.getContentPane().removeAll();
                new BuyOrUpgradeEco(frame, city, game.getPlayer(),game, playerInfo);
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
