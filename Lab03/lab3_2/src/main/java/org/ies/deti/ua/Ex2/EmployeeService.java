package org.ies.deti.ua.Ex2;

import java.util.List;

public interface EmployeeService {
    Employee createEmployee(Employee employee);
    Employee getEmployeeById(Long id);
    Employee getEmployeeByEmail(String email);
    List<Employee> getAllEmployees();
    Employee updateEmployee(Employee employee);
    void deleteEmployee(Long id);
}

