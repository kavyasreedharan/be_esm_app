package com.management.employee.esmapp.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.management.employee.esmapp.entity.EmployeeDataEntity;

/**
 * 
 * @author Kavya Sreedharan
 * This is the repository interface for Employee Data
 *
 */
public interface EmployeeDataRepo extends CrudRepository<EmployeeDataEntity, String>{
	
	Optional<EmployeeDataEntity> findByLogin(String login);
	
	Page<EmployeeDataEntity> findBySalaryBetween(Pageable pageable, float minSalary, float maxSalary);
	
}
