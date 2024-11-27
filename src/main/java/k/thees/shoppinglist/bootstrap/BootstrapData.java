package k.thees.shoppinglist.bootstrap;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import k.thees.shoppinglist.model.Item;
import k.thees.shoppinglist.repositories.ItemRepositoryInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

	private final ItemRepositoryInterface itemRepository;

	@Override
	public void run(String... args) throws Exception {

		log.debug("BootstrapData started");
		insertItemData();
	}

	private void insertItemData() {
		if (itemRepository.count() == 0) {

			var now = LocalDateTime.now();

			Item item1 = Item.builder()
					.modifiedAt(now)
					.modifiedBy("Thees")
					.text("Milk")
					.build();
			Item item2 = Item.builder()
					.modifiedAt(now)
					.modifiedBy("Sandra")
					.text("Bananas")
					.build();
			Item item3 = Item.builder()
					.modifiedAt(now)
					.modifiedBy("Tiga")
					.text("Cat Food")
					.build();

			itemRepository.save(item1);
			itemRepository.save(item2);
			itemRepository.save(item3);

			log.debug("Inserted new items");
		}
	}
}
