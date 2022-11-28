package jsonweb.exitserver.auth

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class OAuthController(private val kakaoOAuth2Manager: KakaoOAuth2Manager) {

    @GetMapping("/oauth/kakao")
    fun kakaoCallback(code: String) = kakaoOAuth2Manager.getKakaoUserInfo(code)

}