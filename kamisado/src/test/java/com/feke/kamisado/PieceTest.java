package com.feke.kamisado;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Piece class.
 */
public class PieceTest {

    private Piece blackPiece;
    private Piece whitePiece;

    @BeforeEach
    public void setUp() {
        blackPiece = new Piece(TeamEnum.BLACK, ColorEnum.RED);
        whitePiece = new Piece(TeamEnum.WHITE, ColorEnum.BLUE);
    }

    @Test
    public void testConstructorAndGetters() {
        assertEquals(TeamEnum.BLACK, blackPiece.getTeam());
        assertEquals(ColorEnum.RED, blackPiece.getColor());
        assertEquals(0, blackPiece.getDragonTeeth());

        assertEquals(TeamEnum.WHITE, whitePiece.getTeam());
        assertEquals(ColorEnum.BLUE, whitePiece.getColor());
        assertEquals(0, whitePiece.getDragonTeeth());
    }

    @Test
    public void testMovementLength() {
        assertEquals(7, blackPiece.getMovementLength());
        blackPiece.increaseDragonTeeth();
        assertEquals(5, blackPiece.getMovementLength());
        blackPiece.increaseDragonTeeth();
        assertEquals(3, blackPiece.getMovementLength());
        blackPiece.increaseDragonTeeth();
        assertEquals(1, blackPiece.getMovementLength());
    }

    @Test
    public void testIncreaseDragonTeeth() {
        assertEquals(0, blackPiece.getDragonTeeth());

        blackPiece.increaseDragonTeeth();
        assertEquals(1, blackPiece.getDragonTeeth());

        blackPiece.increaseDragonTeeth();
        assertEquals(2, blackPiece.getDragonTeeth());

        blackPiece.increaseDragonTeeth();
        assertEquals(3, blackPiece.getDragonTeeth());

        // Should not increase beyond 3
        blackPiece.increaseDragonTeeth();
        assertEquals(3, blackPiece.getDragonTeeth());
    }

    @Test
    public void testCopyConstructor() {
        Piece copiedPiece = new Piece(blackPiece);
        assertEquals(blackPiece.getTeam(), copiedPiece.getTeam());
        assertEquals(blackPiece.getColor(), copiedPiece.getColor());
        assertEquals(blackPiece.getDragonTeeth(), copiedPiece.getDragonTeeth());
    }
}
