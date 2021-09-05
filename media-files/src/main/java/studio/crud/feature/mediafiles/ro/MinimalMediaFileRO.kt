package studio.crud.feature.mediafiles.ro

import studio.crud.feature.jpa.ro.AbstractJpaUpdatableRO

open class MinimalMediaFileRO : AbstractJpaUpdatableRO() {
    var uuid: String? = null
    var name: String? = null
    var extension: String? = null
    var size: Long? = null
    var alias: String? = null
}