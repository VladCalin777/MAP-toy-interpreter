package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.model.exp.IExp;
import interpreter.model.value.IValue;
import interpreter.model.PrgState;

public record PrintStmt(IExp exp) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        var out = state.getOut();
        IValue val = exp.eval(state.getSymTable());
        out.add(val);
        return state;
    }

    @Override
    public String toString() {
        return "print(" + exp + ")";
    }
}