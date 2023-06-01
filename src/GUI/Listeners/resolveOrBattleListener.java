package GUI.Listeners;

import GUI.BattleView;
import GUI.WorldMap;
import engine.City;
import engine.Game;
import exceptions.FriendlyFireException;
import units.Army;
import units.Status;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class resolveOrBattleListener implements ActionListener {
    JFrame frame, resolveOrBattle;
    Game game;

    public resolveOrBattleListener(JFrame frame, JFrame resolveOrBattle, Game game){
        this.resolveOrBattle = resolveOrBattle;
        this.frame = frame;
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Resolve battle!")){
            Army temp2 = null;
            City defCity2 = null;
            for(City c : game.getAvailableCities()){
                if(c.isUnderSiege() && c.getTurnsUnderSiege() <= 3){
                    c.setTurnsUnderSiege(-1);
                    c.setUnderSiege(false);
                    for(Army a : game.getPlayer().getControlledArmies()){
                        if(a.getCurrentLocation().equals(c.getName())){
                            temp2 = a;
                            defCity2 = c;
                            a.setCurrentStatus(Status.IDLE);
                        }
                    }
                }
            }
            try {
                game.autoResolve(temp2, defCity2.getDefendingArmy());
            } catch (FriendlyFireException friendlyFireException) {
                friendlyFireException.printStackTrace();
            }
            if(game.getPlayer().getControlledCities().contains(defCity2)){
                JOptionPane.showMessageDialog(frame, "You won the fight!");
                if(game.isGameOver()){
                    if(game.getPlayer().getControlledCities().size() <= 3)
                        JOptionPane.showMessageDialog(frame, "You Won");
                    else
                        JOptionPane.showMessageDialog(frame, "You Lost");
                    frame.dispose();
                }
            }else {
                for (City c : game.getAvailableCities()) {
                    if (c.getName().equals(defCity2.getName())) {
                        c.setTurnsUnderSiege(-1);
                        c.setUnderSiege(false);
                    }
                }
                JOptionPane.showMessageDialog(frame, "You lost the fight!");
                frame.setVisible(true);
                resolveOrBattle.dispose();
                frame.getContentPane().removeAll();
                new WorldMap(frame, game);
            }

        }


        if(e.getActionCommand().equals("Enter Battle View!")){
            Army temp1 = null;
            City defCity1 = null;
            for(City c : game.getAvailableCities()){
                if(c.isUnderSiege() && c.getTurnsUnderSiege() <= 3){
                    for(Army a : game.getPlayer().getControlledArmies()){
                        if(a.getCurrentLocation().equals(c.getName())){
                            temp1 = a;
                            defCity1 = c;
                            a.setCurrentStatus(Status.IDLE);
                        }
                    }
                }
            }
            for(City c : game.getAvailableCities()){
                if(c.getName().equals(defCity1.getName())){
                    c.setTurnsUnderSiege(-1);
                    c.setUnderSiege(false);
                }
            }
            resolveOrBattle.dispose();
            frame.getContentPane().removeAll();
            frame.setVisible(true);
            new BattleView(frame,game, defCity1.getDefendingArmy(), temp1, "");
        }
    }
}
