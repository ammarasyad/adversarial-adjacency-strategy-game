import javafx.scene.control.Button;

public class LocalSearch implements MoveController {

    private final OutputFrameController controller;

    private MovePair pair;

    private final String player;
    private final String opponent;


    public LocalSearch(OutputFrameController controller, boolean isPlayerX) {
        this.controller = controller;
        this.player = isPlayerX ? "X" : "O";
        this.opponent = isPlayerX ? "O" : "X";
    }

    // Move using Local Search Algorithm
    @Override
    public int[] move() {
        // set initial state
        Button[][] board = controller.getButtons();

        if (pair == null) {
            pair = new MovePair((int) (Math.random() * 8), (int) (Math.random() * 8));
        }

        final MovePair[] neighbors = new MovePair[4]; // 0 = left, 1 = right, 2 = up, 3 = down

        // check for surrounding filled squares
        int surrounding = getSurrounding(pair, neighbors, board);

        MovePair temp = switch (surrounding) {
            case 1, 2, 3 -> new MovePair(heuristicCheck(neighbors, surrounding, board));
            default -> new MovePair(pair.a, pair.b);
        };

        // prevent object reference from changing
        pair.a = temp.a;
        pair.b = temp.b;

        // check if the square is filled
        while (board[pair.a][pair.b].getText().equals(player) || board[pair.a][pair.b].getText().equals(opponent)) {
            // if the square is filled, generate a new random square regardless of surrounding filled squares
            pair.a = (int) (Math.random() * 8);
            pair.b = (int) (Math.random() * 8);
        }

        return new int[]{ pair.a, pair.b };
    }

    // meta-heuristic algorithm
    private int[] heuristicCheck(MovePair[] neighbors, int surrounding, Button[][] board) {
        MovePair point = new MovePair(pair.a, pair.b);

        for (MovePair neighbor : neighbors) {
            if (neighbor != null) {
                point = getEmptySquare(neighbor, surrounding, board);
            }
        }

        return new int[]{ point.a, point.b };
    }

    private MovePair getEmptySquare(MovePair neighbor, int surrounding, Button[][] board) {
        int highestSurrounding = -1;
        MovePair point = new MovePair(pair.a, pair.b);

        for (int i = neighbor.a - 1; i <= neighbor.b; i += 2) {
            for (int j = neighbor.b - 1; j <= neighbor.b; j += 2) {
                if (i >= 0 && i < 8 && j >= 0 && j < 8) {
                    // get the highest surrounding heuristic
                    MovePair localPoint = new MovePair(i, j);
                    int localSurrounding = getSurrounding(localPoint, new MovePair[4] /* ignore */, board);
                    if (highestSurrounding < localSurrounding) {
                        highestSurrounding = localSurrounding;
                        point = localPoint;
                    }
                }
            }
        }

        if (highestSurrounding > surrounding) {
            return point;
        } else {
            return new MovePair(pair.a, pair.b);
        }
    }

    private int getSurrounding(MovePair point, MovePair[] neighbors, Button[][] board) {
        // check if the surrounding squares are filled by other players
        int surrounding = 0;

        if (point.a > 0 && board[point.a - 1][point.b].getText().equals(opponent)) {
            neighbors[2] = new MovePair(point.a - 1, point.b);
            surrounding++;
        }

        if (point.a < 7 && board[point.a + 1][point.b].getText().equals(opponent)) {
            neighbors[3] = new MovePair(point.a + 1, point.b);
            surrounding++;
        }

        if (point.b > 0 && board[point.a][point.b - 1].getText().equals(opponent)) {
            neighbors[0] = new MovePair(point.a, point.b - 1);
            surrounding++;
        }

        if (point.b < 7 && board[point.a][point.b + 1].getText().equals(opponent)) {
            neighbors[1] = new MovePair(point.a, point.b + 1);
            surrounding++;
        }

        return surrounding;
    }

    private static class MovePair {
        int a;
        int b;

        public MovePair(int[] array) {
            this(array[0], array[1]);
        }

        public MovePair(int a, int b) {
            this.a = a;
            this.b = b;
        }
    }
}
