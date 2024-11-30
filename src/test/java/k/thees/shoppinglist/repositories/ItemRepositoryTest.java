package k.thees.shoppinglist.repositories;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.validation.ConstraintViolationException;
import k.thees.shoppinglist.model.Item;

@DataJpaTest
public class ItemRepositoryTest {

	@Autowired
	ItemRepositoryInterface itemRepository;

	@Test
	void testSave() {

		var dateTime = LocalDateTime.of(2024, 1, 2, 3, 4);

		Item item = Item
				.builder()
				.modifiedAt(dateTime)
				.text("Carots")
				.modifiedBy("Bugs B.")
				.done(true)
				.build();

		Item savedItem = itemRepository.save(item);

		// Validation only takes place when writing to the database
		itemRepository.flush();

		assertNotNull(savedItem);
		assertNotNull(savedItem.getId());
		assertNotNull(savedItem.getVersion());
		assertTrue(item.equalsAnotherItem(savedItem));
	}

	@Test
	void testSaveWithNoValidItem() {

		itemRepository.save(Item.builder().build());
		// Validation only takes place when writing to the database
		assertThrows(ConstraintViolationException.class, () -> itemRepository.flush());
	}
}
