package br.unisinos.parthenos.injector.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CannotCreateAttributeException extends AbortedException {
  private String attributeName;
  private String className;

  @Override
  public String getMessage() {
    return "Cannot add attribute '" + this.getAttributeName() + "' in class '" + this.getClassName() + "'. Class is missing, is an interface or attribute already exists.";
  }
}
