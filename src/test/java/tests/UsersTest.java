package tests;

import static core.Endpoints.USER;
import static core.Endpoints.USER_BY_USERNAME;
import static core.Endpoints.USER_LOGIN;

import com.github.javafaker.Faker;
import core.models.user.CreatingUserModel;
import core.models.user.GetUserModel;
import core.models.user.StatusCreateUserModel;
import core.models.user.StatusLoginUserModel;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

public class UsersTest extends BaseTest {

  Faker faker = new Faker();

  String userId;
  String username = faker.name().username();
  String firstName = faker.name().firstName();
  String lastName = faker.name().lastName();
  String email = faker.internet().emailAddress();
  String password = faker.internet().password();
  String phone = faker.phoneNumber().phoneNumber();

  CreatingUserModel creatingUserModel;

  @Test
  public void createUser() {

    CreatingUserModel creatingUserModel = CreatingUserModel.builder()
        .username(username)
        .firstName(firstName)
        .lastName(lastName)
        .email(email)
        .password(password)
        .phone(phone)
        .userStatus(18)
        .build();

    ValidatableResponse userResponse = RestAssured.given()
        .body(creatingUserModel)
        .when()
        .post(USER)
        .then()
        .statusCode(200);
    StatusCreateUserModel createUserStatusResponse = userResponse.extract()
        .as(StatusCreateUserModel.class);

    userId = createUserStatusResponse.getMessage();

    Assertions.assertThat(createUserStatusResponse.getMessage())
        .as("Message body not equals to 0")
        .isNotEqualTo(0);

  }

  @Test(dependsOnMethods = "createUser")
  public void getUser() {

    ValidatableResponse userResponse = RestAssured.given()
        .pathParam("username", username)
        .when()
        .get(USER_BY_USERNAME)
        .then()
        .statusCode(200);
    GetUserModel getUserResponse = userResponse.extract().as(GetUserModel.class);

    SoftAssertions softAssertions = new SoftAssertions();

    softAssertions.assertThat(creatingUserModel)
        .as("All data with test 'createUser' was saved")
        .isEqualToIgnoringGivenFields(getUserResponse, "id");

    softAssertions.assertThat(getUserResponse.getId())
        .as("User id should be equal " + userId)
        .isEqualTo(userId);

    softAssertions.assertAll();
  }

  @Test(dependsOnMethods = "createUser")
  public void LoginWithCreatedUser() {

    String message = "logged in user session:";

    ValidatableResponse userResponse = RestAssured.given()
        .queryParam("username", username)
        .queryParam("password", password)
        .when()
        .get(USER_LOGIN)
        .then()
        .statusCode(200);
    StatusLoginUserModel getUserResponse = userResponse.extract().as(StatusLoginUserModel.class);

    Assertions.assertThat(getUserResponse.getMessage())
        .as("Should be message " + message)
        .contains(message);


  }


}
