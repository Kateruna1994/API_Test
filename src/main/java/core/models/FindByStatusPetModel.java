package core.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import core.models.PetModel.Category;
import core.models.PetModel.Tag;
import java.util.ArrayList;
import java.util.List;
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
public class FindByStatusPetModel {

  public String id;
  public Category category;
  public String name;
  public List<String> photoUrls;
  public List<Tag> tags;
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

  public static List<String> getPetsName(List<FindByStatusPetModel> model) {
    List<String> petsName = new ArrayList<>();
    for (FindByStatusPetModel findByStatusPetModel : model) {
      petsName.add(findByStatusPetModel.getName());
    }
    return petsName;
  }


}
