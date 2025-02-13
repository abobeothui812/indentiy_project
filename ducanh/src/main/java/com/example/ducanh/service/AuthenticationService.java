package com.example.ducanh.service;

import com.example.ducanh.dto.reponse.AuthenticationResponse;
import com.example.ducanh.dto.reponse.IntrospectResponse;
import com.example.ducanh.dto.request.AuthenticationRequest;
import com.example.ducanh.dto.request.IntrospectRequest;
import com.example.ducanh.dto.request.RefreshRequest;
import com.example.ducanh.entity.InvalidToken;
import com.example.ducanh.entity.Users;
import com.example.ducanh.exception.AppException;
import com.example.ducanh.exception.ErrorCode;
import com.example.ducanh.repository.InvalidTokenRepository;
import com.example.ducanh.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.example.ducanh.dto.request.LogOutRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthenticationService {

    UserRepository userRepository;
    InvalidTokenRepository invalidTokenRepository;
    @NonFinal
    @Value("${jwt.signerKey}")
    protected  String SECRET_KEY ;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALIDITY_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    private long REFRESHABLE_DURATION;

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USERNOTFOUND_EXCEPTION));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean isAuthenticated= passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!isAuthenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        var token = generateToken(user);

        return AuthenticationResponse.builder()
                    .isAuthenticated(true)
                    .token(token)
                    .build();

    }

    private String generateToken(Users users){
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(users.getUsername())
                .issuer("ducanh")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALIDITY_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope",buildScope(users))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SECRET_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create Token",e);
            throw new RuntimeException(e);
        }
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token,false);

        } catch (AppException e) {
            isValid = false;
        }


        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    private String buildScope(Users users){
        StringJoiner stringjoiner = new StringJoiner(" ");

        if(!CollectionUtils.isEmpty(users.getRoles())){
            users.getRoles().forEach(role -> {
                stringjoiner.add("ROLE_" + role.getName());

                if( !CollectionUtils.isEmpty(role.getPermissionsSet())){
                role.getPermissionsSet().forEach(permission -> stringjoiner.add(permission.getName()));}

            });
        }


        return stringjoiner.toString();
    }


    public void logout(LogOutRequest request) throws JOSEException, ParseException{

        SignedJWT signToken = null;
        try {
            signToken = verifyToken(request.getToken(),true);

            String jit  = signToken.getJWTClaimsSet().getJWTID();

            Date expirationDate = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidToken token = InvalidToken.builder()
                    .tokenID(jit)
                    .expiryTime(expirationDate)
                    .build();

            invalidTokenRepository.save(token);
        } catch (AppException e) {
            log.info("Token already expired");
        }


    }

    private SignedJWT verifyToken(String token,boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());

        //System.out.println("verifyToken");

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime =(isRefresh) ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESHABLE_DURATION,ChronoUnit.SECONDS).toEpochMilli()) : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified= signedJWT.verify(verifier);

        if(!verified && expiryTime.after(new Date())){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if(invalidTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }


    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var sigJWT = verifyToken(request.getToken(),true);

        var jit = sigJWT.getJWTClaimsSet().getJWTID();

        var expiryTime = sigJWT.getJWTClaimsSet().getExpirationTime();


        InvalidToken invalidToken = InvalidToken.builder()
                .tokenID(jit)
                .expiryTime(expiryTime)
                .build();

        invalidTokenRepository.save(invalidToken);

        var username = sigJWT.getJWTClaimsSet().getSubject();

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .isAuthenticated(true)
                .token(token)
                .build();

    }


}
