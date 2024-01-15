package com.bookingapptim24.clients;

import android.content.Context;
import android.content.SharedPreferences;

import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.util.Base64URL;

import org.json.JSONObject;

import java.util.Arrays;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class SessionManager {
    private static final String SHARED_PREF_NAME = "user_session";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ROLE = "role";
    private static final String KEY_ID = "id";

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserSession(String accessToken) {

        try {
            JWSObject jwsObject = JWSObject.parse(accessToken);

            Base64URL payload = jwsObject.getPayload().toBase64URL();

            JSONObject jsonObject = new JSONObject(payload.decodeToString());

            String username = jsonObject.getString("sub");
            String role = jsonObject.getString("role");
            Long id = jsonObject.getLong("id");
            System.out.println(username + " " + role + " " + id.toString());
            editor.putString(KEY_USERNAME, username);
            editor.putString(KEY_ROLE, role);
            editor.putLong(KEY_ID, id);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    public String getRole() {
        return sharedPreferences.getString(KEY_ROLE, null);
    }

    public Long getUserId() {
        return sharedPreferences.getLong(KEY_ID, -1);
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}
