// @GENERATOR:play-routes-compiler
// @SOURCE:/home/henri/IdeaProjects/Scala_project/play-scala-hello-world-tutorial/conf/routes
// @DATE:Mon Mar 04 17:14:29 CET 2019


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
