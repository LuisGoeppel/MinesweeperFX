package Model;

public class Field {
    private Tile[][] field;
    private FieldState state;
    int nRevealed;
    private int width;
    private int height;
    private int nBombs;
    private boolean isInitialized;

    public Field(int width, int height, int nBombs) {
        this.width = width;
        this.height = height;
        this.nBombs = nBombs;
        nRevealed = 0;

        state = FieldState.IN_PROGRESS;
        isInitialized = false;

        if (nBombs > width * height) {
            throw new IllegalArgumentException("There are too many bombs!");
        }
        field = new Tile[width][height];
    }

    private void setBombs(int xNot, int yNot) {
        int bombsRemaining = nBombs;
        int fieldsRemaining = width * height;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (i == xNot && j == yNot && fieldsRemaining > bombsRemaining) {
                    field[i][j] = new Tile(false);
                } else {
                    double chance = (double)bombsRemaining / (double)fieldsRemaining;
                    double randomNumber = Math.random();
                    if (randomNumber <= chance) {
                        field[i][j] = new Tile(true);
                        bombsRemaining--;
                    } else {
                        field[i][j] = new Tile(false);
                    }
                }
                fieldsRemaining--;
            }
        }
    }

    private void setNeighbourBombs() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int nNeighbourBombs = 0;
                int left = Math.max(0, i - 1);
                int right = Math.min(width - 1, i + 1);
                int top = Math.max(0, j - 1);
                int bottom = Math.min(height - 1, j + 1);

                for (int x = left; x <= right; x++) {
                    for (int y = top; y <= bottom; y++) {
                        if (x != i || y != j) {
                            if (field[x][y].getIsBomb()) {
                                nNeighbourBombs++;
                            }
                        }
                    }
                }

                field[i][j].setNeighbourBombs(nNeighbourBombs);
            }
        }
    }

    public TileState getStateAt(int x, int y) {
        return field[x][y].getState();
    }

    public boolean isWon() {
        return state.equals(FieldState.WON);
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public Tile getTileAt(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException("This Tile does not exist: " + x + ", " + y);
        }
        return field[x][y];
    }

    public boolean reveal(int x, int y) {

        if (x < 0 || y < 0 || x >= width || y >= height) {
            throw new IllegalArgumentException("Enter a valid width and height!");
        }
        if (!state.equals(FieldState.IN_PROGRESS)) {
            throw new IllegalStateException("The game is not in progress!");
        }
        if (!isInitialized) {
            initialize(x, y);
        }
        if (field[x][y].isRevealed()) {
            throw new IllegalStateException("This tile is already revealed!");
        }
        if (!field[x][y].getState().equals(TileState.FLAGGED)) {
            field[x][y].show();
            nRevealed++;

            if (field[x][y].getIsBomb()) {
                //game is Lost
                state = FieldState.LOST;
                return false;
            }
            if (nRevealed == width * height - nBombs) {
                //game is won;
                state = FieldState.WON;
                return true;
            }
            if (field[x][y].getNeighbourBombs() == 0) {

                int left = Math.max(0, x - 1);
                int right = Math.min(width - 1, x + 1);
                int top = Math.max(0, y - 1);
                int bottom = Math.min(height - 1, y + 1);

                for (int i = left; i <= right; i++) {
                    for (int j = top; j <= bottom; j++) {
                        if (x != i || y != j) {
                            if (!field[i][j].isRevealed()) {
                                reveal(i, j);
                            }
                        }
                    }
                }

            }
            return true;
        }
        return true;
    }

    public boolean flag (int x, int y) {
        return field[x][y].flag();
    }

    public void initialize(int xNot, int yNot) {
        if (!isInitialized) {
            isInitialized = true;
            setBombs(xNot, yNot);
            setNeighbourBombs();
        }
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                stringBuilder.append(field[i][j]).append(" ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
