import java.util.Scanner;

public abstract class Piece implements UnicodePiece, Movable{
    private Colour colour;
    private int y, x;
    private boolean firstMove;
    private int value;

    public Piece(Colour colour, int y, int x) {
        this.colour = colour;
        this.y = y;
        this.x = x;
        this.firstMove = true;
        Board.setPiecePos(this);
    }

    public Colour getColour() {
        return colour;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public boolean isFirstMove() {
        return firstMove;
    }

    public void setValue(int value){
        this.value = value;
    }

    public boolean isMoveValidAndNotChecked(int destY, int destX){
        if(!isMoveValid(destY, destX)){
            return false;
        }

        int originalY = this.getY();
        int originalX = this.getX();
        Piece piece = Board.getPiece(destY, destX);
        this.setY(destY);
        this.setX(destX);
        Board.setPiecePos(this);

        int[] kingPosition = Board.findKing(this.getColour());
        if(!Board.isChecked(kingPosition)){
            revertMove(piece, destY, destX, originalY, originalX);

            return true;
        }
        revertMove(piece, destY, destX, originalY, originalX);

        return false;
    }

    public void revertMove(Piece piece, int destY, int destX, int originalY, int originalX){
        if(piece != null){
            piece.setY(destY);
            piece.setX(destX);
            Board.setPiecePos(piece);
        }else{
            Board.tileSetNull(destY, destX);
        }
        this.setY(originalY);
        this.setX(originalX);
        Board.setPiecePos(this);
    }

    public void movePiece(int destY, int destX){
        int sourceY = this.getY();
        int sourceX = this.getX();

        Piece other = Board.getPiece(destY, destX);
        if(other != null){
            Board.capturePiece(other);
            Board.tileClear(destY, destX);
        }
        this.setY(destY);
        this.setX(destX);
        Board.setPiecePos(this);
        this.setFirstMove(false);
        Board.tileSetNull(sourceY, sourceX);
    }

    public void setFirstMove(boolean isFirst){
        this.firstMove = isFirst;
    }

    public int getPieceTypeValue(){
        return this.value;
    }
}
