package jetbrains.buildServer.configs.kotlin.v2018_2.buildFeatures

import jetbrains.buildServer.configs.kotlin.v2018_2.*

/**
 * Caches Gradle dependencies to speed up the builds. The feature tracks [shared cache](https://docs.gradle.org/current/userguide/directory_layout.html) directories
 * (`<gradle_user_home>/caches/modules-2`)
 * used by Gradle steps and caches dependencies in the artifact storage. The cache is automatically updated when dependencies of corresponding Gradle projects change.
 * 
 * Dependency caching is most effective on **short-lived agents**. For permanent or long-lived cloud agents, periodically review hidden
 * `.teamcity.build_cache` build artifacts to monitor cache size and contents. This helps prevent redundant dependencies and unnecessary cache bloat.
 * 
 * This feature is not recommended for builds that require a clean environment, such as release builds.
 *
 * **Example.**
 * Enables Gradle dependency cache to speed up the builds
 * ```
 * gradleCache {
 * }
 * ```
 *
 *
 * @see gradleCache
 */
open class GradleCacheFeature() : BuildFeature() {

    init {
        type = "dependencyCache.gradle-runner"
    }

    constructor(init: GradleCacheFeature.() -> Unit): this() {
        init()
    }

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * Adds Gradle dependency cache to the build. The feature tracks [shared cache](https://docs.gradle.org/current/userguide/directory_layout.html) directories
 * (`<gradle_user_home>/caches/modules-2`)
 * used by Gradle steps and caches dependencies in the artifact storage. The cache is automatically updated when dependencies of corresponding Gradle projects change.
 * 
 * Dependency caching is most effective on **short-lived agents**. For permanent or long-lived cloud agents, periodically review hidden
 * `.teamcity.build_cache` build artifacts to monitor cache size and contents. This helps prevent redundant dependencies and unnecessary cache bloat.
 * 
 * This feature is not recommended for builds that require a clean environment, such as release builds.
 *
 * **Example.**
 * Enables Gradle dependency cache to speed up the builds
 * ```
 * gradleCache {
 * }
 * ```
 *
 *
 * @see GradleCacheFeature
 */
fun BuildFeatures.gradleCache(init: GradleCacheFeature.() -> Unit): GradleCacheFeature {
    val result = GradleCacheFeature(init)
    feature(result)
    return result
}
