package GUI;

import engine.Game;
import exceptions.FriendlyFireException;
import units.Army;
import units.Cavalry;
import units.Infantry;
import units.Unit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class BattleView implements ActionListener {
    JFrame frame;
    JPanel comboPanel;
    JComboBox<Unit> defUnits, attackUnits;
    JButton attack;
    Army defendingArmy, attackingArmy;
    int turn;
    String log = "";
    Game game;

    public BattleView(JFrame frame,Game g, Army defendingArmy, Army attackingArmy, String log){
        Random r = new Random();
        game = g;


        frame.setTitle("Battle View");
        this.turn = turn;
        this.defendingArmy = defendingArmy;
        this.attackingArmy = attackingArmy;
        defUnits = new JComboBox<>();
        for(Unit u : defendingArmy.getUnits()){
            defUnits.addItem(u);
        }
        attackUnits = new JComboBox<>();
        for(Unit u : attackingArmy.getUnits()){
            attackUnits.addItem(u);
        }
        attack = new JButton("Attack!");
        attack.addActionListener(this);
        attack.setFont(new Font("", Font.BOLD, 50));
        comboPanel = new JPanel();
        comboPanel.setLayout(new GridLayout(1, 2));
        comboPanel.add(defUnits);
        comboPanel.add(attackUnits);
        frame.setLayout(new GridLayout(4,2,2,6));

        int i1;     //  THIS PART IS NOT IMPORTANT
        int i2;     //  IT IS MEANT FOR CLEANLINESS ONLY
        int i3;     //  YOU CAN IGNORE IT
        //--------------GAME LOG--------------
        i1 = 1;
        i2 = 1;
        i3 = 1;
        String s = log;
        JTextArea logText = new JTextArea();
        logText.setEditable(false);
        logText.setText(s);
        JScrollPane logPane = new JScrollPane(logText);
        logPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);


        JPanel logTextPanel = new JPanel();
        logTextPanel.setLayout(new GridLayout(1,1));
        JLabel t1 = new JLabel("GAME LOG", SwingConstants.CENTER);
        t1.setFont(new Font("", Font.BOLD, 20));
        logTextPanel.add(t1);
        logTextPanel.setBackground(Color.GRAY);

        frame.add(logTextPanel, BorderLayout.CENTER);
        frame.add(logPane);
        //--------------PLAYER'S UNITS--------------
        i1 = 1;
        i2 = 1;
        i3 = 1;
        s = "";
        JTextArea playerUnits = new JTextArea();
        playerUnits.setEditable(false);
        for(Unit unit : attackingArmy.getUnits()){
            if(unit instanceof Infantry){
                s += "Infantry " + i1 + ": Level: " + unit.getLevel()
                        + " || Current soldier count: " + unit.getCurrentSoldierCount() + "\n\n";
                i1++;
            }else if(unit instanceof Cavalry){
                s += "Cavalry " + i2 + ": Level: " + unit.getLevel()
                        + " || Current soldier count: " + unit.getCurrentSoldierCount() + "\n\n";
                i2++;
            }else{
                s += "Archer " + i3 + ": Level: " + unit.getLevel()
                        + " || Current soldier count: " + unit.getCurrentSoldierCount() + "\n\n";
                i3++;
            }
        }
        playerUnits.setText(s);
        JScrollPane playerUnitsPane = new JScrollPane(playerUnits);
        playerUnitsPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel playerText = new JPanel();
        playerText.setLayout(new GridLayout(1,1));
        JLabel t2 = new JLabel("PLAYER'S UNITS", SwingConstants.CENTER);
        t2.setFont(new Font("", Font.BOLD, 20));
        playerText.add(t2);
        playerText.setBackground(Color.GRAY);

        frame.add(playerText, BorderLayout.CENTER);
        frame.add(playerUnitsPane);
        //--------------DEFENDING ARMY'S UNITS--------------
        i1 = 1;
        i2 = 1;
        i3 = 1;
        s = "";
        JTextArea defendingUnits = new JTextArea();
        defendingUnits.setEditable(false);
        for(Unit unit : defendingArmy.getUnits()){
            if(unit instanceof Infantry){
                s += "Infantry " + i1 + ": Level: " + unit.getLevel()
                        + " || Current soldier count: " + unit.getCurrentSoldierCount() + "\n\n";
                i1++;
            }else if(unit instanceof Cavalry){
                s += "Cavalry " + i2 + ": Level: " + unit.getLevel()
                        + " || Current soldier count: " + unit.getCurrentSoldierCount() + "\n\n";
                i2++;
            }else{
                s += "Archer " + i3 + ": Level: " + unit.getLevel()
                        + " || Current soldier count: " + unit.getCurrentSoldierCount() + "\n\n";
                i3++;
            }
        }
        defendingUnits.setText(s);
        JScrollPane defendingUnitsPane = new JScrollPane(defendingUnits);
        defendingUnitsPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel defendingText = new JPanel();
        defendingText.setLayout(new GridLayout(1,1));
        JLabel t3 = new JLabel("DEFENDING ARMY'S UNITS", SwingConstants.CENTER);
        t3.setFont(new Font("", Font.BOLD, 20));
        defendingText.add(t3);
        defendingText.setBackground(Color.GRAY);

        frame.add(defendingText, BorderLayout.CENTER);
        frame.add(defendingUnitsPane);
        frame.add(attack);
        frame.add(comboPanel);
        //------------------------------------------------

        if(attackingArmy.getUnits().size()==0){
            JOptionPane.showMessageDialog(frame, "You lost this fight!");
            frame.getContentPane().removeAll();
            frame.setVisible(true);
            new WorldMap(frame,game);
        }
        if(defendingArmy.getUnits().size()==0){
            JOptionPane.showMessageDialog(frame, "You won this fight!");
            frame.setVisible(true);
            frame.getContentPane().removeAll();
            game.occupy(attackingArmy, attackingArmy.getCurrentLocation());
            if(game.isGameOver()){
                if(game.getPlayer().getControlledCities().size() == 3)
                    JOptionPane.showMessageDialog(frame, "You Won");
                else
                    JOptionPane.showMessageDialog(frame, "You Lost");
                frame.dispose();
            }else{
                new WorldMap(frame,game);
            }
        }
        frame.revalidate();
        frame.repaint();
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.getContentPane().removeAll();

        //ATTACKING ARMY ATTACKS
        int oldValue = ((Unit) defUnits.getSelectedItem()).getCurrentSoldierCount();
        try {
            ((Unit) attackUnits.getSelectedItem()).attack((Unit) defUnits.getSelectedItem());
        } catch (FriendlyFireException friendlyFireException) {
            friendlyFireException.printStackTrace();
        }
        int newValue = ((Unit) defUnits.getSelectedItem()).getCurrentSoldierCount();
        defendingArmy.handleAttackedUnit(((Unit) defUnits.getSelectedItem()));
        log += "Defending army unit lost " + (oldValue - newValue) + " soldiers!\n\n";

        //DEFENDING ARMY DEFENDS
        int oldValue1 = ((Unit) attackUnits.getSelectedItem()).getCurrentSoldierCount();
        try {
            ((Unit) defUnits.getSelectedItem()).attack((Unit) attackUnits.getSelectedItem());
        } catch (FriendlyFireException friendlyFireException) {
            friendlyFireException.printStackTrace();
        }
        int newValue1 = ((Unit) attackUnits.getSelectedItem()).getCurrentSoldierCount();
        log += "Attacking army unit lost " + (oldValue1 - newValue1) + " soldiers!\n\n";

        attackingArmy.handleAttackedUnit(((Unit) attackUnits.getSelectedItem()));
        new BattleView(frame,game, defendingArmy, attackingArmy, log);

    }
}
