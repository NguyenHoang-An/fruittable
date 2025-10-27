package com.example.fruitables.controller;

import com.example.fruitables.Service.CurrentUserService;
import com.example.fruitables.Service.FileStorageService;
import com.example.fruitables.user.User;
import com.example.fruitables.user.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Controller
public class ProfileEditController {

    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public ProfileEditController(CurrentUserService currentUserService,
                                 UserRepository userRepository,
                                 FileStorageService fileStorageService) {
        this.currentUserService = currentUserService;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/profile/edit")
    public String edit(Model model) {
        User user = currentUserService.getCurrentUser()
                .orElseThrow(() -> new RuntimeException("User chưa đăng nhập"));
        model.addAttribute("user", user);
        return "profile-edit";
    }

    @PostMapping("/profile/edit")
    public String update(String fullName,
                         String email,
                         String phone,
                         String gender,
                         String dob,            // yyyy-MM-dd (từ <input type="date">)
                         String address,
                         MultipartFile avatarFile, // <input type="file" name="avatarFile">
                         Model model) {

        User user = currentUserService.getCurrentUser()
                .orElseThrow(() -> new RuntimeException("User chưa đăng nhập"));

        // Cập nhật trường text
        if (StringUtils.hasText(fullName)) user.setFullName(fullName.trim());
        if (StringUtils.hasText(email))   user.setEmail(email.trim());
        user.setPhone(StringUtils.hasText(phone) ? phone.trim() : null);
        user.setGender(StringUtils.hasText(gender) ? gender.trim() : null);
        user.setAddress(StringUtils.hasText(address) ? address.trim() : null);

        // Parse ngày sinh (nếu có)
        if (StringUtils.hasText(dob)) {
            try {
                user.setDob(LocalDate.parse(dob.trim())); // định dạng mặc định yyyy-MM-dd
            } catch (DateTimeParseException ignored) {
                // Không set nếu sai định dạng; bạn có thể add thông báo nếu muốn
            }
        } else {
            user.setDob(null);
        }

        // Upload ảnh (tùy chọn)
        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                String url = fileStorageService.saveAvatar(avatarFile); // trả về "/uploads/avatars/xxx.png"
                if (url != null) {
                    user.setAvatarUrl(url);
                }
            } catch (IOException e) {
                model.addAttribute("error", "Upload ảnh thất bại: " + e.getMessage());
                model.addAttribute("user", user);
                return "profile-edit";
            }
        }

        userRepository.save(user); // MongoDB
        return "redirect:/profile";
    }
}
