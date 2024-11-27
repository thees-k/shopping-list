package k.thees.shoppinglist.services;

import java.util.List;
import java.util.Optional;

import k.thees.shoppinglist.model.Item;


public interface ItemServiceInterface {

	List<Item> getAll();

	Optional<Item> get(Integer id);

	Item insert(Item item);

	Optional<Item> update(Integer id, Item item);

	boolean delete(Integer id);
}
