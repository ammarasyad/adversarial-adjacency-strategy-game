public class Bot {

    private final MoveController moveController;

    public Bot(OutputFrameController controller) {
        // edit this line to change the algorithm
        moveController = new LocalSearch(controller);
        //  moveController = new Minimax(controller);
    }

    // Move using Local Search Algorithm
    public int[] move() {
        return moveController.move();
    }
}
