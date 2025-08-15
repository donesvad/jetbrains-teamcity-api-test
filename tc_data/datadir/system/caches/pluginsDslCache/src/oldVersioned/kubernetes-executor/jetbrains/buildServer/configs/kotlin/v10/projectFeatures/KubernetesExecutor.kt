package jetbrains.buildServer.configs.kotlin.v10.projectFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * The default "agentless" Kubernetes integration that allows TeamCity to offload all building tasks to a K8S cluster.
 *
 * **Example.**
 * Adds new Kubernetes executor that allows TeamCity to offload builds to Kubernetes
 * ```
 * kubernetesExecutor {
 *   id = "PROJECT_EXT_32"
 *   connectionId = "PROJECT_EXT_14"
 *   profileName = "executor"
 *   templateName = "executors-template"
 * }
 * ```
 *
 * **Example.**
 * Adds new Kubernetes executor that allows TeamCity to offload builds to Kubernetes. This executor is aware of the parameters available to the executor build.
 * ```
 * kubernetesExecutor {
 *   id = "PROJECT_EXT_32"
 *   connectionId = "PROJECT_EXT_14"
 *   profileName = "executor"
 *   containerParameters = "requirement1=value1"
 * }
 * ```
 *
 *
 * @see kubernetesExecutor
 */
open class KubernetesExecutor : ProjectFeature {
    constructor(init: KubernetesExecutor.() -> Unit = {}, base: KubernetesExecutor? = null): super(base = base as ProjectFeature?) {
        type = "BuildExecutor"
        param("executorType", "KubernetesExecutor")
        init()
    }

    /**
     * The Kubernetes connection to use
     */
    var connectionId by stringParameter()

    /**
     * Display name of the profile
     */
    var profileName by stringParameter()

    /**
     * A template of a pod with a single container with the settings to be applied to the build
     */
    var templateContainer by stringParameter()

    /**
     * Maximum number of allowed builds running with the executor
     */
    var buildsLimit by stringParameter()

    /**
     * If executor is enabled for the project
     */
    var enabled by booleanParameter()

    /**
     * A small description of the current profile
     */
    var description by stringParameter("profileDescription")

    /**
     * The TeamCity URL for executor instances to connecto to. Leave empty to use default URL
     */
    var serverURL by stringParameter("profileServerUrl")

    /**
     * Specify parameters in the “name=value” format to be matched against explicit agent requirements of build configurations.
     * Pods without this setting will be compatible only with configurations that impose no explicit requirements.
     * Format: parameter1=value1,parameter2=value2"
     */
    @Deprecated("Deprecated. The value for this property is now naturally fetched from the executor.")
    var containerParameters by stringParameter()

    /**
     * The template's name with a pod with a single container with the settings to be applied to the build. The template must be available in the cluster of the selected connection.
     */
    var templateName by stringParameter()

}


/**
 * Adds a Kubernetes Executor Profile project feature
 *
 * **Example.**
 * Adds new Kubernetes executor that allows TeamCity to offload builds to Kubernetes
 * ```
 * kubernetesExecutor {
 *   id = "PROJECT_EXT_32"
 *   connectionId = "PROJECT_EXT_14"
 *   profileName = "executor"
 *   templateName = "executors-template"
 * }
 * ```
 *
 * **Example.**
 * Adds new Kubernetes executor that allows TeamCity to offload builds to Kubernetes. This executor is aware of the parameters available to the executor build.
 * ```
 * kubernetesExecutor {
 *   id = "PROJECT_EXT_32"
 *   connectionId = "PROJECT_EXT_14"
 *   profileName = "executor"
 *   containerParameters = "requirement1=value1"
 * }
 * ```
 *
 *
 * @see KubernetesExecutor
 */
fun ProjectFeatures.kubernetesExecutor(base: KubernetesExecutor? = null, init: KubernetesExecutor.() -> Unit = {}) {
    feature(KubernetesExecutor(init, base))
}
