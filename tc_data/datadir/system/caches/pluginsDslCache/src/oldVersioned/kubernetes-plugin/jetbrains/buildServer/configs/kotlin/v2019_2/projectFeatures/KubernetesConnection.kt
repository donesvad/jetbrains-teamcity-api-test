package jetbrains.buildServer.configs.kotlin.v2019_2.projectFeatures

import jetbrains.buildServer.configs.kotlin.v2019_2.*

/**
 * Stores information and configuration for the access to a Kubernetes cluster.
 *
 * **Example.**
 * Adds a new Connection that allows TeamCity to store and manage a Kubernetes Cluster using a Bearer Token
 * ```
 * kubernetesConnection {
 *     id = "PROJECT_EXT_3"
 *     name = "Connection"
 *     apiServerUrl = "http://cluster.com"
 *     caCertificate = "credentialsJSON:c77bc0a7-f461-4ca8-959b-ef5c8f6359b2"
 *     namespace = "namespace"
 *     authStrategy = token {
 *       token = "credentialsJSON:fa92592e-ec16-4543-add0-1cdd4de87e5e"
 *     }
 * }
 * ```
 *
 * **Example.**
 * Adds a new Connection that allows TeamCity to store and manage a Kubernetes Cluster using a username and password
 * ```
 * kubernetesConnection {
 *     id = "PROJECT_EXT_3"
 *     name = "Connection"
 *     apiServerUrl = "http://cluster.com"
 *     caCertificate = "credentialsJSON:c77bc0a7-f461-4ca8-959b-ef5c8f6359b2"
 *     namespace = "namespace"
 *     authStrategy = usernameAndPassword {
 *       username = "username"
 *       password = "credentialsJSON:aaef5e7e-5a51-427c-b0ea-3af364cacedd"
 *     }
 * }
 * ```
 *
 * **Example.**
 * Adds a new Connection that allows TeamCity to store and manage a Kubernetes Cluster using the OpenID Connect (OIDC) protocol
 * ```
 * kubernetesConnection {
 *     id = "PROJECT_EXT_3"
 *     name = "Connection"
 *     apiServerUrl = "http://cluster.com"
 *     caCertificate = "credentialsJSON:c77bc0a7-f461-4ca8-959b-ef5c8f6359b2"
 *     namespace = "namespace"
 *     authStrategy = openId {
 *       idpIssuerUrl = "idpurl"
 *       clientId = "clientId"
 *       clientSecret = "credentialsJSON:612f3414-2b25-41ec-9520-12c9669d1f85"
 *       refreshToken = "credentialsJSON:0bb3f85a-7ff4-474e-9c14-8da47099595a"
 *     }
 * }
 * ```
 *
 * **Example.**
 * Adds a new Connection that allows TeamCity to store and manage a Kubernetes Cluster using a Client Certificate and Key
 * ```
 * kubernetesConnection {
 *     id = "PROJECT_EXT_3"
 *     name = "Connection"
 *     apiServerUrl = "http://cluster.com"
 *     caCertificate = "credentialsJSON:c77bc0a7-f461-4ca8-959b-ef5c8f6359b2"
 *     namespace = "namespace"
 *     authStrategy = clientCertificateAndKey {
 *       clientKey = "credentialsJSON:43b46ece-15a7-4621-a271-7d2b33e9e356"
 *       clientCertificate = "credentialsJSON:87a6537c-cb8f-4684-b44d-e0ea183e9fa1"
 *     }
 * }
 * ```
 *
 * **Example.**
 * Adds a new Connection that allows TeamCity to store and manage a Kubernetes Cluster in AWS EKS using an access key and secret key
 * ```
 * kubernetesConnection {
 *     id = "PROJECT_EXT_3"
 *     name = "Connection"
 *     apiServerUrl = "http://cluster.com"
 *     caCertificate = "credentialsJSON:c77bc0a7-f461-4ca8-959b-ef5c8f6359b2"
 *     namespace = "namespace"
 *     authStrategy = eks {
 *       accessId = "accessId"
 *       secretKey = "credentialsJSON:3507800f-ae64-49f1-bbb5-0a4deec5c7b3"
 *       clusterName = "cluster-name"
 *     }
 * }
 * ```
 *
 * **Example.**
 * Adds a new Connection that allows TeamCity to store and manage a Kubernetes Cluster.
 * ```
 * kubernetesConnection {
 *     id = "PROJECT_EXT_3"
 *     name = "Connection"
 *     apiServerUrl = "http://cluster.com"
 *     caCertificate = "credentialsJSON:c77bc0a7-f461-4ca8-959b-ef5c8f6359b2"
 *     namespace = "namespace"
 *     authStrategy = unauthorized()
 * }
 * ```
 *
 *
 * @see kubernetesConnection
 */
open class KubernetesConnection() : ProjectFeature() {

    init {
        type = "OAuthProvider"
        param("providerType", "KubernetesConnection")
    }

    constructor(init: KubernetesConnection.() -> Unit): this() {
        init()
    }

    /**
     * Kubernetes connection display name
     */
    var name by stringParameter("displayName")

    /**
     * Target Kubernetes API server URL
     */
    var apiServerUrl by stringParameter()

    /**
     * The CA Certificate to connect to the cluster. Leave empty to skip TLS verification (insecure option)
     */
    var caCertificate by stringParameter("secure:caCertData")

    /**
     * The Kubernetes namespace to use. Leave empty to use the default namespace.
     */
    var namespace by stringParameter()

    /**
     * The way how to obtain credentials
     */
    var authStrategy by compoundParameter<AuthStrategy>()

    sealed class AuthStrategy(value: String? = null): CompoundParam<AuthStrategy>(value) {
        abstract fun validate(consumer: ErrorConsumer)

        /**
         * Connecting to an AWS EKS (Elastic Kubernetes Service)
         */
        class Eks() : AuthStrategy("eks") {

            /**
             * Uses the AWS credentials provided in the TeamCity Instance. Should be available under `~/.aws`
             */
            @Deprecated("Using the AWS credentials provided in the instance is not recommended, as it increases the risk of leaks.")
            var eksUseInstanceProfile by booleanParameter()

            /**
             * AWS Access Key ID
             */
            var accessId by stringParameter("eksAccessId")

            /**
             * AWS Secret Access Key
             */
            var secretKey by stringParameter("secure:eksSecretKey")

            /**
             * If assuming an IAM role is required to access the cluster. Must fill [iamRoleArn]
             */
            var assumeIamRole by booleanParameter("eksAssumeIAMRole")

            /**
             * The IAM Role ARN needed to access the cluster
             */
            var iamRoleArn by stringParameter("eksIAMRoleArn")

            /**
             * The name of the EKS cluster
             */
            var clusterName by stringParameter("eksClusterName")

            override fun validate(consumer: ErrorConsumer) {
                if (clusterName == null && !hasParam("eksClusterName")) {
                    consumer.consumePropertyError("authStrategy.clusterName", "mandatory 'authStrategy.clusterName' property is not specified")
                }
            }
        }

        /**
         * Connecting to a Kubernetes cluster using a username and password
         */
        class UsernameAndPassword() : AuthStrategy("user-passwd") {

            /**
             * Username
             */
            var username by stringParameter()

            /**
             * Password
             */
            var password by stringParameter("secure:password")

            override fun validate(consumer: ErrorConsumer) {
                if (username == null && !hasParam("username")) {
                    consumer.consumePropertyError("authStrategy.username", "mandatory 'authStrategy.username' property is not specified")
                }
                if (password == null && !hasParam("secure:password")) {
                    consumer.consumePropertyError("authStrategy.password", "mandatory 'authStrategy.password' property is not specified")
                }
            }
        }

        /**
         * Connecting to a Kubernetes cluster using the OpenID Connect (OIDC) protocol
         */
        class OpenId() : AuthStrategy("oidc") {

            /**
             * The IdP Issuer URL
             */
            var idpIssuerUrl by stringParameter()

            /**
             * The Client ID
             */
            var clientId by stringParameter("oidcClientId")

            /**
             * The Client Secret
             */
            var clientSecret by stringParameter("secure:oidcClientSecret")

            /**
             * The Refresh Token
             */
            var refreshToken by stringParameter("secure:oidcRefreshToken")

            override fun validate(consumer: ErrorConsumer) {
                if (idpIssuerUrl == null && !hasParam("idpIssuerUrl")) {
                    consumer.consumePropertyError("authStrategy.idpIssuerUrl", "mandatory 'authStrategy.idpIssuerUrl' property is not specified")
                }
                if (clientId == null && !hasParam("oidcClientId")) {
                    consumer.consumePropertyError("authStrategy.clientId", "mandatory 'authStrategy.clientId' property is not specified")
                }
                if (clientSecret == null && !hasParam("secure:oidcClientSecret")) {
                    consumer.consumePropertyError("authStrategy.clientSecret", "mandatory 'authStrategy.clientSecret' property is not specified")
                }
                if (refreshToken == null && !hasParam("secure:oidcRefreshToken")) {
                    consumer.consumePropertyError("authStrategy.refreshToken", "mandatory 'authStrategy.refreshToken' property is not specified")
                }
            }
        }

        /**
         * Connecting to a Kubernetes cluster using a client certificate and key
         */
        class ClientCertificateAndKey() : AuthStrategy("client-cert") {

            /**
             * The Client Key
             */
            var clientKey by stringParameter("secure:clientKeyData")

            /**
             * The Client Certificate
             */
            var clientCertificate by stringParameter("secure:clientCertData")

            override fun validate(consumer: ErrorConsumer) {
                if (clientKey == null && !hasParam("secure:clientKeyData")) {
                    consumer.consumePropertyError("authStrategy.clientKey", "mandatory 'authStrategy.clientKey' property is not specified")
                }
                if (clientCertificate == null && !hasParam("secure:clientCertData")) {
                    consumer.consumePropertyError("authStrategy.clientCertificate", "mandatory 'authStrategy.clientCertificate' property is not specified")
                }
            }
        }

        /**
         * Connecting to a Kubernetes cluster using a Bearer Token
         */
        class Token() : AuthStrategy("token") {

            /**
             * The Bearer Token
             */
            var token by stringParameter("secure:authToken")

            override fun validate(consumer: ErrorConsumer) {
                if (token == null && !hasParam("secure:authToken")) {
                    consumer.consumePropertyError("authStrategy.token", "mandatory 'authStrategy.token' property is not specified")
                }
            }
        }

        /**
         * Use unauthorized access to the Kubernetes API server
         */
        class Unauthorized() : AuthStrategy("unauthorized") {

            override fun validate(consumer: ErrorConsumer) {
            }
        }

        /**
         * Uses the Kubernetes credentials provided in the TeamCity Instance. Should be available under `/var/run/secrets/kubernetes.io/serviceaccount/token`
         */
        @Deprecated("Using the Kubernetes credentials provided in the instance is not recommended, as it increases the risk of leaks.")
        class ServiceAccount() : AuthStrategy("service-account") {

            override fun validate(consumer: ErrorConsumer) {
            }
        }
    }

    /**
     * Connecting to an AWS EKS (Elastic Kubernetes Service)
     */
    fun eks(init: AuthStrategy.Eks.() -> Unit = {}) : AuthStrategy.Eks {
        val result = AuthStrategy.Eks()
        result.init()
        return result
    }

    /**
     * Connecting to a Kubernetes cluster using a username and password
     */
    fun usernameAndPassword(init: AuthStrategy.UsernameAndPassword.() -> Unit = {}) : AuthStrategy.UsernameAndPassword {
        val result = AuthStrategy.UsernameAndPassword()
        result.init()
        return result
    }

    /**
     * Connecting to a Kubernetes cluster using the OpenID Connect (OIDC) protocol
     */
    fun openId(init: AuthStrategy.OpenId.() -> Unit = {}) : AuthStrategy.OpenId {
        val result = AuthStrategy.OpenId()
        result.init()
        return result
    }

    /**
     * Connecting to a Kubernetes cluster using a client certificate and key
     */
    fun clientCertificateAndKey(init: AuthStrategy.ClientCertificateAndKey.() -> Unit = {}) : AuthStrategy.ClientCertificateAndKey {
        val result = AuthStrategy.ClientCertificateAndKey()
        result.init()
        return result
    }

    /**
     * Connecting to a Kubernetes cluster using a Bearer Token
     */
    fun token(init: AuthStrategy.Token.() -> Unit = {}) : AuthStrategy.Token {
        val result = AuthStrategy.Token()
        result.init()
        return result
    }

    /**
     * Use unauthorized access to the Kubernetes API server
     */
    fun unauthorized() = AuthStrategy.Unauthorized()

    /**
     * Uses the Kubernetes credentials provided in the TeamCity Instance. Should be available under `/var/run/secrets/kubernetes.io/serviceaccount/token`
     */
    @Deprecated("Using the Kubernetes credentials provided in the instance is not recommended, as it increases the risk of leaks.")
    fun serviceAccount() = AuthStrategy.ServiceAccount()

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (apiServerUrl == null && !hasParam("apiServerUrl")) {
            consumer.consumePropertyError("apiServerUrl", "mandatory 'apiServerUrl' property is not specified")
        }
        if (authStrategy == null && !hasParam("authStrategy")) {
            consumer.consumePropertyError("authStrategy", "mandatory 'authStrategy' property is not specified")
        }
        authStrategy?.validate(consumer)
    }
}


/**
 *
 * **Example.**
 * Adds a new Connection that allows TeamCity to store and manage a Kubernetes Cluster using a Bearer Token
 * ```
 * kubernetesConnection {
 *     id = "PROJECT_EXT_3"
 *     name = "Connection"
 *     apiServerUrl = "http://cluster.com"
 *     caCertificate = "credentialsJSON:c77bc0a7-f461-4ca8-959b-ef5c8f6359b2"
 *     namespace = "namespace"
 *     authStrategy = token {
 *       token = "credentialsJSON:fa92592e-ec16-4543-add0-1cdd4de87e5e"
 *     }
 * }
 * ```
 *
 * **Example.**
 * Adds a new Connection that allows TeamCity to store and manage a Kubernetes Cluster using a username and password
 * ```
 * kubernetesConnection {
 *     id = "PROJECT_EXT_3"
 *     name = "Connection"
 *     apiServerUrl = "http://cluster.com"
 *     caCertificate = "credentialsJSON:c77bc0a7-f461-4ca8-959b-ef5c8f6359b2"
 *     namespace = "namespace"
 *     authStrategy = usernameAndPassword {
 *       username = "username"
 *       password = "credentialsJSON:aaef5e7e-5a51-427c-b0ea-3af364cacedd"
 *     }
 * }
 * ```
 *
 * **Example.**
 * Adds a new Connection that allows TeamCity to store and manage a Kubernetes Cluster using the OpenID Connect (OIDC) protocol
 * ```
 * kubernetesConnection {
 *     id = "PROJECT_EXT_3"
 *     name = "Connection"
 *     apiServerUrl = "http://cluster.com"
 *     caCertificate = "credentialsJSON:c77bc0a7-f461-4ca8-959b-ef5c8f6359b2"
 *     namespace = "namespace"
 *     authStrategy = openId {
 *       idpIssuerUrl = "idpurl"
 *       clientId = "clientId"
 *       clientSecret = "credentialsJSON:612f3414-2b25-41ec-9520-12c9669d1f85"
 *       refreshToken = "credentialsJSON:0bb3f85a-7ff4-474e-9c14-8da47099595a"
 *     }
 * }
 * ```
 *
 * **Example.**
 * Adds a new Connection that allows TeamCity to store and manage a Kubernetes Cluster using a Client Certificate and Key
 * ```
 * kubernetesConnection {
 *     id = "PROJECT_EXT_3"
 *     name = "Connection"
 *     apiServerUrl = "http://cluster.com"
 *     caCertificate = "credentialsJSON:c77bc0a7-f461-4ca8-959b-ef5c8f6359b2"
 *     namespace = "namespace"
 *     authStrategy = clientCertificateAndKey {
 *       clientKey = "credentialsJSON:43b46ece-15a7-4621-a271-7d2b33e9e356"
 *       clientCertificate = "credentialsJSON:87a6537c-cb8f-4684-b44d-e0ea183e9fa1"
 *     }
 * }
 * ```
 *
 * **Example.**
 * Adds a new Connection that allows TeamCity to store and manage a Kubernetes Cluster in AWS EKS using an access key and secret key
 * ```
 * kubernetesConnection {
 *     id = "PROJECT_EXT_3"
 *     name = "Connection"
 *     apiServerUrl = "http://cluster.com"
 *     caCertificate = "credentialsJSON:c77bc0a7-f461-4ca8-959b-ef5c8f6359b2"
 *     namespace = "namespace"
 *     authStrategy = eks {
 *       accessId = "accessId"
 *       secretKey = "credentialsJSON:3507800f-ae64-49f1-bbb5-0a4deec5c7b3"
 *       clusterName = "cluster-name"
 *     }
 * }
 * ```
 *
 * **Example.**
 * Adds a new Connection that allows TeamCity to store and manage a Kubernetes Cluster.
 * ```
 * kubernetesConnection {
 *     id = "PROJECT_EXT_3"
 *     name = "Connection"
 *     apiServerUrl = "http://cluster.com"
 *     caCertificate = "credentialsJSON:c77bc0a7-f461-4ca8-959b-ef5c8f6359b2"
 *     namespace = "namespace"
 *     authStrategy = unauthorized()
 * }
 * ```
 *
 *
 * @see KubernetesConnection
 */
fun ProjectFeatures.kubernetesConnection(init: KubernetesConnection.() -> Unit): KubernetesConnection {
    val result = KubernetesConnection(init)
    feature(result)
    return result
}
