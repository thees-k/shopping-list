package k.thees.services;

import java.util.List;
import java.util.Optional;

import k.thees.model.Item;


public interface ItemServiceInterface {

	List<Item> getAll();

	Optional<Item> get(Integer id);

	Item create(Item item);

	Optional<Item> update(Integer id, Item item);

	boolean delete(Integer id);
}
