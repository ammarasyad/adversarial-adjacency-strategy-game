import javafx.scene.control.Button;

public class Bot {

    private OutputFrameController controller;

    public Bot(OutputFrameController controller) {
        this.controller = controller;
    }

    // Move using Local Search Algorithm
    public int[] move() {
        Pair<Integer, Integer> move = new Pair<>(0, 0);
        Button[][] board = controller.getButtons();

        return new int[]{move.getFirst(), move.getSecond()};
    }

    static final class Pair<A, B> {
        A a;
        B b;

        public Pair(A a, B b) {
            this.a = a;
            this.b = b;
        }

        public A getFirst() {
            return a;
        }

        public B getSecond() {
            return b;
        }

        public void setFirst(A value) {
            a = value;
        }

        public void setSecond(B value) {
            b = value;
        }
    }
}
