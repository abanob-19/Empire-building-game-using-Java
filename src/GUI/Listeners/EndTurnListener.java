package GUI.Listeners;

import GUI.BattleView;
import GUI.UnitsView;
import GUI.WorldMap;
import engine.City;
import engine.Game;
import exceptions.FriendlyFireException;
import units.Army;
import units.Status;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EndTurnListener implements ActionListener {
    JFrame frame, resolveOrBattle;
    JButton resolve, battleView;
    Game game;
    JPanel playerInfo;
    public EndTurnListener(JFrame frame, Game game, JPanel playerInfo){
        this.frame = frame;
        this.game = game;
        this.playerInfo = playerInfo;
        resolveOrBattle = new JFrame();
    }

    public void actionPerformed(ActionEvent e) {
        game.endTurn();
        if(game.isGameOver()){
            if(game.getPlayer().getControlledCities().size() == 3)
                JOptionPane.showMessageDialog(frame, "You Won");
            else
                JOptionPane.showMessageDialog(frame, "You Lost");
            frame.dispose();
        }
        for (Component a : playerInfo.getComponents()) {
            if (a instanceof JLabel) {
                if (((JLabel) a).getText().contains("Treasury: "))
                    ((JLabel) a).setText("Treasury: " + game.getPlayer().getTreasury());

            }
            if (a instanceof JLabel) {
                if (((JLabel) a).getText().contains("Food: "))
                    ((JLabel) a).setText("Food: " + game.getPlayer().getFood());

            }
        }
        for(Army a : game.getPlayer().getControlledArmies()){
            if(a.getUnits().size() != 0){
                if(a.getDistancetoTarget() == 0){
                    if(UnitsView.flag){
                        JOptionPane.showMessageDialog(frame, "Your army has reached its destination.");
                    }
                    UnitsView.flag = false;
                }
            }
        }

        for(City ci : game.getAvailableCities()){
            if(ci.getTurnsUnderSiege() < 3 && ci.isUnderSiege()){
                resolveOrBattle.setSize(400,100);
                resolveOrBattle.setLocationRelativeTo(null);
                resolveOrBattle.setVisible(true);
                resolve = new JButton("Resolve battle!");           resolve.addActionListener(new resolveOrBattleListener(frame, resolveOrBattle, game));
                battleView = new JButton("Enter Battle View!");     battleView.addActionListener(new resolveOrBattleListener(frame, resolveOrBattle,  game));
                resolveOrBattle.setLayout(new FlowLayout());
                resolveOrBattle.add(resolve);
                resolveOrBattle.add(battleView);
            }
            if(ci.getTurnsUnderSiege() == 3){
                JOptionPane.showMessageDialog(frame, "You must now choose to either resolve battle or enter battle view!");
                resolveOrBattle = new JFrame();
                resolveOrBattle.setSize(400,100);
                resolveOrBattle.setLocationRelativeTo(null);
                frame.setVisible(false);
                resolveOrBattle.setVisible(true);
                resolve = new JButton("Resolve battle!");           resolve.addActionListener(new resolveOrBattleListener(frame, resolveOrBattle, game));
                battleView = new JButton("Enter Battle View!");     battleView.addActionListener(new resolveOrBattleListener(frame, resolveOrBattle,  game));
                resolveOrBattle.setLayout(new FlowLayout());
                resolveOrBattle.add(resolve);
                resolveOrBattle.add(battleView);
            }
        }

        JLabel turn = (JLabel) playerInfo.getComponent(3);
        turn.setText("Turn Count: "+game.getCurrentTurnCount());
    }
}
