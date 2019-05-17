package br.unisinos.parthenos.injector.parser.java;

import br.unisinos.parthenos.injector.annotation.Language;
import br.unisinos.parthenos.injector.annotation.Result;
import br.unisinos.parthenos.injector.enumeration.SourceLanguage;
import br.unisinos.parthenos.injector.parser.Parser;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.FileNotFoundException;

@Language(SourceLanguage.JAVA)
@Result(CompilationUnit.class)
public class JavaSyntacticTreeParser implements Parser<CompilationUnit> {
  @Override
  public CompilationUnit parse(File sourceFile) {
    try {
      return JavaParser.parse(sourceFile);
    } catch (FileNotFoundException e) {
      return null;
    }
  }
}
