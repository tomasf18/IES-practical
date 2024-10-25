package ies.lab3.ex1.lab3_2;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

@Controller
public class EmployeeController {

    EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // We’ll need a mapping for the /index URL:
    @GetMapping("/index")
    public String showEmployeeList(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        return "index";
    }
    
    // Display the employee signup form
    @GetMapping("/signup")
    public String showSignUpForm(Employee employee) {
        return "add-employee";
    }
    
    // Persist a new entity in the database after validating the constrained fields
    @PostMapping("/addemployee")
    public String addEmployee(@Valid Employee employee, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // If the entity doesn’t pass the validation, the signup form will be redisplayed
            return "add-employee";
        }
        
        employeeRepository.save(employee);
        return "redirect:/index";
    }

    // additional CRUD methods

    // We'll also need to fetch the Employee entity that matches the supplied id from the database.
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Employee employee = employeeRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));

        // If the entity exists, it will be passed on as a model attribute to the update form view.
        // The form can be populated with the values of the name and email fields:
        model.addAttribute("employee", employee);
        return "update-employee";
    }


    // Finally, we have the updateEmployee() and deleteEmployee() methods within the EmployeeController class.
    // Persist the updated entity in the database
    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable("id") long id, @Valid Employee employee, 
      BindingResult result, Model model) {
        if (result.hasErrors()) {
            employee.setId(id);
            return "update-employee";
        }

        employeeRepository.save(employee);
        return "redirect:/index";
    }

    // Remove the given entity
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable("id") long id, Model model) {
        Employee employee = employeeRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));
        employeeRepository.delete(employee);
        return "redirect:/index";
    }

}