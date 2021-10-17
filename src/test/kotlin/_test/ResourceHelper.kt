package io.toolisticon.lib.krid._test

object ResourceHelper {

  fun readFile(resourcePath: String): String = when {
    resourcePath.startsWith("/") -> ResourceHelper::class.java.getResource(resourcePath)?.readText()
      ?: throw IllegalArgumentException("resource not found: $resourcePath")
    else -> ResourceHelper.readFile("/$resourcePath")
  }

}
