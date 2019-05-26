package br.unisinos.parthenos.injector.injector.model.java;

import br.unisinos.parthenos.injector.injector.converter.java.ModifierConverter;
import br.unisinos.parthenos.injector.injector.java.representation.QualifiedName;
import br.unisinos.parthenos.injector.injector.model.Model;
import com.github.javaparser.ast.Modifier;
import com.jsoniter.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClassCreationModel implements Model {
  @JsonProperty("package")
  private String packageName;

  @JsonProperty(decoder = ModifierConverter.class)
  private Modifier[] modifiers;

  @JsonProperty(required = true)
  private String name;

  @JsonProperty
  private String parent;

  @JsonProperty
  private String[] interfaces;

  public QualifiedName getQualifiedName() {
    return QualifiedName.from(this.getPackageName(), this.getName());
  }
}
