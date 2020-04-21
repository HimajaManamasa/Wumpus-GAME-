package wumpusworld;

public class MyAgent implements Agent {

    int random;
    private World w;
    protected wumpusAI agent;
    private Placement currentPosition;

    public MyAgent(World world) {
        w = world;
        agent = new wumpusAI();
        currentPosition = new Placement(w.getPlayerX(), w.getPlayerY());
    }

    @Override
    public void train() {
        Placement tempPosition = currentPosition;
        String move = this.agent.getPlayerRandomMove();
        execute(move);
        this.agent.updateAITable(tempPosition, move);
    }

    /**
      Asks your solver agent to execute an action.
     */
    @Override
    //Assinging an action to any of the possibility found
    public void doAction() {
        String move = agent.getBestMove(currentPosition);
        execute(move);

        if (w.hasGlitter(w.getPlayerX(), w.getPlayerY())) {
            
            System.out.println("Agent wins!!");
            
            w.doAction(World.A_GRAB);
            agent = null;
            
        } else if (w.hasWumpus(w.getPlayerX(), w.getPlayerY())) {
            System.out.println("Found Wumpus!!");
            
        } else if (w.hasPit(w.getPlayerX(), w.getPlayerY()))  {
            System.out.println("pit!!");
        }
    }

    /* Generating a random move for the player
     */
    public int decideRandomMove() {
        return (int) (Math.random() * 4);
    }
    
    //Executing the agent moves.
    private void execute(String move) {

        Placement nxtPosition = agent.getNextPlayerPosition(move, currentPosition);
        
        //evaluating the position
        agent.checkpossibility(w, currentPosition);

        if (w.isValidPosition(nxtPosition.getX(), nxtPosition.getY())) {
            if(move == "UP") {
                if(w.getDirection() == World.DIR_LEFT) {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_MOVE);
                }
                else if(w.getDirection() == World.DIR_RIGHT) {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_MOVE);
                }
                else if(w.getDirection() == World.DIR_DOWN) {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_MOVE);
                }
                else {
                    w.doAction(World.A_MOVE);

                }
            }
            else if(move == "DOWN") {
                    if(w.getDirection() == World.DIR_LEFT) {
                        w.doAction(World.A_TURN_LEFT);
                        w.doAction(World.A_MOVE);
                    }
                    else if(w.getDirection() == World.DIR_RIGHT) {
                        w.doAction(World.A_TURN_RIGHT);
                        w.doAction(World.A_MOVE);
                    }
                    else if(w.getDirection() == World.DIR_UP) {
                        w.doAction(World.A_TURN_RIGHT);
                        w.doAction(World.A_TURN_RIGHT);
                        w.doAction(World.A_MOVE);
                            }
                    else {
                        w.doAction(World.A_MOVE);
                    }
            }
            else if(move == "LEFT") {
                if(w.getDirection() == World.DIR_UP) {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_MOVE);
                }
                else if(w.getDirection() == World.DIR_RIGHT) {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_MOVE);
                }
               else if(w.getDirection() == World.DIR_DOWN) {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_MOVE);
               }
               else {
                    w.doAction(World.A_MOVE);
                   
                }
            }
            else {
                if(w.getDirection() == World.DIR_LEFT) {
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_MOVE);
                }
                else if(w.getDirection() == World.DIR_UP) {
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_MOVE);
                }
            else if(w.getDirection() == World.DIR_DOWN) {
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_MOVE);
            }
            else 
                w.doAction(World.A_MOVE);

            }
            currentPosition = nxtPosition;
        } 

    }
    

    public void printQTable() {
        this.agent.printmessage();
    }
}