package cn.evanzuo.admin.auth.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * 获取RSA公钥接口
 *
 * @author EvanZuo[739221432@qq.com] 2023/09/24
 */
@RestController
public class KeyPairController {

  private final KeyPair keyPair;

  public KeyPairController(KeyPair keyPair) {
    this.keyPair = keyPair;
  }

  @GetMapping("/rsa/publicKey")
  public Map<String, Object> getKey() {
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    RSAKey key = new RSAKey.Builder(publicKey).build();
    return new JWKSet(key).toJSONObject();
  }

}
