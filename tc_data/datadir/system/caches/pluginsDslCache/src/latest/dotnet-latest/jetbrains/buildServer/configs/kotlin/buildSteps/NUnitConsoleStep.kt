package jetbrains.buildServer.configs.kotlin.buildSteps

import jetbrains.buildServer.configs.kotlin.*

/**
 * A [build step](https://www.jetbrains.com/help/teamcity/?NUnit) running NUnit tests
 *
 * **Example.**
 * Runs [NUnit](https://nunit.org/) tests with help of the default NUnit console tool installed on the TeamCity server.
 * Collects сode coverage with help of [JetBrains DotCover](https://www.jetbrains.com/dotcover/) and reports it to the TeamCity server together with the test results.
 * ```
 * nunitConsole {
 *   nunitPath = "%teamcity.tool.NUnit.Console.DEFAULT%"
 *   includeTests = """tests\*.dll"""
 *   coverage = dotcover {
 *     assemblyFilters = "+:*"
 *   }
 * }
 * ```
 *
 *
 * @see nunitConsole
 */
open class NUnitConsoleStep() : BuildStep() {

    init {
        type = "nunit-console"
    }

    constructor(init: NUnitConsoleStep.() -> Unit): this() {
        init()
    }

    /**
     * [Build working directory](https://www.jetbrains.com/help/teamcity/?Build+Working+Directory) for
     * script,
     * specify it if it is different from the [checkout
     * directory](https://www.jetbrains.com/help/teamcity/?Build+Checkout+Directory).
     */
    var workingDir by stringParameter("teamcity.build.workingDir")

    /**
     * A path to NUnit console tool including the file name
     */
    var nunitPath by stringParameter("toolPath")

    /**
     * Comma- or newline-separated list of .NET assemblies where the NUnit tests are specified
     * relative to the checkout directory. Wildcards are supported.
     */
    var includeTests by stringParameter()

    /**
     * Comma- or newline-separated list of .NET assemblies which should be excluded
     * from the list of found assemblies to test.
     */
    var excludeTests by stringParameter()

    /**
     * Comma- or newline-separated list of NUnit categories.
     * [Category expressions](https://www.jetbrains.com/help/teamcity/?TeamCity+NUnit+Test+Launcher#TeamCityNUnitTestLauncher-CategoryExpression) are supported as well.
     */
    var includeCategories by stringParameter()

    /**
     * Comma- or newline-separated list of NUnit categories which should be excluded.
     * [Category expressions](https://www.jetbrains.com/help/teamcity/?TeamCity+NUnit+Test+Launcher#TeamCityNUnitTestLauncher-CategoryExpression) are supported as well.
     */
    var excludeCategories by stringParameter()

    /**
     * Whether TeamCity should run recently failed tests first to reduce test feedback
     */
    var reduceTestFeedback by booleanParameter("teamcity.tests.runRiskGroupTestsFirst", trueValue = "recentlyFailed", falseValue = "")

    /**
     * Whether TeamCity should create *.nunit test project files for each test assemblies location
     */
    var useProjectFile by booleanParameter(trueValue = "true", falseValue = "")

    /**
     * Enter additional command line parameters for nunit console.
     */
    var args by stringParameter("arguments")

    /**
     * Path to nunit configuration file.
     */
    var configFile by stringParameter()

    /**
     * Specifies coverage tool to use
     */
    var coverage by compoundParameter<Coverage>("dotNetCoverage.tool")

    sealed class Coverage(value: String? = null): CompoundParam<Coverage>(value) {
        class Dotcover() : Coverage("dotcover") {

            /**
             * Specify the path to dotCover CLT.
             */
            var toolPath by stringParameter("dotNetCoverage.dotCover.home.path")

            /**
             * Specify a new-line separated list of filters for code coverage.
             */
            var assemblyFilters by stringParameter("dotNetCoverage.dotCover.filters")

            /**
             * Specify a new-line separated list of attribute filters for code coverage.
             * Supported only with dotCover 2.0 or later.
             */
            var attributeFilters by stringParameter("dotNetCoverage.dotCover.attributeFilters")

            /**
             * Enter additional new-line separated command line parameters for dotCover.
             */
            var args by stringParameter("dotNetCoverage.dotCover.customCmd")

        }
    }

    fun dotcover(init: Coverage.Dotcover.() -> Unit = {}) : Coverage.Dotcover {
        val result = Coverage.Dotcover()
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
        if (includeTests == null && !hasParam("includeTests")) {
            consumer.consumePropertyError("includeTests", "mandatory 'includeTests' property is not specified")
        }
    }
}


/**
 * Adds a [build step](https://www.jetbrains.com/help/teamcity/?NUnit) running NUnit tests
 *
 * **Example.**
 * Runs [NUnit](https://nunit.org/) tests with help of the default NUnit console tool installed on the TeamCity server.
 * Collects сode coverage with help of [JetBrains DotCover](https://www.jetbrains.com/dotcover/) and reports it to the TeamCity server together with the test results.
 * ```
 * nunitConsole {
 *   nunitPath = "%teamcity.tool.NUnit.Console.DEFAULT%"
 *   includeTests = """tests\*.dll"""
 *   coverage = dotcover {
 *     assemblyFilters = "+:*"
 *   }
 * }
 * ```
 *
 *
 * @see NUnitConsoleStep
 */
fun BuildSteps.nunitConsole(init: NUnitConsoleStep.() -> Unit): NUnitConsoleStep {
    val result = NUnitConsoleStep(init)
    step(result)
    return result
}
