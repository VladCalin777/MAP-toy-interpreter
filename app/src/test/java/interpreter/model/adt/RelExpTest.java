package interpreter.model.adt;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.exp.IExp;
import interpreter.model.exp.RelExp;
import interpreter.model.exp.RelOp;
import interpreter.model.exp.ValueExp;
import interpreter.model.type.StringType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.IValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RelExpTest {

    @Test
    void testStringDefaultAndEquality() {
        assertEquals(new StringValue(""), new StringType().defaultValue());

        StringValue s1 = new StringValue("test.in");
        StringValue s2 = new StringValue("test.in");
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testRelationalOperators() throws MyException {
        IDictionary<String, IValue> sym = new MyDictionary<>();
        IExp five = new ValueExp(new IntValue(5));
        IExp seven = new ValueExp(new IntValue(7));

        assertEquals(new BoolValue(true), new RelExp(RelOp.LT, five, seven).eval(sym));
        assertEquals(new BoolValue(false), new RelExp(RelOp.LT, seven, five).eval(sym));
        assertEquals(new BoolValue(true), new RelExp(RelOp.LE, five, five).eval(sym));
        assertEquals(new BoolValue(true), new RelExp(RelOp.EQ, five, five).eval(sym));
        assertEquals(new BoolValue(false), new RelExp(RelOp.EQ, five, seven).eval(sym));
        assertEquals(new BoolValue(true), new RelExp(RelOp.NE, five, seven).eval(sym));
        assertEquals(new BoolValue(true), new RelExp(RelOp.GT, seven, five).eval(sym));
        assertEquals(new BoolValue(true), new RelExp(RelOp.GE, seven, seven).eval(sym));
    }

    @Test
    void testNonIntegerOperandThrows() {
        IDictionary<String, IValue> sym = new MyDictionary<>();
        IExp str = new ValueExp(new StringValue("abc"));
        IExp num = new ValueExp(new IntValue(5));

        assertThrows(ExpressionException.class, () -> new RelExp(RelOp.LT, str, num).eval(sym));
        assertThrows(ExpressionException.class, () -> new RelExp(RelOp.EQ, num, str).eval(sym));
    }
}