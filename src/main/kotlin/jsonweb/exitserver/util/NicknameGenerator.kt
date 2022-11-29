package jsonweb.exitserver.util

import jsonweb.exitserver.common.logger
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

    @PostConstruct
    private fun generateNickname() {
        try {
            val response = RestTemplate().getForEntity(
                "https://nickname.hwanmoo.kr?format=json&count=50",
                String::class.java
            )
            JSONObject(response.body).getJSONArray("words")
                .forEach { randomNicknameList.add("$it 탈출러${Random.nextInt(1000)}") }
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
            if (randomNicknameList.isEmpty()) getRandomNickname()
            if (!userRepository.existsByNickname(nickname)) return nickname
        }
    }
}