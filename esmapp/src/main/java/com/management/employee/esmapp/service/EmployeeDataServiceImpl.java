package com.management.employee.esmapp.service;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.management.employee.esmapp.entity.EmployeeData;
import com.management.employee.esmapp.model.Response;
import com.management.employee.esmapp.repository.EmployeeDataRepo;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import io.micrometer.common.util.StringUtils;

/**
 * 
 * @author Kavya Sreedharan
 * This is the service implementation class for Employee Data
 *
 */

@Service
public class EmployeeDataServiceImpl implements EmployeeDataService{

	Logger logger = LoggerFactory.getLogger(EmployeeDataServiceImpl.class);

	@Autowired
	private EmployeeDataRepo employeeDataRepo;

	/**
	 * This method will extract data from CSV file and save data to database
	 * @param file
	 * @return response
	 */
	public Response extractEmployeeData(MultipartFile file) {
		try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(file.getInputStream())).withSkipLines(1).build()){
			// Reading from CSV file and adding to list
			List<EmployeeData> employeeDataList = reader.readAll().stream()
					.map(data-> {
						System.out.println("data=>" + data[0]);
						EmployeeData employeeData = new EmployeeData();
						if(data.length == 4 && !data[0].contains("#") && !StringUtils.isEmpty(data[0]) && !StringUtils.isEmpty(data[1]) 
								&& !StringUtils.isEmpty(data[2]) && !StringUtils.isEmpty(data[3])) {
							employeeData.setEmployeeId(data[0]);
							employeeData.setEmployeeLogin(data[1]);
							employeeData.setEmployeeName(data[2]);
							if(Float.parseFloat(data[3]) >= 0) { // checking if salary is greater than 0
								employeeData.setEmployeeSalary(Float.parseFloat(data[3]));
							} else { // incorrect or empty data
								throw new NullPointerException();
							}
						} else if(data[0].contains("#")){ // ignoring lines with comments
						} else if(data.length > 4){ // incorrect or empty data condition
							throw new NullPointerException();
						} else { // incorrect or empty data condition
							throw new NullPointerException();
						}
						return employeeData;
					})
					.collect(Collectors.toList());

			// removing null objects which may have inserted for commented lines
			employeeDataList.removeIf(emp -> emp.getEmployeeId() == null);

			// checking for empty file contents
			if(employeeDataList.size() == 0) {
				throw new NullPointerException();
			} else {
				Iterable<EmployeeData> iterableEmpList = employeeDataList;

				// saving data to database
				Iterable<EmployeeData> resultList = employeeDataRepo.saveAll(iterableEmpList);

				if(null != resultList) {
					List<EmployeeData> employeeDataResultList = new ArrayList<>();
					resultList.forEach(employeeDataResultList::add);

					// checking if user data saved to database successfully
					if(employeeDataList.size() == employeeDataResultList.size()) {
						return new Response(HttpStatus.OK, "User data uploaded successfully");
					} else {
						return new Response(HttpStatus.OK, "Unable to upload data, please try again later");
					}
				} else {
					return new Response(HttpStatus.OK, "Unable to upload data, please try again later");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new Response(HttpStatus.NOT_ACCEPTABLE, "Incorrect data in user data file");
		}
	}

}
