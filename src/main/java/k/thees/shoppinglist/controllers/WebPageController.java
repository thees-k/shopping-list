package k.thees.shoppinglist.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebPageController {

	@GetMapping("/index")  // e.g. http://localhost:8080/index?user=Sandra
	public String getHomePage(@RequestParam(required=false, defaultValue="Thees") String user, Model model) {
		model.addAttribute("user", user);
		return "index"; // Refers to `src/main/resources/templates/index.html`
	}
}
