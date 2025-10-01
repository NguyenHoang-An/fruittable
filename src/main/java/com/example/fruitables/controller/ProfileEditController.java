package com.example.fruitables.controller;

import com.example.fruitables.Service.CurrentUserService;
import com.example.fruitables.user.User;
import com.example.fruitables.user.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Controller
public class ProfileEditController {

    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;

    // đường dẫn thư mục lưu file ngoài project (có thể cấu hình trong application.properties)
    private final Path uploadRoot = Paths.get("uploads/avatars");

    public ProfileEditController(CurrentUserService currentUserService, UserRepository userRepository) {
        this.currentUserService = currentUserService;
        this.userRepository = userRepository;
    }

    @GetMapping("/profile/edit")
    public String edit(Model model) {
        User user = currentUserService.getCurrentUser()
                .orElseThrow(() -> new RuntimeException("User chưa đăng nhập"));
        model.addAttribute("user", user);
        return "profile-edit";
    }

    @PostMapping("/profile/edit")
    public String update(String fullname,
                         String phone,
                         String address,
                         MultipartFile avatarFile,
                         Model model) throws IOException {

        User user = currentUserService.getCurrentUser()
                .orElseThrow(() -> new RuntimeException("User chưa đăng nhập"));

        // cập nhật text fields
        if (StringUtils.hasText(fullname)) user.setFullname(fullname.trim());
        user.setPhone(StringUtils.hasText(phone) ? phone.trim() : null);
        user.setAddress(StringUtils.hasText(address) ? address.trim() : null);

        // xử lý upload avatar (tùy chọn)
        if (avatarFile != null && !avatarFile.isEmpty()) {
            // đảm bảo thư mục tồn tại
            if (!Files.exists(uploadRoot)) Files.createDirectories(uploadRoot);

            String ext = OptionalExt.getExtension(avatarFile.getOriginalFilename());
            String filename = UUID.randomUUID() + (ext.isEmpty() ? ".png" : ext);
            Path dest = uploadRoot.resolve(filename);
            Files.copy(avatarFile.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);

            // URL public trỏ tới ResourceHandler (mục 5.2)
            user.setAvatarUrl("/uploads/avatars/" + filename);
        }

        userRepository.save(user);
        return "redirect:/profile";
    }

    // helper để lấy đuôi file an toàn
    static class OptionalExt {
        static String getExtension(String originalName) {
            if (!StringUtils.hasText(originalName)) return "";
            String name = originalName.trim();
            int i = name.lastIndexOf('.');
            if (i < 0) return "";
            String ext = name.substring(i).toLowerCase();
            // chặn 1 số đuôi nguy hiểm
            return switch (ext) {
                case ".png", ".jpg", ".jpeg", ".webp", ".gif" -> ext;
                default -> ".png";
            };
        }
    }
}
