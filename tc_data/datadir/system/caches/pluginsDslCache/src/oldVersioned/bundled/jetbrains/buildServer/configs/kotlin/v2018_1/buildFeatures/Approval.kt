package jetbrains.buildServer.configs.kotlin.v2018_1.buildFeatures

import jetbrains.buildServer.configs.kotlin.v2018_1.*

/**
 * Build feature that allows to request manual approve before build starts
 *
 * **Example.**
 * Do not start a build until at least one user from the user group with key 'STAGING_ADMIN' approves it.
 * Wait for approval for 15 minutes, then cancel the build.
 * ```
 * approval {
 *   approvalRules = "group:STAGING_ADMIN:1"
 *   timeout = 15
 * }
 * ```
 *
 * **Example.**
 * Do not start a build until any three users from groups(DEVS or QA) or from list of users(jane.doe or john doe) approve the build
 * ```
 * approval {
 * approvalRules = "(groups:DEVS,QA,users:jane.doe,john.doe):3"
 * }
 * ```
 *
 *
 * @see approval
 */
open class Approval() : BuildFeature() {

    init {
        type = "approval-feature"
    }

    constructor(init: Approval.() -> Unit): this() {
        init()
    }

    /**
     * Use `user:<username>` for individuals and `group:<group key>:<required count>` for groups (group keys are case-sensitive).
     * Each new line adds to a previous ruleset using the logical "AND" operator. To combine users and groups with a shared vote count, use brackets: `(users:johndoe,janedoe,groups:ADMINS,DEVS):2`
     */
    var approvalRules by stringParameter("rules")

    /**
     * Amount of time (in minutes) before the build is cancelled, defaults to 6 hours
     */
    var timeout by intParameter()

    /**
     * If started by user with sufficient permissions, mark build as approved by user
     */
    var manualRunsApproved by booleanParameter("manualStartIsApproval", trueValue = "true", falseValue = "")

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (approvalRules == null && !hasParam("rules")) {
            consumer.consumePropertyError("approvalRules", "mandatory 'approvalRules' property is not specified")
        }
    }
}


/**
 * Make build require manual approval before it will be assigned to an agent
 *
 * **Example.**
 * Do not start a build until at least one user from the user group with key 'STAGING_ADMIN' approves it.
 * Wait for approval for 15 minutes, then cancel the build.
 * ```
 * approval {
 *   approvalRules = "group:STAGING_ADMIN:1"
 *   timeout = 15
 * }
 * ```
 *
 * **Example.**
 * Do not start a build until any three users from groups(DEVS or QA) or from list of users(jane.doe or john doe) approve the build
 * ```
 * approval {
 * approvalRules = "(groups:DEVS,QA,users:jane.doe,john.doe):3"
 * }
 * ```
 *
 *
 * @see Approval
 */
fun BuildFeatures.approval(init: Approval.() -> Unit): Approval {
    val result = Approval(init)
    feature(result)
    return result
}
