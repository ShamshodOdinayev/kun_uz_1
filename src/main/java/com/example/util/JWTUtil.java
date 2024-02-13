package com.example.util;

import com.example.dto.JwtDTO;
import com.example.enums.ProfileRole;
import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.Objects;

public class JWTUtil {
    private static final int tokenLiveTime = 1000 * 3600 * 24; // 1-day
    private static final int emailTokenLiveTime = 1000 * 3600; // 1-day
    private static final String secretKey = "dsjks34gdEffjksWEFWejkhefjk4i387SFre739fr742974chejhfejkfhdfs8734389gherugt8743g4g3h4g3487348g38743487hrt3hRFGERTEE34erERr34reFEREr43erger";

    public static String encode(Integer profileId, ProfileRole role) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.issuedAt(new Date());

        SignatureAlgorithm sa = SignatureAlgorithm.HS512;
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), sa.getJcaName());

        jwtBuilder.signWith(secretKeySpec);

        jwtBuilder.claim("id", profileId);
        jwtBuilder.claim("role", role);

        jwtBuilder.expiration(new Date(System.currentTimeMillis() + (tokenLiveTime)));
        jwtBuilder.issuer("KunUzTest");
        return jwtBuilder.compact();
    }

    public static String encode(String email, ProfileRole role) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.issuedAt(new Date());

        SignatureAlgorithm sa = SignatureAlgorithm.HS512;
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), sa.getJcaName());

        jwtBuilder.signWith(secretKeySpec);

        jwtBuilder.claim("email", email);
        jwtBuilder.claim("role", role);

        jwtBuilder.expiration(new Date(System.currentTimeMillis() + (tokenLiveTime)));
        jwtBuilder.issuer("KunUzTest");
        return jwtBuilder.compact();
    }


    public static JwtDTO decode(String token) {
        SignatureAlgorithm sa = SignatureAlgorithm.HS512;
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), sa.getJcaName());
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(secretKeySpec)
                .build();
        Jws<Claims> jws = jwtParser.parseSignedClaims(token);
        Claims claims = jws.getPayload();
        Integer id = (Integer) claims.get("id");
        String role = (String) claims.get("role");
        if (role != null) {
            ProfileRole profileRole = ProfileRole.valueOf(role);
            return new JwtDTO(id, profileRole);
        }
        return new JwtDTO(id);
    }

    public static JwtDTO decodeForSpringSecurity(String token) {
        SignatureAlgorithm sa = SignatureAlgorithm.HS512;
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), sa.getJcaName());
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(secretKeySpec)
                .build();

        Jws<Claims> jws = jwtParser.parseSignedClaims(token);
        Claims claims = jws.getPayload();

        String email = (String) claims.get("email");
        String role = (String) claims.get("role");
        ProfileRole profileRole = ProfileRole.valueOf(role);
        return new JwtDTO(profileRole, email);
    }


    public static String encodeForEmail(Integer profileId) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.issuedAt(new Date());
        SignatureAlgorithm sa = SignatureAlgorithm.HS512;
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), sa.getJcaName());
        jwtBuilder.signWith(secretKeySpec);
        jwtBuilder.claim("id", profileId);
        jwtBuilder.expiration(new Date(System.currentTimeMillis() + (emailTokenLiveTime)));
        jwtBuilder.issuer("KunUzTest");
        return jwtBuilder.compact();
    }

    public static Boolean requestHeaderCheckAdmin(String jwt) {
        JwtDTO jwtDTO = JWTUtil.decode(jwt);
        return jwtDTO.getRole().equals(ProfileRole.ROLE_ADMIN);
    }

    public static Boolean requestHeaderCheckAdmin(String jwt, Integer id) {
        JwtDTO jwtDTO = JWTUtil.decode(jwt);
        return jwtDTO.getRole().equals(ProfileRole.ROLE_ADMIN) && Objects.equals(jwtDTO.getId(), id);
    }

    public static Boolean requestHeaderCheckUser(String jwt, Integer id) {
        JwtDTO jwtDTO = JWTUtil.decode(jwt);
        return jwtDTO.getRole().equals(ProfileRole.ROLE_USER) && Objects.equals(jwtDTO.getId(), id);
    }

}
