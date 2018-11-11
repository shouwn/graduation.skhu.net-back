package com.shouwn.graduation.utils.annotation


@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class Detail(val type: String = "ROW") {
    companion object {
        val ROW = "ROW"
        val ID = "ID"
        val IDS = "IDS"
    }
}
