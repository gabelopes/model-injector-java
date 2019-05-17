package br.unisinos.parthenos.injector.injector.converter.java;

import com.github.javaparser.ast.Modifier;
import com.jsoniter.JsonIterator;
import com.jsoniter.spi.Decoder;

import java.io.IOException;
import java.util.Arrays;

public class ModifierConverter implements Decoder {
  @Override
  public Object decode(JsonIterator jsonIterator) throws IOException {
    final String[] modifiers = jsonIterator.read(String[].class);

    return Arrays.stream(modifiers)
      .map(String::toUpperCase)
      .map(Modifier::valueOf)
      .distinct()
      .toArray(Modifier[]::new);
  }
}
