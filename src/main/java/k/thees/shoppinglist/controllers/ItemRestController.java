package k.thees.shoppinglist.controllers;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import k.thees.shoppinglist.model.Item;
import k.thees.shoppinglist.services.ItemServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ItemRestController {

	public static final String ITEMS_PATH = "/api/v1/items";
	public static final String ITEMS_PATH_ID = ITEMS_PATH + "/{id}";

	private final ItemServiceInterface itemService;

	@GetMapping(value = ITEMS_PATH)
	public ResponseEntity<List<Item>> getAll() {
		return new ResponseEntity<>(itemService.getAll(), HttpStatus.OK);
	}

	@GetMapping(value = ITEMS_PATH_ID)
	public ResponseEntity<Item> get(@PathVariable Integer id) {

		log.debug("Get item by Id - in controller");

		var bookDto = itemService.get(id).orElseThrow(MyNotFoundException::new);

		return new ResponseEntity<>(bookDto, HttpStatus.OK);
	}

	@PostMapping(ITEMS_PATH)
	public ResponseEntity<Item> create(@Validated @RequestBody Item item) {

		Item savedItem = itemService.insert(item);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", ITEMS_PATH + "/" + savedItem.getId().toString());

		return new ResponseEntity<Item>(headers, HttpStatus.CREATED);
	}

	@DeleteMapping(ITEMS_PATH_ID)
	public ResponseEntity<Item> delete(@PathVariable Integer id) {

		if (itemService.delete(id)) {
			return new ResponseEntity<Item>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<Item>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(ITEMS_PATH_ID)
	public ResponseEntity<Item> update(@PathVariable Integer id, @Validated @RequestBody Item item) {

		return itemService.update(id, item)
				.map(it -> new ResponseEntity<Item>(HttpStatus.NO_CONTENT))
				.orElseThrow(MyNotFoundException::new);
	}

	//	PATCH http://localhost:8080/api/v1/items/42 HTTP/1.1
	//	content-type: application/json
	//
	//	{
	//	    "text": "New item text",
	//	}
	//
	// (Note that the empty line after the header is important!)
	//
	// Patch = To update only the fields listed inside the JSON
	// The values of the not listed fields will be null inside the item
	@PatchMapping(ITEMS_PATH_ID)
	public ResponseEntity<Item> patch(@PathVariable Integer id, @RequestBody Item item) {

		return itemService.patch(id, item)
				.map(it -> new ResponseEntity<Item>(HttpStatus.NO_CONTENT))
				.orElseThrow(MyNotFoundException::new);
	}

}
