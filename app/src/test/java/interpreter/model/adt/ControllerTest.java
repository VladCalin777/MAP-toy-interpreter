package interpreter.model.adt;

import interpreter.controller.Controller;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.exp.RelOp;
import interpreter.model.exp.RelExp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.stmt.*;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.repository.Repository;
import interpreter.view.Examples;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    @TempDir
    Path dir;

    private PrgState run(IStmt program) throws MyException {
        PrgState state = PrgState.of(program);
        var repo = new Repository(state, dir.resolve("log.txt").toString());
        new Controller(repo, false).allStep();
        return state;
    }

    @Test
    void logFileContainsFourSectionsInOrder() throws Exception {
        run(Examples.EX1);

        String log = Files.readString(dir.resolve("log.txt"));
        int a = log.indexOf("ExeStack:");
        int b = log.indexOf("SymTable:");
        int c = log.indexOf("Out:");
        int d = log.indexOf("FileTable:");

        assertTrue(0 <= a && a < b && b < c && c < d);
    }

    @Test
    void ex4ReadsNumbersAndEndsWithEmptyFileTable() throws Exception {
        Path in = dir.resolve("test.in");
        Files.writeString(in, "15\n50\n");

        PrgState s = run(Examples.fileExample(in.toString()));

        assertEquals(new IntValue(15), s.getOut().toList().get(0));
        assertEquals(new IntValue(50), s.getOut().toList().get(1));
        assertTrue(s.getFileTable().toMap().isEmpty());
    }

    @Test
    void ex4OnEmptyFilePrintsZeros() throws Exception {
        Path empty = dir.resolve("empty.in");
        Files.writeString(empty, "");

        PrgState s = run(Examples.fileExample(empty.toString()));

        assertEquals(new IntValue(0), s.getOut().toList().get(0));
        assertEquals(new IntValue(0), s.getOut().toList().get(1));
        assertTrue(s.getFileTable().toMap().isEmpty());
    }

    @Test
    void ex5PrintsFive() throws Exception {
        PrgState s = run(Examples.EX5);

        assertEquals(1, s.getOut().toList().size());
        assertEquals(new IntValue(5), s.getOut().toList().get(0));
    }

    @Test
    void notEqualComparisonPrintsTrue() throws Exception {
        IStmt program = new CompStmt(new VarDeclStmt("a", new IntType()),
                new CompStmt(new VarDeclStmt("b", new IntType()),
                        new CompStmt(new VarDeclStmt("c", new BoolType()),
                                new CompStmt(new AssignStmt("a", new ValueExp(new IntValue(1))),
                                        new CompStmt(new AssignStmt("b", new ValueExp(new IntValue(2))),
                                                new CompStmt(new AssignStmt("c", new RelExp(RelOp.NE, new VarExp("a"), new VarExp("b"))),
                                                        new PrintStmt(new VarExp("c"))))))));

        PrgState s = run(program);

        assertEquals(1, s.getOut().toList().size());
        assertEquals(new BoolValue(true), s.getOut().toList().get(0));
    }

    @Test
    void logFileContainsTheFourSectionsInOrder() throws Exception {
        // int v; v = 2; print(v);
        IStmt ex1 = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(2))),
                        new PrintStmt(new VarExp("v"))));

        run(ex1);

        String log = Files.readString(dir.resolve("log.txt"));
        int a = log.indexOf("ExeStack:");
        int b = log.indexOf("SymTable:");
        int c = log.indexOf("Out:");
        int d = log.indexOf("FileTable:");
        assertTrue(0 <= a && a < b && b < c && c < d);
    }

}