package k.thees.shoppinglist.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

	@GetMapping("/")
	public String rootRedirect() {
		// Forward to JSF's login.xhtml
		// This does NOT change the URL to /login.xhtml in the browser.
		return "forward:/login.xhtml";
	}
}
