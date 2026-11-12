package interpreter.model.adt;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.*;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.*;
import interpreter.model.stmt.*;
import interpreter.model.type.*;
import interpreter.model.value.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StmtTest {

    @Test
    void testVarDeclStmt() throws MyException {
        PrgState state = PrgState.of(new NopStmt());
        IStmt stmt = new VarDeclStmt("v", new IntType());

        stmt.execute(state);

        assertTrue(state.getSymTable().isDefined("v"));
        assertEquals(new IntValue(0), state.getSymTable().lookup("v"));
    }

    @Test
    void testAssignStmt() throws MyException {
        PrgState state = PrgState.of(new NopStmt());
        state.getSymTable().put("v", new IntValue(0));

        IStmt stmt = new AssignStmt("v", new ValueExp(new IntValue(5)));
        stmt.execute(state);

        assertEquals(new IntValue(5), state.getSymTable().lookup("v"));
    }

    @Test
    void testPrintStmt() throws MyException {
        PrgState state = PrgState.of(new NopStmt());
        IStmt stmt = new PrintStmt(new ValueExp(new IntValue(10)));

        stmt.execute(state);

        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(10), state.getOut().get(0));
    }
        //int a; a=true; print(a);
    @Test
    void testspecial() {
        PrgState state = PrgState.of(new NopStmt());
        state.getSymTable().put("a", new IntValue(0));

        IStmt invalidAssign = new AssignStmt("a", new ValueExp(new interpreter.model.value.BoolValue(true)));

        try {
            invalidAssign.execute(state);

            org.junit.jupiter.api.Assertions.fail("eroare la eroare");

        } catch (interpreter.exceptions.MyException e) {
        }
    }
}