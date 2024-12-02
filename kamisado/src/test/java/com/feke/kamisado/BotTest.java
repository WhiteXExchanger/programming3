package com.feke.kamisado;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

public class BotTest
{
    Bot bot = new Bot();
    
    @Test
    public void sanityCheck()
    {
        Map map = new Map(true);
        assertNotNull(
            bot.getMovement(new Coordinate(0, 0), map.getMap())
        );
    }
}
