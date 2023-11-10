# -Human-Resources JAVA SPRING BOOT 
.Công ty ABC muốn phát triển hệ thống support :
Trong table NhanVien thêm 1 cột mới có tên là quyen. Cột này kiểu số nguyên có 1 trong 3 giá trị:
    1.	Nhân Viên
    2.	Nhân Viên Support
    3.	Admin
1.	Admin:
    a.	Đăng nhập vào hệ thống với quyền admin. Tài khoản admin là tài khoản tạo mặc định ban đầu.
    b.	Xem danh sách nhân viên. Khi chọn 1 người nhân viên sẽ xem các yêu cầu của người nhân viên được chọn.
    c.	Tạo tài khoản cho nhân viên. Lưu ý khi tạo tài khoản cho phép chọn loại nhân viên cần tạo là nhân viên hay nhân viên support.
    d.	Xem các yêu cầu được nhân viên gửi lên và gán yêu cầu đó cho nhân viên support
    e.	Tìm các yêu cầu theo khoảng thời gian, theo độ ưu tiên.
2.	Nhân Viên Support:
    a.	Đăng nhập vào hệ thống với quyền nhân viên support. Tài khoản này do admin tạo.
    b.	Xem các yêu cầu được gán cho mình xử lý. Tìm các yêu cầu theo khoảng thời gian, theo độ ưu tiên.
    c.	Cập nhật thông tin tài khoản đang đăng nhập.
3.	Nhân Viên:¬¬
    a.	Đăng nhập vào hệ thống với quyền nhân viên. Tài khoản này do admin tạo.
    b.	Gửi yêu cầu với độ ưu tiên được chọn từ table DoUuTien. 
    Lưu ý: Khi yêu cầu vừa được tạo thì mã nhân viên xử lý yêu cầu đó là NULL.
    c.	Xem các yêu cầu mình đã tạo. Tìm các yêu cầu đã tạo theo khoảng thời gian, theo độ ưu tiên.
    d.	Cập nhật thông tin tài khoản đang đăng nhập.
