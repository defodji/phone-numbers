package au.com.belong.phonenumbers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static au.com.belong.phonenumbers.repository.PhoneNumberRepository.PHONE_NUMBER_TABLE;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
class PhoneNumbersApplicationTests {
	private static final String BASE_URI = "http://localhost";
	private static final String BASE_PATH = "/managePhoneNumbers";
	private static final Integer PORT = 8090;
	private static final String PHONE_NUMBERS_API_PATH = "/v1/phonenumbers";

	@BeforeAll
	static void setup() {
		RestAssured.baseURI = BASE_URI;
		RestAssured.basePath = BASE_PATH;
		RestAssured.port = PORT;
	}

	@Test
	void callListAllPhoneNumbers() {
		given()
				.log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.get(PHONE_NUMBERS_API_PATH)
				.then()
				.log().all()
				.statusCode(HttpStatus.OK.value())
				.contentType(ContentType.JSON)
				.body(not(empty()))
				.body("size()", is(PHONE_NUMBER_TABLE.size()));
	}

	@Test
	void callListPhoneNumbersForAGivenCustomer() {
		given()
				.log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.queryParam("customerId", "02f84c05-e48b-4986-8275-e8674f956cd6")
				.get(PHONE_NUMBERS_API_PATH)
				.then()
				.log().all()
				.statusCode(HttpStatus.OK.value())
				.contentType(ContentType.JSON)
				.body(not(empty()))
				.body("number[0]", is("0245678945"))
				.body("customer.id[0]", is("02f84c05-e48b-4986-8275-e8674f956cd6"));
	}

	@Test
	void callListPhoneNumbersForAGivenCustomerWithInvalidUUID() {
		given()
				.log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.queryParam("customerId", "abc")
				.get(PHONE_NUMBERS_API_PATH)
				.then()
				.log().all()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.contentType(ContentType.JSON)
				.body(not(empty()))
				.body("errorId", is("BAD_REQUEST"));
	}

	@Test
	void callActivatePhoneNumberWhenCustomerNotFound() {
		given()
				.log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.body("{\n" +
						"    \"number\": \"0444555222\",\n" +
						"    \"customerId\": \"92f74c05-e48b-4986-8275-e8674f956cd6\"\n" +
						"}")
				.when()
				.post(PHONE_NUMBERS_API_PATH)
				.then()
				.log().all()
				.statusCode(HttpStatus.CONFLICT.value())
				.contentType(ContentType.JSON)
				.body(not(empty()))
				.body("errorId", is("CUSTOMER_NOT_FOUND"));
	}

	@Test
	void callActivatePhoneNumberWhenExistingPhoneNumber() {
		given()
				.log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.body("{\n" +
						"    \"number\": \"0245678945\",\n" +
						"    \"customerId\": \"92f74c05-e48b-4986-8275-e8674f956cd6\"\n" +
						"}")
				.when()
				.post(PHONE_NUMBERS_API_PATH)
				.then()
				.log().all()
				.statusCode(HttpStatus.CONFLICT.value())
				.contentType(ContentType.JSON)
				.body(not(empty()))
				.body("errorId", is("NUMBER_ALREADY_ACTIVATED"));
	}

	@Test
	void callActivatePhoneNumberWhenInvalidPhoneNumberFormat() {
		given()
				.log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.body("{\n" +
						"    \"number\": \"abc\",\n" +
						"    \"customerId\": \"92f74c05-e48b-4986-8275-e8674f956cd6\"\n" +
						"}")
				.when()
				.post(PHONE_NUMBERS_API_PATH)
				.then()
				.log().all()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.contentType(ContentType.JSON)
				.body(not(empty()))
				.body("errorId", is("BAD_REQUEST"));
	}


	@Test
	void callActivatePhoneNumberSuccess() {
		given()
				.log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.body("{\n" +
						"    \"number\": \"0444555222\",\n" +
						"    \"customerId\": \"02f84c05-e48b-4986-8275-e8674f956cd6\"\n" +
						"}")
				.when()
				.post(PHONE_NUMBERS_API_PATH)
				.then()
				.log().all()
				.statusCode(HttpStatus.CREATED.value())
				.contentType(ContentType.JSON)
				.body(not(empty()))
				.body("number", is("0444555222"))
				.body("customer.id", is("02f84c05-e48b-4986-8275-e8674f956cd6"));
	}


}
