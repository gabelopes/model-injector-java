package br.unisinos.parthenos.injector.injector.java.representation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@AllArgsConstructor
@Getter
public class QualifiedName {
  private static final String QUALIFIED_NAME_SEPARATOR = ".";

  private String[] parts;

  private String construct() {
    return String.join(QUALIFIED_NAME_SEPARATOR, this.getParts());
  }

  public String getName() {
    if (this.hasName()) {
      return this.getParts()[this.getParts().length - 1];
    }

    return null;
  }

  public String[] getPackageParts() {
    final String[] parts = this.getParts();

    return Arrays
      .stream(parts, 0, parts.length - 1)
      .toArray(String[]::new);
  }

  public String getPackage() {
    return String.join(QUALIFIED_NAME_SEPARATOR, this.getPackageParts());
  }

  public boolean hasName() {
    return this.getParts().length > 0;
  }

  public boolean hasPackage() {
    return this.getParts().length > 1;
  }

  @Override
  public String toString() {
    return this.construct();
  }

  public static QualifiedName from(String... parts) {
    final String[] singleParts = Arrays.stream(parts)
      .filter(Objects::nonNull)
      .map((part) -> part.split("\\."))
      .flatMap(Arrays::stream)
      .toArray(String[]::new);

    return new QualifiedName(singleParts);
  }
}
