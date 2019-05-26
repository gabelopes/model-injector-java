package br.unisinos.parthenos.injector.injector.java;

import br.unisinos.parthenos.injector.annotation.Language;
import br.unisinos.parthenos.injector.annotation.Model;
import br.unisinos.parthenos.injector.annotation.Name;
import br.unisinos.parthenos.injector.annotation.Target;
import br.unisinos.parthenos.injector.enumeration.Status;
import br.unisinos.parthenos.injector.exception.ClassAlreadyExistsException;
import br.unisinos.parthenos.injector.injector.Injector;
import br.unisinos.parthenos.injector.injector.java.io.Folder;
import br.unisinos.parthenos.injector.injector.java.io.PackageResolver;
import br.unisinos.parthenos.injector.injector.model.java.ClassCreationModel;
import br.unisinos.parthenos.injector.result.InjectResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.google.common.base.Strings;

import java.io.File;
import java.util.Arrays;

@Name("create-class")
@Language("java")
@Model(ClassCreationModel.class)
@Target(CompilationUnit.class)
public class ClassCreationInjector extends Injector<CompilationUnit, ClassCreationModel> {
  private static final String JAVA_EXTENSION = ".java";

  public ClassCreationInjector(ClassCreationModel model) {
    super(model);
  }

  private void addInterfaces(ClassOrInterfaceDeclaration classDeclaration) {
    final String[] interfaces = this.getModel().getInterfaces();

    if (interfaces == null) { return; }

    Arrays
      .stream(interfaces)
      .forEach(classDeclaration::addImplementedType);
  }

  private void addParent(ClassOrInterfaceDeclaration classDeclaration) {
    final String parent = this.getModel().getParent();

    if (Strings.isNullOrEmpty(parent)) { return; }

    classDeclaration.addExtendedType(parent);
  }

  private ClassOrInterfaceDeclaration createClass(CompilationUnit source) {
    return source.addClass(this.getModel().getName(), this.getModel().getModifiers());
  }

  private void addPackage(CompilationUnit source) {
    final String packageName = this.getModel().getPackageName();

    if (Strings.isNullOrEmpty(packageName)) { return; }

    source.setPackageDeclaration(packageName);
  }

  private CompilationUnit createSource() {
    return new CompilationUnit();
  }

  private String getFileName() {
    return this.getModel().getName() + JAVA_EXTENSION;
  }

  @Override
  public InjectResult<CompilationUnit> inject(CompilationUnit compilationUnit, File repositoryFolder) {
    final PackageResolver packageResolver = new PackageResolver(this.getModel().getQualifiedName(), repositoryFolder);
    final File packageFolder = packageResolver.findPackageFolder();
    final File classFile = Folder.resolve(packageFolder, this.getFileName());

    if (classFile.exists()) {
      throw new ClassAlreadyExistsException(this.getModel().getName());
    }

    final CompilationUnit source = this.createSource();
    final ClassOrInterfaceDeclaration classDeclaration = this.createClass(source);

    this.addPackage(source);
    this.addParent(classDeclaration);
    this.addInterfaces(classDeclaration);

    return new InjectResult<>(Status.SUCCESS, source, classFile);
  }
}
