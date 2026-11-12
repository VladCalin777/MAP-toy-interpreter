package interpreter.model.adt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.stmt.*;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileStmtTest {
    @TempDir
    Path dir;

    private PrgState state;
    private ValueExp file;

    @BeforeEach
    void setup() throws Exception {
        Path f = dir.resolve("numbers.in");
        Files.writeString(f, "7\n");
        file = new ValueExp(new StringValue(f.toString()));
        state = PrgState.of(new NopStmt());
        new VarDeclStmt("x", new IntType()).execute(state);
    }

    @Test
    void openReadAndCloseWorkInSequence() throws MyException {
        new OpenRFileStmt(file).execute(state);
        assertTrue(state.getFileTable().isDefined(new StringValue(file.val().toString().replace("\"", ""))));

        new ReadFileStmt(file, "x").execute(state);
        assertEquals(new IntValue(7), state.getSymTable().lookup("x"));

        // La sfârșitul fișierului trebuie să întoarcă 0
        new ReadFileStmt(file, "x").execute(state);
        assertEquals(new IntValue(0), state.getSymTable().lookup("x"));

        new ClosedRFileStmt(file).execute(state);
        assertTrue(state.getFileTable().toMap().isEmpty());
    }

    @Test
    void openErrorsThrowStatementException() throws MyException {
        // Expresie care nu este string
        assertThrows(StatementException.class,
                () -> new OpenRFileStmt(new ValueExp(new IntValue(5))).execute(state));

        // Fișier inexistent
        ValueExp missing = new ValueExp(new StringValue(dir.resolve("absent.in").toString()));
        assertThrows(StatementException.class,
                () -> new OpenRFileStmt(missing).execute(state));

        // Fișier deschis de două ori
        new OpenRFileStmt(file).execute(state);
        assertThrows(StatementException.class,
                () -> new OpenRFileStmt(file).execute(state));
    }

    @Test
    void readErrorsThrowStatementException() throws Exception {
        // Fișierul nu este deschis încă
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(file, "x").execute(state));

        new OpenRFileStmt(file).execute(state);

        // Variabilă nedeclarată
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(file, "inexistenta").execute(state));

        // Variabilă care nu este int
        new VarDeclStmt("flag", new BoolType()).execute(state);
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(file, "flag").execute(state));

        // Linie care nu conține un număr întreg
        Path badFile = dir.resolve("bad.in");
        Files.writeString(badFile, "salut\n");
        ValueExp badExp = new ValueExp(new StringValue(badFile.toString()));
        new OpenRFileStmt(badExp).execute(state);
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(badExp, "x").execute(state));
    }

    @Test
    void closeUnopenedFileThrows() {
        assertThrows(StatementException.class,
                () -> new ClosedRFileStmt(file).execute(state));
    }
}