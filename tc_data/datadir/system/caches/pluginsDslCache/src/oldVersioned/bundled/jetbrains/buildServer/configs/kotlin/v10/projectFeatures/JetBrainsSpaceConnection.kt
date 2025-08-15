package jetbrains.buildServer.configs.kotlin.v10.projectFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * Project feature defining an OAuth connection settings for JetBrains Space
 *
 * **Example.**
 * It is not recommended to store secure values such as the secret directly in the DSL code,
 * see [Managing Tokens](https://www.jetbrains.com/help/teamcity/storing-project-settings-in-version-control.html#Managing+Tokens)
 * section of our documentation.
 * ```
 * spaceConnection {
 *   id = "<Connection id>" // arbitrary ID that can be later used to refer to the connection
 *   displayName = "<Connection display name>"
 *   serverUrl = "<JetBrains Space URL>"
 *   clientId = "<OAuth2 application client ID>"
 *   clientSecret = "credentialsJSON:*****"
 *   useUniqueRedirect = true // optional, false by default
 * }
 * ```
 *
 *
 * @see spaceConnection
 */
open class JetBrainsSpaceConnection : ProjectFeature {
    constructor(init: JetBrainsSpaceConnection.() -> Unit = {}, base: JetBrainsSpaceConnection? = null): super(base = base as ProjectFeature?) {
        type = "OAuthProvider"
        param("providerType", "JetBrains Space")
        init()
    }

    /**
     * Human friendly connection name
     */
    var displayName by stringParameter()

    /**
     * JetBrains Space server URL
     */
    var serverUrl by stringParameter("spaceServerUrl")

    /**
     * JetBrains Space OAuth connection client ID
     */
    var clientId by stringParameter("spaceClientId")

    /**
     * JetBrains Space OAuth connection client secret
     */
    var clientSecret by stringParameter("secure:spaceClientSecret")

    /**
     * If <code>true</code>, TeamCity adds a unique value to the redirect URL, preventing your access token from being intercepted during a mix-up attack.
     * The final redirect URL is displayed inside a corresponding section of connection settings in TeamCity admin UI.
     * The default value is <code>false</code>.
     */
    var useUniqueRedirect by booleanParameter()

}


/**
 * Creates a JetBrains Space OAuth connection in the current project
 *
 * **Example.**
 * It is not recommended to store secure values such as the secret directly in the DSL code,
 * see [Managing Tokens](https://www.jetbrains.com/help/teamcity/storing-project-settings-in-version-control.html#Managing+Tokens)
 * section of our documentation.
 * ```
 * spaceConnection {
 *   id = "<Connection id>" // arbitrary ID that can be later used to refer to the connection
 *   displayName = "<Connection display name>"
 *   serverUrl = "<JetBrains Space URL>"
 *   clientId = "<OAuth2 application client ID>"
 *   clientSecret = "credentialsJSON:*****"
 *   useUniqueRedirect = true // optional, false by default
 * }
 * ```
 *
 *
 * @see JetBrainsSpaceConnection
 */
fun ProjectFeatures.spaceConnection(base: JetBrainsSpaceConnection? = null, init: JetBrainsSpaceConnection.() -> Unit = {}) {
    feature(JetBrainsSpaceConnection(init, base))
}
