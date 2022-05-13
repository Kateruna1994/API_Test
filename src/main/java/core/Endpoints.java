package core;

public interface Endpoints {

  String PET = "pet/";
  String PET_BY_ID = "pet/{id}/";
  String PET_BY_STATUS="pet/findByStatus/";


  String USER = "user/";
  String USER_BY_USERNAME = "user/{username}";
  String USER_LOGIN = "user/login";
}
