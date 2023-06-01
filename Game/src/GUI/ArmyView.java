package GUI;

import engine.City;
import engine.Game;
import engine.Player;
import units.Army;
import units.Unit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ArmyView implements ActionListener {
    JFrame frame;
    JButton previous;
    JPanel defendingArmy, otherArmies, playerInfo;
    JLabel defending, armies;
    JScrollPane def, arm;
    JTextArea defendingText, armiesText;
    City city;
    Player player;
    Game game;


    public ArmyView(JFrame frame, City c, Player p, Game g, JPanel playerInfo){
        city = c;
        player = p;
        game = g;
        this.playerInfo = playerInfo;
        frame.setLayout(new GridLayout(2, 2,5,0));
        String sDef = "";
        String s = "";
        int i = 1;
        //INITIALIZE SCROLL PANES AND TEXT AREAS
        defendingText = new JTextArea();
        defendingText.setEditable(false);
        defendingText.setBackground(Color.GRAY);
        armiesText = new JTextArea();
        armiesText.setEditable(false);
        armiesText.setBackground(Color.GRAY);
        for(Army a : p.getControlledArmies()){
            for(Unit u : a.getUnits()) {
                if (p.getControlledArmies().size() > 1)
                    s += "Army " + i + ": ";
                if (c.getDefendingArmy() == a) {
                    sDef += u.toString() + "\n";
                } else {
                    s += u.toString() + "\n";
                }
            }
            s +="\n";
            i++;
        }
        defendingText.setText(sDef);
        armiesText.setText(s);
        def = new JScrollPane(defendingText);
        arm = new JScrollPane(armiesText);
        //INITIALIZE LABELS
        defending = new JLabel("Defending Army", SwingConstants.CENTER);
        defending.setFont(new Font("", Font.BOLD, 40));
        defending.setBackground(Color.GRAY);
        armies = new JLabel("Other Armies", SwingConstants.CENTER);
        armies.setFont(new Font("", Font.BOLD, 40));
        armies.setBackground(Color.GRAY);
        //INITIALIZE JPANELS
        defendingArmy = new JPanel();
        defendingArmy.setLayout(new GridLayout(3,1));
        defendingArmy.add(defending);
        defendingArmy.add(def);
        previous = new JButton("Return to City View");          previous.addActionListener(this);
        defendingArmy.add(previous);
        frame.add(defendingArmy);
        otherArmies = new JPanel();
        otherArmies.setLayout(new GridLayout(2,1));
        otherArmies.add(armies);
        otherArmies.add(arm);
        frame.add(otherArmies);

        frame.revalidate();
        frame.repaint();
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.getContentPane().removeAll();
        new CityView(city, game, frame, playerInfo);
    }
}
