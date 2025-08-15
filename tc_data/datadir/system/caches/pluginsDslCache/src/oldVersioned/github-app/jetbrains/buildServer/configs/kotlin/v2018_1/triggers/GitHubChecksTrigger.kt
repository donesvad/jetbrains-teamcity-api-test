package jetbrains.buildServer.configs.kotlin.v2018_1.triggers

import jetbrains.buildServer.configs.kotlin.v2018_1.*

/**
 * [GitHub Checks Webhook Trigger](https://www.jetbrains.com/help/teamcity/github-checks-trigger.html) allows TeamCity to automatically run builds on every commit and communicate build statuses to GitHub as Checks page messages.
 *
 * **Example.**
 * Trigger a TeamCity build whenever a new change is pushed to a remote GitHub repository. In addition, this trigger posts detailed build result info to GitHub.
 * ```
 * gitHubChecks { }
 * ```
 *
 * **Example.**
 * Disable links to TeamCity in detailed reports on the GitHub
 * ```
 * gitHubChecks {
 *   disableLinksToTeamcity = true
 * }
 * ```
 *
 *
 * @see gitHubChecks
 */
open class GitHubChecksTrigger() : Trigger() {

    init {
        type = "GitHubChecksTrigger"
    }

    constructor(init: GitHubChecksTrigger.() -> Unit): this() {
        init()
    }

    /**
     * Disable TeamCity links in GitHub check run output
     */
    var disableLinksToTeamcity by booleanParameter("disableLinks")

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * Adds [GitHub Checks Webhook Trigger](https://www.jetbrains.com/help/teamcity/github-checks-trigger.html) to the build configuration or template
 *
 * **Example.**
 * Trigger a TeamCity build whenever a new change is pushed to a remote GitHub repository. In addition, this trigger posts detailed build result info to GitHub.
 * ```
 * gitHubChecks { }
 * ```
 *
 * **Example.**
 * Disable links to TeamCity in detailed reports on the GitHub
 * ```
 * gitHubChecks {
 *   disableLinksToTeamcity = true
 * }
 * ```
 *
 *
 * @see GitHubChecksTrigger
 */
fun Triggers.gitHubChecks(init: GitHubChecksTrigger.() -> Unit): GitHubChecksTrigger {
    val result = GitHubChecksTrigger(init)
    trigger(result)
    return result
}
