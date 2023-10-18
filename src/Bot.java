public class Bot {

    private final MoveController moveController;

    public Bot(OutputFrameController controller, String mode) {
        // edit this line to change the algorithm

        moveController = switch (mode) {
            case "Minimax Bot" -> new Minimax(controller, false);
            case "Local Search Bot" -> new LocalSearch(controller, false);
            case "Genetic Search Bot" -> new LocalSearch(controller, false); // add genetic bot here

            default -> new LocalSearch(controller, false);

        };
    }

    // Move using Local Search Algorithm
    public int[] move() {
        return moveController.move();
    }
}
