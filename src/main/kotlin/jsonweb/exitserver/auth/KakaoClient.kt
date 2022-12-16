package jsonweb.exitserver.auth

import org.json.JSONException
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate


data class KakaoUserInfo(
    val kakaoId: Long,
    val gender: String,
    val ageRange: String,
)

@Service
class KakaoClient(
    @Value("\${kakao.rest-api-key}") private val restApiKey: String,
    @Value("\${kakao.app-admin-key}") private val appAdminKey: String
) {

    fun getRedirectUri(): String {
        return if (System.getProperty("os.name").contains("linux")) {
            // deploy linux
            "http://13.124.179.64:8080/user/login"
        } else {
            // local window
            "http://localhost:8080/user/login"
        }
    }

    fun getKakaoUserInfo(authorizedCode: String): KakaoUserInfo {
        val kakaoAccessToken = fetchKakaoAccessToken(authorizedCode)
        return fetchKakaoUserInfoByToken(kakaoAccessToken)
    }

    private fun fetchKakaoAccessToken(authorizedCode: String): String {
        val header = HttpHeaders()
        header.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8")

        val params = LinkedMultiValueMap<String, String>()
        params.add("grant_type", "authorization_code")
        params.add("client_id", restApiKey)
        params.add("redirect_uri", getRedirectUri())
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

    private fun fetchKakaoUserInfoByToken(kakaoAccessToken: String): KakaoUserInfo {
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
            gender = kakaoAccountJson.getString("gender")
        } catch (e: JSONException) {
            // 비동의시 디폴트 값
            ageRange = ""
            gender = ""
        }
        return KakaoUserInfo(kakaoId, gender, ageRange)
    }

    fun logout(kakaoId: Long): Boolean =
        sendRevokeRequest(kakaoId, "https://kapi.kakao.com/v1/user/logout")


    fun unlink(kakaoId: Long): Boolean =
        sendRevokeRequest(kakaoId, "https://kapi.kakao.com/v1/user/unlink")


    private fun sendRevokeRequest(kakaoId: Long, url: String): Boolean {
        val header = HttpHeaders()
        header.add("Authorization", "KakaoAK $appAdminKey")
        header.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8")

        val params = LinkedMultiValueMap<String, String>()
        params.add("target_id_type", "user_id")
        params.add("target_id", kakaoId.toString())

        val unlinkRequest = HttpEntity<MultiValueMap<String, String>>(params, header)
        val response: ResponseEntity<String> = RestTemplate().exchange(
            url,
            HttpMethod.POST,
            unlinkRequest,
            String::class.java
        )

        return JSONObject(response.body).getLong("id") == kakaoId
    }

}