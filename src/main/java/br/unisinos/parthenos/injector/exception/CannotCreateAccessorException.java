package br.unisinos.parthenos.injector.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CannotCreateAccessorException extends AbortedException {
  private String attributeName;
  private String className;

  @Override
  public String getMessage() {
    return "Cannot add accessor for attribute '"
         + this.getAttributeName()
         + "' in class '"
         + this.getClassName()
         + "'. Class is missing or is an interface, attribute does not exist, or accessor already exists.";
  }
}
