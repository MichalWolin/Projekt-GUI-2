public class Pawn extends Piece {
    public Pawn(Colour colour, int y, int x) {
        super(colour, y, x);
    }

    public String toString() {
        return getColour() == Colour.WHITE ? "♙" : "♟";
    }

    @Override
    public boolean isMoveValid(int y, int x){
        Piece piece = Board.getPiece(y, x);
        int srcX = this.getX();
        int srcY = this.getY();

        if(this.getColour() == Colour.WHITE){
            if(srcY - y == -1 && srcX - x == 0 && piece == null){
                return true;
            }

            if(this.isFirstMove() && srcY - y == -2 && srcX - x == 0 && piece == null
                    && Board.isPathClear(srcY, srcX, y, x)){
                return true;
            }

            if(srcY - y == -1 && Math.abs(srcX - x) == 1 && piece != null && piece.getColour() != this.getColour()){
                return true;
            }
        }

        if(this.getColour() == Colour.BLACK){
            if(srcY - y == 1 && srcX - x == 0 && piece == null){
                return true;
            }

            if(this.isFirstMove() && srcY - y == 2 && srcX - x == 0 && piece == null
                                    && Board.isPathClear(srcY, srcX, y, x)){
                return true;
            }

            if(srcY - y == 1 && Math.abs(srcX - x) == 1 && piece != null && piece.getColour() != this.getColour()){
                return true;
            }

        }

        return false;
    }
}
