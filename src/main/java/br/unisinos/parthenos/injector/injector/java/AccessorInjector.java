package br.unisinos.parthenos.injector.injector.java;

import br.unisinos.parthenos.injector.exception.AttributeNotFoundException;
import br.unisinos.parthenos.injector.injector.Injector;
import br.unisinos.parthenos.injector.injector.model.java.AccessorModel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.VoidType;

public abstract class AccessorInjector<M extends AccessorModel> extends Injector<CompilationUnit, M> {
  public AccessorInjector(M model) {
    super(model);
  }

  protected abstract String getDefaultName(FieldDeclaration attributeDeclaration);
  protected abstract BlockStmt getBody(FieldDeclaration attributeDeclaration);
  protected abstract Parameter[] getParameters(FieldDeclaration attributeDeclaration);
  protected abstract boolean containsMethod(ClassOrInterfaceDeclaration classDeclaration, FieldDeclaration attributeDeclaration);

  private NodeList<Parameter> getParametersNodeList(FieldDeclaration attributeDeclaration) {
    return new NodeList<>(this.getParameters(attributeDeclaration));
  }

  protected Type getReturn(FieldDeclaration attributeDeclaration) {
    return new VoidType();
  }

  protected Modifier[] getMethodModifiers() {
    final Modifier[] modifiers = this.getModel().getModifiers();

    if (modifiers == null) {
      return new Modifier[] { Modifier.PUBLIC };
    }

    return modifiers;
  }

  protected String getMethodName(FieldDeclaration attributeDeclaration) {
    String name = this.getModel().getName();

    if (name == null) {
      name = this.getDefaultName(attributeDeclaration);
    }

    if (name == null) {
      throw new AttributeNotFoundException();
    }

    return name;
  }

  private void addMethod(ClassOrInterfaceDeclaration classDeclaration) {
    final FieldDeclaration attributeDeclaration = this.getAttribute(classDeclaration);
    final String methodName = this.getMethodName(attributeDeclaration);
    final MethodDeclaration methodDeclaration = classDeclaration.addMethod(methodName, this.getMethodModifiers());

    methodDeclaration.setType(this.getReturn(attributeDeclaration));
    methodDeclaration.setParameters(this.getParametersNodeList(attributeDeclaration));
    methodDeclaration.setBody(this.getBody(attributeDeclaration));
  }

  private VariableDeclarator getAttributeVariable(FieldDeclaration fieldDeclaration) {
    return fieldDeclaration.getVariables()
      .stream()
      .findFirst()
      .orElse(null);
  }

  protected String getAttributeVariableName(FieldDeclaration attributeDeclaration) {
    final VariableDeclarator attributeVariable = this.getAttributeVariable(attributeDeclaration);

    if (attributeVariable == null) { return null; }

    return attributeVariable.getNameAsString();
  }

  protected FieldDeclaration getAttribute(ClassOrInterfaceDeclaration classDeclaration) {
    return classDeclaration.getFieldByName(this.getModel().getAttributeName()).orElse(null);
  }

  private boolean containsAttribute(ClassOrInterfaceDeclaration classDeclaration) {
    return this.getAttribute(classDeclaration) != null;
  }

  private boolean canAddMethod(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
    if (classOrInterfaceDeclaration == null) { return false; }

    final FieldDeclaration attributeDeclaration = this.getAttribute(classOrInterfaceDeclaration);

    return !classOrInterfaceDeclaration.isInterface()
        && !this.containsMethod(classOrInterfaceDeclaration, attributeDeclaration)
        && this.containsAttribute(classOrInterfaceDeclaration);
  }

  private ClassOrInterfaceDeclaration getTargetClass(CompilationUnit parsedSource) {
    return parsedSource.getClassByName(this.getModel().getClassName()).orElse(null);
  }

  @Override
  public boolean inject(CompilationUnit parsedSource) {
    final ClassOrInterfaceDeclaration classDeclaration = this.getTargetClass(parsedSource);

    if (!this.canAddMethod(classDeclaration)) { return false; }

    this.addMethod(classDeclaration);

    return true;
  }
}
