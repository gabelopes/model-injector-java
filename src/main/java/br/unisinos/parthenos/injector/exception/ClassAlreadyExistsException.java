package br.unisinos.parthenos.injector.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClassAlreadyExistsException extends AbortedException {
  private String className;

  @Override
  public String getMessage() {
    return "Class with name '" + this.getClassName() + "' already exists";
  }
}
