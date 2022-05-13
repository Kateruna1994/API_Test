package tests;

import static core.Endpoints.PET;
import static core.Endpoints.PET_BY_ID;
import static core.Endpoints.PET_BY_STATUS;
import static core.Statuses.AVAILABLE;
import static core.Statuses.SOLD;

import com.github.javafaker.Faker;
import core.models.DeletePetModel;
import core.models.FindByStatusPetModel;
import core.models.NotFoundModel;
import core.models.PetModel;
import core.models.PetModel.Category;
import core.models.PetModel.Tag;
import core.models.UpdatePetModel;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

public class PostTest extends BaseTest {

  static String petName = "Rex";
  static String petId;
  static String setPetName = "Sky";

  @Test
  public void checkStatusCodeAndIdPetTest() {
    ArrayList<Tag> tagList = new ArrayList<>();
    tagList.add(new Tag(31, "Small dog"));
    tagList.add(new Tag(30, "Cute"));
    tagList.add(new Tag(20, "Silent"));
    ArrayList<String> listUrl = new ArrayList<>();
    listUrl.add("https://unsplash.com/photos/v3-zcCWMjgM");
    listUrl.add("https://unsplash.com/photos/T-0EW-SEbsE");
    listUrl.add("https://unsplash.com/photos/BJaqPaH6AGQ");

    PetModel pet = PetModel.builder()
        .name(petName)
        .category(new Category(10, "Dog"))
        .tags(tagList)
        .photoUrls(listUrl)
        .status(AVAILABLE.getStatus())
        .build();

    PetModel petResponse = RestAssured.given()
        .body(pet)
        .when()
        .post(PET)
        .then()
        .statusCode(200).extract().as(PetModel.class);
    petId = petResponse.getId();

    SoftAssertions softAssertions = new SoftAssertions();

    softAssertions.assertThat(petId)
        .as("After creation pet " + petId + " should be not 0")
        .isNotEqualTo(0);
    softAssertions.assertThat(petResponse)
        .as("All data in response should be the same as in request")
        .isEqualToIgnoringGivenFields(pet, "id");

    softAssertions.assertAll();

  }

  @Test(dependsOnMethods = "checkStatusCodeAndIdPetTest")
  public void getPetByPetIdTest() {
    ValidatableResponse petResponse = RestAssured.given()
        .pathParam("id", petId)
        .when()
        .get(PET_BY_ID)
        .then()
        .statusCode(200);

    PetModel actualPetModel = petResponse.extract().as(PetModel.class);

    SoftAssertions softAssertions = new SoftAssertions();

    softAssertions.assertThat(actualPetModel.getName())
        .as("Name pet should be: " + petName)
        .isEqualTo(petName);

    softAssertions.assertThat(actualPetModel.getStatus())
        .as("Status should be: available")
        .isEqualTo("available");

    softAssertions.assertAll();
  }

  @Test(dependsOnMethods = "checkStatusCodeAndIdPetTest")
  public void updatePetName() {
    ValidatableResponse petResponse = RestAssured
        .given()
        .contentType("application/x-www-form-urlencoded")
        .pathParam("id", petId)
        .formParam("name", "Sky")
        .formParam("status", SOLD.getStatus())
        .when()
        .post(PET_BY_ID)
        .then()
        .statusCode(200);

    UpdatePetModel newPetModel = petResponse.extract().as(UpdatePetModel.class);

    SoftAssertions softAssertions = new SoftAssertions();

    softAssertions.assertThat(newPetModel.getMessage())
        .as("Value of message field in response should be equals to pet id")
        .isEqualTo(petId);
    PetModel pet = RestAssured
        .given()
        .pathParam("id", petId)
        .when()
        .get(PET_BY_ID)
        .then()
        .statusCode(200).extract().as(PetModel.class);

    softAssertions.assertThat(pet.getName()).as("New name of dog should be: " + setPetName)
        .isEqualTo(setPetName);
    softAssertions.assertThat(pet.getStatus()).as("New status should be: " + SOLD.getStatus())
        .isEqualTo(SOLD.getStatus());
    softAssertions.assertAll();
  }

  @Test(dependsOnMethods = "checkStatusCodeAndIdPetTest")
  public void deleteCreatedPetTest() {
    ValidatableResponse petResponse = RestAssured.given()
        .pathParam("id", petId)
        .when()
        .delete(PET_BY_ID)
        .then()
        .statusCode(200);

    DeletePetModel deleteResponse = petResponse.extract().as(DeletePetModel.class);

    SoftAssertions softAssertions = new SoftAssertions();

    softAssertions.assertThat(deleteResponse.getMessage())
        .as("llll")
        .isEqualTo(String.valueOf(petId));

    ValidatableResponse petResponseAfterDelete = RestAssured.given()
        .pathParam("id", petId)
        .when()
        .get(PET_BY_ID)
        .then()
        .statusCode(404);

    NotFoundModel actualCreatePetModelAfterDelete = petResponseAfterDelete.extract()
        .as(NotFoundModel.class);

    softAssertions.assertThat(actualCreatePetModelAfterDelete.getCode())
        .as("Code should be 1")
        .isEqualTo(1);

    softAssertions.assertThat(actualCreatePetModelAfterDelete.getType())
        .as("Type should be error")
        .isEqualTo("error");

    softAssertions.assertThat(actualCreatePetModelAfterDelete.getMessage())
        .as("Message should be ")
        .isEqualTo("Pet not found");

    softAssertions.assertAll();
  }

  @Test
  public void createNewDog() {

    Faker faker = new Faker();

    ArrayList<Tag> tagList = new ArrayList<>();
    tagList.add(new Tag(faker.number().numberBetween(5, 50), faker.dog().breed()));
    tagList.add(new Tag(faker.number().numberBetween(5, 50), faker.dog().breed()));
    tagList.add(new Tag(faker.number().numberBetween(5, 50), faker.dog().breed()));
    ArrayList<String> listUrl = new ArrayList<>();
    listUrl.add("https://unsplash.com/photos/v3-zcCWMjgM");
    listUrl.add("https://unsplash.com/photos/T-0EW-SEbsE");
    listUrl.add("https://unsplash.com/photos/BJaqPaH6AGQ");
    String petName = faker.dog().name();

    PetModel expectPetModel = PetModel.builder()
        .name(petName)
        .category(new Category(18, "Dog"))
        .tags(tagList)
        .photoUrls(listUrl)
        .status(SOLD.getStatus())
        .build();

    ValidatableResponse createPetResponse = RestAssured.given()
        .body(expectPetModel)
        .when()
        .post(PET)
        .then()
        .statusCode(200);

    ValidatableResponse petResponse = RestAssured.given()
        .queryParam("status", SOLD.getStatus())
        .when()
        .get(PET_BY_STATUS)
        .then()
        .statusCode(200);

    List<FindByStatusPetModel> findByStatusPetModels = Arrays.asList(petResponse.extract()
        .as(FindByStatusPetModel[].class));
    List<String> petsName = FindByStatusPetModel.getPetsName(findByStatusPetModels);

    Assertions.assertThat(petsName)
        .as("Should be creating dog with name " + petName)
        .contains(petName);

  }
}
