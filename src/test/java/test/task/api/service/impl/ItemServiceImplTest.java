package test.task.api.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import test.task.api.dto.entityDto.ItemDto;
import test.task.api.exception.ModelExistsException;
import test.task.api.model.Item;
import test.task.api.model.ItemGroup;
import test.task.api.model.UnitOfMeasurement;
import test.task.api.repository.ItemGroupRepository;
import test.task.api.repository.ItemRepository;
import test.task.api.repository.UnitOfMeasurementRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemGroupRepository itemGroupRepository;

    @Mock
    private UnitOfMeasurementRepository unitOfMeasurementRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createItem_Success() throws IOException {
        ItemDto itemDto = setItemDto();
        itemDto.setItemName("Test Item");

        when(itemRepository.findByItemName(any())).thenReturn(Optional.empty());
        when(itemGroupRepository.findByType(any())).thenReturn(Optional.of(new ItemGroup()));
        when(unitOfMeasurementRepository.findByUnit(any())).thenReturn(Optional.of(new UnitOfMeasurement()));

        assertDoesNotThrow(() -> itemService.createItem(itemDto));

    }

    @Test
    void createItem_ThrowsModelExistsException() {
        ItemDto itemDto = setItemDto();
        itemDto.setItemName("Test Item");

        when(itemRepository.findByItemName(anyString())).thenReturn(Optional.of(new Item()));

        assertThrows(ModelExistsException.class, () -> itemService.createItem(itemDto));
    }

    @Test
    void updateItem_Success() throws IOException {
        String itemName = "Test Item";
        ItemDto itemDto = setItemDto();
        itemDto.setItemName(itemName);

        Item item = new Item();
        item.setItemName(itemName);

        when(itemRepository.findByItemName("Test Item")).thenReturn(Optional.of(new Item()));
        when(itemGroupRepository.findByType(anyString())).thenReturn(Optional.of(new ItemGroup()));
        when(unitOfMeasurementRepository.findByUnit(anyString())).thenReturn(Optional.of(new UnitOfMeasurement()));
        assertDoesNotThrow(() -> itemService.updateItem("Test Item", itemDto));
    }

    @Test
    void updateItem_ThrowsModelExistsException() {
        String itemName = "Test Item";
        ItemDto itemDto = setItemDto();
        itemDto.setItemName(itemName);

        when(itemRepository.findByItemName(anyString())).thenReturn(Optional.empty());

        assertThrows(ModelExistsException.class, () -> itemService.updateItem(itemName, itemDto));
    }

    @Test
    void deleteItemByItemName_Success() {
        String itemName = "Test Item";

        when(itemRepository.findByItemName(itemName)).thenReturn(Optional.of(new Item()));

        assertDoesNotThrow(() -> itemService.deleteItemByItemName(itemName));
    }

    private ItemDto setItemDto(){
        ItemDto itemDto = new ItemDto();
        itemDto.setQuantity(10);
        itemDto.setPriceWithoutVAT(BigDecimal.valueOf(100));
        itemDto.setItemGroup("Test Group");
        itemDto.setUnitOfMeasurement("Test Unit");

        return itemDto;
    }

    @Test
    void filterItems_Success() {
        Item item1 = new Item();
        item1.setItemName("Item 1");
        item1.setQuantity(10);
        item1.setPriceWithoutVAT(BigDecimal.valueOf(100));

        Item item2 = new Item();
        item2.setItemName("Item 2");
        item2.setQuantity(20);
        item2.setPriceWithoutVAT(BigDecimal.valueOf(200));

        List<Item> items = List.of(item1, item2);

        List<Item> filteredItems = itemService.filterItems("quantity", 10, items);
        assertEquals(1, filteredItems.size());
        assertEquals("Item 1", filteredItems.get(0).getItemName());
    }

    @Test
    void getAllItemsSorted_Success() {
        Item item1 = new Item();
        item1.setItemName("Item 1");
        item1.setQuantity(10);
        item1.setPriceWithoutVAT(BigDecimal.valueOf(100));

        Item item2 = new Item();
        item2.setItemName("Item 2");
        item2.setQuantity(20);
        item2.setPriceWithoutVAT(BigDecimal.valueOf(200));

        List<Item> items = List.of(item1, item2);

        when(itemRepository.findAll(Sort.by("quantity"))).thenReturn(items);

        List<Item> sortedItems = itemService.getAllItemsSorted("quantity");
        assertEquals(2, sortedItems.size());
        assertEquals("Item 1", sortedItems.get(0).getItemName());
        assertEquals("Item 2", sortedItems.get(1).getItemName());
    }

    @Test
    void getAll_Success() {
        Item item1 = new Item();
        item1.setItemName("Item 1");
        item1.setQuantity(10);
        item1.setPriceWithoutVAT(BigDecimal.valueOf(100));

        Item item2 = new Item();
        item2.setItemName("Item 2");
        item2.setQuantity(20);
        item2.setPriceWithoutVAT(BigDecimal.valueOf(200));

        List<Item> items = List.of(item1, item2);

        when(itemRepository.findAll()).thenReturn(items);

        List<Item> allItems = itemService.getAll();
        assertEquals(2, allItems.size());
    }

}
