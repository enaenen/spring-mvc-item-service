package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {

    private static final Map<Long, Item> store = new HashMap<>(); // static , 실제는 HashMap 사용 X
    private static long sequence = 0L; // static

    public Item save(Item item){
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id){
        return store.get(id);
    }

    public List<Item> findAll() {
        return new ArrayList<>(store.values()); // ArrayList 에 값을 넣어도 Store 는 변하지 않도록 Wrapping
    }

    public void update(Long itemId, Item updateParam){
        Item findItem = findById(itemId);
        // ID 는 사용하지 않기때문에, ItemParameterDto 로 만들어서 name,price,quantity 만 가진 것들을 넣어주어야 한다.
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void clearStore(){
        store.clear();
    }


}
