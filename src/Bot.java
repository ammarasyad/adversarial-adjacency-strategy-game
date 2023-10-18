public class Bot {

    private final MoveController moveController;

    public Bot(OutputFrameController controller, String mode) {
        // edit this line to change the algorithm
        MoveController selectedController;
        if (mode.equals("Minimax Bot")) {
            selectedController = new Minimax(controller, false);
        }
        else if (mode.equals("Local Search Bot")) {
            selectedController = new LocalSearch(controller);
        } else if (mode.equals("Genetic Search Bot")) {
            selectedController = new LocalSearch(controller); // add genetic bot here
        } else { // else dont assign, since it will ultimately be prevented by moveBot()
            selectedController = new LocalSearch(controller); // whatever fill as to make the compilation wont go error
        }

        moveController = selectedController;
    }

    // Move using Local Search Algorithm
    public int[] move() {
        return moveController.move();
    }
}
