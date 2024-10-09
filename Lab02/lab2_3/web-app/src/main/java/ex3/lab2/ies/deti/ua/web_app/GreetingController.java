package ex3.lab2.ies.deti.ua.web_app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {

	@GetMapping("/greeting") // handle GET do endpoint /greeting (quando o user visita este endpoint, a página devolvida por este método apresenta se no ecrã)
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		return "greeting"; // html page
	}

}

// The @GetMapping annotation ensures that HTTP GET requests to /greeting are mapped to the greeting() method.

