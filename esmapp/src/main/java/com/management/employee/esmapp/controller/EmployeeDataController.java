package com.management.employee.esmapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.management.employee.esmapp.model.Response;
import com.management.employee.esmapp.service.EmployeeDataService;
import com.management.employee.esmapp.service.ValidatorService;

/**
 * @author Kavya Sreedharan
 * 
 * This is the controller class for Employee Data CRUD Operations
 */


@RestController
public class EmployeeDataController {
	
	Logger logger = LoggerFactory.getLogger(EmployeeDataController.class);
	
	@Autowired
	private ValidatorService validatorService;
	
	@Autowired
	private EmployeeDataService employeeDataService;
	
	/**
	 * This method is used to upload user data into the database
	 * @param file
	 * @return responseCode and ResponseData
	 */
	@PostMapping(value="/users/upload", consumes={"multipart/form-data","text/csv"})
	public ResponseEntity<Response> userDataUpload(@RequestParam("file") MultipartFile file) {
		Response responseData= new Response();
		// Validate content type of file is CSV
		if(validatorService.validateUserFileType(file.getContentType())) {
			responseData = employeeDataService.extractEmployeeData(file);
		} else {
			responseData.setResponseCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
			responseData.setMessage("Input file is not in CSV format");
		}

		return ResponseEntity.status(responseData.getResponseCode()).body(responseData);
	}

}
