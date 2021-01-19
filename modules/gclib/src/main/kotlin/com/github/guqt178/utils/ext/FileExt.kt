package com.github.guqt178.utils.ext

/*fun File.guessMimeType(): MediaType? {
    var path = absolutePath
    val fileNameMap = URLConnection.getFileNameMap()
    path = path.replace("#", "")
    var contentType = fileNameMap.getContentTypeFor(path)
    if (contentType == null) {
        contentType = "application/octet-stream"
    }
    return MediaType.parse(contentType)
}

fun String?.guessMimeType(): MediaType? {

    if (this.isNullOrEmpty() || !File(this).isFile) return null

    var filePath = this
    val fileNameMap = URLConnection.getFileNameMap()
    filePath = filePath.replace("#", "")
    var contentType = fileNameMap.getContentTypeFor(filePath)
    if (contentType == null) {
        contentType = "application/octet-stream"
    }
    return MediaType.parse(contentType)
}*/
