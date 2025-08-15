package jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps

import jetbrains.buildServer.configs.kotlin.v2018_2.*

/**
 * A [build step](https://www.jetbrains.com/help/teamcity/?Gradle) running gradle script
 *
 * **Example.**
 * Adds a simple Gradle step with custom tasks and a build file determined by Gradle.
 * The Gradle Wrapper located in the checkout directory is used.
 * ```
 * gradle {
 *     name = "Build myproject"
 *     tasks = ":myproject:clean :myproject:build"
 * }
 * ```
 *
 * **Example.**
 * Add a Gradle build step with a custom Gradle task and a build file in a custom
 * [working directory](https://www.jetbrains.com/help/teamcity/?Build+Working+Directory).
 * Gradle incremental building feature is enabled.
 * Additional Gradle command line parameters are specified with a reference to a
 * [configuration parameter](https://www.jetbrains.com/help/teamcity/?Using+Build+Parameters).
 * Gralde build step is set up not to use Gradle Wrapper, so Gradle will be taken from the agent's GRADLE_HOME environment variable.
 * Additional [run parameter](https://www.jetbrains.com/help/teamcity/?Gradle#Run+Parameters) for printing stacktrace is enabled.
 * This step will be run inside a [Docker](https://www.jetbrains.com/help/teamcity/?Gradle#Docker+Settings) container.
 * IDEA-based [code coverage](https://www.jetbrains.com/help/teamcity/?Gradle#Code+Coverage) is enabled.
 * ```
 * gradle {
 *     name = "Test my project in Docker"
 *
 *     tasks = "clean test"
 *     buildFile = "build-test.gradle"
 *     incremental = true
 *     workingDir = "tests/"
 *     gradleParams = "%myproject.version%"
 *
 *     useGradleWrapper = false
 *
 *     enableStacktrace = true
 *
 *     coverageEngine = idea {
 *         includeClasses = """
 *             org.group.example.*
 *             org.group.common
 *         """.trimIndent()
 *         excludeClasses = "org.group.common.test.*"
 *     }
 *
 *     dockerImage = "gradle:jdk11"
 *     dockerImagePlatform = GradleBuildStep.ImagePlatform.Linux
 * }
 * ```
 *
 * **Example.**
 * Adds a Gradle build step with 'default' Gradle task and custom Gradle build file.
 * Gradle Wrapper using is disabled, so Gradle will be taken with reference to an
 * [environment variable](https://www.jetbrains.com/help/teamcity/?Using+Build+Parameters).
 * JDK is set to the [environment variable](https://www.jetbrains.com/help/teamcity/?Using+Build+Parameters) value
 * with custom command line [parameters](https://www.jetbrains.com/help/teamcity/?Gradle#Java+Parameters).
 * This build step will run even if some previous build steps failed.
 * ```
 * gradle {
 *     name = "Default run on JDK 11"
 *     executionMode = BuildStep.ExecutionMode.RUN_ON_FAILURE
 *
 *     buildFile = "build-dev.gradle"
 *     gradleHome = "%env.GRADLE_DEV_HOME%"
 *     useGradleWrapper = false
 *
 *     jdkHome = "%env.JDK_11_0%"
 *     jvmArgs = "-Xmx2048m"
 * }
 * ```
 *
 *
 * @see gradle
 */
open class GradleBuildStep() : BuildStep() {

    init {
        type = "gradle-runner"
    }

    constructor(init: GradleBuildStep.() -> Unit): this() {
        init()
    }

    /**
     * Space-separated task names. TeamCity runs the 'default' task if this field is empty.
     */
    var tasks by stringParameter("ui.gradleRunner.gradle.tasks.names")

    /**
     * The path to a custom Gradle build file. Leave this field empty if your build file is build.gradle located in the root directory.
     * This property is deprecated for Gradle versions 9.0 and higher, use the additional `-p <path-relative-to-checkout-directory>` command line parameter instead.
     */
    var buildFile by stringParameter("ui.gradleRUnner.gradle.build.file")

    /**
     * Enable this option to allow TeamCity to detect Gradle modules affected by a modified build, and run the :buildDependents only for these affected modules.
     */
    var incremental by booleanParameter("ui.gradleRunner.gradle.incremental", trueValue = "true", falseValue = "")

    /**
     * Custom working directory for the Gradle script
     */
    var workingDir by stringParameter("teamcity.build.workingDir")

    /**
     * The path to a custom Gradle version. This version will be used instead of the default Gradle version referenced by the GRADLE_HOME environment variable.
     */
    var gradleHome by stringParameter("ui.gradleRunner.gradle.home")

    /**
     * Optional space-separated command-line parameters
     */
    var gradleParams by stringParameter("ui.gradleRunner.additional.gradle.cmd.params")

    /**
     * Enable this setting if TeamCity should look for a [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) script in the project directory.
     */
    var useGradleWrapper by booleanParameter("ui.gradleRunner.gradle.wrapper.useWrapper", trueValue = "true", falseValue = "")

    /**
     * The path (relative to the working directory) to a Gradle Wrapper script
     */
    var gradleWrapperPath by stringParameter("ui.gradleRunner.gradle.wrapper.path")

    /**
     * Runs Gradle with the 'debug' (-d) log level. See also: [Logging Sensitive Information](https://docs.gradle.org/current/userguide/logging.html#sec:debug_security).
     */
    var enableDebug by booleanParameter("ui.gradleRunner.gradle.debug.enabled", trueValue = "true", falseValue = "")

    /**
     * Allows Gradle to print [truncated stacktraces](https://docs.gradle.org/current/userguide/logging.html#stacktraces).
     */
    var enableStacktrace by booleanParameter("ui.gradleRunner.gradle.stacktrace.enabled", trueValue = "true", falseValue = "")

    /**
     * Custom [JDK](https://www.jetbrains.com/help/teamcity/?Predefined+Build+Parameters#PredefinedBuildParameters-DefiningJava-relatedEnvironmentVariables) to use.
     * The default is JAVA_HOME environment variable or the agent's own Java.
     */
    var jdkHome by stringParameter("target.jdk.home")

    /**
     * Space-separated list of additional arguments for JVM
     */
    var jvmArgs by stringParameter()

    /**
     * Specifies coverage engine to use
     */
    var coverageEngine by compoundParameter<CoverageEngine>("teamcity.coverage.runner")

    sealed class CoverageEngine(value: String? = null): CompoundParam<CoverageEngine>(value) {
        class Idea() : CoverageEngine("IDEA") {

            /**
             * Newline-separated patterns for fully qualified class names to be analyzed by code coverage.
             * A pattern should start with a valid package name and can contain a wildcard, for example: org.apache.*
             */
            var includeClasses by stringParameter("teamcity.coverage.idea.includePatterns")

            /**
             * Newline-separated patterns for fully qualified class names to be excluded from the coverage. Exclude patterns have priority over include patterns.
             */
            var excludeClasses by stringParameter("teamcity.coverage.idea.excludePatterns")

        }

        class Jacoco() : CoverageEngine("JACOCO") {

            /**
             * Newline-delimited set of path patterns in the form of +|-:path to scan for classfiles to be analyzed.
             * Excluding libraries and test classes from analysis is recommended. Ant like patterns are supported.
             */
            var classLocations by stringParameter("teamcity.coverage.jacoco.classpath")

            /**
             * Newline-separated patterns for fully qualified class names to be excluded from the coverage.
             * Exclude patterns have priority over include patterns.
             */
            var excludeClasses by stringParameter("teamcity.coverage.jacoco.patterns")

            /**
             * JaCoCo version to use
             */
            var jacocoVersion by stringParameter("teamcity.tool.jacoco")

        }
    }

    fun idea(init: CoverageEngine.Idea.() -> Unit = {}) : CoverageEngine.Idea {
        val result = CoverageEngine.Idea()
        result.init()
        return result
    }

    fun jacoco(init: CoverageEngine.Jacoco.() -> Unit = {}) : CoverageEngine.Jacoco {
        val result = CoverageEngine.Jacoco()
        result.init()
        return result
    }

    /**
     * Specifies which Docker image to use for running this build step. I.e. the build step will be run inside specified docker image, using 'docker run' wrapper.
     */
    var dockerImage by stringParameter("plugin.docker.imageId")

    /**
     * Specifies which Docker image platform will be used to run this build step.
     */
    var dockerImagePlatform by enumParameter<ImagePlatform>("plugin.docker.imagePlatform", mapping = ImagePlatform.mapping)

    /**
     * If enabled, "docker pull [image][dockerImage]" will be run before docker run.
     */
    var dockerPull by booleanParameter("plugin.docker.pull.enabled", trueValue = "true", falseValue = "")

    /**
     * Additional docker run command arguments
     */
    var dockerRunParameters by stringParameter("plugin.docker.run.parameters")

    /**
     * Docker image platforms
     */
    enum class ImagePlatform {
        Any,
        Linux,
        Windows;

        companion object {
            val mapping = mapOf<ImagePlatform, String>(Any to "", Linux to "linux", Windows to "windows")
        }

    }
    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * Adds a [build step](https://www.jetbrains.com/help/teamcity/?Gradle) running gradle script
 *
 * **Example.**
 * Adds a simple Gradle step with custom tasks and a build file determined by Gradle.
 * The Gradle Wrapper located in the checkout directory is used.
 * ```
 * gradle {
 *     name = "Build myproject"
 *     tasks = ":myproject:clean :myproject:build"
 * }
 * ```
 *
 * **Example.**
 * Add a Gradle build step with a custom Gradle task and a build file in a custom
 * [working directory](https://www.jetbrains.com/help/teamcity/?Build+Working+Directory).
 * Gradle incremental building feature is enabled.
 * Additional Gradle command line parameters are specified with a reference to a
 * [configuration parameter](https://www.jetbrains.com/help/teamcity/?Using+Build+Parameters).
 * Gralde build step is set up not to use Gradle Wrapper, so Gradle will be taken from the agent's GRADLE_HOME environment variable.
 * Additional [run parameter](https://www.jetbrains.com/help/teamcity/?Gradle#Run+Parameters) for printing stacktrace is enabled.
 * This step will be run inside a [Docker](https://www.jetbrains.com/help/teamcity/?Gradle#Docker+Settings) container.
 * IDEA-based [code coverage](https://www.jetbrains.com/help/teamcity/?Gradle#Code+Coverage) is enabled.
 * ```
 * gradle {
 *     name = "Test my project in Docker"
 *
 *     tasks = "clean test"
 *     buildFile = "build-test.gradle"
 *     incremental = true
 *     workingDir = "tests/"
 *     gradleParams = "%myproject.version%"
 *
 *     useGradleWrapper = false
 *
 *     enableStacktrace = true
 *
 *     coverageEngine = idea {
 *         includeClasses = """
 *             org.group.example.*
 *             org.group.common
 *         """.trimIndent()
 *         excludeClasses = "org.group.common.test.*"
 *     }
 *
 *     dockerImage = "gradle:jdk11"
 *     dockerImagePlatform = GradleBuildStep.ImagePlatform.Linux
 * }
 * ```
 *
 * **Example.**
 * Adds a Gradle build step with 'default' Gradle task and custom Gradle build file.
 * Gradle Wrapper using is disabled, so Gradle will be taken with reference to an
 * [environment variable](https://www.jetbrains.com/help/teamcity/?Using+Build+Parameters).
 * JDK is set to the [environment variable](https://www.jetbrains.com/help/teamcity/?Using+Build+Parameters) value
 * with custom command line [parameters](https://www.jetbrains.com/help/teamcity/?Gradle#Java+Parameters).
 * This build step will run even if some previous build steps failed.
 * ```
 * gradle {
 *     name = "Default run on JDK 11"
 *     executionMode = BuildStep.ExecutionMode.RUN_ON_FAILURE
 *
 *     buildFile = "build-dev.gradle"
 *     gradleHome = "%env.GRADLE_DEV_HOME%"
 *     useGradleWrapper = false
 *
 *     jdkHome = "%env.JDK_11_0%"
 *     jvmArgs = "-Xmx2048m"
 * }
 * ```
 *
 *
 * @see GradleBuildStep
 */
fun BuildSteps.gradle(init: GradleBuildStep.() -> Unit): GradleBuildStep {
    val result = GradleBuildStep(init)
    step(result)
    return result
}
