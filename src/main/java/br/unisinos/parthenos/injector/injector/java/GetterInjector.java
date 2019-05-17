package br.unisinos.parthenos.injector.injector.java;

import br.unisinos.parthenos.injector.annotation.Language;
import br.unisinos.parthenos.injector.annotation.Model;
import br.unisinos.parthenos.injector.annotation.Name;
import br.unisinos.parthenos.injector.annotation.Target;
import br.unisinos.parthenos.injector.enumeration.SourceLanguage;
import br.unisinos.parthenos.injector.exception.AttributeNotFoundException;
import br.unisinos.parthenos.injector.injector.model.java.AccessorModel;
import br.unisinos.parthenos.injector.representation.Text;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.Type;

import java.util.List;

@Name("getter")
@Language(SourceLanguage.JAVA)
@Model(AccessorModel.class)
@Target(CompilationUnit.class)
public class GetterInjector extends AccessorInjector<AccessorModel> {
  private static final String THIS_PREFIX = "this";
  private static final String GETTER_PREFIX = "get";

  public GetterInjector(AccessorModel model) {
    super(model);
  }

  @Override
  protected String getDefaultName(FieldDeclaration attributeDeclaration) {
    final String attributeVariableName = this.getAttributeVariableName(attributeDeclaration);

    return Text.prefixCamelCase(GETTER_PREFIX, attributeVariableName);
  }

  private ReturnStmt getReturnStatement(FieldDeclaration attributeDeclaration) {
    final String attributeVariableName = this.getAttributeVariableName(attributeDeclaration);

    if (attributeVariableName == null) { throw new AttributeNotFoundException(); }

    return new ReturnStmt(THIS_PREFIX + "." +  attributeVariableName);
  }

  @Override
  protected BlockStmt getBody(FieldDeclaration attributeDeclaration) {
    final BlockStmt blockStatement = new BlockStmt();

    blockStatement.addStatement(this.getReturnStatement(attributeDeclaration));

    return blockStatement;
  }

  @Override
  protected Type getReturn(FieldDeclaration attributeDeclaration) {
    return attributeDeclaration.getCommonType();
  }

  @Override
  protected Parameter[] getParameters(FieldDeclaration attributeDeclaration) {
    return new Parameter[0];
  }

  @Override
  protected boolean containsMethod(ClassOrInterfaceDeclaration classDeclaration, FieldDeclaration attributeDeclaration) {
    final String methodName = this.getMethodName(attributeDeclaration);
    final List<MethodDeclaration> methodsWithoutParameters = classDeclaration.getMethodsBySignature(methodName);

    return methodsWithoutParameters.size() > 0;
  }
}
