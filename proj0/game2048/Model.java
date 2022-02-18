package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author TODO: FalKon1256
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.

        // Check for null input.
        if (side == null || board == null) {
            return false;
        }

        // Initialize trace parameters.
        int topMovablePos = size() - 1;
        int prevTileValue = 0;
        boolean merged = false;

        // Set for which direction the tile will move
        board.setViewingPerspective(side);

        /** Iterate over each column.
         *  Check the rules for each tile in the columns.
         *  Move the tile if the rules matches.
         */
        for (int col = 0; col < size(); col++) {
            for (int row = size() - 1; row >= 0; row--) {

                // Set t as the current tile.
                Tile t = board.tile(col, row);

                // Check if tile is not empty.
                if (t != null) {

                    // There's at least 1 tile with value above.
                    if (prevTileValue != 0) {
                        /** Value is same as the top movable position tile:
                         *  Initial state for merge must be false.
                         *  The tile must move.
                         *
                         *  1. Move the tile to top movable position.
                         *  2. Set changed state to true.
                         *  3. Check if the tile merges (or not) after this move.
                         *     If tile merges:
                         *     1. Update score.
                         *     2. Top movable position - 1.
                         */
                        if (t.value() == prevTileValue) {
                            merged = board.move(col, topMovablePos, t);
                            changed = true;
                            if (merged) {
                                score += board.tile(col, topMovablePos).value();
                                topMovablePos--;
                            }
                        } else {
                            /**
                             *  Value is different from the top movable position tile:
                             *  1. Record the tile value.
                             *  2. Check if the previous tile have merged.
                             *     If merged:
                             *     1. Top movable position - 1
                             *  3. Move the tile to top movable position.
                             *  4. Check if the tile moves.
                             *     If tile moves:
                             *     1. Set changed state to true
                             */
                            prevTileValue = t.value();
                            if (!merged) {
                                topMovablePos--;
                            }
                            merged = board.move(col, topMovablePos, t);
                            if (row != topMovablePos) {
                                changed = true;
                            }
                        }
                    } else {
                        /** There's no tile with value above.
                         *  All tiles above are empty or At the top tile of the column:
                         *  1. Record the tile value.
                         *  2. Check if top tile is empty
                         *     If top tile is empty:
                         *     1. Move the tile to top.
                         *     2. Set changed state to true
                         */
                        prevTileValue = t.value();
                        if (board.tile(col, topMovablePos) == null) {
                            merged = board.move(col, topMovablePos, t);
                            changed = true;
                        }
                    }
                }
            }

            // Reset trace parameters for next column.
            topMovablePos = size() - 1;
            prevTileValue = 0;
            merged = false;
        }

        // Set the observing direction back to north
        board.setViewingPerspective(Side.NORTH);

        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        // TODO: Fill in this function.

        // Check for null input.
        if (b == null) {
            return false;
        }
        /** Must implement Iterable & Iterator
         *  to create AllTileIterator class object.
         *  This will override at run-time and return Tile class object.
         *  Note: This will work as well.
         *      for (int col = 0; col < b.size(); col++) {
         *          for (int row = 0; row < b.size(); row++) {
         *              ...
         *          }
         *      }
         */
        for (Tile t : b) {
            if (t == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        // TODO: Fill in this function.

        // Check for null input.
        if (b == null) {
            return false;
        }

        /** Iterate over the 2D array.
         *  (Tile[][] attribute in the input variable).
         *  Must check first if tile is null.
         */
        for (Tile t : b) {
            if (t != null && t.value() == MAX_PIECE) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        // TODO: Fill in this function.

        // Check for null input.
        if (b == null) {
            return false;
        }

        /** From col0 to col2 & row0 to row2, check the rules as follows:
         *  1. Check for at least one empty space.
         *  2. Check for two adjacent tiles with the same value.
         */

        // Check rule 1 for start tile (0,0).
        if (b.tile(0, 0) == null) {
            return true;
        }

        for (int col = 0; col < b.size() - 1; col++) {
            for (int row = 0; row < b.size() - 1; row++) {
                // Check rule 1.
                if (b.tile(col + 1, row) == null || b.tile(col, row + 1) == null) {
                    return true;
                }
                // Check rule 2.
                if (b.tile(col, row).value() == b.tile(col + 1, row).value()
                    || b.tile(col, row).value() == b.tile(col, row + 1).value()) {
                    return true;
                }
            }
        }

        // Check rule 1 for the remaining unchecked tile (3,3).
        if (b.tile(3, 3) == null) {
            return true;
        }

        // Check last col & row for the rule 2.
        for (int i = 0; i < b.size() - 1; i++) {
            if (b.tile(3, i).value() == b.tile(3, i + 1).value()) {
                return true;
            }
            if (b.tile(i, 3).value() == b.tile(i + 1, 3).value()) {
                return true;
            }
        }
        return false;
    }


    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
