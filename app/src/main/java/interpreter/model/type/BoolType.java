package interpreter.model.type;

import com.sun.jdi.BooleanValue;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;

public record BoolType() implements IType {
    @Override
    public IValue defaultValue() {
        return new BooleanValue(false);
    }
    @Override
    public String toString() {
        return "bool";
    }
}
