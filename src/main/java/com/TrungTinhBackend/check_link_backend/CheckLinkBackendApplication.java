package com.TrungTinhBackend.check_link_backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class CheckLinkBackendApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

		Dotenv dotenv = Dotenv.load();

		// Đọc các biến từ file .env và thiết lập chúng vào hệ thống
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

//		System.setProperty("DATABASE_URL", System.getenv("DATABASE_URL"));
		SpringApplication.run(CheckLinkBackendApplication.class, args);
	}

}
