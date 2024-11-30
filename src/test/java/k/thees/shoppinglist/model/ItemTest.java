package k.thees.shoppinglist.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class ItemTest {

	@Test
	public void testEqualsAnotherItem() {

		var dateTime = LocalDateTime.of(2020, 1, 2, 3, 4);

		Item item1 = Item.builder()
				.id(1)
				.version(1)
				.text("text")
				.modifiedBy("modifiedBy")
				.modifiedAt(dateTime)
				.done(false)
				.build();

		Item item2 = Item.builder()
				.text("text")
				.modifiedBy("modifiedBy")
				.modifiedAt(dateTime)
				.done(false)
				.build();

		assertTrue(item1.equalsAnotherItem(item2));
	}

	@Test
	public void testEqualsAnotherItemNotEqual() {

		var dateTime = LocalDateTime.of(2020, 1, 2, 3, 4);
		Item item1 = Item.builder()
				.text("text")
				.modifiedBy("modifiedBy")
				.modifiedAt(dateTime)
				.done(false)
				.build();

		Item item2 = Item.builder()
				.text("text X")
				.modifiedBy("modifiedBy")
				.modifiedAt(dateTime)
				.done(false)
				.build();
		assertFalse(item1.equalsAnotherItem(item2));

		item2 = Item.builder()
				.text("text")
				.modifiedBy("modifiedBy X")
				.modifiedAt(dateTime)
				.done(false)
				.build();
		assertFalse(item1.equalsAnotherItem(item2));

		item2 = Item.builder()
				.text("text")
				.modifiedBy("modifiedBy")
				.modifiedAt(LocalDateTime.now())
				.done(false)
				.build();
		assertFalse(item1.equalsAnotherItem(item2));

		item2 = Item.builder()
				.text("TEXT")
				.modifiedBy("modifiedBy")
				.modifiedAt(dateTime)
				.done(false)
				.build();
		assertFalse(item1.equalsAnotherItem(item2));

		item2 = Item.builder()
				.text("text")
				.modifiedBy("modifiedBy")
				.modifiedAt(dateTime)
				.done(true)
				.build();
		assertFalse(item1.equalsAnotherItem(item2));

	}
}
