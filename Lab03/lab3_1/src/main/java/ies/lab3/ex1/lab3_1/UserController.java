package ies.lab3.ex1.lab3_1;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

@Controller
public class UserController {

    UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // We’ll need a mapping for the /index URL:
    @GetMapping("/index")
    public String showUserList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "index";
    }
    
    // Display the user signup form
    @GetMapping("/signup")
    public String showSignUpForm(User user) {
        return "add-user";
    }
    
    // Persist a new entity in the database after validating the constrained fields
    @PostMapping("/adduser")
    public String addUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // If the entity doesn’t pass the validation, the signup form will be redisplayed
            return "add-user";
        }
        
        userRepository.save(user);
        return "redirect:/index";
    }

    // additional CRUD methods

    // We'll also need to fetch the User entity that matches the supplied id from the database.
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        // If the entity exists, it will be passed on as a model attribute to the update form view.
        // The form can be populated with the values of the name and email fields:
        model.addAttribute("user", user);
        return "update-user";
    }


    // Finally, we have the updateUser() and deleteUser() methods within the UserController class.
    // Persist the updated entity in the database
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid User user, 
      BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return "update-user";
        }

        userRepository.save(user);
        return "redirect:/index";
    }

    // Remove the given entity
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.delete(user);
        return "redirect:/index";
    }

}