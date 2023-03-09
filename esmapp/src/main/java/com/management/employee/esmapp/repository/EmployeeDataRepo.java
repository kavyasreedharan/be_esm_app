package com.management.employee.esmapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.management.employee.esmapp.entity.EmployeeData;

/**
 * 
 * @author Kavya Sreedharan
 * This is the repository interface for Employee Data
 *
 */
public interface EmployeeDataRepo extends CrudRepository<EmployeeData, String>{
	
	Page<EmployeeData> findAll(Pageable pageable);
	
}
