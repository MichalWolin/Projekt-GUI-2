import java.util.Scanner;

public abstract class Piece implements UnicodePiece, Movable{
    private Colour colour;
    private int y, x;
    private boolean firstMove;

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

            return true;
        }
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

        return false;
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

        if(this instanceof Pawn && (destY == 1 || destY == 8)){
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
            }
            System.out.println("Piece promoted!\n");
        }
    }

    public void setFirstMove(boolean isFirst){
        this.firstMove = isFirst;
    }

}
