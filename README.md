# TheGioiCongNghe - Website Bán Hàng Công Nghệ

## Giới thiệu

TheGioiCongNghe là một ứng dụng web thương mại điện tử được phát triển bằng Spring Boot, cho phép người dùng mua sắm các sản phẩm công nghệ như laptop, PC, màn hình, điện thoại và linh kiện chính hãng. Dự án này bao gồm cả phần quản trị (admin) và phần người dùng (user).

## Công nghệ sử dụng

- **Java 17**
- **Spring Boot 3.3.4**
- **Spring Security** - Bảo mật và phân quyền người dùng
- **Spring Data JPA** - Tương tác với cơ sở dữ liệu
- **Thymeleaf** - Template engine cho giao diện người dùng
- **MySQL** - Cơ sở dữ liệu
- **Maven** - Quản lý dependency và build project
- **Bootstrap** - Framework CSS cho giao diện người dùng
- **jQuery** - Thư viện JavaScript

## Cấu trúc dự án

### Mô hình MVC

Dự án được tổ chức theo mô hình MVC (Model-View-Controller):

- **Model**: Các entity như Product, ProductCategory, BlogPost, Order, v.v.
- **View**: Các template Thymeleaf trong thư mục templates
- **Controller**: Các controller xử lý request từ người dùng

### Cấu trúc thư mục chính

```
src/
├── main/
│   ├── java/
│   │   └── com/example/thegioicongnghe/
│   │       ├── Admin/
│   │       │   ├── Controller/
│   │       │   ├── Model/
│   │       │   ├── Repository/
│   │       │   └── Service/
│   │       ├── User/
│   │       │   └── Controller/
│   │       ├── config/
│   │       └── util/
│   └── resources/
│       ├── static/
│       └── templates/
│           ├── admin/
│           ├── layouts/
│           └── user/
└── test/
```

## Tính năng chính

### Phần người dùng (User)

- Đăng ký và đăng nhập tài khoản
- Xem danh sách sản phẩm theo danh mục
- Tìm kiếm sản phẩm
- Xem chi tiết sản phẩm
- Thêm sản phẩm vào giỏ hàng
- Thanh toán đơn hàng
- Xem bài viết (blog)
- Quản lý thông tin cá nhân

### Phần quản trị (Admin)

- Quản lý danh mục sản phẩm
- Quản lý sản phẩm (thêm, sửa, xóa)
- Quản lý đơn hàng
- Quản lý bài viết (blog)
- Quản lý người dùng

## Cài đặt và chạy dự án

### Yêu cầu hệ thống

- Java Development Kit (JDK) 17 trở lên
- MySQL 8.0 trở lên
- Maven 3.6 trở lên

### Các bước cài đặt

1. Clone dự án từ repository:
   ```bash
   git clone <repository-url>
   cd TheGioiCongNghe
   ```

2. Cấu hình cơ sở dữ liệu trong file `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/thegioicongnghe
   spring.datasource.username=root
   spring.datasource.password=123456
   ```

3. Build dự án bằng Maven:
   ```bash
   mvn clean install
   ```

4. Chạy ứng dụng:
   ```bash
   mvn spring-boot:run
   ```

5. Truy cập ứng dụng tại địa chỉ: http://localhost:2003

## Cấu trúc cơ sở dữ liệu

Dự án sử dụng các entity chính sau:

- **Product**: Sản phẩm
- **ProductCategory**: Danh mục sản phẩm
- **ProductImage**: Hình ảnh sản phẩm
- **ProductReview**: Đánh giá sản phẩm
- **BlogPost**: Bài viết blog
- **Order**: Đơn hàng
- **OrderItem**: Chi tiết đơn hàng
- **UserDtls**: Thông tin người dùng
