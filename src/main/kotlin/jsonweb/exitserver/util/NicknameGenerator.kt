package jsonweb.exitserver.util

import jsonweb.exitserver.domain.user.UserRepository
import org.json.JSONObject
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import javax.annotation.PostConstruct
import kotlin.random.Random

@Component
class NicknameGenerator(private val userRepository: UserRepository) {

    val log = logger()
    private var randomNicknameList: MutableList<String> = mutableListOf()

    companion object {
        private const val MAX_LENGTH = 6
    }

    @PostConstruct
    private fun generateNickname() {
        try {
            val response = RestTemplate().getForEntity(
                "https://nickname.hwanmoo.kr?format=json&count=50",
                String::class.java
            )
            JSONObject(response.body).getJSONArray("words")
                .forEach {
                    it as String
                    val generatedNickname = "${it.split(" ")[0]}${Random.nextInt(1000)}"
                    if (generatedNickname.length <= MAX_LENGTH)
                        randomNicknameList.add(generatedNickname)
                }
        } catch (e: Exception) {
            log.warn(
                "[{}] random nickname auto generation fail, DETAIL {}",
                e.javaClass.simpleName, e.message
            )
        }
    }

    fun getRandomNickname(): String {
        while (true) {
            val nickname = randomNicknameList.removeAt(0)
            if (randomNicknameList.isEmpty()) generateNickname()
            if (!userRepository.existsByNickname(nickname)) return nickname
        }
    }


}