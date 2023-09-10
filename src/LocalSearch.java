public class LocalSearch implements MoveController {

    private OutputFrameController controller;

    public LocalSearch(OutputFrameController controller) {
        this.controller = controller;
    }

    @Override
    public int[] move() {
        return new int[0];
    }
}
