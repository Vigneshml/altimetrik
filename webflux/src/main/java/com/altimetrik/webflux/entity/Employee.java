package com.altimetrik.webflux.entity;

import com.altimetrik.webflux.enums.CapabilityCentre;
import com.altimetrik.webflux.enums.Designation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Document(collection = "employees")
public class Employee {
    @Id
    private String id;

    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Employee ID must be alphanumeric")
    private String employeeId;

    @NotBlank(message = "Employee Name is required")
    private String employeeName;

    @NotNull(message = "Capability Centre is required")
    private CapabilityCentre capabilityCentre;

    @NotNull(message = "Date of joining is required")
    private LocalDate dateOfJoining;

    @NotNull(message = "Designation is required")
    private Designation designation;

    @NotBlank(message = "Primary skill is required")
    private String primarySkill;

    private String secondarySkill;

    @Min(value = 0, message = "Overall experience must be a positive number")
    private Double overallExperience;

    @Size(max = 3, message = "An employee can be assigned to a maximum of 3 projects")
    private List<@Valid Project> projects;

    // note: lombak getters didnt work in my machine so have generated them.


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public CapabilityCentre getCapabilityCentre() {
        return capabilityCentre;
    }

    public void setCapabilityCentre(CapabilityCentre capabilityCentre) {
        this.capabilityCentre = capabilityCentre;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public Designation getDesignation() {
        return designation;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    public String getPrimarySkill() {
        return primarySkill;
    }

    public void setPrimarySkill(String primarySkill) {
        this.primarySkill = primarySkill;
    }

    public String getSecondarySkill() {
        return secondarySkill;
    }

    public void setSecondarySkill(String secondarySkill) {
        this.secondarySkill = secondarySkill;
    }

    public Double getOverallExperience() {
        return overallExperience;
    }

    public void setOverallExperience(Double overallExperience) {
        this.overallExperience = overallExperience;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
