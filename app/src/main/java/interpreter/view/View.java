package interpreter.view;

import interpreter.exceptions.*;
import interpreter.model.PrgState;
import interpreter.model.adt.*;
import interpreter.model.stmt.*;
import interpreter.model.type.*;
import interpreter.repository.*;
import interpreter.controller.*;
import interpreter.model.value.*;
import interpreter.model.exp.*;
import java.util.Scanner;

public class View {
    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            System.out.print("Alege opțiunea: ");
            String option = scanner.nextLine();
            switch (option) {
                case "1" -> runProgram(getExample1());
                case "2" -> runProgram(getExample2());
                case "3" -> runProgram(getExample3());
                case "0" -> {
                    System.out.println("La revedere!");
                    return;
                }
                default -> System.out.println("Opțiune invalida!");
            }
        }
    }

    private void printMenu() {
        System.out.println("1. Ex1: int v; v=2; Print(v)");
        System.out.println("2. Ex2: int a; int b; a=2+3*5; b=a+1; Print(b)");
        System.out.println("3. Ex3: bool a; int v; a=true; (If a Then v=2 Else v=3); Print(v)");
        System.out.println("0. Iesire");
    }

    private void runProgram(IStmt stmt) {
        PrgState prg = PrgState.of(stmt);
        IRepository repo = new Repository(prg);
        Controller controller = new Controller(repo);
        try {
            controller.allStep();
        } catch (MyException e) {
            System.out.println("Eroare la execuție: " + e.getMessage());
        }
    }

    private IStmt getExample1() {
        // int v; v=2; Print(v)
        return new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(2))),
                        new PrintStmt(new VarExp("v"))
                )
        );
    }

    private IStmt getExample2() {
        // int a; int b; a=2+3*5; b=a+1; Print(b)
        return new CompStmt(
                new VarDeclStmt("a", new IntType()),
                new CompStmt(
                        new VarDeclStmt("b", new IntType()),
                        new CompStmt(
                                new AssignStmt("a", new ArithExp(
                                        ArithOP.ADD,
                                        new ValueExp(new IntValue(2)),
                                        new ArithExp(ArithOP.MUL, new ValueExp(new IntValue(3)), new ValueExp(new IntValue(5)))
                                )),
                                new CompStmt(
                                        new AssignStmt("b", new ArithExp(
                                                ArithOP.ADD,
                                                new VarExp("a"),
                                                new ValueExp(new IntValue(1))
                                        )),
                                        new PrintStmt(new VarExp("b"))
                                )
                        )
                )
        );
    }

    private IStmt getExample3() {
        // bool a; int v; a=true; (If a Then v=2 Else v=3); Print(v)
        return new CompStmt(
                new VarDeclStmt("a", new BoolType()),
                new CompStmt(
                        new VarDeclStmt("v", new IntType()),
                        new CompStmt(
                                new AssignStmt("a", new ValueExp(new BoolValue(true))),
                                new CompStmt(
                                        new IfStmt(
                                                new VarExp("a"),
                                                new AssignStmt("v", new ValueExp(new IntValue(2))),
                                                new AssignStmt("v", new ValueExp(new IntValue(3)))
                                        ),
                                        new PrintStmt(new VarExp("v"))
                                )
                        )
                )
        );
    }
}
