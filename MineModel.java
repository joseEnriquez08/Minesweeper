import java.io.Serializable;
import java.util.Scanner;

public class MineModel implements Serializable{

    //number of squares buttons to make and to set up.
    private static final int cells = 10; 

    // booleans to tell if we have lost or won the game. 
    // will be updated accordingly throughout the code.
    private boolean lost = false;
    private boolean won = false;

    /* symbols symbolizing the state of a "cell" on the grid
        "M" defines a Mine
        "?" defines an unclicked "cell"
        "F" defines a flag
    */

    private String mine = "M";
    private String initialState = "?";
    private String flag = "F"; 

    // amount of bombs and flags
    private int randAmountOfBombs = 0;
    private int numOfFlags;

    //state[][] is used to determine the state of cell using the string symbols defined above
    //numOfBombs[][] stores the amount of bombs that are adjacent to each square.
    private String[][] state;
    private int[][] numOfBombs;


    public MineModel(){
        
        //sets a range of 25-30 bombs
        //if(randAmountOfBombs < 1){
            randAmountOfBombs = (int)(Math.random()*10)+25;
        //}
        numOfFlags = randAmountOfBombs;
        //initializes the 2 2D variables
        state = new String[cells][cells];
        numOfBombs = new int[cells][cells];
        initializeState();
        initializeNumOfBombs();

    }

     public MineModel(String diff){
        if(diff.equalsIgnoreCase("recruit")){
         randAmountOfBombs = (int)(Math.random()*10)+5;
         System.out.println("recruit");
        }
        if(diff.equalsIgnoreCase("normal")){
            randAmountOfBombs = (int)(Math.random()*10)+25;
        }
        if(diff.equalsIgnoreCase("Impossible")){
            randAmountOfBombs = (int)(Math.random()*10)+50;
        }
        numOfFlags = randAmountOfBombs;

        //initializes the 2 2D variables
        state = new String[cells][cells];
        numOfBombs = new int[cells][cells];
        initializeState();
        initializeNumOfBombs();
         

    }


    //randomizes the state of each cell
    public void initializeState() {

        //counter to test if we have implemented enough bombs
        int counter = 0;
        int randx = 0;
        int randy =0;

        //sets every cell to the state of "?"
        for(int i = 0; i <cells; i++){
            for(int j = 0; j < cells; j++){

                state[i][j] = initialState;
            }
        }

        //infinit loop that breaks when we have sufficient bombs
       while(true){

            // sets the two coordinates to be a random but still within the range of the array
           randx = (int)(Math.random() * cells);
           randy = (int)(Math.random() * cells);

           if(counter == randAmountOfBombs) break;

           /*that cordinate is already a mine, it will continue, else it
             will set the state of that cell into a mine*/
           if(state[randx][randy].equals(mine)) continue;
           state[randx][randy] = mine;
           counter++;

        }

    }

    public void initializeNumOfBombs(){

        //nested loop to cycle through each of the cells
        for(int i = 0; i < cells; i++){
            for(int j = 0; j < cells; j++){

                //if cell is not a mine
                if(state[i][j].equals(mine) == false){

                    //using max and mins to set the bounds of the next loop. used to not go out of bounds of an array
                    int count = 0;
                    int xLeft = Math.max(i-1, 0);
                    int xRight = Math.min(i+1, cells-1);
                    int yLower = Math.max(j-1, 0);
                    int yUpper = Math.min(j+1, cells-1);

                    for(int x = xLeft; x <= xRight; x++){
                        for(int y = yLower; y <= yUpper; y++){

                            //exlcuding the current cell, it counts the bombs adjacent to it
                            if(x != i || y != j){
                                if(state[x][y].equals(mine) == true) count++;
                            }
                        }
                    }

                    // assigns that cell the number of bombs found
                    numOfBombs[i][j] = count;
                }
            }
        }

    }

    //basically a make move function
    //called when a cell is selected
    public void reveal(int i, int j){

        //checks if coordinates are within range
        if(i >=0 && i < cells && j >=0 && j < cells){

            //if cell hasnt been selected, reveal number of bombs
            if(state[i][j].equals("?")){ 
                state[i][j]=Integer.toString(numOfBombs[i][j]); 

            // if cell contains a bomb, lost game and reveal the bomb scheme
            }  else if(state[i][j].equals(mine)){
                System.out.println("You Lost");
                printBombField();
                lost=true;

            } else if(state[i][j].equals(flag)){
                state[i][j] = initialState;
                numOfFlags++;
            }
            else System.out.println("Already clicked on");

        } else System.out.println("Invalid coordinates");

    }

    public void reveal(int i, int j, String f){

        if(i >=0 && i < cells && j >=0 && j < cells){

            if(state[i][j].equals(flag) ){
                state[i][j] = initialState;
                numOfFlags++;

            }
            else if(state[i][j].equals(initialState) || state[i][j].equals("M")){//Checks if the space has either a ? or a M
                state[i][j] = "F";
                numOfFlags++;
            }
        } else System.out.println("Invalid coordinates");

    }


    //checks if player has won
    public void checkWin(){

        won = false;
        int count = 0;
        //cycles through each cell
        for(int i = 0; i < cells; i++){
            for(int j = 0; j<cells; j++){
                
                if(state[i][j].equals(initialState)){
                    count++;
                }

            }
        }
        if(count == 0){
            won = true;
        }

        if(won) System.out.println("You Won");
    }

    //checks if a string is a number
    public boolean checkIfStringIsNumeric(String str){

        try{
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    //prints the playing board
    public void printField(){

        for(int i = 0; i<cells; i++){
            for(int j = 0; j <cells; j++){

                if(state[i][j].equals(mine)) System.out.print("| " + "?" + " |");
                else System.out.print("| " + state[i][j] + " |");
            }
            System.out.println();
        }

    }

    //prints the board with all bombs revealed
    public void printBombField(){
        for(int i = 0; i<cells; i++){
            for(int j = 0; j <cells; j++){

                if(state[i][j].equals(mine)) System.out.print("| " + state[i][j] + " |");
                else System.out.print("| " + numOfBombs[i][j] + " |");
            }
            System.out.println();
        }

    }

    public String[][] getState(){
        return state;
    }

    public int getCells(){
        return cells;
    }
    public String getFlag(){
        return flag;
    }
    public boolean getLost(){
        return lost;
    }
    public boolean getWon(){
        return won;
    }
    public int getRandAmountOfBombs(){
        return randAmountOfBombs;
    }
    public int getNumOfFlags(){
        return numOfFlags;
    }

    public void setRandAmountOfBombs(String diff){
        if(diff.equalsIgnoreCase("recruit")){

        }
        if(diff.equalsIgnoreCase("normal")){

        }
        if(diff.equalsIgnoreCase("Impossible")){

        }

    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MineModel are = new MineModel();

        while(true){
            are.printField();
            int i = scanner.nextInt();
            int j = scanner.nextInt();
            are.reveal(i, j);
            if(are.lost)break;
            are.checkWin();
            System.out.println();

        }
    
    }

}