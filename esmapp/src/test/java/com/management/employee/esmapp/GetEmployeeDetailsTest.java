package com.management.employee.esmapp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class GetEmployeeDetailsTest {

	Logger logger = LoggerFactory.getLogger(GetEmployeeDetailsTest.class);
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
	public void getEmployeeDetailsTest() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(get("/users/{id}", "e0001"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.responseCode").value(HttpStatus.OK.value()))
		.andExpect(jsonPath("$.message").value("Employee data retrieved successfully"));
	}

	@Test
	public void employeeIdNotExistTest() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(get("/users/{id}", "e0011"))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.responseCode").value(HttpStatus.BAD_REQUEST.value()))
		.andExpect(jsonPath("$.message").value("Employee data does not exists with this id."));
	}

	@Test
	public void employeeIdInvalidTest() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(get("/users/"))
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void getAllEmployeesDataTest() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(get("/users")
				.param("minSalary", "0")
				.param("maxSalary", "4000")
				.param("offset", "0")
				.param("limit", "1")
				.param("sort", "+name"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.message").value("Success"))
		.andExpect(jsonPath("$.totalElements").value("6"))
		.andExpect(jsonPath("$.results[0].id").value("e0008"))
		.andExpect(jsonPath("$.results[0].login").value("adumbledore"))
		.andExpect(jsonPath("$.results[0].name").value("Albus Dumbledore"))
		.andExpect(jsonPath("$.results[0].salary").value("34.23"));
	}
	
	@Test
	public void noMinSalaryFieldTest() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(get("/users")
				.param("maxSalary", "4000")
				.param("offset", "0")
				.param("limit", "1")
				.param("sort", "+name"))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void invalidMinSalaryDataTest() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(get("/users")
				.param("minSalary", "-1")
				.param("maxSalary", "4000")
				.param("offset", "0")
				.param("limit", "1")
				.param("sort", "+name"))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").value("Request validation failed"));
	}
	
	@Test
	public void noMaxSalaryFieldTest() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(get("/users")
				.param("minSalary", "0")
				.param("offset", "0")
				.param("limit", "1")
				.param("sort", "+name"))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void invalidMaxSalaryDataTest() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(get("/users")
				.param("minSalary", "0")
				.param("maxSalary", "-1")
				.param("offset", "0")
				.param("limit", "1")
				.param("sort", "+name"))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").value("Request validation failed"));
	}

	
	@Test
	public void noOffsetFieldTest() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(get("/users")
				.param("minSalary", "0")
				.param("maxSalary", "4000")
				.param("limit", "1")
				.param("sort", "+name"))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void invalidOffsetDataTest() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(get("/users")
				.param("minSalary", "0")
				.param("maxSalary", "-1")
				.param("offset", "-1")
				.param("limit", "1")
				.param("sort", "+name"))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").value("Request validation failed"));
	}
	
	@Test
	public void noLimitFieldTest() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(get("/users")
				.param("minSalary", "0")
				.param("maxSalary", "4000")
				.param("offset", "0")
				.param("sort", "+name"))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void invalidLimitDataTest() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(get("/users")
				.param("minSalary", "0")
				.param("maxSalary", "-1")
				.param("offset", "0")
				.param("limit", "-1")
				.param("sort", "+name"))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").value("Request validation failed"));
	}

	@Test
	public void noSortFieldTest() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(get("/users")
				.param("minSalary", "0")
				.param("maxSalary", "4000")
				.param("offset", "0")
				.param("limit", "0"))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void invalidSortDataTest() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(get("/users")
				.param("minSalary", "0")
				.param("maxSalary", "-1")
				.param("offset", "0")
				.param("limit", "1")
				.param("sort", "+fullName"))
		.andExpect(status().isBadRequest())
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
