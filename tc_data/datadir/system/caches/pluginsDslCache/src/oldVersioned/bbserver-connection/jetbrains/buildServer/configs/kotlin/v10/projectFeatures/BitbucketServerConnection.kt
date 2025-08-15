package jetbrains.buildServer.configs.kotlin.v10.projectFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Project feature defining an OAuth connection settings for Bitbucket Server / Data Center
 *
 * **Example.**
 * It is not recommended to store secure values such as the secret directly in the DSL code,
 * see [Managing Tokens](https://www.jetbrains.com/help/teamcity/storing-project-settings-in-version-control.html#Managing+Tokens)
 * section of our documentation.
 * ```
 * bitbucketServerConnection {
 *   id = "<Connection id>" // arbitrary ID that can be later used to refer to the connection
 *   displayName = "<Connection display name>"
 *   serverUrl = "<Bitbucket Server / Data Center URL>"
 *   clientId = "<OAuth2 application client ID>"
 *   clientSecret = "credentialsJSON:*****"
 *   useUniqueRedirect = true // optional, false by default
 * }
 * ```
 *
 *
 * @see bitbucketServerConnection
 */
open class BitbucketServerConnection : ProjectFeature {
    constructor(init: BitbucketServerConnection.() -> Unit = {}, base: BitbucketServerConnection? = null): super(base = base as ProjectFeature?) {
        type = "OAuthProvider"
        param("providerType", "BitbucketServer")
        init()
    }

    /**
     * Human friendly connection name
     */
    var displayName by stringParameter()

    /**
     * Bitbucket Server URL
     */
    var serverUrl by stringParameter("bitbucketUrl")

    /**
     * Bitbucket Server OAuth connection key
     */
    var clientId by stringParameter()

    /**
     * Bitbucket Server OAuth connection client secret
     */
    var clientSecret by stringParameter("secure:clientSecret")

    /**
     * If <code>true</code>, TeamCity adds a unique value to the redirect URL, preventing your access token from being intercepted during a mix-up attack.
     * The final redirect URL is displayed inside a corresponding section of connection settings in TeamCity admin UI.
     * The default value is <code>false</code>.
     */
    var useUniqueRedirect by booleanParameter()

}


/**
 * Creates a Bitbucket Server / Data Center OAuth connection in the current project
 *
 * **Example.**
 * It is not recommended to store secure values such as the secret directly in the DSL code,
 * see [Managing Tokens](https://www.jetbrains.com/help/teamcity/storing-project-settings-in-version-control.html#Managing+Tokens)
 * section of our documentation.
 * ```
 * bitbucketServerConnection {
 *   id = "<Connection id>" // arbitrary ID that can be later used to refer to the connection
 *   displayName = "<Connection display name>"
 *   serverUrl = "<Bitbucket Server / Data Center URL>"
 *   clientId = "<OAuth2 application client ID>"
 *   clientSecret = "credentialsJSON:*****"
 *   useUniqueRedirect = true // optional, false by default
 * }
 * ```
 *
 *
 * @see BitbucketServerConnection
 */
fun ProjectFeatures.bitbucketServerConnection(base: BitbucketServerConnection? = null, init: BitbucketServerConnection.() -> Unit = {}) {
    feature(BitbucketServerConnection(init, base))
}
