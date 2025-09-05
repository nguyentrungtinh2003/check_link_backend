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
		System.setProperty("google.api.key", dotenv.get("google.api.key"));

//		System.setProperty("DATABASE_URL", System.getenv("DATABASE_URL"));
		SpringApplication.run(CheckLinkBackendApplication.class, args);
	}

}
