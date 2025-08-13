package com.donesvad.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class GitUtil {

  public static Path mkTempWorkdir() throws IOException {
    Path dir = Files.createTempDirectory("dsl-repo-" + UUID.randomUUID());
    dir.toFile().deleteOnExit();
    return dir;
  }

  public static void run(Path workdir, List<String> cmd) throws IOException, InterruptedException {
    ProcessBuilder pb = new ProcessBuilder(cmd);
    pb.directory(workdir.toFile());
    pb.redirectErrorStream(true);
    Process p = pb.start();
    String out = new String(p.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    int code = p.waitFor();
    if (code != 0) {
      throw new IllegalStateException("Cmd failed: " + String.join(" ", cmd) + "\n" + out);
    }
  }

  public static void gitClone(
      Path workdir, String repoUrl, String branch, String user, String token) throws Exception {
    String url = repoUrl;
    if (repoUrl.startsWith("https://")
        && user != null
        && !user.isBlank()
        && token != null
        && !token.isBlank()) {
      // embed credentials safely for this process (runner environment)
      String withoutProto = repoUrl.substring("https://".length());
      url = "https://" + user + ":" + token + "@" + withoutProto;
    }
    run(
        workdir,
        List.of(
            "bash",
            "-lc",
            "git clone --depth 1 --single-branch --branch " + sh(branch) + " " + sh(url) + " ."));
  }

  public static void gitConfigUser(Path workdir, String name, String email) throws Exception {
    run(workdir, List.of("bash", "-lc", "git config user.name " + sh(name)));
    run(workdir, List.of("bash", "-lc", "git config user.email " + sh(email)));
  }

  public static void gitCommitAll(Path workdir, String message) throws Exception {
    run(workdir, List.of("bash", "-lc", "git add -A"));
    run(workdir, List.of("bash", "-lc", "git commit -m " + sh(message)));
  }

  public static void gitPush(Path workdir) throws Exception {
    run(workdir, List.of("bash", "-lc", "git push"));
  }

  public static String slurp(Path file) throws IOException {
    return Files.readString(file, StandardCharsets.UTF_8);
  }

  public static void write(Path file, String content) throws IOException {
    Files.createDirectories(file.getParent());
    Files.writeString(
        file,
        content,
        StandardCharsets.UTF_8,
        StandardOpenOption.TRUNCATE_EXISTING,
        StandardOpenOption.CREATE);
  }

  private static String sh(String s) {
    return "'" + s.replace("'", "'\"'\"'") + "'";
  }
}
