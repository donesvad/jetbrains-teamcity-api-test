package jetbrains.buildServer.configs.kotlin.v10.projectFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * A collection of settings that control public recipies' availability
 *
 * **Example.**
 * Enables public recipes for this project and its subprojects
 * ```
 * publicRecipes {
 *     id = "PublicRecipes",
 *     mode = PublicRecipes.Mode.ENABLED
 * }
 * ```
 *
 *
 * @see publicRecipes
 */
open class PublicRecipes : ProjectFeature {
    constructor(init: PublicRecipes.() -> Unit = {}, base: PublicRecipes? = null): super(base = base as ProjectFeature?) {
        type = "publicRecipes"
        init()
    }

    /**
     * Controls whether public recipes are allowed in this project and its subprojects
     *
     *
     * @see Mode
     */
    var mode by enumParameter<Mode>()

    /**
     * Public recipes project mode that controls whether public recipes are allowed in the project
     */
    enum class Mode {
        /**
         * Public recipes are allowed to use in the project and its subprojects
         */
        ENABLED,
        /**
         * Public recipes are not allowed to use in the project and its subprojects
         */
        DISABLED;

    }
}


/**
 * Controls settings related to the public recipes.
 *
 * **Example.**
 * Enables public recipes for this project and its subprojects
 * ```
 * publicRecipes {
 *     id = "PublicRecipes",
 *     mode = PublicRecipes.Mode.ENABLED
 * }
 * ```
 *
 *
 * @see PublicRecipes
 */
fun ProjectFeatures.publicRecipes(base: PublicRecipes? = null, init: PublicRecipes.() -> Unit = {}) {
    feature(PublicRecipes(init, base))
}
