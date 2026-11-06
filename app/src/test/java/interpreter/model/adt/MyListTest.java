package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyListTest {
    private IList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new Mylist<>();
    }

    @Test
    void addAndGet() throws ADTException {
        assertEquals(0, list.size());
        list.add(100);
        list.add(200);

        assertEquals(2, list.size());
        assertEquals(100, list.get(0));
        assertEquals(200, list.get(1));
    }

    @Test
    void getOutOfBoundsThrowsException() {
        list.add(10);
        assertThrows(ADTException.class, () -> list.get(1));
        assertThrows(ADTException.class, () -> list.get(-1));
    }
}