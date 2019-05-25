package br.unisinos.parthenos.injector.injector.java;

import br.unisinos.parthenos.injector.annotation.Language;
import br.unisinos.parthenos.injector.annotation.Model;
import br.unisinos.parthenos.injector.annotation.Name;
import br.unisinos.parthenos.injector.annotation.Target;
import br.unisinos.parthenos.injector.injector.Injector;
import br.unisinos.parthenos.injector.injector.model.java.AttributeModel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;

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
  public boolean inject(CompilationUnit parsedSource) {
    final ClassOrInterfaceDeclaration classDefinition = this.getTargetClass(parsedSource);

    if (!this.canAddAttribute(classDefinition)) { return false; }

    classDefinition.addField(this.getModel().getType(), this.getModel().getName(), this.getModel().getModifiers());

    return true;
  }
}
