package com.altimetrik.webflux.entity;

import com.altimetrik.webflux.enums.AccountName;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Project {
    @NotNull(message = "Account Name is required")
    private AccountName accountName;

    @NotBlank(message = "Project Name is required")
    private String projectName;

    @DecimalMin(value = "0.1", message = "Allocation must be at least 0.1")
    @DecimalMax(value = "1.0", message = "Allocation must not exceed 1.0")
    private Float allocation;

    @NotNull(message = "Project start date is required")
    private LocalDate projectStartDate;

    private LocalDate projectEndDate;

    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Remarks must be alphanumeric")
    private String remarks;

    public AccountName getAccountName() {
        return accountName;
    }

    public void setAccountName(AccountName accountName) {
        this.accountName = accountName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Float getAllocation() {
        return allocation;
    }

    public void setAllocation(Float allocation) {
        this.allocation = allocation;
    }

    public LocalDate getProjectStartDate() {
        return projectStartDate;
    }

    public void setProjectStartDate(LocalDate projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    public LocalDate getProjectEndDate() {
        return projectEndDate;
    }

    public void setProjectEndDate(LocalDate projectEndDate) {
        this.projectEndDate = projectEndDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
