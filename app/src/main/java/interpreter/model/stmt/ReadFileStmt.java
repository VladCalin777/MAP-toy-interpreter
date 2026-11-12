package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public record ReadFileStmt(IExp exp, String varName) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        var symTable = state.getSymTable();
        if (!symTable.isDefined(varName))
            throw new StatementException("readFile: variabila " + varName + " nu este definita");
        if (!symTable.lookup(varName).getType().equals(new IntType()))
            throw new StatementException("readFile: variabila " + varName + " nu este de tipul int");

        if (!(exp.eval(symTable) instanceof StringValue fileName))
            throw new StatementException("readFile: " + exp + " nu este de tipul string");

        var fileTable = state.getFileTable();
        if (!fileTable.isDefined(fileName))
            throw new StatementException("readFile: fisierul " + fileName + " nu este deschis");

        BufferedReader reader = fileTable.lookup(fileName);
        try {
            String line = reader.readLine();
            int value = (line == null) ? 0 : Integer.parseInt(line.strip());
            symTable.update(varName, new IntValue(value));
        } catch (IOException e) {
            throw new StatementException("readFile: I/O eroare la citire " + fileName + ": " + e.getMessage());
        } catch (NumberFormatException e) {
            throw new StatementException("readFile: numar invalid " + fileName + ": " + e.getMessage());
        }
        return state;
    }

    @Override
    public String toString() {
        return "readFile(" + exp + ", " + varName + ")";
    }
}