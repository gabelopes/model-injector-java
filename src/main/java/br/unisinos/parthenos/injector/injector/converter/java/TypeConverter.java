package br.unisinos.parthenos.injector.injector.converter.java;

import com.github.javaparser.JavaParser;
import com.jsoniter.JsonIterator;
import com.jsoniter.spi.Decoder;

import java.io.IOException;

public class TypeConverter implements Decoder {
  @Override
  public Object decode(JsonIterator jsonIterator) throws IOException {
    final String qualifiedTypeName = jsonIterator.readString();

    return JavaParser.parseType(qualifiedTypeName);
  }
}
