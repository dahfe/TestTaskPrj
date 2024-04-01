package test.task.api.service;

import test.task.api.dto.entityDto.ItemDto;
import test.task.api.model.Item;

import java.io.IOException;
import java.util.List;

public interface ItemService {
    void createItem(ItemDto itemDto) throws IOException;
    void updateItem(String itemName, ItemDto itemDto) throws IOException;
    void deleteItemByItemName(String name);
    List<Item> filterItems(String field, Object value, List<Item> items);
    List<Item> getAll();
    List<Item> getAllItemsSorted(String sortBy);
}
