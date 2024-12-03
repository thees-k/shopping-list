package k.thees.shoppinglist.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.micrometer.common.util.StringUtils;
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
	public Item insert(Item item) {

		return itemRepository.save(item);
	}

	@Override
	public Optional<Item> update(Integer id, Item changedItem) {

		return itemRepository.findById(id).map(item -> saveItemWithNewValues(item, changedItem));
	}

	private Item saveItemWithNewValues(Item item, Item valuesFromItem) {
		item.setModifiedAt(valuesFromItem.getModifiedAt());
		item.setModifiedBy(valuesFromItem.getModifiedBy());
		item.setText(valuesFromItem.getText());
		item.setDone(valuesFromItem.getDone());
		return itemRepository.save(item);
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

	@Override
	public Optional<Item> patch(Integer id, Item changedItem) {
		return itemRepository.findById(id).map(item -> patchItemWithNewValues(item, changedItem));
	}

	private Item patchItemWithNewValues(Item item, Item valuesFromItem) {
		if(valuesFromItem.getModifiedAt() != null) {
			item.setModifiedAt(valuesFromItem.getModifiedAt());
		}
		if(!StringUtils.isBlank(valuesFromItem.getModifiedBy())) {
			item.setModifiedBy(valuesFromItem.getModifiedBy());
		}
		if(!StringUtils.isBlank(valuesFromItem.getText())) {
			item.setText(valuesFromItem.getText());
		}
		if(valuesFromItem.getDone() != null) {
			item.setDone(valuesFromItem.getDone());
		}
		return itemRepository.save(item);
	}

}
