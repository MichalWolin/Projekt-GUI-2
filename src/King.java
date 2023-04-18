public class King extends Piece{
    public King(Colour colour, int y, int x) {
        super(colour, y, x);
    }

    public String toString() {
        return getColour() == Colour.WHITE ? "♔" : "♚";
    }

    @Override
    public boolean isMoveValid(int destY, int destX) {
        Piece piece = Board.getPiece(destY, destX);
        int srcX = this.getX();
        int srcY = this.getY();

        if(!Board.isPathClear(srcY, srcX, destY, destX)){
            return false;
        }

        if(piece != null){
            if(piece.getColour() == this.getColour()){
                return false;
            }
        }
        //TODO trzeba to usunac i dac do castlingu imo, usunac ismovevalid and not checked i zastapic sztucznym ischecked
//        if(Math.abs(srcY - destY) == 0 && Math.abs(srcX - destX) == 2 || Math.abs(srcX - destX) == 1){
//            return true;
//        }

        if(Math.abs(srcY - destY) == 1 && Math.abs(srcX - destX) == 1 ||
                Math.abs(srcY - destY) == 1 && Math.abs(srcX - destX) == 0 ||
                Math.abs(srcY - destY) == 0 && Math.abs(srcX - destY) == 1){
            return true;
        }

        return false;
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

        int[] possibleKingPosition = {destY, destX};
        if(!Board.isChecked(possibleKingPosition)){
            revertMove(piece, destY, destX, originalY, originalX);

            return true;
        }
        revertMove(piece, destY, destX, originalY, originalX);

        return false;
    }

    public static boolean isCastling(Piece piece, King king){
        int srcY = king.getY();
        int srcX = king.getX();
        int destY = piece.getY();
        int destX = piece.getX();

        if(srcY != destY){
            return false;
        }

        if(!king.isFirstMove() || !piece.isFirstMove()){
            return false;
        }

        if(srcX == 5 && destX == 8){
            if(piece instanceof Rook){
                king.setX(6);
                Board.setPiecePos(king);

                if(!Board.isChecked(new int[]{destY, 6})){
                    king.revertMove(null, destY, 6, srcY, srcX);
                }else{
                    king.revertMove(null, destY, 6, srcY, srcX);
                    return false;
                }

                king.setX(7);
                Board.setPiecePos(king);

                if(!Board.isChecked(new int[]{destY, 7})){
                    king.revertMove(null, destY, 7, srcY, srcX);
                    return true;
                }else{
                    king.revertMove(null, destY, 7, srcY, srcX);
                    return false;
                }
            }
        }else if(srcX == 5 && destX == 1){
            Piece probablyRook = Board.getPiece(srcY, 1);
            if(probablyRook instanceof Rook){
                king.setX(4);
                Board.setPiecePos(king);

                if(!Board.isChecked(new int[]{destY, 4})){
                    king.revertMove(null, destY, 4, srcY, srcX);
                }else{
                    king.revertMove(null, destY, 4, srcY, srcX);
                    return false;
                }

                king.setX(3);
                Board.setPiecePos(king);

                if(!Board.isChecked(new int[]{destY, 3})){
                    king.revertMove(null, destY, 3, srcY, srcX);
                    return true;
                }else{
                    king.revertMove(null, destY, 3, srcY, srcX);
                    return false;
                }
            }
        }

        return false;
    }

    public static void castlingMove(Piece piece, King king){
        int srcY = king.getY();
        int srcX = king.getX();
        int destY = piece.getY();
        int destX = piece.getX();

        Board.tileSetNull(srcY, srcX);
        Board.tileSetNull(destY, destX);

        if(srcX == 5 && destX == 8) {
            king.setX(7);
            piece.setX(6);
            Board.setPiecePos(king);
            Board.setPiecePos(piece);
        }else if(srcX == 5 && destX == 1){
            king.setX(3);
            piece.setX(4);
            Board.setPiecePos(king);
            Board.setPiecePos(piece);
        }
    }
}
