package com.altimetrik.webflux.repository;

import com.altimetrik.webflux.entity.Employee;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String> {
    Flux<Employee> findByPrimarySkillAndSecondarySkill(String primarySkill, String secondarySkill);
}
