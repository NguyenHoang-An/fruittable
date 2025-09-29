package com.example.fruitables.config;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

// Cấu hình MongoDB: tạo bean MongoTemplate từ URI trong application.properties
@Configuration
public class MongoConfig {

    // Lấy chuỗi kết nối MongoDB từ cấu hình
    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    // Tạo bean MongoTemplate dùng cho thao tác với MongoDB
    @Bean
    public MongoTemplate mongoTemplate() {
        // Kiểm tra nếu URI không hợp lệ hoặc không có
        if (mongoUri == null || mongoUri.isEmpty()) {
            throw new IllegalStateException("mongoDb connection string is missing");
        }

        // Kiểm tra xem URI có hợp lệ không
        if (!mongoUri.startsWith("mongodb://") && !mongoUri.startsWith("mongodb+srv://")) {
            throw new IllegalArgumentException("Invalid MongoDB URI. It should start with 'mongodb://' or 'mongodb+srv://'.");
        }

        // Suy ra tên database từ URI nếu có, mặc định "fruitable"
        ConnectionString cs = new ConnectionString(mongoUri);
        String dbName = cs.getDatabase() != null ? cs.getDatabase() : "fruitable";

        // Tạo MongoTemplate với URI MongoDB đã kiểm tra
        return new MongoTemplate(MongoClients.create(mongoUri), dbName);
    }
}
