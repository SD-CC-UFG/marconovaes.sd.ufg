import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Set implements Iterable<Integer>{

    HashSet<Integer> listId = new HashSet<Integer>();

    public Set() {
    }

    public Set(HashSet<Integer> listId) {
        this.listId = listId;
    }

    public boolean contain(Integer id){
        return listId.contains(id);
    }

    public void insert(Integer id){
        listId.add(id);
    }

    @Override
    public Iterator<Integer> iterator() {
        return listId.iterator();
    }
}
