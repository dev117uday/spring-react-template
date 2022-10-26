package com.example.jwt.utility;

import java.io.IOException;

import com.example.jwt.exception.ExceptionBroker;
import com.example.jwt.model.Users;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Component
public class GoogleOAuthUtility {

	public Users verifyUserFromIdToken(String idToken) throws ExceptionBroker {

		OkHttpClient client = new OkHttpClient().newBuilder()
				.build();

		String url = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=" + idToken;

		Request request = new Request.Builder()
				.url(url)
				.method("GET", null)
				.build();

		String userInfoString;
		Integer responseCode;

		try {
			Response response = client.newCall(request).execute();
			userInfoString = response.body().string();
			responseCode = response.code();
			response.close();
		} catch (IOException e) {
			throw new ExceptionBroker("internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (responseCode / 100 == 4) {
			throw new ExceptionBroker("invalid id token", HttpStatus.BAD_REQUEST);
		}

		Users user;

		try {
			Gson gson = new Gson();
			user = gson.fromJson(userInfoString, Users.class);
		} catch (JsonSyntaxException e) {
			throw new ExceptionBroker("internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return user;
	}

}