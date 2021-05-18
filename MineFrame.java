import javax.swing.JFrame;
import javax.swing.JMenuBar;

import javax.swing.JMenu;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import java.awt.*;

public class MineFrame extends JFrame {
    public MineFrame(){
        super("Minesweeper");

        JMenuBar menu = new JMenuBar();
        this.setJMenuBar(menu);
        
        add(new MinePanel(menu));
  
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }
    
}