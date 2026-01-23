package com.TrungTinhBackend.check_link_backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.util.TimeZone;

@SpringBootApplication
@EnableCaching
public class CheckLinkBackendApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

		Dotenv dotenv = Dotenv.load();

		System.setProperty("SECRET_STRING", dotenv.get("SECRET_STRING"));
		System.setProperty("DATABASE_URL", dotenv.get("DATABASE_URL"));
		System.setProperty("DATABASE_USERNAME", dotenv.get("DATABASE_USERNAME"));
		System.setProperty("DATABASE_PASSWORD", dotenv.get("DATABASE_PASSWORD"));
		System.setProperty("google.api.key", dotenv.get("google.api.key"));
		System.setProperty("virustotal.api.key", dotenv.get("virustotal.api.key"));
		System.setProperty("phishtank.api.url", dotenv.get("phishtank.api.url"));
		System.setProperty("MAIL_HOST", dotenv.get("MAIL_HOST"));
		System.setProperty("MAIL_PORT", dotenv.get("MAIL_PORT"));
		System.setProperty("MAIL_USERNAME", dotenv.get("MAIL_USERNAME"));
		System.setProperty("MAIL_PASSWORD", dotenv.get("MAIL_PASSWORD"));
		System.setProperty("GOOGLE_AUTHORIZATION_URI", dotenv.get("GOOGLE_AUTHORIZATION_URI"));
		System.setProperty("GOOGLE_TOKEN_URI", dotenv.get("GOOGLE_TOKEN_URI"));
		System.setProperty("GOOGLE_USER_INFO_URI", dotenv.get("GOOGLE_USER_INFO_URI"));
		System.setProperty("GOOGLE_CLIENT_ID", dotenv.get("GOOGLE_CLIENT_ID"));
		System.setProperty("GOOGLE_CLIENT_SECRET", dotenv.get("GOOGLE_CLIENT_SECRET"));
		System.setProperty("GOOGLE_REDIRECT_URI", dotenv.get("GOOGLE_REDIRECT_URI"));
		System.setProperty("GOOGLE_SCOPE", dotenv.get("GOOGLE_SCOPE"));
		System.setProperty("REDIS_HOST", dotenv.get("REDIS_HOST"));
		System.setProperty("REDIS_PORT", dotenv.get("REDIS_PORT"));
		System.setProperty("REDIS_PASSWORD", dotenv.get("REDIS_PASSWORD"));

//		----------------------------------------------------------------
//		System.setProperty("SECRET_STRING", System.getenv("SECRET_STRING"));
//		System.setProperty("DATABASE_URL", System.getenv("DATABASE_URL"));
//		System.setProperty("DATABASE_USERNAME", System.getenv("DATABASE_USERNAME"));
//		System.setProperty("DATABASE_PASSWORD", System.getenv("DATABASE_PASSWORD"));
//		System.setProperty("google.api.key", System.getenv("google.api.key"));
//		System.setProperty("virustotal.api.key", System.getenv("virustotal.api.key"));
//		System.setProperty("phishtank.api.url", System.getenv("phishtank.api.url"));
//		System.setProperty("MAIL_HOST", System.getenv("MAIL_HOST"));
//		System.setProperty("MAIL_PORT", System.getenv("MAIL_PORT"));
//		System.setProperty("MAIL_USERNAME", System.getenv("MAIL_USERNAME"));
//		System.setProperty("MAIL_PASSWORD", System.getenv("MAIL_PASSWORD"));
//		System.setProperty("mailersend.api.key", System.getenv("mailersend.api.key"));
//		System.setProperty("GOOGLE_AUTHORIZATION_URI", System.getenv("GOOGLE_AUTHORIZATION_URI"));
//		System.setProperty("GOOGLE_TOKEN_URI", System.getenv("GOOGLE_TOKEN_URI"));
//		System.setProperty("GOOGLE_USER_INFO_URI", System.getenv("GOOGLE_USER_INFO_URI"));
//		System.setProperty("GOOGLE_CLIENT_ID", System.getenv("GOOGLE_CLIENT_ID"));
//		System.setProperty("GOOGLE_CLIENT_SECRET", System.getenv("GOOGLE_CLIENT_SECRET"));
//		System.setProperty("GOOGLE_REDIRECT_URI", System.getenv("GOOGLE_REDIRECT_URI"));
//		System.setProperty("GOOGLE_SCOPE", System.getenv("GOOGLE_SCOPE"));
//		System.setProperty("REDIS_HOST", System.getenv("REDIS_HOST"));
//		System.setProperty("REDIS_PORT", System.getenv("REDIS_PORT"));
//		System.setProperty("REDIS_PASSWORD", System.getenv("REDIS_PASSWORD"));

		SpringApplication.run(CheckLinkBackendApplication.class, args);
	}

}
