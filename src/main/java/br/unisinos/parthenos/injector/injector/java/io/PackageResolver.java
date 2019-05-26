package br.unisinos.parthenos.injector.injector.java.io;

import br.unisinos.parthenos.injector.injector.java.representation.QualifiedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

@Getter
@AllArgsConstructor
public class PackageResolver {
  private QualifiedName qualifiedName;
  private File repositoryFolder;

  private File[] getSubFolders(File folder) {
    return folder.listFiles(File::isDirectory);
  }

  private boolean isWantedFolder(File folder, String name) {
    final Path folderName = folder.toPath().getFileName();
    return folderName.toString().equals(name);
  }

  private File getFolderForPackage() {
    final String packageName = String.join(File.separator, this.getQualifiedName().getPackageParts());
    return Folder.resolve(this.getRepositoryFolder(), packageName);
  }

  private File findPackageFolder(File rootFolder, Stack<String> packageParts) {
    final File[] childrenFolders = this.getSubFolders(rootFolder);
    final String packagePart = packageParts.pop();

    for (File childFolder : childrenFolders) {
      if (!this.isWantedFolder(childFolder, packagePart)) { continue; }

      if (packageParts.empty()) {
        return childFolder;
      }

      return this.findPackageFolder(childFolder, packageParts);
    }

    return null;
  }

  private File findInitialFolder(Queue<File> currentFolderQueue, String initialName) {
    final Queue<File> nextFolderQueue = new ArrayDeque<>();
    File folder = currentFolderQueue.poll();

    while (folder != null) {
      if (this.isWantedFolder(folder, initialName)) {
        return folder;
      }

      Arrays
        .stream(this.getSubFolders(folder))
        .forEach(nextFolderQueue::offer);

      folder = currentFolderQueue.poll();
    }

    if (nextFolderQueue.isEmpty()) {
      return null;
    }

    return findInitialFolder(nextFolderQueue, initialName);
  }

  private File findInitialFolder(String initialName) {
    final Queue<File> folderQueue = new ArrayDeque<>(Collections.singleton(this.getRepositoryFolder()));
    return this.findInitialFolder(folderQueue, initialName);
  }

  private Stack<String> getPackageParts() {
    final Stack<String> packageParts = new Stack<>();

    Arrays
      .stream(this.getQualifiedName().getPackageParts())
      .sorted(Comparator.reverseOrder())
      .forEach(packageParts::push);

    return packageParts;
  }

  public File findPackageFolder() {
    final Stack<String> packageParts = this.getPackageParts();

    if (packageParts.empty()) {
      return this.getRepositoryFolder();
    }

    final File initialFolder = this.findInitialFolder(packageParts.pop());

    if (initialFolder == null) {
      return this.getFolderForPackage();
    }

    final File packageFolder = this.findPackageFolder(initialFolder, packageParts);

    if (packageFolder == null) {
      return this.getFolderForPackage();
    }

    return packageFolder;
  }
}
