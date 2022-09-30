package com.bulpros.formio.repository.util;

import com.bulpros.formio.model.User;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class GenerateTokenUtil {

    @Value("${com.bulpros.formio.userprofile.project.id}")
    private String projectUserProfileId;
    @Value("${com.bulpros.formio.userprofile.form.id}")
    private String formUserProfileId;
    @Value("${com.bulpros.formio.jwt.secret}")
    private String secretKey;


    private static String PROJECT_USER_PROFILE_ID;
    private static String FORM_USER_PROFILE_ID;
    private static String SECRET_KEY;

    @Value("${com.bulpros.formio.userprofile.project.id}")
    public void setProjectUserProfileId(String projectUserProfileId){
        GenerateTokenUtil.PROJECT_USER_PROFILE_ID = projectUserProfileId;
    }

    @Value("${com.bulpros.formio.userprofile.form.id}")
    public void setFormUserProfileId(String formUserProfileId){
        GenerateTokenUtil.FORM_USER_PROFILE_ID = formUserProfileId;
    }

    @Value("${com.bulpros.formio.jwt.secret}")
    public void setSecretKey(String secretKey){
        GenerateTokenUtil.SECRET_KEY = secretKey;
    }

    public static String createFormioJWT(User user){
        Map<String, Object> claims = user.getClaims();
        List<String> formIoGroupIds = (List<String>) claims.get("formio_groups");
        String firstName = (String) claims.get("given_name");
        String lastName = (String) claims.get("family_name");

        JSONObject form = new JSONObject();
        form.put("_id", FORM_USER_PROFILE_ID);

        JSONObject project = new JSONObject();
        project.put("_id", PROJECT_USER_PROFILE_ID);

        JSONObject data = new JSONObject();
        data.put("firstName", firstName);
        data.put("lastname", lastName);

        JSONObject userFormIo = new JSONObject();
        userFormIo.put("_id", "external");
        userFormIo.put("data", data);
        userFormIo.put("roles", formIoGroupIds);

        Map<String, Object> claimsFormioToken = new HashMap<>();
        claimsFormioToken.put("external", "true");
        claimsFormioToken.put("form", form);
        claimsFormioToken.put("project", project);
        claimsFormioToken.put("user", userFormIo);

        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");

        JwtBuilder builder = Jwts.builder()
            .setHeader(header)
            .setClaims(claimsFormioToken)
            .signWith(
            Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)),
            SignatureAlgorithm.HS256
        );
            return builder.compact();
        }
    }

