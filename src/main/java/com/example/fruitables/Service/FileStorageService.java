package com.example.fruitables.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.upload-dir:uploads}")
    private String uploadDir; // Thư mục lưu trữ file, mặc định là "uploads"
    @Value("${app.upload-avatar-subdir:avatars}")
    private String avatarSubDir; // Thư mục con lưu trữ avatar, mặc định là "avatars"
    @Value("${app.static-upload.enabled:false}")
    private boolean staticUploadEnabled; // Dev: ghi vào static

    /**
     *  Trả về thư mục gốc ghi file ở DEV (static) hoặc PROD (ngoài classpath).
     */
    private Path resolveRootDir(){
        if (staticUploadEnabled){
            // DEV: ghi trực tiếp vào static để thấy ngay
            return Paths.get("src/main/resources/static").toAbsolutePath().normalize()
                    .resolve(uploadDir);
        }else {
            // PROD: ghi vào thư mục ngoài classpath, cạnh file jar
            return Paths.get(uploadDir).toAbsolutePath().normalize();
        }
    }

    private static String safeExtension(String originalFilename){
        if (!StringUtils.hasText(originalFilename))
            return ".png";
        String name = originalFilename.trim();
        int dot = name.lastIndexOf('.');
        String ext = (dot >= 0 ? name.substring(dot).toLowerCase() : ".png");
        return switch (ext) {
            case ".png", ".jpg", ".jpeg", ".gif", ".bmp" -> ext;
            default -> ".png"; //chuyển về png nếu đuôi lạ
        };
    }

    private static void ensureDir(Path p) throws IOException{
        if (!Files.exists(p)){
            Files.createDirectories(p);
        }
    }

    /**
     * Lưu avatar và trả về đường dẫn **tương đối** để nhúng vào HTML (ví dụ: "/uploads/avatars/xxx.png")
     */

    public String saveAvatar(MultipartFile file) throws IOException{
        if (file == null || file.isEmpty()){
            return null;
        }
        //kiểm tra Mime cơ bản
        String ct = file.getContentType() != null ? file.getContentType().toLowerCase() : "";
        if (!(ct.contains("png") || ct.contains("jpeg") || ct.contains("jpg") || ct.contains("gif") || ct.contains("bmp"))){
            throw new IOException("File không phải ảnh hợp lệ (png/jpg/jpeg/webp/gif). " );
        }

        //Tạo tên file an toàn
        String ext = safeExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID().toString().replace("-","") + ext;

        //Xác định thư mục lưu
        Path root = resolveRootDir(); //thư mục gốc
        Path avatarDir = root.resolve(avatarSubDir); //thư mục con lưu avatar
        ensureDir(avatarDir);

        //ghi file
        Path target = avatarDir.resolve(filename).normalize();
        file.transferTo(target.toFile());

        //Trả về URL hiển thị
        return "/uploads/" + avatarSubDir + "/" + filename;

    }
    public String saveTo(String subDir, MultipartFile file) throws IOException {
        String cleanName = StringUtils.cleanPath(file.getOriginalFilename());
        String ext = "";
        int dot = cleanName.lastIndexOf('.');
        if (dot >= 0) ext = cleanName.substring(dot);
        String filename = java.util.UUID.randomUUID().toString().replace("-", "") + ext;

        Path root = resolveRootDir(); // đã có sẵn trong lớp của bạn
        Path dir = root.resolve(subDir.replaceFirst("^/+", ""));
        ensureDir(dir);
        Path target = dir.resolve(filename).normalize();
        file.transferTo(target.toFile());

        String sub = subDir.startsWith("/") ? subDir.substring(1) : subDir;
        return "/uploads/" + sub + "/" + filename;
    }

}
