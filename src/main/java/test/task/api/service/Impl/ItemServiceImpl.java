package test.task.api.service.Impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import test.task.api.tool.ImageDecoder;
import test.task.api.dto.entityDto.ItemDto;
import test.task.api.exception.ModelExistsException;
import test.task.api.model.Item;
import test.task.api.model.Status;
import test.task.api.repository.ItemGroupRepository;
import test.task.api.repository.ItemRepository;
import test.task.api.repository.UnitOfMeasurementRepository;
import test.task.api.service.ItemService;


import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemGroupRepository itemGroupRepository;
    private final UnitOfMeasurementRepository unitOfMeasurementRepository;
    @Override
    public void createItem(ItemDto itemDto) throws IOException {
        if(itemRepository.findByItemName(itemDto.getItemName()).isPresent()){
            throw new ModelExistsException("Item already taken");
        }
        Item item = new Item();
        settingItem(itemDto, item);
        item.setStatus(Status.NEW.toString());
        itemRepository.save(item);
    }

    @Override
    public void updateItem(String itemName, ItemDto itemDto) throws IOException {
        if(itemRepository.findByItemName(itemName).isEmpty()){
            throw new ModelExistsException(
                    String.format(("Item %S does not exist"), itemDto.getItemName()));
        }
        Item item = itemRepository.findByItemName(itemName).orElseThrow();
        settingItem(itemDto, item);
        item.setStatus(Status.UPDATED.toString());
        itemRepository.save(item);
    }

    @Override
    public void deleteItemByItemName(String name) {
        itemRepository.delete(itemRepository.findByItemName(name).orElseThrow());
    }

    @Override
    public List<Item> filterItems(String field, Object value, List<Item> items) {

        List<Item> filteredItems = items.stream()
                .filter(item -> {
                    switch (field){
                        case "itemName": return item.getItemName().equals(value.toString());
                        case "itemGroup": return item.getItemGroup().getType().equals(value.toString());
                        case "unitOfMeasurement": return item.getUnitOfMeasurement().getUnit().equals(value.toString());
                        case "quantity": return item.getQuantity() == (int) value;
                        case "priceWithoutVAT": return item.getPriceWithoutVAT().equals(value);
                        case "status": return item.getStatus().equals(value.toString());
                        case "storageLocation": return item.getStorageLocation().equals(value.toString());
                        case "contactPerson": return item.getContactPerson().equals(value.toString());
                        default: return false;

                    }
                }).toList();
    return filteredItems;
    }

    @Override
    public List<Item> getAllItemsSorted(String sortBy) {
        return itemRepository.findAll(Sort.by(sortBy)).stream().toList();
    }

    @Override
    public List<Item> getAll() {
        return itemRepository.findAll();
    }

    private void settingItem(ItemDto itemDto, Item item) throws IOException {
        item.setItemName(itemDto.getItemName());
        item.setItemGroup(itemGroupRepository.findByType(itemDto.getItemGroup()).orElseThrow());
        item.setUnitOfMeasurement(unitOfMeasurementRepository.findByUnit(itemDto.getUnitOfMeasurement()).orElseThrow());
        item.setQuantity(itemDto.getQuantity());
        item.setPriceWithoutVAT(itemDto.getPriceWithoutVAT());
        item.setStorageLocation(itemDto.getStorageLocation());
        item.setContactPerson(itemDto.getContactPerson());
        item.setPhoto(ImageDecoder.decodeBase64Image(itemDto.getPhoto()));
    }

}
