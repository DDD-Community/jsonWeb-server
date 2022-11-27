package jsonweb.exitserver.auth

import org.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate


data class KakaoUserInfo(
    val id: Long,
    val email: String,
    val nickname: String,
)

@Component
class KakaoOAuth2Manager(
    @Value("\${kakao.rest-api-key}") private val restApiKey: String
) {

    fun getKakaoUserInfo(authorizedCode: String) {
        val kakaoAccessToken = fetchKakaoAccessToken(authorizedCode)
        fetchKakaoUserInfoByToken(kakaoAccessToken)
    }

    fun fetchKakaoAccessToken(authorizedCode: String): String {
        val header = HttpHeaders()
        header.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8")

        val params = LinkedMultiValueMap<String, String>()
        params.add("grant_type", "authorization_code")
        params.add("client_id", restApiKey)
        params.add("redirect_uri", "http://localhost:8080/oauth/kakao")
        params.add("code", authorizedCode)

        val kakaoTokenRequest = HttpEntity<MultiValueMap<String, String>>(params, header)
        val response: ResponseEntity<String> = RestTemplate().exchange(
            "https://kauth.kakao.com/oauth/token",
            HttpMethod.POST,
            kakaoTokenRequest,
            String::class.java
        )

        return JSONObject(response.body).getString("access_token")
    }

    fun fetchKakaoUserInfoByToken(kakaoAccessToken: String) {
        val header = HttpHeaders()
        header.add("Authorization", "Bearer $kakaoAccessToken")
        header.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8")

        val kakaoProfileRequest = HttpEntity<MultiValueMap<String, String>>(header)
        val response: ResponseEntity<String> = RestTemplate().exchange(
            "https://kapi.kakao.com/v2/user/me",
            HttpMethod.POST,
            kakaoProfileRequest,
            String::class.java
        )

        println(JSONObject(response.body))
    }

}