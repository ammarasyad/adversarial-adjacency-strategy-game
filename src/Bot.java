public class Bot {

    private final MoveController moveController;

    public Bot(OutputFrameController controller, String mode, boolean isPlayerX) {
        // edit this line to change the algorithm

        moveController = switch (mode) {
            case "Minimax Bot" -> new Minimax(controller, isPlayerX);
            case "Local Search Bot" -> new LocalSearch(controller, isPlayerX);
            case "Genetic Search Bot" -> new GeneticAlgorithm(controller, isPlayerX); // add genetic bot here

            default -> new LocalSearch(controller, isPlayerX);

        };
    }

    // Move using Local Search Algorithm
    public int[] move() {
        return moveController.move();
    }
}
