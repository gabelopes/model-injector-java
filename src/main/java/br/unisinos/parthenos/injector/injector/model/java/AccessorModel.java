package br.unisinos.parthenos.injector.injector.model.java;

import br.unisinos.parthenos.injector.injector.converter.java.ModifierConverter;
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
public class AccessorModel implements Model {
  @JsonProperty("class")
  private String className;

  @JsonProperty("attribute")
  private String attributeName;

  @JsonProperty(decoder = ModifierConverter.class)
  private Modifier[] modifiers;

  @JsonProperty
  private String name;
}
