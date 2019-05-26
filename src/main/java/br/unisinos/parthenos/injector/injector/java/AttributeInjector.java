package br.unisinos.parthenos.injector.injector.java;

import br.unisinos.parthenos.injector.annotation.Language;
import br.unisinos.parthenos.injector.annotation.Model;
import br.unisinos.parthenos.injector.annotation.Name;
import br.unisinos.parthenos.injector.annotation.Target;
import br.unisinos.parthenos.injector.enumeration.Status;
import br.unisinos.parthenos.injector.exception.CannotCreateAttributeException;
import br.unisinos.parthenos.injector.injector.Injector;
import br.unisinos.parthenos.injector.injector.model.java.AttributeModel;
import br.unisinos.parthenos.injector.result.InjectResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;

import java.io.File;

@Name("attribute")
@Language("java")
@Model(AttributeModel.class)
@Target(CompilationUnit.class)
public class AttributeInjector extends Injector<CompilationUnit, AttributeModel> {
  public AttributeInjector(AttributeModel model) {
    super(model);
  }

  private ClassOrInterfaceDeclaration getTargetClass(CompilationUnit compilationUnit) {
    return compilationUnit
      .getClassByName(this.getModel().getTargetClassName())
      .orElse(null);
  }

  private boolean containsAttribute(ClassOrInterfaceDeclaration classDefinition) {
    final FieldDeclaration fieldDeclaration = classDefinition.getFieldByName(this.getModel().getName()).orElse(null);

    return fieldDeclaration != null;
  }

  private boolean canAddAttribute(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
    return classOrInterfaceDeclaration != null
        && !classOrInterfaceDeclaration.isInterface()
        && !this.containsAttribute(classOrInterfaceDeclaration);
  }

  @Override
  public InjectResult<CompilationUnit> inject(CompilationUnit parsedSource, File sourceFile) {
    final ClassOrInterfaceDeclaration classDefinition = this.getTargetClass(parsedSource);

    if (!this.canAddAttribute(classDefinition)) {
      throw new CannotCreateAttributeException(this.getModel().getName(), this.getModel().getTargetClassName());
    }

    classDefinition.addField(this.getModel().getType(), this.getModel().getName(), this.getModel().getModifiers());

    return new InjectResult<>(Status.SUCCESS, parsedSource, sourceFile);
  }
}
