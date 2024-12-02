package com.feke.kamisado;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class MapTest
{
    Map map = new Map(true);
    
    @Test
    public void sanityCheck()
    {
        assertNotNull(map.getMap());
    }

    @Test
    public void testValidMovement() {
        Coordinate curruntCoordinate = new Coordinate(0, 7);
        map.selectTile(curruntCoordinate);
        assertTrue(map.movePiece(curruntCoordinate, new Coordinate(0, 1)));
    }

    @Test
    public void testInvalidMovement() {
        Coordinate curruntCoordinate = new Coordinate(0, 7);
        map.selectTile(curruntCoordinate);
        assertFalse(map.movePiece(curruntCoordinate, new Coordinate(1, 1)));
    }
}
