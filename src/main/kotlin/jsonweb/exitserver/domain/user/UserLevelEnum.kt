package jsonweb.exitserver.domain.user

enum class UserLevelEnum(private val levelName: String, private val needExp: Int) {
    LEVEL_1("탈출노비", 100),
    LEVEL_2("탈출평민", 200),
    LEVEL_3("탈출양반", 300),
    LEVEL_4("탈출왕", 400);
    fun getLevelName() = levelName
    fun getNeedExp() = needExp
}