package interpreter.model.adt;
import interpreter.exceptions.ADTException;
import java.util.ArrayList;
import java.util.List;

public class Mylist<T> implements IList<T> {
    private final List<T> list = new ArrayList<>();

    @Override
    public void add(T v){
        list.add(v);
    }

    @Override
    public T get(int index) throws ADTException {
        if (index < 0 || index >= list.size()) {
            throw new ADTException("In afara limitei");
        }
        return list.get(index);
    }

    @Override
    public  int size() {
        return list.size();
    }

    @Override
    public List<T> toList() {
        return List.copyOf(list);
    }

    @Override
    public String toString() {
        return list.toString();
    }
}
