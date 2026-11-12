package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.IStack;
import interpreter.model.stmt.IStmt;
import interpreter.repository.IRepository;

public class Controller {
    private final IRepository repo;
    private boolean displayFlag;

    public Controller(IRepository repo, boolean displayFlag) {
        this.repo = repo;
        this.displayFlag= displayFlag;
    }

    public void setDisplayFlag(boolean flag) {
        this.displayFlag = flag;
    }

    public PrgState oneStep(PrgState state) throws MyException {
        IStack<IStmt> stack = state.getExeStack();
        if (stack.isEmpty()) {
            throw new MyException("prgstate e gol");
        }
        IStmt crtStmt = stack.pop();
        return crtStmt.execute(state);
    }

    public void allStep() throws MyException {
        PrgState prg = repo.getCrtPrg();
        repo.logPrgStateExec();
        display(prg);
        while (!prg.getExeStack().isEmpty()) {
            oneStep(prg);
            repo.logPrgStateExec();
            display(prg);
        }
    }
    private void display(PrgState prg) {
        if (displayFlag) {
            System.out.println(prg.toString());
        }
    }
}