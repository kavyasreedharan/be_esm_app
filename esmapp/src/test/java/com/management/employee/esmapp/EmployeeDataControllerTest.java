package com.management.employee.esmapp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.management.employee.esmapp.controller.EmployeeDataController;
import com.management.employee.esmapp.model.EmployeeData;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeDataControllerTest {

	Logger logger = LoggerFactory.getLogger(EmployeeDataController.class);
	public static final String fileType = "text/csv";

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Test
	public void userDataUploadSuccessTest() {
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
	public void newAndExistingDataTest() throws Exception {
		try( InputStream uploadStream = EmployeeDataControllerTest.class.getClassLoader().getResourceAsStream("sample-data2.csv")){
			MockMultipartFile file = new MockMultipartFile("file", uploadStream);
			MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), fileType, file.getBytes());
			MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

			mockMvc.perform(multipart("/users/upload").file(multipartFile)).andExpect(status().isOk());
		} catch(Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Test
	public void emptyDataTest() throws Exception {
		try( InputStream uploadStream = EmployeeDataControllerTest.class.getClassLoader().getResourceAsStream("sample-data3.csv")){
			MockMultipartFile file = new MockMultipartFile("file", uploadStream);
			MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), fileType, file.getBytes());
			MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

			mockMvc.perform(multipart("/users/upload").file(multipartFile)).andExpect(status().isNotAcceptable());
		} catch(Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Test
	public void incorrectFileTypeTest() throws Exception {
		try( InputStream uploadStream = EmployeeDataControllerTest.class.getClassLoader().getResourceAsStream("sample-data4.txt")){
			MockMultipartFile file = new MockMultipartFile("file", uploadStream);
			MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/pdf", file.getBytes());
			MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

			mockMvc.perform(multipart("/users/upload").file(multipartFile)).andExpect(status().isUnsupportedMediaType());
		} catch(Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Test
	public void partialRowDataTest() throws Exception {
		try( InputStream uploadStream = EmployeeDataControllerTest.class.getClassLoader().getResourceAsStream("sample-data5.csv")){
			MockMultipartFile file = new MockMultipartFile("file", uploadStream);
			MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), fileType, file.getBytes());
			MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

			mockMvc.perform(multipart("/users/upload").file(multipartFile)).andExpect(status().isNotAcceptable());
		} catch(Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Test
	public void tooManyDataInRowTest() throws Exception {
		try( InputStream uploadStream = EmployeeDataControllerTest.class.getClassLoader().getResourceAsStream("sample-data6.csv")){
			MockMultipartFile file = new MockMultipartFile("file", uploadStream);
			MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), fileType, file.getBytes());
			MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

			mockMvc.perform(multipart("/users/upload").file(multipartFile)).andExpect(status().isNotAcceptable());
		} catch(Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Test
	public void negativeSalaryTest() throws Exception {
		try( InputStream uploadStream = EmployeeDataControllerTest.class.getClassLoader().getResourceAsStream("sample-data7.csv")){
			MockMultipartFile file = new MockMultipartFile("file", uploadStream);
			MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), fileType, file.getBytes());
			MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

			mockMvc.perform(multipart("/users/upload").file(multipartFile)).andExpect(status().isNotAcceptable());
		} catch(Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Test
	public void uploadFileUtf8SupportTest() throws Exception {
		try( InputStream uploadStream = EmployeeDataControllerTest.class.getClassLoader().getResourceAsStream("sample-data8.csv")){
			MockMultipartFile file = new MockMultipartFile("file", uploadStream);
			MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), fileType, file.getBytes());
			MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

			mockMvc.perform(multipart("/users/upload").file(multipartFile)).andExpect(status().isOk());
		} catch(Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Test
	public void getAllUserDataTest() throws Exception {
		try( InputStream uploadStream = EmployeeDataControllerTest.class.getClassLoader().getResourceAsStream("sample-data1.csv")){
			MockMultipartFile file = new MockMultipartFile("file", uploadStream);
			System.out.println("file type ===> " + file.getContentType());
			MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), fileType, file.getBytes());
			MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

			mockMvc.perform(multipart("/users/upload").file(multipartFile)).andExpect(status().isOk());
		} catch(Exception e) {
			logger.error(e.getMessage());
		}

		List<EmployeeData> empDataList = new ArrayList<EmployeeData>();
		EmployeeData empData = new EmployeeData("e0001", "hpotter", "Harry Potters", 1234.0f);
		empDataList.add(empData);

		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(get("/users")
				.param("minSalary", "0")
				.param("maxSalary", "4000")
				.param("offset", "0")
				.param("limit", "1")
				.param("sort", "+name"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.message").value("Success"))
		.andExpect(jsonPath("$.currentPage").value("0"))	
		.andExpect(jsonPath("$.totalPages").value("9"))	
		.andExpect(jsonPath("$.totalElements").value("9"))
		.andExpect(jsonPath("$.results[0].id").value("e0001"))
		.andExpect(jsonPath("$.results[0].login").value("hpotter"))
		.andExpect(jsonPath("$.results[0].name").value("Harry Potter"))
		.andExpect(jsonPath("$.results[0].salary").value("1234.0"));
	}

}
