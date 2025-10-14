package dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

import connectDB.connectDB; // Import lớp kết nối
// Import tất cả các Entity
import entity.ChoNgoi;
import entity.ChuyenTau;
import entity.HanhKhach;
import entity.NhanVien;
import entity.Ve;

public class VeDAOTester {

    public static void main(String[] args) {
        VeDAO veDAO = null;
        String maVeTest = "V" + new Random().nextInt(99999); // Tạo mã vé ngẫu nhiên

        try {
            // Bước 1: Thiết lập kết nối và khởi tạo DAO
            connectDB.getConnection(); // Đảm bảo kết nối được thiết lập trước
            veDAO = new VeDAO();

            // Bước 2: Chuẩn bị dữ liệu đầu vào (Mock Data)
            
            // LƯU Ý QUAN TRỌNG: Các mã khóa ngoại (FK) này phải TỒN TẠI 
            // trong các bảng tương ứng (HanhKhach, ChuyenTau, ChoNgoi, NhanVien) 
            // trong CSDL của bạn, nếu không sẽ xảy ra lỗi vi phạm ràng buộc khóa ngoại [55.1, 2387].
            
            // Tạo các đối tượng phụ thuộc (chỉ cần các mã ID tối thiểu)
            HanhKhach hk = new HanhKhach("HK-00001"); // Giả định HK-00001 đã có trong DB
            ChuyenTau ct = new ChuyenTau("C-001", null, null, null, null, null); // Giả định C-001 đã có
            ChoNgoi cn = new ChoNgoi("A-01", "Ghế mềm", ChoNgoi.TrangThai.TRONG); // Giả định A-01 đã có
            NhanVien nv = new NhanVien("NV-001", null, null, null, null, null); // Giả định NV-001 đã có
            
            // Giá tiền sử dụng BigDecimal như bạn yêu cầu
            BigDecimal giaGoc = new BigDecimal("150000.50");
            BigDecimal giaTT = new BigDecimal("140000.50");

            // Tạo đối tượng Ve hợp lệ
            Ve veMoi = new Ve(
                maVeTest, 
                LocalDateTime.now(), 
                "Khả Dụng", 
                giaGoc,         // Chú ý: constructor mẫu dùng double
                giaTT,         // Nếu constructor dùng BigDecimal, bạn phải thay đổi
                cn, ct, hk, null, nv
            );

            // Bước 3: Thực thi phương thức cần kiểm thử
            System.out.println("Đang thực hiện thêm vé: " + maVeTest);
            boolean themThanhCong = veDAO.themVe(veMoi);

            // Bước 4: Xác nhận (Verification)
            if (themThanhCong) {
                System.out.println("\n[THÀNH CÔNG] Thêm vé " + maVeTest + " vào CSDL.");
                // Để xác minh đầy đủ, ta cần gọi phương thức tìm kiếm (Read)
                // Ví dụ: Ve veCheck = veDAO.timVeTheoMa(maVeTest);
                // System.out.println("Tìm thấy bản ghi? " + (veCheck != null));
            } else {
                System.out.println("\n[THẤT BẠI] Không thể thêm vé " + maVeTest + ".");
            }

        } catch (Exception e) {
            // Xử lý các lỗi bất ngờ (như SQLException, ClassCastException, hoặc IllegalArgumentException)
            System.err.println("\n[LỖI CHẶN] Lỗi trong quá trình kiểm thử:");
            e.printStackTrace();
        } finally {
            // Bước 5: Dọn dẹp và đóng kết nối (Quan trọng!) [1]
            if (veDAO != null) {
                try {
                    // Cần xóa bản ghi thử nghiệm để tránh lỗi trùng lặp nếu chạy lại
                    // veDAO.xoaVe(maVeTest); 
                    System.out.println("\nKết thúc kiểm thử.");
                } catch (Exception cleanupException) {
                    System.err.println("Lỗi dọn dẹp: " + cleanupException.getMessage());
                }
            }
            connectDB.closeConnection(); 
        }
    }
    
    // Lưu ý: Nếu lớp VeDAO của bạn yêu cầu BigDecimal trong constructor, 
    // hãy đảm bảo các hàm setter/getter trong lớp Entity hỗ trợ điều đó.
    
    /** 
     * Hướng dẫn về cách cập nhật Entity Ve (nếu cần thiết):
     * Trong Entity Ve, nếu bạn đã chuyển sang BigDecimal, bạn cần thay đổi 
     * kiểu dữ liệu của giaVeGoc và giaThanhToan thành BigDecimal thay vì double.
     */
}