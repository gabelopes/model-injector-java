package br.unisinos.parthenos.injector.injector.model.java;

import br.unisinos.parthenos.injector.injector.converter.java.ModifierConverter;
import br.unisinos.parthenos.injector.injector.converter.java.TypeConverter;
import br.unisinos.parthenos.injector.injector.model.Model;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.type.Type;
import com.jsoniter.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttributeModel implements Model {
  @JsonProperty("class")
  private String targetClassName;

  @JsonProperty(decoder = ModifierConverter.class)
  private Modifier[] modifiers;

  @JsonProperty(decoder = TypeConverter.class)
  private Type type;

  @JsonProperty
  private String name;
}
