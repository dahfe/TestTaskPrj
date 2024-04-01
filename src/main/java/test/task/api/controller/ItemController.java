package test.task.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.task.api.dto.entityDto.FilterDto;
import test.task.api.dto.entityDto.ItemDto;
import test.task.api.model.Item;
import test.task.api.service.ItemService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping("/items")
    public ResponseEntity<List<Item>> getFilteredItems(@RequestBody(required = false) @Valid FilterDto filterDto) {
        if(filterDto == null){
            return new ResponseEntity<>(itemService.getAll(), HttpStatus.OK);
        }
        return new ResponseEntity<>(itemService.filterItems(filterDto.getField(), filterDto.getValue()), HttpStatus.OK);
    }
    @PostMapping("/items/create")
    public ResponseEntity createItem(@RequestBody @Valid ItemDto itemDto) throws IOException {
        itemService.createItem(itemDto);
        return new ResponseEntity(HttpStatus.OK);
    }
    @PostMapping("/items/{name}/edit")
    public ResponseEntity updateItem(@PathVariable(name = "name") String name, @RequestBody @Valid ItemDto itemDto) throws IOException {
        itemService.updateItem(name, itemDto);
        return new ResponseEntity(HttpStatus.OK);
    }
    @PostMapping("/items/{name}/remove")
    public ResponseEntity deleteItem(@PathVariable(name = "name") String name) {
        itemService.deleteItemByItemName(name);
        return new ResponseEntity(HttpStatus.OK);
    }
}
