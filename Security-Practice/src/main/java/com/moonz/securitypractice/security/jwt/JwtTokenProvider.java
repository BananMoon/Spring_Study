package com.moonz.securitypractice.security.jwt;

import com.moonz.securitypractice.security.UserDetailsServiceImpl;
import com.moonz.securitypractice.security.auth.dto.AuthResponseDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final long accessTokenExpiredTimeInMilliseconds = 30 * 60 * 1000L;   //30m
    private static final long REFRESH_TOKEN_VALID_TIME_IN_MS = 14 * 24 * 60 * 60 * 1000L;   //14d
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode("Y29tLWRuZC1iYWNrZW5kLW5pY2V0b21lZXR0ZWFtLXByb2plY3Qtand0LWRldi1zZWNyZXQta2V5LWNvbS1kbmQtYmFja2VuZC1uaWNldG9tZWV0dGVhbS1wcm9qZWN0LWp3dC1kZXYtc2VjcmV0LWtleQ=="));

/*
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[]  keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);    // HMAC-SHA algorithms
    }
*/

    /*
    토큰 생성 메서드
     */
    public AuthResponseDto.TokenInfo generateTokens(String username, String role) {
        System.out.println("로그: JwtTokenProvider.generateToken 실행!!");


        String accessToken = createAccessToken(username, role);
        String refreshToken = createRefreshToken();

        return new AuthResponseDto.TokenInfo(accessToken,refreshToken);
    }

    private String createAccessToken(String username, String role) {
        System.out.println("JwtTokenProvider.createAccessToken 수행!");

        Date accessTokenExpiryTime = new Date(System.currentTimeMillis() + accessTokenExpiredTimeInMilliseconds);
        // 권한
        String authorities = convertRoleToAuthorities(role);
        System.out.println(authorities);
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(accessTokenExpiryTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private String createRefreshToken() {
        Date refreshTokenExpiryTime = new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALID_TIME_IN_MS);
        return Jwts.builder()
                .setExpiration(refreshTokenExpiryTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /*
    토큰 유효성 검증 메서드
     */
    public boolean validateToken(String token) {
        try {
            System.out.println("로그: JwtTokenProvider.validateToken 실행!");
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            /* 자동으로 catch를 통해 만료 체크하나봐.
            // 토큰의 만료시간 체크
            if (!claimsJws.getBody().getExpiration().before(new Date())) {
                return true;
            }*/
            return true;
        } catch (io.jsonwebtoken.security.SecurityException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        System.out.println("로그: JwtTokenProvider.validateToken에서 false 리턴할 것임");
        return false;
    }

    /*
    토큰 복호화(decryption)하여 정보 추출 메서드
    @Return
     */
    public Authentication getAuthentication(String accessToken) {

        String usernameFromToken = getUsernameFromToken(accessToken);
        System.out.println("Token에서 꺼낸 subject(username)"+usernameFromToken);

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(usernameFromToken);
        System.out.println("userDeatils의 authorities:"+ userDetails.getAuthorities());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUsernameFromToken(String token) {
        return parseClaimsJws(token).getBody().getSubject();
    }

    private Jws<Claims> parseClaimsJws(String token) {
        // 만료된 토큰이어도 전달
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }
    private String convertRoleToAuthorities(String fullName) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(fullName));

        return grantedAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }
}