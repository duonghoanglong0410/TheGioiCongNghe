package com.example.thegioicongnghe.Admin.Service;

import java.text.Normalizer;
import java.util.Locale;

public class SlugUtils {
    public static String toSlug(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        // Bước 1: Loại bỏ dấu
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String withoutDiacritics = normalized.replaceAll("\\p{M}", "");

        // Bước 2: Chuyển thành chữ thường
        String lowerCase = withoutDiacritics.toLowerCase(Locale.ENGLISH);

        // Bước 3: Thay thế khoảng trắng và ký tự đặc biệt bằng dấu gạch ngang
        String slug = lowerCase.replaceAll("[^a-z0-9\\s-]", "") // Loại bỏ ký tự không hợp lệ
                .replaceAll("\\s+", "-")         // Thay khoảng trắng bằng gạch ngang
                .replaceAll("-+", "-");          // Loại bỏ gạch ngang thừa
        return slug;
    }
}
