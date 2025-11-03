package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import connectDB.connectDB;
import entity.ChoNgoi;
import entity.ChuyenTau;
import entity.HanhKhach;
import entity.KhuyenMai;
import entity.NhanVien;
import entity.Ve;

public class VeDAO {

	private Ve getVeTuResultSet(ResultSet rs) throws SQLException {

    	String maVe = rs.getString("maVe");
        String trangThai = rs.getString("trangThai");

        LocalDateTime ngayDat = rs.getTimestamp("ngayDat").toLocalDateTime();

        BigDecimal giaVeGoc = rs.getBigDecimal("giaVeGoc");
        BigDecimal giaThanhToan = rs.getBigDecimal("giaThanhToan");

        String maChoNgoiFK = rs.getString("maChoNgoi");
        String maChuyenTauFK = rs.getString("maChuyenTau");
        String maHanhKhachFK = rs.getString("maHanhKhach");
        String maNhanVienFK = rs.getString("maNhanVien"); 
        String maKhuyenMaiFK = rs.getString("maKhuyenMai");

        
        ChoNgoi choNgoi = new ChoNgoi();
        choNgoi.setMaChoNgoi(maChoNgoiFK);
        
        ChuyenTau chuyenTau = new ChuyenTau();
        chuyenTau.setMaChuyenTau(maChuyenTauFK);

        HanhKhach hanhKhach = new HanhKhach();
        hanhKhach.setMaKH(maHanhKhachFK);
        
        NhanVien nhanVien = new NhanVien();
        nhanVien.setMaNhanVien(maNhanVienFK);
        
        KhuyenMai khuyenMai = null;
        if (maKhuyenMaiFK != null) {
            khuyenMai = new KhuyenMai();
            khuyenMai.setMaKhuyenMai(maKhuyenMaiFK);
        }

        try {
            Ve ve = new Ve(maVe, ngayDat, trangThai, giaVeGoc, giaThanhToan,
            		choNgoi, chuyenTau, hanhKhach, khuyenMai, nhanVien);
            return ve;
        } catch (Exception e) {
            throw new SQLException("Lỗi khi tạo đối tượng Ve từ ResultSet: " + e.getMessage(), e);
        }
    }
    
    public String themVe(Ve ve, Connection conn) throws SQLException {
        String sql = """
        INSERT INTO Ve (ngayDat, giaVeGoc, giaThanhToan, trangThai,
        maChoNgoi, maChuyenTau, maHanhKhach, maKhuyenMai, maNhanVien)
        OUTPUT INSERTED.maVe
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        // Dùng conn được truyền vào
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        	stmt.setTimestamp(1, Timestamp.valueOf(ve.getNgayDat())); // ngayDat
            stmt.setBigDecimal(2, ve.getGiaVeGoc()); // giaVeGoc (giaVeGoc > 0) [3, 4]
            stmt.setBigDecimal(3, ve.getGiaThanhToan()); // giaThanhToan (giaThanhToan > 0) [3, 4]
            stmt.setString(4, ve.getTrangThai()); // Trang thái ("Khả dụng")

            // Tham số 5 đến 9: Khóa ngoại
            stmt.setString(5, ve.getMaChoNgoi().getMaChoNgoi());
            stmt.setString(6, ve.getMaChuyenTau().getMaChuyenTau());
            stmt.setString(7, ve.getMaHanhkhach().getMaKH());
            
            // Xử lý MaKhuyenMai có thể NULL
            if (ve.getMaKhuyenMai() != null) {
                stmt.setString(8, ve.getMaKhuyenMai().getMaKhuyenMai());
            } else {
                stmt.setNull(8, Types.VARCHAR);
            }
            stmt.setString(9, ve.getMaNhanVien().getMaNhanVien());
            
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String maVe = rs.getString(1); 
                    ve.setMaVe(maVe); // Cập nhật lại mã vé cho đối tượng
                    return maVe;
                }
            }
            throw new SQLException("Thêm vé thất bại, không nhận được mã vé trả về.");
        }
    }


    //ĐỌC
    public Ve layVeTheoMa(String maVe) throws SQLException {
    	String sql = "SELECT * FROM Ve WHERE maVe = ?";
    	Ve ve = null;
    	try (Connection con = connectDB.getConnection();
    		 PreparedStatement ps = con.prepareStatement(sql);) {
			
			ps.setString(1, maVe);
			try (ResultSet rs = ps.executeQuery()){
				if(rs.next()) {
					ve = getVeTuResultSet(rs);
				}
				
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
    	return ve;
    }
    
    public Ve layVeTheoMaHK(String maHanhKhach) throws SQLException {
    	String sql = "SELECT * FROM Ve Where maHanhKhach = ?";
    	Ve ve = null;
    	try (Connection con = connectDB.getConnection();
    		 PreparedStatement ps = con.prepareStatement(sql);){
			ps.setString(1, maHanhKhach);
			try(ResultSet rs = ps.executeQuery()){
				if(rs.next()) {
					ve = getVeTuResultSet(rs);
				}
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
    	return ve;
    }
    public List<Ve> layDanhSachVeTheoMaHanhKhach(String maHanhKhach) throws SQLException {
        List<Ve> danhSachVe = new ArrayList<>();
        String sql = "SELECT * FROM Ve WHERE maHanhKhach = ?"; 
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHanhKhach);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    danhSachVe.add(getVeTuResultSet(rs)); 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachVe;
    }
    public List<Ve> layDanhSachVe() throws SQLException {

    	List<Ve> danhSachVe = new ArrayList<>();
    	String sql = "SELECT * FROM Ve";
    	
    	try(Connection con = connectDB.getConnection();
    		PreparedStatement ps = con.prepareStatement(sql);
    		ResultSet rs = ps.executeQuery()){
    		while(rs.next()) {
    			Ve ve = getVeTuResultSet(rs);
    			danhSachVe.add(ve);
    		}
    	}catch (SQLException e) {
			// TODO: handle exception
    		e.printStackTrace();
		}
    	return danhSachVe;
    }
    
    //CapNhat
    public boolean capNhatTrangThaiVe(String maVe, String TrangThaiMoi) throws SQLException {
    	String sql = "UPDATE Ve SET trangThai = ? WHERE maVe = ?";
    	try(Connection con = connectDB.getConnection();
    		PreparedStatement ps = con.prepareStatement(sql);){
    		ps.setString(1, TrangThaiMoi);
    		ps.setString(2, maVe);
    		
    		return ps.executeUpdate() > 0;
    	}catch (SQLException e) {
			// TODO: handle exception
    		e.printStackTrace();
    		return false;
    	}
    }
    //Xoa
    public boolean xoaVe(String maVe) throws SQLException{
    	String sql = "DELETE FROM Ve WHERE maVe = ?";
    	try(Connection con = connectDB.getConnection();
    		PreparedStatement ps = con.prepareStatement(sql);){
    		ps.setString(1, maVe);
    		return ps.executeUpdate() > 0;
    	}catch (SQLException e) {
			// TODO: handle exception
    		e.printStackTrace();
    		return false;
    	}
    }
    
    public List<Ve> timVeTongHop(String maVe, String hoTen, String cmnd, String sdt, String loaiVe) 
            throws SQLException {
        List<Ve> danhSachVe = new ArrayList<>();
        String sql = """
            SELECT V.* 
            FROM Ve V
            JOIN HanhKhach HK ON V.maHanhKhach = HK.maHanhKhach
            JOIN ChoNgoi CN ON V.maChoNgoi = CN.maChoNgoi 
            JOIN LoaiGhe LG ON CN.IDloaiGhe = LG.IDloaiGhe
            WHERE 1=1
        """; 
        
        ArrayList<Object> parameters = new ArrayList<>();

        // 1. Lọc theo Mã vé
        if (maVe != null && !maVe.trim().isEmpty()) {
            sql += " AND V.maVe LIKE ? ";
            parameters.add("%" + maVe + "%");
        }

        // 2. Lọc theo Họ tên
        if (hoTen != null && !hoTen.trim().isEmpty()) {
            sql += " AND HK.hoTen LIKE ? "; 
            parameters.add("%" + hoTen + "%");
        }

        // 3. Lọc theo CMND/CCCD
        if (cmnd != null && !cmnd.trim().isEmpty()) {
            sql += " AND HK.cmndCccd = ? ";
            parameters.add(cmnd);
        }
        
        // 4. Lọc theo Số điện thoại
        if (sdt != null && !sdt.trim().isEmpty()) {
            sql += " AND HK.soDienThoai = ? ";
            parameters.add(sdt);
        }
        
        // 5. Lọc theo Loại vé (Loại ghế)
        if (loaiVe != null && !loaiVe.trim().isEmpty() && !loaiVe.equalsIgnoreCase("Tất cả")) {
            sql += " AND LG.tenLoai = ? ";
            parameters.add(loaiVe);
        }
        
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (int i = 0; i < parameters.size(); i++) {
                ps.setObject(i + 1, parameters.get(i)); 
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    danhSachVe.add(getVeTuResultSet(rs)); 
                }
            }
        } catch (SQLException e) {
            throw e;
        }
        return danhSachVe;
    }
    
    public List<String> getDanhSachChoDaDat(String maChuyenTau) throws SQLException {
        List<String> dsCho = new ArrayList<>();
        Connection con = connectDB.getConnection();
        String sql = "SELECT maChoNgoi FROM Ve WHERE maChuyenTau = ? AND trangThai = N'Đã đặt'";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, maChuyenTau);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            dsCho.add(rs.getString("maChoNgoi"));
        }
        return dsCho;
    }
    
    public boolean isChoNgoiBookedForChuyenTau(String maChoNgoi, String maChuyenTau) throws SQLException {
        // Truy vấn CSDL để kiểm tra sự tồn tại của vé ĐÃ ĐẶT (N'Đã đặt')
        String sql = "SELECT COUNT(*) FROM Ve WHERE maChoNgoi = ? AND maChuyenTau = ? AND trangThai = N'Đã đặt'";

        try (Connection conn = connectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // Kết nối và làm việc với dữ liệu thật
            ps.setString(1, maChoNgoi);
            ps.setString(2, maChuyenTau);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Trả về true nếu tìm thấy ít nhất 1 vé hợp lệ
                }
            }
        }
        return false; 
    }

	public boolean kiemTraChoNgoiDaDuocDat(String maChoNgoi, String maChuyenTau) throws SQLException {
    String sql = "SELECT COUNT(*) FROM Ve WHERE maChoNgoi = ? AND maChuyenTau = ? AND trangThai = 'Đã đặt'";
    try (Connection conn = connectDB.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, maChoNgoi);
        ps.setString(2, maChuyenTau);
        try (ResultSet rs = ps.executeQuery()) {
            return rs.next() && rs.getInt(1) > 0;
        }
    }
}
    
}
