package k.thees.shoppinglist.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import jakarta.transaction.Transactional;
import k.thees.shoppinglist.model.Item;
import k.thees.shoppinglist.repositories.ItemRepositoryInterface;

@SpringBootTest
class ItemServiceTest {

	@Autowired
	ItemServiceInterface itemService;

	@Autowired
	ItemRepositoryInterface itemRepository;

	@Test
	public void testGetAll() {
		assertEquals(3, itemService.getAll().size());
	}

	@Test
	public void testGet() {
		assertTrue(itemService.get(1).isPresent());
		assertTrue(itemService.get(2).isPresent());
		assertTrue(itemService.get(3).isPresent());
		assertFalse(itemService.get(4).isPresent());
	}

	@Transactional
	@Rollback
	@Test
	public void testInsert() {

		var dateTime = LocalDateTime.of(2020, 1, 2, 3, 4);

		Item item = Item
				.builder()
				.modifiedAt(dateTime)
				.text("Carots")
				.modifiedBy("Bugs B.")
				.build();

		Item savedItem = itemService.insert(item);

		// Validation only takes place when writing to the database
		itemRepository.flush();

		assertNotNull(savedItem);
		assertNotNull(savedItem.getId());
		assertNotNull(savedItem.getVersion());

		assertTrue(item.equalsAnotherItem(savedItem));
	}

	@Transactional
	@Rollback
	@Test
	public void testUpdate() {

		var dateTime = LocalDateTime.of(2020, 1, 2, 3, 4);

		Item item = Item
				.builder()
				.modifiedAt(dateTime)
				.text("Carots")
				.modifiedBy("Bugs B.")
				.done(false)
				.build();

		Item changedItem = itemService.update(1, item).orElseThrow(AssertionError::new);

		// Validation only takes place when writing to the database
		itemRepository.flush();

		assertEquals(1, changedItem.getId());
		assertEquals(1, changedItem.getVersion());
		assertTrue(item.equalsAnotherItem(changedItem));
	}

	@Test
	public void testUpdateNotFound() {

		var dateTime = LocalDateTime.of(2020, 1, 2, 3, 4);

		Item item = Item
				.builder()
				.modifiedAt(dateTime)
				.text("Carots")
				.modifiedBy("Bugs B.")
				.done(true)
				.build();

		assertTrue(itemService.update(4, item).isEmpty());
	}


	@Transactional
	@Rollback
	@Test
	public void testDelete() {
		assertTrue(itemService.delete(1));

		// Validation only takes place when writing to the database
		itemRepository.flush();

		assertFalse(itemRepository.findById(1).isPresent());
	}

	@Test
	public void testDeleteNotFound() {
		assertFalse(itemService.delete(4));
	}
}
