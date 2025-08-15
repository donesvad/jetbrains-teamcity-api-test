package jetbrains.buildServer.configs.kotlin.v2018_2.buildFeatures

import jetbrains.buildServer.configs.kotlin.v2018_2.*

/**
 * Caches NuGet packages on .NET steps to speed up the builds. The feature tracks NuGet [global-packages](https://learn.microsoft.com/en-us/nuget/consume-packages/managing-the-global-packages-and-cache-folders)
 * directories used by the `dotnet` command and caches packages in the artifact storage.
 * The cache is automatically updated when dependencies of the corresponding .NET projects change.
 * **NuGet package caching is supported when the build command uses .NET SDK 7.0.200 or higher.**
 * 
 * Package caching is most effective on **short-lived agents**. For permanent or long-lived cloud agents, periodically review hidden
 * `.teamcity.build_cache` build artifacts to monitor cache size and contents. This helps prevent redundant dependencies and unnecessary cache bloat.
 * 
 * This feature is not recommended for builds that require a clean environment, such as release builds.
 *
 * **Example.**
 * Enables NuGet cache to speed up the builds
 * ```
 * nugetCache {
 * }
 * ```
 *
 *
 * @see nugetCache
 */
open class NugetCacheFeature() : BuildFeature() {

    init {
        type = "dependencyCache.dotnet"
    }

    constructor(init: NugetCacheFeature.() -> Unit): this() {
        init()
    }

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * Adds NuGet cache to the build. The feature tracks NuGet [global-packages](https://learn.microsoft.com/en-us/nuget/consume-packages/managing-the-global-packages-and-cache-folders)
 * directories used by the `dotnet` command and caches packages in the artifact storage.
 * The cache is automatically updated when dependencies of the corresponding .NET projects change.
 * **NuGet package caching is supported when the build command uses .NET SDK 7.0.200 or higher.**
 * 
 * Package caching is most effective on **short-lived agents**. For permanent or long-lived cloud agents, periodically review hidden
 * `.teamcity.build_cache` build artifacts to monitor cache size and contents. This helps prevent redundant dependencies and unnecessary cache bloat.
 * 
 * This feature is not recommended for builds that require a clean environment, such as release builds.
 *
 * **Example.**
 * Enables NuGet cache to speed up the builds
 * ```
 * nugetCache {
 * }
 * ```
 *
 *
 * @see NugetCacheFeature
 */
fun BuildFeatures.nugetCache(init: NugetCacheFeature.() -> Unit): NugetCacheFeature {
    val result = NugetCacheFeature(init)
    feature(result)
    return result
}
