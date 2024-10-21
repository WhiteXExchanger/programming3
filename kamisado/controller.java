package kamisado;

class Controller {
    Board board;
    View view;
    Position from = null;
    int[] points = {0, 0};
    boolean isFirstMove = true;
    boolean isBottomBlack;

    Controller() {
        view = new View(this);
        ViewEnum viewEnum = ViewEnum.gameView; // getting from outside

        switch (viewEnum) {
            case gameView -> {
                isBottomBlack = true; // getting from outside
                startGame(isBottomBlack); // if this is false -> ai is present
                updateGame();
            }
        
            case menuView -> loadMenu();
        }
    }

    void touchTile(Position to) {
        if (from == null) {
            from = to;
            board.flagTiles(to);
        } else if (board.isSameTeam(from, to) && isFirstMove) {
            board.unflagBoard();
            from = to;
            board.flagTiles(from);
        } 
        else {
            if (!board.movePiece(from, to)) return;
            if(whoWon(board.isGameOver(to))) {
                startGame(isBottomBlack);
            }
            board.changeActivePlayer();
            isFirstMove = false;
            from = board.getNextPiecePosition(to);
            while (!board.flagTiles(from)) {
                board.changeActivePlayer();
                from = board.getNextPiecePosition(from);
                board.flagTiles(from);
            }
        }
        updateGame();
    }

    private boolean whoWon(int gameOver) {
        if (gameOver == 1) {
            System.out.println("Black won");
            points[0]++;
            return true;
        } else if (gameOver == -1) {
            System.out.println("White won");
            points[1]++;
            return true;
        }
        return false;
    }

    private void loadMenu() {
        view.renderMenu();
    }

    void startGame(boolean isBottomBlack) {
        from = null;
        board = new Board(isBottomBlack);
    }

    void updateGame() {
        view.renderGame(board.getTileMatrix());
    }
}
