public class Knight extends Piece{
    public Knight(Colour colour, int y, int x) {
        super(colour, y, x);
    }

    public String toString(){return Colour.WHITE == this.getColour() ? "♞" : "\u001B[30m♞";}

    @Override
    public boolean isMoveValid(int y, int x) {
        Piece piece = Board.getPiece(y, x);
        int srcX = this.getX();
        int srcY = this.getY();

        if(piece != null){
            if(piece.getColour() == this.getColour()){
                return false;
            }
        }

        if(Math.abs(srcY - y) == 2 && Math.abs(srcX - x) == 1 ||
                Math.abs(srcY - y) == 1 && Math.abs(srcX - x) == 2){
            return true;
        }

        return false;
    }
}
