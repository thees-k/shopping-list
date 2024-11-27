package k.thees.shoppinglist.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import k.thees.shoppinglist.model.Item;



public interface ItemRepositoryInterface extends JpaRepository<Item, Integer> {
	//
}
