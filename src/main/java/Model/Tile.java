package Model;

public class Tile {
    private TileState state;
    private boolean isBomb;
    private int nNeighbourBombs;

    public Tile(boolean isBomb) {
        this.isBomb = isBomb;
        state = TileState.HIDDEN;
        nNeighbourBombs = -1;
    }

    public void setNeighbourBombs(int n) {
        if (nNeighbourBombs != -1) {
            throw new IllegalStateException("Neighbour bombs have already been initialized!");
        }
        if (n >= 0 && n <= 8) {
            nNeighbourBombs = n;
        } else {
            throw new IllegalArgumentException("The input for bombs must be between 0 and 8!");
        }
    }
    public boolean getIsBomb() {
        return isBomb;
    }
    public int getNeighbourBombs() {
        if (nNeighbourBombs != -1) {
            return nNeighbourBombs;
        }
        throw new IllegalStateException("The neighbour bombs have not been set yet!");
    }
    public void show() {
        if (state.equals(TileState.HIDDEN)) {
            state = TileState.SHOWN;
        }
    }

    public boolean flag() {
        if (!state.equals(TileState.SHOWN)) {
            state = state.equals(TileState.FLAGGED) ? TileState.HIDDEN : TileState.FLAGGED;
        }
        return state.equals(TileState.FLAGGED);
    }
    public boolean isRevealed() {
        return state.equals(TileState.SHOWN);
    }

    public TileState getState() {
        return state;
    }

    @Override
    public String toString() {
        if (state.equals(TileState.HIDDEN)) {
            return "-";
        } else if (state.equals(TileState.FLAGGED)) {
            return "!";
        } else if (isBomb) {
            return "X";
        } else {
            return Integer.toString(nNeighbourBombs);
        }
    }
}
