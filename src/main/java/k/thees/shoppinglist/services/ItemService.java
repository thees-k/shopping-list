package k.thees.shoppinglist.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import k.thees.shoppinglist.model.Item;
import k.thees.shoppinglist.repositories.ItemRepositoryInterface;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService implements ItemServiceInterface {

	private final ItemRepositoryInterface itemRepository;

	@Override
	public List<Item> getAll() {
		return itemRepository
				.findAll()
				.stream()
				.toList();
	}

	@Override
	public Optional<Item> get(Integer id) {
		return itemRepository.findById(id);
	}

	@Override
	public Item create(Item item) {

		return itemRepository.save(item);
	}

	@Override
	public Optional<Item> update(Integer id, Item changedItem) {

		if(itemRepository.existsById(id)) {
			itemRepository.findById(id).ifPresent(itemRepository::save);
			return Optional.of(changedItem);
		} else {
			return Optional.empty();
		}
	}

	@Override
	public boolean delete(Integer id) {

		if(itemRepository.existsById(id)) {
			itemRepository.deleteById(id);
			return true;
		} else {
			return false;
		}
	}
}
