import javafx.scene.control.Button;

public class Minimax implements MoveController {

    static int MAX_VAL = 64;
    static int MIN_VAL = -64;

    private final OutputFrameController controller;


    public Minimax(OutputFrameController controller) {
        this.controller = controller;
    }

    @Override
    public int[] move() {
        return new int[2];
    }

    private static class Board {
        String[][] states; // can be optimized by turning it into a String of 64 char length

        public Board(String[][] states) {
            this.states = states; // be careful of parameter input that doesn't comply with 8x8 rule
        }

        public Board(Button[][] states) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    this.states[i][j] = states[i][j].getText();
                }
            }
        }

        public Board() {
            this.states = new String[8][8];
        }

        public int getFunctionValue() {
            int countX = 0, countO = 0;

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (states[i][j].equals("X")) {
                        countX++;
                    } else if (states[i][j].equals("O")){ // me, as a bot
                        countO++;
                    }
                }
            }

            return countO - countX;
        }
    }
}
