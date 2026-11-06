package interpreter.model.adt;

import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.exp.*;
import interpreter.model.value.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExpTest {

    @Test
    void testValueExp() throws MyException {
        IExp exp = new ValueExp(new IntValue(5));
        IDictionary<String, IValue> symTable = new MyDictionary<>();

        assertEquals(new IntValue(5), exp.eval(symTable));
    }

    @Test
    void testVarExp() throws MyException {
        IDictionary<String, IValue> symTable = new MyDictionary<>();
        symTable.put("x", new IntValue(10));

        IExp exp = new VarExp("x");
        assertEquals(new IntValue(10), exp.eval(symTable));
    }

    @Test
    void testArithExp() throws MyException {
        // 2 + 3 = 5
        IExp exp = new ArithExp(ArithOP.ADD, new ValueExp(new IntValue(2)), new ValueExp(new IntValue(3)));
        IDictionary<String, IValue> symTable = new MyDictionary<>();

        assertEquals(new IntValue(5), exp.eval(symTable));
    }
}