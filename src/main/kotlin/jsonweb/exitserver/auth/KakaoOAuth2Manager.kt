package jsonweb.exitserver.auth

import org.json.JSONException
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
    val kakaoId: Long,
    val gender: String,
    val ageRange: String,
)

@Component
class KakaoOAuth2Manager(
    @Value("\${kakao.rest-api-key}") private val restApiKey: String
) {

    fun getKakaoUserInfo(authorizedCode: String): KakaoUserInfo {
        val kakaoAccessToken = fetchKakaoAccessToken(authorizedCode)
        return fetchKakaoUserInfoByToken(kakaoAccessToken)
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

    fun fetchKakaoUserInfoByToken(kakaoAccessToken: String): KakaoUserInfo {
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
        val bodyJson = JSONObject(response.body)
        val kakaoAccountJson = bodyJson.getJSONObject("kakao_account")
        val kakaoId = bodyJson.getLong("id")
        var ageRange: String
        var gender: String
        try {
            // 유저가 성별, 연령대 수집에 동의
            ageRange = kakaoAccountJson.getString("age_range")
            gender= kakaoAccountJson.getString("gender")
        } catch (e: JSONException) {
            // 비동의시 디폴트 값
            ageRange = "20~29"
            gender = "female"
        }
        return KakaoUserInfo(kakaoId, gender, ageRange)
    }

}