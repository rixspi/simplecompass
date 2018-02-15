object Versions {

}

object RxVersions {
    val java = "2.0.6"
    val android = "2.0.1"
    val gallery = "0.6.0"
    val permissions = "0.9.5@aar"
    val kotlin = "2.2.0"
    val binding = "2.0.0"
}

object Rx {
    val java = "io.reactivex.rxjava2:rxjava:${RxVersions.java}"
    val kotlin = "io.reactivex.rxjava2:rxkotlin:${RxVersions.kotlin}"
    val android = "io.reactivex.rxjava2:rxandroid:${RxVersions.android}"
    val binding = "com.jakewharton.rxbinding2:rxbinding-kotlin:${RxVersions.binding}"
    val permissions = "com.tbruyelle.rxpermissions2:rxpermissions:${RxVersions.permissions}"
}