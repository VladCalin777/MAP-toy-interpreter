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
    private StringValue fileKey;
    private ValueExp fileExp;

    @BeforeEach
    void setup() throws Exception {
        Path f = dir.resolve("numbers.in");
        Files.writeString(f, "15\n50\n");
        fileKey = new StringValue(f.toString());
        fileExp = new ValueExp(fileKey);

        state = PrgState.of(new NopStmt());
        new VarDeclStmt("x", new IntType()).execute(state);
    }

    @Test
    void openAddsToFileTableReadReadsInOrderAndZeroAtEndCloseRemovesIt() throws MyException {
        // openRFile
        new OpenRFileStmt(fileExp).execute(state);
        assertTrue(state.getFileTable().isDefined(fileKey));

        // readFile -> 15, apoi 50, apoi 0 la EOF
        new ReadFileStmt(fileExp, "x").execute(state);
        assertEquals(new IntValue(15), state.getSymTable().lookup("x"));

        new ReadFileStmt(fileExp, "x").execute(state);
        assertEquals(new IntValue(50), state.getSymTable().lookup("x"));

        new ReadFileStmt(fileExp, "x").execute(state);
        assertEquals(new IntValue(0), state.getSymTable().lookup("x"));

        // closeRFile
        new ClosedRFileStmt(fileExp).execute(state);
        assertFalse(state.getFileTable().isDefined(fileKey));
    }

    @Test
    void openErrorsThrowStatementException() throws MyException {
        // expresie care nu e string
        assertThrows(StatementException.class,
                () -> new OpenRFileStmt(new ValueExp(new IntValue(10))).execute(state));

        // fișier inexistent
        ValueExp missing = new ValueExp(new StringValue(dir.resolve("missing.in").toString()));
        assertThrows(StatementException.class,
                () -> new OpenRFileStmt(missing).execute(state));

        // fișier deja deschis
        new OpenRFileStmt(fileExp).execute(state);
        assertThrows(StatementException.class,
                () -> new OpenRFileStmt(fileExp).execute(state));
    }

    @Test
    void readErrorsThrowStatementException() throws Exception {
        // fișierul nu este în FileTable
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(fileExp, "x").execute(state));

        new OpenRFileStmt(fileExp).execute(state);

        // variabilă nedeclarată
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(fileExp, "undeclared").execute(state));

        // variabilă care nu e de tip int
        new VarDeclStmt("flag", new BoolType()).execute(state);
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(fileExp, "flag").execute(state));

        // linie care nu este număr întreg
        Path badPath = dir.resolve("invalid.in");
        Files.writeString(badPath, "not_an_int\n");
        ValueExp badExp = new ValueExp(new StringValue(badPath.toString()));
        new OpenRFileStmt(badExp).execute(state);

        assertThrows(StatementException.class,
                () -> new ReadFileStmt(badExp, "x").execute(state));
    }

    @Test
    void closeUnopenedFileThrowsStatementException() {
        assertThrows(StatementException.class,
                () -> new ClosedRFileStmt(fileExp).execute(state));
    }
}