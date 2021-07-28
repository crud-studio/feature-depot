package studio.crud.feature.parameters



/**
 * Handler for operations with Parameter entity.
 */
interface ParameterHandler {
    /**
     * Retrieves a parameter of type string
     *
     * @param name of the parameter.
     * @param defaultValue in case of null
     * @return the parameter value
     */
    fun getStringParameter(name: String, defaultValue: String?): String?

    /**
     * Retrieves a parameter of type boolean
     *
     * @param name of the parameter.
     * @param defaultValue in case of null
     * @return the parameter value
     */
    fun getBooleanParameter(name: String, defaultValue: Boolean): Boolean

    /**
     * Retrieves a parameter of type integer
     *
     * @param name of the parameter.
     * @param defaultValue in case of null
     * @return the parameter value
     */
    fun getIntegerParameter(name: String, defaultValue: Int): Int

    /**
     * Retrieves a parameter of type Double
     * @param name of the parameter.
     * @param defaultValue in case of null
     * @return the parameter value
     */
    fun getDoubleParameter(name: String, defaultValue: Double): Double

    /**
     * Retrieves a parameter of type string
     *
     * @param name of the parameter.
     * @throws ParameterNotConfiguredException if the parameter value is null
     * @return the parameter value
     */
    fun getStringParameter(name: String): String

    /**
     * Retrieves a parameter of type boolean
     *
     * @param name of the parameter.
     * @throws ParameterNotConfiguredException if the parameter value is null
     * @throws ParameterMisconfiguredException if the parameter value failed boolean parsing
     * @return the parameter value
     */
    fun getBooleanParameter(name: String): Boolean

    /**
     * Retrieves a parameter of type integer
     *
     * @param name of the parameter.
     * @throws ParameterNotConfiguredException if the parameter value is null
     * @throws ParameterMisconfiguredException if the parameter value failed integer parsing
     * @return the parameter value
     */
    fun getIntegerParameter(name: String): Int

    /**
     * Retrieves a parameter of type Double
     * @param name of the parameter.
     * @throws ParameterNotConfiguredException if the parameter value is null
     * @throws ParameterMisconfiguredException if the parameter value failed double parsing
     * @return the parameter value
     */
    fun getDoubleParameter(name: String): Double
}