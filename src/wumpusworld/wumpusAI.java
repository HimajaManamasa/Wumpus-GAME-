package wumpusworld;

import java.util.Arrays;
import java.util.Random;

public class wumpusAI implements AIAgent {

    public static final String[] MOVE = {"LEFT", "RIGHT", "UP", "DOWN"}; // Player moves


    public static final double MAXACTIONSCORE = -1; // Score for each move


    public static final int SPEEDOFROUNDING = 20000; //The running time for the Agent to get trained.

   
    public static final int PITREWARD = -1; 
    public static final int WUMPUSREWARD = -1; 
    public static final int GOLDREWARD = 2; 

    
    public static final double ACTIONC = -0.04;// cost for a move in any direction
    
    public static final double RATEL = 0.5; // Learning rate
    public static final double DIS_FACTOR = 0.8; //Discounting factor

    public static final double PROBABILITYOFBADMOVE = 0.2;
    public static double[][] aiTableData = new double[16][5]; // Enumerating the boxes/blocks

    
    
    public String getBestMove(Placement currentPosition) {
        double maxActionPlayerReward = MAXACTIONSCORE;
        
        String p_move = null;
        int currentAIRowIndex = this.getAITableRowData(currentPosition);
        int best_pmove = 1;
        while(best_pmove < 5)
        {
            if (aiTableData[currentAIRowIndex][best_pmove] > maxActionPlayerReward) {
                p_move = MOVE[best_pmove - 1];
                maxActionPlayerReward = aiTableData[currentAIRowIndex][best_pmove];
            }
            best_pmove++;
        }
        return p_move;
    }
   
    public String getPlayerRandomMove() {
        Random randpos = new Random();

        int movePlayerIndex = randpos.nextInt(4);
        String move = MOVE[movePlayerIndex];
        return move;
    }

  
    private int getAITableRowData(Placement position) {
        int indexData = -1;
        if (position.getX() <= 4 && position.getY() <= 4)
        {
            if(position.getX() == 1 && position.getY() == 1){
            indexData = 0;
            }
            else if(position.getX() == 2 && position.getY() == 1){
            indexData = 1;
            }
            else if(position.getX() == 3 && position.getY() == 1){
            indexData = 2;
            }
            else if(position.getX() == 4 && position.getY() == 1){
            indexData = 3;
            }
            else if(position.getX() == 1 && position.getY() == 2){
            indexData = 4;
            }
            else if(position.getX() == 2 && position.getY() == 2){
            indexData = 5;
            }
            else if(position.getX() == 3 && position.getY() == 2){
            indexData = 6;
            }
            else if(position.getX() == 4 && position.getY() == 2){
            indexData = 7;
            }
            else if(position.getX() == 1 && position.getY() == 3){
            indexData = 8;
            }
            else if(position.getX() == 2 && position.getY() == 3){
            indexData = 9;
            }
            else if(position.getX() == 3 && position.getY() == 3){
            indexData = 10;
            }
            else if(position.getX() == 4 && position.getY() == 3){
            indexData = 11;
            }
            else if(position.getX() == 1 && position.getY() == 4){
            indexData = 12;
            }
            else if(position.getX() == 2 && position.getY() == 4){
            indexData = 13;
            }
            else if(position.getX() == 3 && position.getY() == 4){
            indexData = 14;
            }
            else if(position.getX() == 4 && position.getY() == 4){
            indexData = 15;
            }
            else{
            indexData = -1;
            }
        } 
    
    return indexData;
    }

    
    public void updateAITable(Placement currentPosition, String move) {
        int presentAITableRowData = this.getAITableRowData(currentPosition);
        int nextQTableRowIndex = this.getAITableRowData(this.getNextPlayerPosition(move, currentPosition));

        int playerActionValue = Arrays.asList(MOVE).indexOf(move) + 1;

        if (nextQTableRowIndex > -1) {
            aiTableData[presentAITableRowData][playerActionValue] += (ACTIONC + aiTableData[presentAITableRowData][0] + (DIS_FACTOR * this.getMaxPlayerReward(aiTableData[nextQTableRowIndex])) - aiTableData[presentAITableRowData][playerActionValue])* RATEL;
        } else {
            aiTableData[presentAITableRowData][playerActionValue] += RATEL * ACTIONC;
        }
    }
    
    
    private double getMaxPlayerReward(double[] aiHorizontal_Row) {
        double maxi = MAXACTIONSCORE;
        int maxloop = 1;
        while(maxloop < 5) {
            if (aiHorizontal_Row[maxloop] > maxi) {
                maxi = aiHorizontal_Row[maxloop];
            }
            maxloop++;
        }
        return maxi;
    }
    
    
    public Placement getNextPlayerPosition(String move, Placement currentPosition) {
        Placement newPosition = new Placement();

        if(move == "UP") {
                newPosition.setX(currentPosition.getX());
                newPosition.setY(currentPosition.getY() + 1);
        }
        else if(move == "DOWN") {
                newPosition.setX(currentPosition.getX());
                newPosition.setY(currentPosition.getY() - 1);
        }
           else if(move == "LEFT"){
                newPosition.setX(currentPosition.getX() - 1);
                newPosition.setY(currentPosition.getY());
        }
           else {
                newPosition.setX(currentPosition.getX() + 1);
                newPosition.setY(currentPosition.getY());
        }      

        return newPosition;
    }
    
    public void checkpossibility(World world, Placement currentPosition) {

        int presAIRowIndex = this.getAITableRowData(currentPosition);

        if (world.hasGlitter(world.getPlayerX(), world.getPlayerY())) {
            aiTableData[presAIRowIndex][0] = GOLDREWARD;
            world.doAction(World.A_GRAB);
        
        } else if (world.hasPit(world.getPlayerX(), world.getPlayerY())) {
            aiTableData[presAIRowIndex][0] = PITREWARD;
            world.doAction(World.A_CLIMB);
        } else if (world.gameOver()) {
            if(world.hasWumpus(world.getPlayerX(), world.getPlayerY())) {
            aiTableData[presAIRowIndex][0] = WUMPUSREWARD;
            } 
        }    
        
    }
    
    public void printmessage() {
        System.out.println(String.format("%s     %15s     %15s      %15s   %15s  %15s", "****ROW****", "****SCORE****", "****Arrow LEFT****", "****Arrow Right****", "****Arrow UP****", "****Arrow Down****"));
        
        for (int i = 0; i <= 15; i++) {
            System.out.println(String.format("****%s****%20f****%17f****   %15f****   %15f****%15f", i, aiTableData[i][0], aiTableData[i][1], aiTableData[i][2], aiTableData[i][3], aiTableData[i][4]));
        }
        
    }
}