package com.management.employee.esmapp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.management.employee.esmapp.model.EmployeeData;

@SpringBootTest
@AutoConfigureMockMvc
public class CreateEmployeeTest {

	Logger logger = LoggerFactory.getLogger(CreateEmployeeTest.class);
	public static final String fileType = "text/csv";

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@BeforeEach
	public void insertDataToDb() {
		try( InputStream uploadStream = EmployeeDataControllerTest.class.getClassLoader().getResourceAsStream("sample-data1.csv")){
			MockMultipartFile file = new MockMultipartFile("file", uploadStream);
			MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), fileType, file.getBytes());
			MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

			mockMvc.perform(multipart("/users/upload").file(multipartFile)).andExpect(status().isOk());
		} catch(Exception e) {
			logger.error(e.getMessage());
		}

	}

	@Test
	public void createNewEmployeeSuccessTest() throws Exception {
		EmployeeData empData = new EmployeeData("e0101", "gweasley", "Ginny Weasley", 1555.0f);
		
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(post("/users/{id}", empData.getId())
				 .contentType(MediaType.APPLICATION_JSON)
				.content(convertToJson(empData)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.responseCode").value(HttpStatus.OK.value()))
		.andExpect(jsonPath("$.message").value("New employee data saved successfully"));
	}
	
	@Test
	public void employeeIdExistTest() throws Exception {
		EmployeeData empData = new EmployeeData("e0101", "gwesley1", "Ginny Weasley", 1555.0f);
		
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(post("/users/{id}", empData.getId())
				 .contentType(MediaType.APPLICATION_JSON)
				.content(convertToJson(empData)))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.responseCode").value(HttpStatus.BAD_REQUEST.value()))
		.andExpect(jsonPath("$.message").value("Employee data already exists with given id or login details"));
	}
	
	@Test
	public void employeeLoginExistTest() throws Exception {
		EmployeeData empData = new EmployeeData("e0111", "gweasley", "Ginny Weasley", 1555.0f);
		
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(post("/users/{id}", empData.getId())
				 .contentType(MediaType.APPLICATION_JSON)
				.content(convertToJson(empData)))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.responseCode").value(HttpStatus.BAD_REQUEST.value()))
		.andExpect(jsonPath("$.message").value("Employee data already exists with given id or login details"));
	}
	
	@Test
	public void noPathVariableTest() throws Exception {
		EmployeeData empData = new EmployeeData("e0111", "gweasley", "Ginny Weasley", 1555.0f);
		
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(post("/users")
				 .contentType(MediaType.APPLICATION_JSON)
				.content(convertToJson(empData)))
		.andExpect(status().isMethodNotAllowed());
	}
	
	@Test
	public void noEmployeeLoginData() throws Exception {
		EmployeeData empData = new EmployeeData("e0111", "", "Ginny Weasley", 1555.0f);
		
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(post("/users/{id}", empData.getId())
				 .contentType(MediaType.APPLICATION_JSON)
				.content(convertToJson(empData)))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.responseCode").value(HttpStatus.BAD_REQUEST.value()))
		.andExpect(jsonPath("$.message").value("Request validation failed"));
	}
	
	@Test
	public void noEmployeeNameData() throws Exception {
		EmployeeData empData = new EmployeeData("e0112", "gweasley", "", 1555.0f);
		
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(post("/users/{id}", empData.getId())
				 .contentType(MediaType.APPLICATION_JSON)
				.content(convertToJson(empData)))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.responseCode").value(HttpStatus.BAD_REQUEST.value()))
		.andExpect(jsonPath("$.message").value("Request validation failed"));
	}
	
	@Test
	public void noEmployeeSalaryData() throws Exception {
		EmployeeData empData = new EmployeeData("e0112", "gweasley", "Ginny Weasley", -1);
		
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(post("/users/{id}", empData.getId())
				 .contentType(MediaType.APPLICATION_JSON)
				.content(convertToJson(empData)))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.responseCode").value(HttpStatus.BAD_REQUEST.value()))
		.andExpect(jsonPath("$.message").value("Request validation failed"));
	}
	
	 public static String convertToJson(final Object obj) {
	        try {
	            return new ObjectMapper().writeValueAsString(obj);
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }

}
