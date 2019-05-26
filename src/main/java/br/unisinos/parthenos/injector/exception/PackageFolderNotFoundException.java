package br.unisinos.parthenos.injector.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

@Getter
@AllArgsConstructor
public class PackageFolderNotFoundException extends AbortedException {
  private String packagePart;
  private File folder;

  @Override
  public String getMessage() {
    return "Could not find folder '" + this.getPackagePart() + "' in folder " + this.getFolder().getAbsolutePath();
  }
}
