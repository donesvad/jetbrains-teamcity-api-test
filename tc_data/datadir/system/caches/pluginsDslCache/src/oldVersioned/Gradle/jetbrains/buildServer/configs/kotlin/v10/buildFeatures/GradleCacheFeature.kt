package jetbrains.buildServer.configs.kotlin.v10.buildFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

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
open class GradleCacheFeature : BuildFeature {
    constructor(init: GradleCacheFeature.() -> Unit = {}, base: GradleCacheFeature? = null): super(base = base as BuildFeature?) {
        type = "dependencyCache.gradle-runner"
        init()
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
fun BuildFeatures.gradleCache(base: GradleCacheFeature? = null, init: GradleCacheFeature.() -> Unit = {}) {
    feature(GradleCacheFeature(init, base))
}
