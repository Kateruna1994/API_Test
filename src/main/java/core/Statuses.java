package core;

import lombok.Getter;

@Getter
public enum Statuses {

  AVAILABLE("available"),
  SOLD("sold");

  private final String status;

  Statuses(String status) {
    this.status = status;
  }
}
