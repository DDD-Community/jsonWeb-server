package jsonweb.exitserver.domain.user

enum class UserLevelEnum(private val levelName: String, private val needExp: Int) {
    LEVEL_1("탈출노비", 0),
    LEVEL_2("탈출평민", 99),
    LEVEL_3("탈출양반", 299),
    LEVEL_4("탈출왕", 399);
    fun getLevelName() = levelName

    companion object {
        @JvmStatic
        fun getLevelName(exp: Int): String {
            return if (exp > LEVEL_4.needExp) {
                LEVEL_4.levelName
            } else if (exp > LEVEL_3.needExp) {
                LEVEL_3.levelName
            } else if (exp > LEVEL_2.needExp) {
                LEVEL_2.levelName
            } else {
                LEVEL_1.levelName
            }
        }
    }
}