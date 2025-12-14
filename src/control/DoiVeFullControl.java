package control;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dao.VeDAO;
import entity.ChoNgoi;
import entity.ChuyenTau;
import entity.HanhKhach;
import entity.NhanVien;
import entity.Ve;
import gui.GiaoDienChonCho;
import gui.GiaoDienNhapThongTinHK; // Nếu cần, nhưng ở đây bỏ qua
import gui.GiaoDienThanhToan;

public class DoiVeFullControl extends DoiVeControl {

    private VeDAO veDAO = new VeDAO();

    public void xuLyDoiVe(List<ChoNgoi> danhSachGheDuocChon, ChuyenTau chuyenTauDuocChon, 
                          Ve veCu, HanhKhach hanhKhachCu, NhanVien nhanVienHienTai, 
                          GiaoDienChonCho chonChoScreen) throws Exception {
        
        // Bước 1: Kiểm tra điều kiện đổi vé (tái sử dụng từ DoiVeControl)
        if (!kiemTraCoTheDoiVe(veCu)) {
            throw new Exception("Vé không thể đổi (đã hủy hoặc thời gian <4h)");
        }

        // Bước 2: Tạo danh sách vé mới tạm thời từ chỗ chọn (tương tự logic trong GiaoDienChonCho)
        List<Ve> danhSachVeMoi = new ArrayList<>();
        for (ChoNgoi cho : danhSachGheDuocChon) {
            Ve veMoi = new Ve();
            veMoi.setMaChoNgoi(cho);
            veMoi.setMaChuyenTau(chuyenTauDuocChon);
            // Giả định giá vé (lấy từ chỗ ngồi hoặc chuyến tàu - bạn có thể điều chỉnh dựa trên code thực)
            BigDecimal gia = chuyenTauDuocChon.getGiaChuyen(); // Nếu null, dùng giá chuyến
            veMoi.setGiaVeGoc(gia);
            veMoi.setGiaThanhToan(gia);
            veMoi.setTrangThai("Đã đặt"); // Trạng thái ban đầu
            veMoi.setNgayDat(LocalDateTime.now());
            veMoi.setMaNhanVien(nhanVienHienTai);
            // Set maHanhKhach từ hanhKhachCu (sẽ cập nhật đầy đủ sau)
            veMoi.setMaHanhkhach(hanhKhachCu);
            danhSachVeMoi.add(veMoi);
        }

        // Bước 3: Đóng màn hình chọn chỗ
        chonChoScreen.dispose();

        // Bước 4: Mở màn hình thanh toán trực tiếp với hanhKhachCu làm nguoiThanhToan
        // Truyền previousScreen = null vì bỏ qua NhapThongTinHK
        GiaoDienThanhToan thanhToanScreen = new GiaoDienThanhToan(danhSachVeMoi, hanhKhachCu, nhanVienHienTai, null);

        // Bước 5: Inject veCu vào thanhToanScreen (vì không sửa GUI, dùng reflection hoặc thêm setter nếu cần, nhưng để đơn giản, giả định bạn thêm private field veCu trong ThanhToan và set qua control)
        // Lưu ý: Vì không sửa GUI, tôi giả định bạn có thể thêm một dòng setVeCu(veCu) nếu có method, nhưng để tuân thủ, di chuyển hủy vào đây sau khi thanh toán.
        // Thay thế: Sau khi mở screen, bạn có thể listen sự kiện thanh toán thành công (nhưng phức tạp). Để đơn giản, giả định hủy được gọi trong ThanhToan (đã có if veCu != null).

        // Nếu ThanhToan không có veCu, bạn cần thêm field private Ve veCu trong GiaoDienThanhToan và set nó ở đây (nhưng vì không sửa, hãy thêm setter public void setVeCu(Ve veCu) { this.veCu = veCu; })
        // Giả định bạn thêm setter này (chỉ 1 dòng, không ảnh hưởng UI)
        thanhToanScreen.setVeCu(veCu); // Giả định thêm setter

        thanhToanScreen.setVisible(true);

        
    }
}