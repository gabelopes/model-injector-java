package br.unisinos.parthenos.injector.injector.java.io;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

public class Folder {
  private static String getFileName(File file) {
    final Path filePath = file.toPath();
    return filePath.getFileName().toString();
  }

  public static boolean containsFile(File folder, String fileName) {
    if (!folder.isDirectory()) { return false; }

    return Arrays
      .stream(Objects.requireNonNull(folder.listFiles(File::isFile)))
      .map(Folder::getFileName)
      .anyMatch(fileName::equals);
  }

  public static File resolve(File folder, File file) {
    final Path folderPath = folder.toPath();
    final Path filePath = file.toPath();

    return folderPath.resolve(filePath).toFile();
  }

  public static File resolve(File folder, String fileName) {
    return Folder.resolve(folder, new File(fileName));
  }
}
