package com.altimetrik.webflux.controller;

import com.altimetrik.webflux.entity.Employee;
import com.altimetrik.webflux.entity.Project;
import com.altimetrik.webflux.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("altimetrik/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * method to get all the employees of an org
     * @return
     */
    @GetMapping
    public Flux<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    /**
     * method to get the employee details by id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Mono<Employee> getEmployeeById(@PathVariable String id) {
        return employeeService.getEmployeeById(id);
    }

    /**
     * method to enroll new employee
     * @param employee
     * @return
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        return employeeService.saveEmployee(employee);
    }

    /**
     * method to update employee details
     * @param id
     * @param employee
     * @return
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Employee> updateEmployee(@PathVariable String id, @Valid @RequestBody Employee employee) {
        return employeeService.getEmployeeById(id)
                .flatMap(existingEmployee -> {
                    employee.setId(existingEmployee.getId());
                    return employeeService.saveEmployee(employee);
                });
    }

    /**
     * method to allocate project
     * @param id
     * @param project
     * @return
     */
    @PostMapping("/{id}/allocate-project")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Employee> allocateProject(
            @PathVariable String id,
            @Valid @RequestBody Project project
    ) {
        return employeeService.allocateProject(id, project);
    }

    /**
     * method to delete employee by id
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteEmployee(@PathVariable String id) {
        return employeeService.deleteEmployee(id);
    }

    /**
     * method to modify project allocation
     * @param id
     * @param projectName
     * @param updatedProject
     * @return
     */
    @PutMapping("/{id}/projects/{projectName}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Employee> modifyProjectAllocation(
            @PathVariable String id,
            @PathVariable String projectName,
            @Valid @RequestBody Project updatedProject
    ) {
        return employeeService.modifyProjectAllocation(id, projectName, updatedProject);
    }

    /**
     * method to get the second most experienced person in a given project
     * @param projectName
     * @return
     */
    @GetMapping("/projects/{projectName}/second-most-experienced")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Employee> getSecondMostExperiencedInProject(@PathVariable String projectName) {
        return employeeService.getSecondMostExperiencedInProject(projectName);
    }

    /**
     * method to get employee details based on their primary and secondary skill
     * @param primarySkill
     * @param secondarySkill
     * @return
     */
    @GetMapping("/skills")
    public Flux<Employee> getEmployeesBySkills(
            @RequestParam String primarySkill,
            @RequestParam String secondarySkill) {
        return employeeService.getEmployeesBySkills(primarySkill, secondarySkill);
    }
}
