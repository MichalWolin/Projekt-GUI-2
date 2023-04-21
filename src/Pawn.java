import javax.swing.*;
import java.util.Scanner;

public class Pawn extends Piece{
    private boolean enPassantable;
    public Pawn(Colour colour, int y, int x) {
        super(colour, y, x);
        this.enPassantable = false;
        this.setValue(0);
    }

    public String toString() {
        return getColour() == Colour.WHITE ? "♟" : "\u001B[30m♟";
    }

    public boolean isEnPassantable(){
        return enPassantable;
    }

    public void setEnPassantable(boolean isEnPassantable){
        enPassantable = isEnPassantable;
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
                this.setEnPassantable(true);
                return true;
            }

            if(srcY - y == -1 && Math.abs(srcX - x) == 1 && piece != null && piece.getColour() != this.getColour()){
                return true;
            }

            if(srcY - y == -1 && Math.abs(srcX - x) == 1 && piece == null){
                return isEnPassant(y, x);
            }
        }

        if(this.getColour() == Colour.BLACK){
            if(srcY - y == 1 && srcX - x == 0 && piece == null){
                return true;
            }

            if(this.isFirstMove() && srcY - y == 2 && srcX - x == 0 && piece == null
                                    && Board.isPathClear(srcY, srcX, y, x)){
                this.setEnPassantable(true);
                return true;
            }

            if(srcY - y == 1 && Math.abs(srcX - x) == 1 && piece != null && piece.getColour() != this.getColour()){
                return true;
            }

            if(srcY - y == 1 && Math.abs(srcX - x) == 1 && piece == null){
                return isEnPassant(y, x);
            }
        }

        return false;
    }

    public void movePiece(int destY, int destX){
        int sourceY = this.getY();
        int sourceX = this.getX();

        if(isEnPassant(destY, destX)){
            Pawn enemyPawn;
            if(this.getColour() == Colour.WHITE){
                enemyPawn = (Pawn)Board.getPiece(destY - 1, destX);
            }else{
                enemyPawn = (Pawn)Board.getPiece(destY + 1, destX);
            }

            Board.capturePiece(enemyPawn);
            Board.tileSetNull(enemyPawn.getY(), enemyPawn.getX());
            this.setY(destY);
            this.setX(destX);
            Board.setPiecePos(this);
            this.setFirstMove(false);
            Board.tileSetNull(sourceY, sourceX);
        }else{
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

            if(destY == 1 || destY == 8){
                Scanner scanner = new Scanner(System.in);
                System.out.println("Choose a piece to promote to! (rook, knight, bishop, queen): ");
                boolean inputValid = false;
                while(!inputValid){
                    String input = scanner.nextLine();
                    inputValid = true;
                    switch(input){
                        case "rook" -> {
                            new Rook(this.getColour(), destY, destX);
                        }
                        case "knight" -> {
                            new Knight(this.getColour(), destY, destX);
                        }
                        case "bishop" -> {
                            new Bishop(this.getColour(), destY, destX);
                        }
                        case "queen" -> {
                            new Queen(this.getColour(), destY, destX);
                        }
                        default -> {
                            System.out.println("Invalid input, please try again.");
                            inputValid = false;
                        }
                    }
                    Board.capturePiece(this);
                }
                System.out.println("Piece promoted!\n");
            }
        }


    }

    public boolean isEnPassant(int destY, int destX){
        Piece probablyPawn;
        if(this.getColour() == Colour.WHITE){
            probablyPawn = Board.getPiece(destY - 1, destX);
        }else{
            probablyPawn = Board.getPiece(destY + 1, destX);
        }

        if(!(probablyPawn instanceof Pawn)){
            return false;
        }
        Pawn pawn = (Pawn)probablyPawn;

        return pawn.isEnPassantable();
    }
}
