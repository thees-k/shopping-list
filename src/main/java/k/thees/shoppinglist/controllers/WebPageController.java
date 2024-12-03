package k.thees.shoppinglist.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebPageController {

	@GetMapping("/index")  // e.g. http://localhost:8080/index?user=Sandra
	public String getHomePage() {
		return "index"; // Refers to `src/main/resources/templates/index.html`
	}
}
