package com.management.employee.esmapp.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.management.employee.esmapp.entity.EmployeeDataEntity;
import com.management.employee.esmapp.model.EmployeeData;
import com.management.employee.esmapp.model.EmployeeDataResponse;
import com.management.employee.esmapp.model.Response;
import com.management.employee.esmapp.repository.EmployeeDataRepo;
import com.management.employee.esmapp.service.EmployeeDataService;
import com.management.employee.esmapp.service.ValidatorService;

/**
 * @author Kavya Sreedharan
 * 
 * This is the controller class for Employee Data CRUD Operations
 */


@RestController
@CrossOrigin(origins = "http://localhost:4200/")
public class EmployeeDataController {

	Logger logger = LoggerFactory.getLogger(EmployeeDataController.class);

	@Autowired
	private ValidatorService validatorService;

	@Autowired
	private EmployeeDataService employeeDataService;

	@Autowired
	private EmployeeDataRepo employeeDataRepo;

	/**
	 * This method is used to upload employee data into the database
	 * @param file
	 * @return responseData
	 */
	@PostMapping(value="/users/upload", consumes={"multipart/form-data","text/csv"})
	public ResponseEntity<Response> employeeDataUpload(@RequestParam("file") MultipartFile file) {
		Response responseData= new Response();

		try {
			// Validate content type of file is CSV
			if(validatorService.validateEmployeeFileType(file.getContentType())) {
				logger.info("Employee file upload request validation is successfull");

				responseData = employeeDataService.extractEmployeeData(file);
			} else {
				responseData.setResponseCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
				responseData.setMessage("Input file is not in CSV format");
			}
		} catch (Exception e) {
			logger.error("Error in userDataUpload => " + e.getMessage());
			responseData.setResponseCode(HttpStatus.BAD_REQUEST.value());
			responseData.setMessage("Exception occured and unable to process your request now");
		}
		logger.info("Employee file upload response :: " + responseData);

		return ResponseEntity.status(responseData.getResponseCode()).body(responseData);
	}

	/**
	 * This method is used to get all employee details from database
	 * @param minSalary
	 * @param maxSalary
	 * @param offset
	 * @param limit
	 * @param columnHeader
	 * @return responseData
	 */
	@GetMapping(value="/users")
	public ResponseEntity<Response> getEmployeeDetails(@RequestParam("minSalary") int minSalary,
			@RequestParam("maxSalary") int maxSalary,
			@RequestParam("offset") int offset,
			@RequestParam("limit") int limit,
			@RequestParam("sort") String columnHeader) {
		Response responseData= new Response();

		try {
			// Validate request parameters
			logger.info("Request parameter of getEmployeeDetails request :: minSalary = " + minSalary + " maxSalary = " + 
					maxSalary + " offset = " + offset + " limit = " + limit + " columnHeader = " + columnHeader);

			if(validatorService.validateGetEmployeeDetailsRequest(minSalary, maxSalary, offset, limit, columnHeader)) {
				logger.info("Get all employee details request validation is successfull");

				responseData = employeeDataService.getUserDetails(minSalary, maxSalary, offset, limit, columnHeader);
			} else {
				responseData.setResponseCode(HttpStatus.BAD_REQUEST.value());
				responseData.setMessage("Request validation failed");
			}
		} catch (Exception e) {
			logger.error("Error in userDataUpload => " + e.getMessage());
			responseData.setResponseCode(HttpStatus.BAD_REQUEST.value());
			responseData.setMessage("Exception occured and unable to process your request now");
		}

		logger.info("Response of get all employee details :: " + responseData);

		return ResponseEntity.status(responseData.getResponseCode()).body(responseData);
	}

	/**
	 * This method is used to update employee details to database
	 * @param id
	 * @param employeeData
	 * @return responseData
	 */
	@RequestMapping(
			value = "/users/{id}", 
			method = {RequestMethod.PATCH})
	public ResponseEntity<Response> updateEmployeeDetails(@PathVariable(value = "id") String id, 
			@RequestBody EmployeeData employeeData) {
		Response responseData= new Response();
		try {
			logger.info("Employee details update for :: id = " + id);
			if(validatorService.validateEmployeeDataRequest(id, employeeData)) {
				Optional<EmployeeDataEntity> employee = employeeDataRepo.findById(id);
				logger.info("Checking for employee details with id in DB with result = " + employee);
				
				if(employee.isPresent()) {
					Optional<EmployeeDataEntity> existingEmpLogin = employeeDataRepo.findByLogin(employeeData.getLogin());
					logger.info("Checking for employee details with login in DB with result = " + existingEmpLogin);
					
					if(employeeData.getLogin().equals(employee.get().getLogin()) || existingEmpLogin.isEmpty()) {
						EmployeeDataEntity data = new EmployeeDataEntity(employeeData.getId(), 
								employeeData.getLogin(), employeeData.getName(), employeeData.getSalary());
						EmployeeDataEntity updatedEmpData =  employeeDataRepo.save(data);
						logger.info("Employee data updation in DB result :: " + updatedEmpData);
						if(null != updatedEmpData) {
							responseData.setResponseCode(HttpStatus.OK.value());
							responseData.setMessage("Employee data updated successfully");
						} else {
							responseData.setResponseCode(HttpStatus.BAD_REQUEST.value());
							responseData.setMessage("Unable to save employee data. Please try again after sometime.");
						}
					} else {
						responseData.setResponseCode(HttpStatus.BAD_REQUEST.value());
						responseData.setMessage("Employee login already exists. Please choose another login.");
					}
				} else {
					responseData.setResponseCode(HttpStatus.BAD_REQUEST.value());
					responseData.setMessage("Employee data does not exists with this id.");
				}
			} else {
				responseData.setResponseCode(HttpStatus.BAD_REQUEST.value());
				responseData.setMessage("Request validation failed");
			}
		} catch (Exception e) {
			logger.error("Error in updating employee data :: " + e.getMessage());
			responseData.setResponseCode(HttpStatus.BAD_REQUEST.value());
			responseData.setMessage("Exception occured and unable to process your request now");
		}
		logger.info("Response in updating employee records :: " + responseData);
		
		return ResponseEntity.status(responseData.getResponseCode()).body(responseData);
	}

	/**
	 * This method is used to delete employee details in database
	 * @param id
	 * @return responseData
	 */
	@RequestMapping(
			value = "/users/{id}", 
			method = {RequestMethod.DELETE})
	public ResponseEntity<Response> deleteEmployeeDetails(@PathVariable(value = "id") String id) {
		Response responseData= new Response();
		try {
			logger.info("Employee details delete for :: id = " + id);
			if(validatorService.validateEmployeeIdDataRequest(id)) {
				Optional<EmployeeDataEntity> employee = employeeDataRepo.findById(id);
				logger.info("Employee details find by id result :: " + employee);
				
				if(employee.isPresent()) {
					employeeDataRepo.deleteById(id);
					logger.info("Employee details deleted successfully");
					responseData.setResponseCode(HttpStatus.OK.value());
					responseData.setMessage("Employee data deleted successfully");
				} else {
					responseData.setResponseCode(HttpStatus.BAD_REQUEST.value());
					responseData.setMessage("Employee data does not exists with this id.");
				}
			} else {
				responseData.setResponseCode(HttpStatus.BAD_REQUEST.value());
				responseData.setMessage("Request validation failed");
			}
		} catch (Exception e) {
			logger.error("Error in deleteUserDetails => " + e.getMessage());
			responseData.setResponseCode(HttpStatus.BAD_REQUEST.value());
			responseData.setMessage("Exception occured and unable to process your request now");
		}
		
		logger.info("Response for delete employee details :: " + responseData);
		return ResponseEntity.status(responseData.getResponseCode()).body(responseData);
	}
	
	/**
	 * This method is used to get an employee's details from database
	 * @param id
	 * @return responseData
	 */
	@RequestMapping(
			value = "/users/{id}", 
			produces = "application/json",
			method = {RequestMethod.GET})
	public ResponseEntity<Response> retrieveEmployeeDetails(@PathVariable(value = "id") String id) {
		Response responseData= new Response();
		EmployeeDataResponse empDataResp = new EmployeeDataResponse();
		try {
			logger.info("Reterieve employee details for id :: " + id);
			
			Optional<EmployeeDataEntity> employee = employeeDataRepo.findById(id);
			logger.info("Employee details find by id results :: " + employee);
			
			if(employee.isPresent()) {
					EmployeeData data = new EmployeeData();
					data.setId(id);
					data.setLogin(employee.get().getLogin());
					data.setName(employee.get().getName());
					data.setSalary(employee.get().getSalary());
					
					empDataResp.setEmpDataRecord(data);
					empDataResp.setTotalElements(1);
					empDataResp.setResponseCode(HttpStatus.OK.value());
					empDataResp.setMessage("Employee data retrieved successfully");
			} else {
				empDataResp.setResponseCode(HttpStatus.BAD_REQUEST.value());
				empDataResp.setMessage("Employee data does not exists with this id.");
			}
			responseData = empDataResp;
		} catch (Exception e) {
			logger.error("Error in retrieveUserDetails => " + e.getMessage());
			responseData.setResponseCode(HttpStatus.BAD_REQUEST.value());
			responseData.setMessage("Exception occured and unable to process your request now");
		}
		logger.info("Reterieve employee details response :: " + responseData);
		
		return ResponseEntity.status(responseData.getResponseCode()).body(responseData);
	}

	/**
	 * This method is used to save new employee's details to database
	 * @param id
	 * @param employeeData
	 * @return responseData
	 */
	@RequestMapping(
			value = "/users/{id}", 
			produces = "application/json",  
			method = {RequestMethod.POST})
	public ResponseEntity<Response> createNewUser(@PathVariable(value = "id") String id,
			@RequestBody EmployeeData employeeData) {
		Response responseData= new Response();
		try {
			logger.info("Create new employee with request details id = " + id + " employeeData = " + employeeData);
			
			if(validatorService.validateEmployeeDataRequest(id, employeeData)) {
				Optional<EmployeeDataEntity> employeeRecById = employeeDataRepo.findById(id);
				logger.info("Employee details find by id results :: " + employeeRecById);
				
				Optional<EmployeeDataEntity> employeeRecByLogin = employeeDataRepo.findByLogin(employeeData.getLogin());
				logger.info("Employee details find by login results :: " + employeeRecByLogin);
				
				if(employeeRecById.isEmpty() && employeeRecByLogin.isEmpty()) {
					EmployeeDataEntity data = new EmployeeDataEntity();
						data.setId(id);
						data.setLogin(employeeData.getLogin());
						data.setName(employeeData.getName());
						data.setSalary(employeeData.getSalary());
						
						EmployeeDataEntity saveResp = employeeDataRepo.save(data);
						logger.info("Saved new employee data => " + saveResp);
						if(saveResp != null) {
							responseData.setResponseCode(HttpStatus.OK.value());
							responseData.setMessage("New employee data saved successfully");
						} else {
							responseData.setResponseCode(HttpStatus.OK.value());
							responseData.setMessage("Error in saving employee data");
						}
				} else {
					responseData.setResponseCode(HttpStatus.BAD_REQUEST.value());
					responseData.setMessage("Employee data already exists with given id or login details");
				}
			} else {
				responseData.setResponseCode(HttpStatus.BAD_REQUEST.value());
				responseData.setMessage("Request validation failed");
			}
			
			
		} catch (Exception e) {
			logger.error("Error in retrieveUserDetails => " + e.getMessage());
			responseData.setResponseCode(HttpStatus.BAD_REQUEST.value());
			responseData.setMessage("Exception occured and unable to process your request now");
		}
		logger.info("Create new employee details response :: " + responseData);

		return ResponseEntity.status(responseData.getResponseCode()).body(responseData);
	}
}
