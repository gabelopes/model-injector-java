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
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;

import java.util.List;

@Name("setter")
@Language(SourceLanguage.JAVA)
@Model(AccessorModel.class)
@Target(CompilationUnit.class)
public class SetterInjector extends AccessorInjector<AccessorModel> {
  private static final String THIS_PREFIX = "this";
  private static final String SETTER_PREFIX = "set";

  public SetterInjector(AccessorModel model) {
    super(model);
  }

  @Override
  protected String getDefaultName(FieldDeclaration attributeDeclaration) {
    final String attributeVariableName = this.getAttributeVariableName(attributeDeclaration);

    return Text.prefixCamelCase(SETTER_PREFIX, attributeVariableName);
  }

  private AssignExpr getAssignExpression(FieldDeclaration attributeDeclaration) {
    final String attributeVariableName = this.getAttributeVariableName(attributeDeclaration);

    if (attributeVariableName == null) { throw new AttributeNotFoundException(); }

    final NameExpr classAttributeExpression = new NameExpr(THIS_PREFIX + "." +  attributeVariableName);
    final NameExpr parameterExpression = new NameExpr(attributeVariableName);

    return new AssignExpr(classAttributeExpression, parameterExpression, AssignExpr.Operator.ASSIGN);
  }

  @Override
  protected BlockStmt getBody(FieldDeclaration attributeDeclaration) {
    final BlockStmt blockStatement = new BlockStmt();

    blockStatement.addStatement(this.getAssignExpression(attributeDeclaration));

    return blockStatement;
  }

  @Override
  protected Parameter[] getParameters(FieldDeclaration attributeDeclaration) {
    final String attributeVariableName = this.getAttributeVariableName(attributeDeclaration);

    if (attributeVariableName == null) { throw new AttributeNotFoundException(); }

    final Parameter parameter = new Parameter(attributeDeclaration.getCommonType(), attributeVariableName);

    return new Parameter[] { parameter };
  }

  @Override
  protected boolean containsMethod(ClassOrInterfaceDeclaration classDeclaration, FieldDeclaration attributeDeclaration) {
    final String methodName = this.getMethodName(attributeDeclaration);
    final List<MethodDeclaration> methodsWithType = classDeclaration.getMethodsBySignature(methodName, attributeDeclaration.getCommonType().asString());

    return methodsWithType.size() > 0;
  }
}
