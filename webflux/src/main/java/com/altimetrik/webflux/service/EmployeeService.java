package com.altimetrik.webflux.service;

import com.altimetrik.webflux.entity.Employee;
import com.altimetrik.webflux.entity.Project;
import com.altimetrik.webflux.repository.EmployeeRepository;
import com.altimetrik.webflux.service.utility.EmailService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final EmailService emailService;

    public EmployeeService(EmployeeRepository employeeRepository, EmailService emailService) {
        this.employeeRepository = employeeRepository;
        this.emailService = emailService;
    }

    public Flux<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    public Mono<Employee> getEmployeeById(String id){
        return employeeRepository.findById(id);
    }

    public Mono<Employee> saveEmployee(Employee employee) {
        if (employee.getProjects() != null && employee.getProjects().size() > 3) {
            return Mono.error(new IllegalArgumentException("An employee can be assigned to a maximum of 3 projects"));
        }
        return employeeRepository.save(employee);
    }

    public Mono<Void> deleteEmployee(String id) {
        return employeeRepository.deleteById(id);
    }

    public Mono<Employee> allocateProject(String employeeId, Project project) {
        return employeeRepository.findById(employeeId)
                .flatMap(employee -> {
                    if (employee.getProjects() == null) {
                        employee.setProjects(new ArrayList<>());
                    }
                    if (employee.getProjects().size() >= 3) {
                        return Mono.error(new IllegalArgumentException("An employee can be assigned to a maximum of 3 projects"));
                    }
                    if (project.getAllocation() < 0.1 || project.getAllocation() > 1.0) {
                        return Mono.error(new IllegalArgumentException("Allocation must be between 0.1 and 1.0"));
                    }
                    employee.getProjects().add(project);
                    Mono<Employee> result = employeeRepository.save(employee);
                    sendAllocationNotification(employee, project);
                    return result;
                })
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Employee not found")));
    }

    private Mono<Void> sendAllocationNotification(Employee employee, Project project) {
        String subject = "Project Allocation Notification";
        String body = String.format("Hello %s,\n\nYou have been allocated to the project: %s.\n\nBest regards,\nAltimetrik",
                employee.getEmployeeName(), project.getProjectName());
        // Todo: employee pojo need modification to store email details also.
        // Todo: Side car design pattern for email notifications.
        return emailService.sendEmail("vigneshmanivasagan@gmail.com", subject, body);
    }

    public Mono<Employee> modifyProjectAllocation(String employeeId, String projectName, Project updatedProject) {
        return employeeRepository.findById(employeeId)
                .flatMap(employee -> {
                    if (employee.getProjects() == null || employee.getProjects().isEmpty()) {
                        return Mono.error(new IllegalArgumentException("No projects found for this employee"));
                    }

                    List<Project> projects = employee.getProjects();
                    Optional<Project> existingProjectOpt = projects.stream()
                            .filter(project -> project.getProjectName().equalsIgnoreCase(projectName))
                            .findFirst();

                    if (existingProjectOpt.isEmpty()) {
                        return Mono.error(new IllegalArgumentException("Project with the given name not found"));
                    }

                    Project existingProject = existingProjectOpt.get();

                    // Validate allocation range. I'm assuming 0.1 is 10 % and 1 is 100% allocation
                    if (updatedProject.getAllocation() < 0.1 || updatedProject.getAllocation() > 1.0) {
                        return Mono.error(new IllegalArgumentException("Allocation must be between 0.1 and 1.0"));
                    }

                    // Update remaining project details
                    existingProject.setAccountName(updatedProject.getAccountName());
                    existingProject.setAllocation(updatedProject.getAllocation());
                    existingProject.setProjectStartDate(updatedProject.getProjectStartDate());
                    existingProject.setProjectEndDate(updatedProject.getProjectEndDate());
                    existingProject.setRemarks(updatedProject.getRemarks());

                    return employeeRepository.save(employee);
                })
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Employee not found")));
    }

    public Mono<Employee> getSecondMostExperiencedInProject(String projectName) {
        return employeeRepository.findAll()
                .filter(employee -> employee.getProjects() != null &&
                        employee.getProjects().stream()
                                .anyMatch(project -> project.getProjectName().equalsIgnoreCase(projectName)))
                .sort((e1, e2) -> Double.compare(e2.getOverallExperience(), e1.getOverallExperience())) // Descending sort by experience
                .elementAt(1) // Get the second element
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Project seems to contain 1 or no employees")));
    }

    public Flux<Employee> getEmployeesBySkills(String primarySkill, String secondarySkill) {
        return employeeRepository.findByPrimarySkillAndSecondarySkill(primarySkill, secondarySkill);
    }


}
