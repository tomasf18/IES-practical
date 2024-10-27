package org.ies.deti.ua.Ex2;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public Employee getEmployeeByEmail(String email) {
        Optional<Employee> optionalEmployee = employeeRepository.findEmployeeByEmail(email);
        return optionalEmployee.get();
    }
    
    @Override
    public List<Employee> getAllEmployees() {
        return (List<Employee>) employeeRepository.findAll();
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        Employee employeeToUpdate = employeeRepository.findById(employee.getId()).get();
        employeeToUpdate.setName(employee.getName());
        employeeToUpdate.setEmail(employee.getEmail());
        Employee updatedEmployee = employeeRepository.save(employeeToUpdate);
        return updatedEmployee;
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
    
}
