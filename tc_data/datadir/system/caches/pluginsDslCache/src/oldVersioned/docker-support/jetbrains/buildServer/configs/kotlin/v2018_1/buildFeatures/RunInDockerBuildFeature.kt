package jetbrains.buildServer.configs.kotlin.v2018_1.buildFeatures

import jetbrains.buildServer.configs.kotlin.v2018_1.*

/**
 * Runs all build steps of a configuration in the specified container.
 * Steps that cannot run [inside the container](https://www.jetbrains.com/help/teamcity/container-wrapper.html) are executed outside of it.
 * If the image registry requires authorization, enable the Docker Registry Connections build feature.
 *
 * **Example.**
 * 
 * ```
 * buildType {
 *     name = "Build in Docker"
 *
 *     features {
 *         runInDocker {
 *             dockerImage = "busybox"
 *         }
 *     }
 *
 *     steps {
 *         script {
 *             scriptContent = "echo this step is running inside the container"
 *         }
 *         script {
 *             scriptContent = "echo and this step is running in the same container""
 *         }
 *     }
 * }
 * ```
 *
 *
 * @see runInDocker
 */
open class RunInDockerBuildFeature() : BuildFeature() {

    init {
        type = "RunInDocker"
    }

    constructor(init: RunInDockerBuildFeature.() -> Unit): this() {
        init()
    }

    /**
     * Image name to use for running build steps, for example, "ubuntu:latest".
     * The image will be pulled via "docker pull" or "podman pull" commands,
     * depending on which container manager is installed on the agent that runs the build.
     */
    var dockerImage by stringParameter("plugin.docker.imageId")

    /**
     * Select a specific image OS platform. Limits the pool of compatible agents to those running on this platform.
     */
    var dockerImagePlatform by enumParameter<ImagePlatform>("plugin.docker.imagePlatform", mapping = ImagePlatform.mapping)

    /**
     * If enabled, TeamCity will explicitly pull the target image every time the build is about to start.
     */
    var dockerPull by booleanParameter("plugin.docker.pull.enabled", trueValue = "true", falseValue = "")

    /**
     * Additional arguments passed to "docker run" command.
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
 *
 * **Example.**
 * 
 * ```
 * buildType {
 *     name = "Build in Docker"
 *
 *     features {
 *         runInDocker {
 *             dockerImage = "busybox"
 *         }
 *     }
 *
 *     steps {
 *         script {
 *             scriptContent = "echo this step is running inside the container"
 *         }
 *         script {
 *             scriptContent = "echo and this step is running in the same container""
 *         }
 *     }
 * }
 * ```
 *
 *
 * @see RunInDockerBuildFeature
 */
fun BuildFeatures.runInDocker(init: RunInDockerBuildFeature.() -> Unit): RunInDockerBuildFeature {
    val result = RunInDockerBuildFeature(init)
    feature(result)
    return result
}
