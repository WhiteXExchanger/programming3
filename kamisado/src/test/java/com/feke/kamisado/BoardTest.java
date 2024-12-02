package com.feke.kamisado;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

public class BoardTest
{
    Board board = new Board(true);

    @Test
    public void sanityCheck()
    {
        assertNull(board.getSelected());
    }

    @Test
    public void testPoints() {
        int[] p = {0,0};
        for (int i = 0; i < p.length; i++) {
            assertEquals(p[i], board.getPoints()[i]);
        }
    }
}
