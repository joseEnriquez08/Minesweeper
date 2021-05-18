import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.lang.Thread.State;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.*;


import javax.swing.JMenu;
import javax.swing.JMenuBar;


public class MinePanel extends JPanel {

    private MineModel model = new MineModel();
    private int cells = model.getCells();
    private int flags = model.getNumOfFlags();    
   
    //an array of buttons to fill the gridlayout
    //buttonHandler object that will be added to all the buttons
    private JButton[][] btns = new JButton[cells][cells];
    private ButtonHandler buttonHandler = new ButtonHandler();

    //booleans to tell if player has won or lost
    private boolean won;
    private boolean lost;

    private JMenu file = new JMenu("File");

    private JMenu newGame = new JMenu("New");
    private JMenuItem saveGame = new JMenuItem("Save");
    private JMenuItem loadGame = new JMenuItem("Load");
    private JMenuItem quitGame = new JMenuItem("Quit");

    private JMenuItem recruit = new JMenuItem("Recruit Difficulty");
    private JMenuItem normal = new JMenuItem("Medium Difficulty");
    private JMenuItem impossible = new JMenuItem("Impossible");
    
    

    public MinePanel(JMenuBar menu){
        setLayout(new GridLayout(0,cells));
        createAndAddButtons();                                 //initializes and adds the buttons to the jpanel

        saveGame.addActionListener(new menuHandler());
        loadGame.addActionListener(new menuHandler());
        quitGame.addActionListener(new menuHandler());
        recruit.addActionListener(new menuHandler());
        normal.addActionListener(new menuHandler());
        impossible.addActionListener(new menuHandler());

        newGame.add(recruit);
        newGame.add(normal);
        newGame.add(impossible);

        file.add(newGame);
        file.add(saveGame);
        file.add(loadGame);
        file.add(quitGame);
        menu.add(file);
        

    }

    public void createAndAddButtons(){

        for(int i = 0; i < cells; i++){
            for(int j = 0; j <cells; j++){

                btns[i][j] = new JButton("?");                  // sets all buttons to unclicked "?" state
                btns[i][j].setBackground(Color.BLACK);          // changes the color
                btns[i][j].setForeground(Color.WHITE);
                btns[i][j].addMouseListener(buttonHandler);     // adds a mouseAdappter object to all buttons
                add(btns[i][j]);                                // adds button to jpanel*/
            }
        }
    }

    public void createAndAddButtons(String[][] state){ // method used only when loading a game

        for(int i = 0; i < cells; i++){
            for(int j = 0; j <cells; j++){

                if(state[i][j].equals("?") || state[i][j].equals("F") || state[i][j].equals("M")){
                    if(state[i][j].equals("M")){
                        btns[i][j] = new JButton("?");                 
                    } else{
                        btns[i][j] = new JButton(state[i][j]);                  
                    }
                    btns[i][j].setBackground(Color.BLACK);         
                    btns[i][j].setForeground(Color.WHITE);
                     
                }
                else {
                    btns[i][j] = new JButton(state[i][j]);
                    btns[i][j].setBackground(Color.GREEN);
                    btns[i][j].setForeground(Color.BLACK);
                }

                add(btns[i][j]);
                btns[i][j].addMouseListener(buttonHandler);     // adds a mouseAdappter object to all buttons

            }
        }


    }

    public void revealAll(){
        for(int i = 0; i < cells; i++){
            for(int j = 0; j < cells; j++){
                model.reveal(i, j);
                btns[i][j].setText(model.getState()[i][j]);
                if(btns[i][j].getText().equals("M"))btns[i][j].setBackground(Color.RED);        
                else btns[i][j].setBackground(Color.GREEN);                                    
                    btns[i][j].setForeground(Color.BLACK);
            }

        }
    }

    public class menuHandler implements ActionListener {
        public void actionPerformed(ActionEvent e){
          
            if(e.getSource() ==  saveGame ){
            
                JFileChooser fileChooser = new JFileChooser();
                int x = fileChooser.showOpenDialog(file);
                File savedFile = fileChooser.getSelectedFile();

                try{
                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(savedFile));
                    out.writeObject(model);
                }catch(IOException error){
                    error.printStackTrace();
                }

            }
            if(e.getSource() ==  loadGame ){
                
                JFileChooser fileChooser = new JFileChooser();
                int x = fileChooser.showOpenDialog(file);
                File loadingFile = fileChooser.getSelectedFile();

                try{
                    
                    ObjectInputStream in = new ObjectInputStream(new FileInputStream(loadingFile));
                    model = (MineModel) in.readObject();
                    removeAll();
                    createAndAddButtons(model.getState());
                    validate();
                   
                }catch(IOException error){
                    error.printStackTrace();
                } catch(ClassNotFoundException error){
                    System.out.println("off");
                }
            }
            if(e.getSource() ==  quitGame ){
                System.exit(0);

            }
            if(e.getSource() ==  recruit){
                removeAll();
                createAndAddButtons();
                validate();
                model = new MineModel("recruit");
                

            }
            if(e.getSource() ==  normal){
                removeAll();
                createAndAddButtons();
                validate();
                model = new MineModel("normal");

            }
            if(e.getSource() ==  impossible){
                removeAll();
                createAndAddButtons();
                validate();
                model = new MineModel("Impossible");

            }
        }
    }
    

    public class ButtonHandler extends MouseAdapter {

        public void mouseClicked(MouseEvent m){
            //nested loop to dteremine wich button was clicked
            for(int i = 0; i < cells; i++){
                for(int j = 0; j < cells; j++){

                    //if button is left clicked
                    if(m.getButton() == MouseEvent.BUTTON1){

                        // if the source of the clicked button aligns with the coordinates
                        if(m.getSource() == btns[i][j]){

                            if(btns[i][j].getText().equals("F")){
                                JOptionPane.showMessageDialog(null, "Must unflag first");
                                break;
                            }

                            model.reveal(i, j);                                                             //calls reveal using the cordinates
                            btns[i][j].setText(model.getState()[i][j]);                                     //udpates the state of the button
                            model.checkWin();                                                               //checks if that was the winning click
                            won = model.getWon();                                                           //updates won boolean
                            lost = model.getLost(); 
                                                                                                            //updates lost boolean
                            if(btns[i][j].getText().equals("M"))btns[i][j].setBackground(Color.RED);        //if button is a mine, change color to red
                            else btns[i][j].setBackground(Color.GREEN);                                     //else change color to green
                            btns[i][j].setForeground(Color.BLACK);                                          // changes font to black
                                                                                                            
                            if(lost){                                                                       // if player looses game, shut down game
                                JOptionPane.showMessageDialog(null, "Loser");
                                revealAll();
                            }

                            if(won){                                                                        // if player won game, shut down game
                                JOptionPane.showMessageDialog(null, "You won!!!!");
                                System.exit(0);
                            }
                        }
                     }


                     if(m.getButton() == MouseEvent.BUTTON3){                                               //if button is right clicked, button gets flagged
                        if(m.getSource() == btns[i][j]){       
                            
                            if(model.getNumOfFlags() == 0){
                                JOptionPane.showMessageDialog(null, "no more flags");
                            }else{
                                model.reveal(i, j, model.getFlag());                                            //calls reveal using coordinates
                                btns[i][j].setText(model.getState()[i][j]);                                     //updates the state of the button
                            }
                        }
                     }
                }
            }
            
        }
    
    }
}

