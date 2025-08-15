package jetbrains.buildServer.configs.kotlin.v2017_2.buildFeatures

import jetbrains.buildServer.configs.kotlin.v2017_2.*

/**
 * Caches Maven dependencies to speed up the builds. The feature tracks [local repositories](https://maven.apache.org/guides/introduction/introduction-to-repositories.html)
 * used by Maven steps and caches dependencies in the artifact storage. The cache is automatically updated when dependencies of corresponding Maven projects change.
 * 
 * Dependency caching is most effective on **short-lived agents**. For permanent or long-lived cloud agents, periodically review hidden
 * `.teamcity.build_cache` build artifacts to monitor cache size and contents. This helps prevent redundant dependencies and unnecessary cache bloat.
 * 
 * This feature is not recommended for builds that require a clean environment, such as release builds.
 *
 * **Example.**
 * Enables Maven dependency cache to speed up the builds
 * ```
 * mavenCache {
 * }
 * ```
 *
 *
 * @see mavenCache
 */
open class MavenCacheFeature() : BuildFeature() {

    init {
        type = "dependencyCache.Maven2"
    }

    constructor(init: MavenCacheFeature.() -> Unit): this() {
        init()
    }

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * Adds Maven dependency cache to the build. The feature tracks [local repositories](https://maven.apache.org/guides/introduction/introduction-to-repositories.html)
 * used by Maven steps and caches dependencies in the artifact storage. The cache is automatically updated when dependencies of corresponding Maven projects change.
 * 
 * Dependency caching is most effective on **short-lived agents**. For permanent or long-lived cloud agents, periodically review hidden
 * `.teamcity.build_cache` build artifacts to monitor cache size and contents. This helps prevent redundant dependencies and unnecessary cache bloat.
 * 
 * This feature is not recommended for builds that require a clean environment, such as release builds.
 *
 * **Example.**
 * Enables Maven dependency cache to speed up the builds
 * ```
 * mavenCache {
 * }
 * ```
 *
 *
 * @see MavenCacheFeature
 */
fun BuildFeatures.mavenCache(init: MavenCacheFeature.() -> Unit): MavenCacheFeature {
    val result = MavenCacheFeature(init)
    feature(result)
    return result
}
