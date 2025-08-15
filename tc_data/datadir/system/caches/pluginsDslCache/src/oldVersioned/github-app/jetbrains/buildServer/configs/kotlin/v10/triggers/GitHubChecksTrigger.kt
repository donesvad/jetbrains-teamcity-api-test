package jetbrains.buildServer.configs.kotlin.v10.triggers

import jetbrains.buildServer.configs.kotlin.v10.*

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
open class GitHubChecksTrigger : Trigger {
    constructor(init: GitHubChecksTrigger.() -> Unit = {}, base: GitHubChecksTrigger? = null): super(base = base as Trigger?) {
        type = "GitHubChecksTrigger"
        init()
    }

    /**
     * Disable TeamCity links in GitHub check run output
     */
    var disableLinksToTeamcity by booleanParameter("disableLinks")

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
fun Triggers.gitHubChecks(base: GitHubChecksTrigger? = null, init: GitHubChecksTrigger.() -> Unit = {}) {
    trigger(GitHubChecksTrigger(init, base))
}
