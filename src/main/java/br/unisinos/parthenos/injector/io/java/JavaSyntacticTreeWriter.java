package br.unisinos.parthenos.injector.io.java;

import br.unisinos.parthenos.injector.annotation.Language;
import br.unisinos.parthenos.injector.annotation.Target;
import br.unisinos.parthenos.injector.enumeration.Status;
import br.unisinos.parthenos.injector.io.Writer;
import br.unisinos.parthenos.injector.result.InjectResult;
import br.unisinos.parthenos.injector.result.WriteResult;
import com.github.javaparser.ast.CompilationUnit;

import java.io.IOException;
import java.nio.file.Files;

import static java.nio.file.StandardOpenOption.*;

@Language("java")
@Target(CompilationUnit.class)
public class JavaSyntacticTreeWriter implements Writer<CompilationUnit> {
  @Override
  public WriteResult write(InjectResult<?> injectResult) {
    try {
      Files.createDirectories(injectResult.getOutputFile().toPath().getParent());
      Files.write(injectResult.getOutputFile().toPath(), injectResult.getOutput().toString().getBytes(), CREATE, WRITE, TRUNCATE_EXISTING);
    } catch (IOException exception) {
      return new WriteResult(Status.EXCEPTION, exception.getMessage());
    }

    return new WriteResult(Status.SUCCESS, injectResult.getOutputFile().getAbsolutePath());
  }
}
