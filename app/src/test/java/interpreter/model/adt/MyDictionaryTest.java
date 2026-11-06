package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyDictionaryTest {
    private IDictionary<String, Integer> dictionary;

    @BeforeEach
    void setUp() {
        dictionary = new MyDictionary<>();
    }

    @Test
    void putAndLookup() throws ADTException {
        assertFalse(dictionary.isDefined("a"));
        dictionary.put("a", 5);
        assertTrue(dictionary.isDefined("a"));
        assertEquals(5, dictionary.lookup("a"));
    }

    @Test
    void lookupUndefinedKeyThrowsException() {
        assertThrows(ADTException.class, () -> dictionary.lookup("x"));
    }

    @Test
    void updateExistingKey() throws ADTException {
        dictionary.put("b", 10);
        dictionary.update("b", 20);
        assertEquals(20, dictionary.lookup("b"));
    }

    @Test
    void updateUndefinedKeyThrowsException() {
        assertThrows(ADTException.class, () -> dictionary.update("c", 30));
    }
}