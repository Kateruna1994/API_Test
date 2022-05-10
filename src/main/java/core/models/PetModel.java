package core.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)

public class PetModel {

  public String id;
  public Category category;
  public String name;
  public ArrayList<String> photoUrls;
  public ArrayList<Tag> tags;
  public String status;


  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  @Data

  public static class Category {

    public int id;
    public String name;
  }

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  @Data

  public static class Tag {

    public int id;
    public String name;
  }

}
