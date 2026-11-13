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
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class StmtTest {
    @TempDir
    Path dir;

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
    //int a; int b; bool c; a=1; b=2; c=a!=b; print(c);
    @Test
    void testRelationalNotEqualExp() throws Exception {
        IStmt program = new CompStmt(new VarDeclStmt("a", new IntType()),
                new CompStmt(new VarDeclStmt("b", new IntType()),
                        new CompStmt(new VarDeclStmt("c", new BoolType()),
                                new CompStmt(new AssignStmt("a", new ValueExp(new IntValue(1))),
                                        new CompStmt(new AssignStmt("b", new ValueExp(new IntValue(2))),
                                                new CompStmt(new AssignStmt("c", new RelExp(RelOp.NE, new VarExp("a"), new VarExp("b"))),
                                                        new PrintStmt(new VarExp("c"))))))));

        PrgState state = PrgState.of(program);

        var repo = new interpreter.repository.Repository(state, dir.resolve("log_relational_test.txt").toString());
        var controller = new interpreter.controller.Controller(repo, false);

        controller.allStep();

        assertEquals(1, state.getOut().toList().size(), "Lista Out ar trebui să conțină exact un element.");
        assertEquals(new BoolValue(true), state.getOut().toList().get(0), "Rezultatul lui 1 != 2 ar trebui să fie true.");
    }
}