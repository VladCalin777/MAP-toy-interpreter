package interpreter.model.exp;

public enum ArithOP {
    ADD("+"),
    SUB("-"),
    MUL("*"),
    DIV("/");

    private final String symbol;

    ArithOP(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}