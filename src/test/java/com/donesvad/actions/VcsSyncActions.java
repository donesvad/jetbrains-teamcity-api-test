package com.donesvad.actions;

import com.donesvad.configuration.TestConfig;
import com.donesvad.util.RandomUtil;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.transport.TransportHttp;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Component;

/**
 * Actions focused on VCS -> Server synchronization scenarios. Encapsulates Git operations and
 * waiting logic to keep tests concise.
 */
@Component
@RequiredArgsConstructor
public class VcsSyncActions {

  private static final String REFS_HEADS = "refs/heads/";
  private static final String TC_SYNC = "tc-sync-";
  private static final String ORIGIN = "origin";
  private final TestConfig config;

  // Track created temp directories and temporary branches for cleanup
  private final List<Path> createdTempDirs = Collections.synchronizedList(new ArrayList<>());
  private final List<String> createdTempBranches = Collections.synchronizedList(new ArrayList<>());

  private static String ensureBranchRef(String branchName) {
    if (branchName.startsWith(REFS_HEADS)) return branchName;
    return REFS_HEADS + branchName;
  }

  private static String stripRefPrefix(String branchRef) {
    if (branchRef.startsWith(REFS_HEADS)) return branchRef.substring(REFS_HEADS.length());
    return branchRef;
  }

  private static Git cloneRepo(Path dir, String url, String username, String token)
      throws GitAPIException {
    if (url == null || !url.startsWith("https://")) {
      throw new IllegalArgumentException("Expected https URL, got: " + url);
    }
    if (username == null || username.isBlank()) username = "git"; // GitHub accepts any non-empty
    UsernamePasswordCredentialsProvider creds =
        new UsernamePasswordCredentialsProvider(username, token);

    return Git.cloneRepository()
        .setURI(url)
        .setDirectory(dir.toFile())
        .setCredentialsProvider(creds)
        .setCloneAllBranches(true)
        .setTransportConfigCallback(
            (Transport t) -> {
              if (!(t instanceof TransportHttp)) {
                throw new IllegalStateException("URL rewrite to non-HTTPS detected: " + t.getURI());
              }
            })
        .call();
  }

  private Path createTempDirTracked() throws IOException {
    Path dir = Files.createTempDirectory(TC_SYNC);
    createdTempDirs.add(dir);
    return dir;
  }

  private static void replaceWithResource(Path file, String resourcePath) throws IOException {
    byte[] content = readResource(resourcePath);
    Files.write(file, content);
  }

  private static byte[] readResource(String resourcePath) throws IOException {
    try (InputStream is = VcsSyncActions.class.getResourceAsStream(resourcePath)) {
      if (is == null) {
        throw new IOException("Resource not found: " + resourcePath);
      }
      return is.readAllBytes();
    }
  }

  /**
   * Create a temporary branch from the configured source branch without applying any file changes,
   * and push it to origin. Returns full ref (refs/heads/{branch}).
   */
  public String createAndPushTempBranchFromSource() throws IOException, GitAPIException {
    String sourceBranchRef = config.getDslRepoBranch();
    String tempBranchName = RandomUtil.randomBranchName(TC_SYNC);
    String tempBranchRef = ensureBranchRef(tempBranchName);

    Path workdir = createTempDirTracked();
    try (Git git =
        cloneRepo(workdir, config.getDslRepoUrl(), config.getVcsUsername(), config.getVcsToken())) {
      git.checkout()
          .setName(stripRefPrefix(sourceBranchRef))
          .setStartPoint(stripRefPrefix(sourceBranchRef))
          .call();
      git.branchCreate().setName(tempBranchName).call();
      git.checkout().setName(tempBranchName).call();
      // Push the new branch pointing to same commit
      git.push()
          .setCredentialsProvider(
              new UsernamePasswordCredentialsProvider(
                  config.getVcsUsername(), config.getVcsToken()))
          .setRemote(ORIGIN)
          .add(tempBranchName)
          .call();
    }
    // Track created temporary branch for later cleanup
    createdTempBranches.add(tempBranchRef);
    return tempBranchRef;
  }

  /** Push prepared change to a specific branch (refs/heads/...). */
  public void pushChangeToBranch(String branchRef, String resourcePath)
      throws IOException, GitAPIException {
    Path workdir = createTempDirTracked();
    try (Git git =
        cloneRepo(workdir, config.getDslRepoUrl(), config.getVcsUsername(), config.getVcsToken())) {
      // checkout target branch (must exist on remote)
      String name = stripRefPrefix(branchRef);
      git.checkout().setCreateBranch(true).setName(name).setStartPoint(ORIGIN + "/" + name).call();

      String filePath = ".teamcity/patches/buildTypes/Build.kts";
      String[] filePathArray = filePath.split("/");
      // Replace file content with provided resource
      Path projectConfig =
          workdir
              .resolve(filePathArray[0])
              .resolve(filePathArray[1])
              .resolve(filePathArray[2])
              .resolve(filePathArray[3]);
      replaceWithResource(projectConfig, resourcePath);

      git.add().addFilepattern(filePath).call();
      git.commit().setMessage("[test] VCS->Server sync change (temp branch)").call();

      // Push to the specified branch
      git.push()
          .setCredentialsProvider(
              new UsernamePasswordCredentialsProvider(
                  config.getVcsUsername(), config.getVcsToken()))
          .setRemote(ORIGIN)
          .add(name)
          .call();
    }
  }

  /**
   * Cleanup temporary artifacts created during sync tests: remote temp branches and local temp dirs.
   * Safe to call multiple times; ignores errors.
   */
  public void cleanupTempArtifacts() {
    // Delete remote branches
    List<String> branches;
    synchronized (createdTempBranches) {
      branches = new ArrayList<>(createdTempBranches);
      createdTempBranches.clear();
    }
    for (String branchRef : branches) {
      try {
        deleteRemoteBranch(branchRef);
      } catch (Exception ignored) {
        // ignore cleanup errors
      }
    }
    // Delete local temp directories
    List<Path> dirs;
    synchronized (createdTempDirs) {
      dirs = new ArrayList<>(createdTempDirs);
      createdTempDirs.clear();
    }
    for (Path dir : dirs) {
      try {
        deleteDirectoryRecursive(dir);
      } catch (Exception ignored) {
        // ignore cleanup errors
      }
    }
  }

  private void deleteRemoteBranch(String branchRef) throws IOException, GitAPIException {
    String name = stripRefPrefix(branchRef);
    Path workdir = createTempDirTracked();
    try (Git git =
        cloneRepo(workdir, config.getDslRepoUrl(), config.getVcsUsername(), config.getVcsToken())) {
      RefSpec deleteSpec = new RefSpec(":" + REFS_HEADS + name);
      git.push()
          .setCredentialsProvider(
              new UsernamePasswordCredentialsProvider(
                  config.getVcsUsername(), config.getVcsToken()))
          .setRemote(ORIGIN)
          .setRefSpecs(deleteSpec)
          .call();
    }
  }

  private static void deleteDirectoryRecursive(Path path) throws IOException {
    if (path == null || !Files.exists(path)) return;
    Files.walkFileTree(
        path,
        new SimpleFileVisitor<>() {
          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
              throws IOException {
            Files.deleteIfExists(file);
            return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Files.deleteIfExists(dir);
            return FileVisitResult.CONTINUE;
          }
        });
  }
}
