package jetbrains.buildServer.configs.kotlin.v10.projectFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Project feature for Azure DevOps OAuth connection settings
 *
 * **Example.**
 * It is not recommended to store secure values such as the secret directly in the DSL code,
 * see [Managing Tokens](https://www.jetbrains.com/help/teamcity/storing-project-settings-in-version-control.html#Managing+Tokens)
 * section of our documentation.
 * ```
 * azureDevOpsOAuthConnection {
 *   id = "<Connection id>" // arbitrary ID that can be later used to refer to the connection
 *   displayName = "<Connection display name>"
 *   azureDevOpsUrl = "<Azure DevOps server URL>" // e.g. https://app.vssps.visualstudio.com
 *   applicationId = "<OAuth2 application ID>"
 *   clientSecret = "credentialsJSON:*****"
 *   scope = "<Custom scope>" // optional, if omitted the following scope is used: **vso.identity vso.code vso.project.**
 *   useUniqueCallback = true // optional, false by default
 * }
 * ```
 *
 *
 * @see azureDevOpsOAuthConnection
 */
open class AzureDevOpsOAuthConnection : ProjectFeature {
    constructor(init: AzureDevOpsOAuthConnection.() -> Unit = {}, base: AzureDevOpsOAuthConnection? = null): super(base = base as ProjectFeature?) {
        type = "OAuthProvider"
        param("providerType", "AzureDevOps")
        init()
    }

    /**
     * Human friendly connection name
     */
    var displayName by stringParameter()

    /**
     * Azure DevOps server URL, for example:
     * https://app.vssps.visualstudio.com
     */
    var azureDevOpsUrl by stringParameter()

    /**
     * Azure DevOps Application ID
     */
    var applicationId by stringParameter()

    /**
     * Client Secret
     */
    var clientSecret by stringParameter("secure:clientSecret")

    /**
     * Custom OAuth 2.0 App scope. If left blank 'vso.identity vso.code vso.project' is assumed
     */
    var scope by stringParameter()

    /**
     * If <code>true</code>, TeamCity adds a unique value to the callback URL, preventing your access token from being intercepted during a mix-up attack.
     * The final callback URL is displayed inside a corresponding section of connection settings in TeamCity admin UI.
     * The default value is <code>false</code>.
     */
    var useUniqueCallback by booleanParameter("useUniqueRedirect")

}


/**
 * Creates an Azure DevOps OAuth connection in the current project
 *
 * **Example.**
 * It is not recommended to store secure values such as the secret directly in the DSL code,
 * see [Managing Tokens](https://www.jetbrains.com/help/teamcity/storing-project-settings-in-version-control.html#Managing+Tokens)
 * section of our documentation.
 * ```
 * azureDevOpsOAuthConnection {
 *   id = "<Connection id>" // arbitrary ID that can be later used to refer to the connection
 *   displayName = "<Connection display name>"
 *   azureDevOpsUrl = "<Azure DevOps server URL>" // e.g. https://app.vssps.visualstudio.com
 *   applicationId = "<OAuth2 application ID>"
 *   clientSecret = "credentialsJSON:*****"
 *   scope = "<Custom scope>" // optional, if omitted the following scope is used: **vso.identity vso.code vso.project.**
 *   useUniqueCallback = true // optional, false by default
 * }
 * ```
 *
 *
 * @see AzureDevOpsOAuthConnection
 */
fun ProjectFeatures.azureDevOpsOAuthConnection(base: AzureDevOpsOAuthConnection? = null, init: AzureDevOpsOAuthConnection.() -> Unit = {}) {
    feature(AzureDevOpsOAuthConnection(init, base))
}
