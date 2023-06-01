package GUI;

import GUI.Listeners.EndTurnListener;
import GUI.Listeners.resolveOrBattleListener;
import engine.City;
import engine.Game;
import engine.Player;
import exceptions.FriendlyCityException;
import exceptions.MaxCapacityException;
import exceptions.TargetNotReachedException;
import units.Army;
import units.Status;
import units.Unit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UnitsView implements ActionListener {

    private JFrame frame, resolveOrBattle;
    int totalUnitCount;
    private JButton previous, relocate, target, initiateArmy, endTurn, laySiege, resolve, battleView;
    private JOptionPane warning;
    private JLabel row1, row2, row3;
    private JComboBox<Unit> units;
    private JComboBox<City> cities;
    private JComboBox<Army> armies;
    private JPanel playerInfo, corePanel, buttonPanel;
    private City city;
    private Player player;
    private Game game;
    public static Boolean flag;

    public City getCity(String x){
        for(City t : game.getAvailableCities()){
            if(t.getName().equals(x)){
                return t;
            }
        }
        return null;
    }

    public UnitsView(JFrame frame, City c, Player p, Game g, JPanel playerInfo){
        city = c;
        player = p;
        game = g;
        totalUnitCount = 0;
        this.playerInfo = playerInfo;
        frame.setLayout(new FlowLayout());
        //INITIALIZE STUFF
        warning = new JOptionPane();
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        corePanel = new JPanel();
        corePanel.setLayout(new BoxLayout(corePanel, BoxLayout.Y_AXIS));
        row1 = new JLabel("Unit to be selected: ");
        row2 = new JLabel("City to target: ");
        row3 = new JLabel("Available armies to relocate to: ");
        units = new JComboBox<>();
        cities = new JComboBox<>();
        armies = new JComboBox<>();
        for(Army a : p.getControlledArmies()){
            armies.addItem(a);
            for(Unit u : a.getUnits()){
                totalUnitCount++;
                units.addItem(u);
            }
        }

        corePanel.add(playerInfo);
        corePanel.add(row1);
        corePanel.add(units);
        corePanel.add(row2);
        corePanel.add(cities);
        corePanel.add(row3);
        corePanel.add(armies);
        frame.add(corePanel);
        endTurn = new JButton("End Turn");                                      endTurn.addActionListener(new EndTurnListener(frame, game, playerInfo));
        endTurn.setFont(new Font("", Font.BOLD, 20));
        endTurn.setBackground(Color.BLACK);
        endTurn.setForeground(Color.WHITE);
        previous = new JButton("Return to City View");                          previous.addActionListener(this);
        relocate = new JButton("Relocate selected unit to an army");            relocate.addActionListener(this);
        target = new JButton("Target selected army to a city");                 target.addActionListener(this);
        initiateArmy = new JButton("Initiate army with selected unit");         initiateArmy.addActionListener(this);
        laySiege = new JButton("Lay siege");                                    laySiege.addActionListener(this);

        buttonPanel.add(previous);  buttonPanel.add(relocate);  buttonPanel.add(target);    buttonPanel.add(initiateArmy);  buttonPanel.add(laySiege);   buttonPanel.add(endTurn);
        frame.add(buttonPanel);

        frame.revalidate();
        frame.repaint();
        this.frame = frame;

        for(City ci : game.getAvailableCities()){
            if(!(ci.getName().equals(c.getName()))){
                cities.addItem(ci);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String src = e.getActionCommand();
        frame.getContentPane().removeAll();
        switch (src) {
            case "Lay siege":
                if(((Army) armies.getSelectedItem()) == null) {
                    JOptionPane.showMessageDialog(frame, "You must have selected an army.");
                }
                else if(((Army) armies.getSelectedItem()).getUnits().size() == 0){
                    JOptionPane.showMessageDialog(frame, "Your army must have units.");
                }
                else{
                    if (((Army) armies.getSelectedItem()).getCurrentStatus() != Status.BESIEGING) {
                        try {
                            player.laySiege((((Army) armies.getSelectedItem())), (((City) cities.getSelectedItem())));
                        } catch (TargetNotReachedException | FriendlyCityException targetNotReachedException) {
                            JOptionPane.showMessageDialog(frame, targetNotReachedException.getMessage());
                        }
                        if((((Army) armies.getSelectedItem())).getCurrentStatus() == Status.BESIEGING) {
                            JOptionPane.showMessageDialog(frame, "You can choose to continue sieging or enter battle now.");
                            resolveOrBattle = new JFrame();
                            resolveOrBattle.setSize(400, 100);
                            resolveOrBattle.setLocationRelativeTo(null);
                            resolveOrBattle.setVisible(true);
                            resolve = new JButton("Resolve battle!");
                            resolve.addActionListener(new resolveOrBattleListener(frame, resolveOrBattle, game));
                            battleView = new JButton("Enter Battle View!");
                            battleView.addActionListener(new resolveOrBattleListener(frame, resolveOrBattle, game));
                            resolveOrBattle.setLayout(new FlowLayout());
                            resolveOrBattle.add(resolve);
                            resolveOrBattle.add(battleView);
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "You are already sieging this city!");
                    }
                }
                new UnitsView(frame, city, player, game, playerInfo);
                break;
            case "Return to City View":
                new CityView(city, game, frame, playerInfo);
                break;
            case "Relocate selected unit to an army":
                if (((Unit) units.getSelectedItem()) == null || ((Army) armies.getSelectedItem()) == null) {
                    JOptionPane.showMessageDialog(frame, "You must select a unit AND an army!");
                }else{
                    try {
                        ((Army) armies.getSelectedItem()).relocateUnit(((Unit) units.getSelectedItem()));
                    } catch (MaxCapacityException maxCapacityException) {
                        JOptionPane.showMessageDialog(frame, maxCapacityException);
                    }
                }
                new UnitsView(frame, city, player, game, playerInfo);
                break;
            case "Target selected army to a city":
                if(((Army) armies.getSelectedItem()) == null || ((City) cities.getSelectedItem()) == null){
                    JOptionPane.showMessageDialog(frame, "You must select a unit AND a city.");
                }
                else if(((Army) armies.getSelectedItem()).getUnits().size() == 0){
                    JOptionPane.showMessageDialog(frame, "Your army must have units.");
                }
                else if(((Army) armies.getSelectedItem()).getTarget().equals(((City) cities.getSelectedItem()).getName())){
                    JOptionPane.showMessageDialog(frame, "You've already targetted this city!");
                }
                else if(((Army) armies.getSelectedItem()).getCurrentLocation().equals(((City) cities.getSelectedItem()).getName())){
                    JOptionPane.showMessageDialog(frame, "Your army is already there!");
                }
                else {
                    if (!(armies.getSelectedItem() == city.getDefendingArmy())) {
                        game.targetCity((((Army) armies.getSelectedItem())), (((City) cities.getSelectedItem()).getName()));
                    } else {
                        JOptionPane.showMessageDialog(frame, "You can't attack with your defending army.");
                    }
                }
                flag = true;
                new UnitsView(frame, city, player, game, playerInfo);
                break;
            default:
                if(((Unit) units.getSelectedItem() == null)){
                    JOptionPane.showMessageDialog(frame, "You must select a unit!");
                }
                else{
                    player.initiateArmy(city, ((Unit) units.getSelectedItem()));
                }

                new UnitsView(frame, city, player, game, playerInfo);
                break;
        }
    }
}
