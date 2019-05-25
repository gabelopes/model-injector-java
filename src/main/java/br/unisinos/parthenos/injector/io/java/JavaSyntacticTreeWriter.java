package br.unisinos.parthenos.injector.io.java;

import br.unisinos.parthenos.injector.annotation.Language;
import br.unisinos.parthenos.injector.annotation.Target;
import br.unisinos.parthenos.injector.io.Writer;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

@Language("java")
@Target(CompilationUnit.class)
public class JavaSyntacticTreeWriter implements Writer<CompilationUnit> {
  @Override
  public boolean write(File sourceFile, CompilationUnit injectedSource) {
    try {
      Files.write(sourceFile.toPath(), injectedSource.toString().getBytes(), StandardOpenOption.WRITE);
    } catch (IOException e) {
      return false;
    }

    return true;
  }
}
