package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.exp.IExp;
import interpreter.model.value.IValue;
import interpreter.model.PrgState;

public record AssignStmt(String id, IExp exp) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        var symTable = state.getSymTable();
        if (!symTable.isDefined(id)) {
            throw new StatementException("variabila " + id + "nu a fost declarata");
        }
        IValue val = exp.eval(symTable);
        if (!val.getType().equals(symTable.lookup(id).getType())) {
            throw new StatementException("tipul variabilei " + id + " si tipul expresiei nu se potrivesc");
        }
        symTable.update(id, val);
        return state;
    }

    @Override
    public String toString() {
        return id + " = " + exp;
    }
}
