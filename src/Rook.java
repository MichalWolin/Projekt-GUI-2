public class Rook extends Piece{
    public Rook(Colour colour, int y, int x) {
        super(colour, y, x);
        this.setValue(3);
    }

    public String toString() {
        return getColour() == Colour.WHITE ? "♜" : "\u001B[30m♜";
    }

    @Override
    public boolean isMoveValid(int y, int x) {
        Piece piece = Board.getPiece(y, x);
        int srcX = this.getX();
        int srcY = this.getY();

        if(!Board.isPathClear(srcY, srcX, y, x)){
            return false;
        }

        if(piece != null){
            if(piece.getColour() == this.getColour()){
                return false;
            }
        }

        if(Math.abs(srcY - y) == 0 && Math.abs(srcX - x) != 0 ||
                Math.abs(srcY - y) != 0 && Math.abs(srcX - x) == 0){
            return true;
        }

        return false;
    }
}
