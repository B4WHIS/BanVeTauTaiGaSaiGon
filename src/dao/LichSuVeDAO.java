package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import connectDB.connectDB;
import entity.LichSuVe;

public class LichSuVeDAO {
	
	private LichSuVe layLichSuVeTuRS(ResultSet rs) throws SQLException {
		try {
			String lichSuVe = rs.getString("maLichSuVe");
			int idLoaiGiaoDich = rs.getInt("IDloaiGiaoDich");
			LocalDateTime ngayGiaoDich = rs.getTimestamp("ngayGiaoDich").toLocalDateTime();
			
			String maVe = rs.getString("maVe");
			String maNhanVien = rs.getString("maNhanVien");
			
			LichSuVe LSV = new LichSuVe(lichSuVe, idLoaiGiaoDich, ngayGiaoDich, maVe, maNhanVien);
			
	        String lyDo = rs.getString("lyDo");
	        if (lyDo != null) {
	            LSV.setLyDo(lyDo);
	        }
	
	        BigDecimal phiXuLy = rs.getBigDecimal("phiXuLy");
	        LSV.setPhiXuLy(phiXuLy); 
	
	        String maHoaDon = rs.getString("maHoaDon");
	        if (maHoaDon != null) {
	            LSV.setMaHoaDon(maHoaDon);
	        }
	
	        String maHanhKhach = rs.getString("maHanhKhach");
	        if (maHanhKhach != null) {
	            LSV.setMaHanhKhach(maHanhKhach);
	        }
	
	        return LSV;
		} catch (Exception e) {
			// TODO: handle exception
			throw new SQLException("Lỗi khi tạo đối tượng Ve từ ResultSet: " + e.getMessage(), e);
		}	
	}
//	THEM
    public boolean themLichSuVe(LichSuVe lichSu) {
        String sql = "INSERT INTO LichSuVe (IDloaiGiaoDich, ngayGiaoDich, lyDo, phiXuLy, maVe, maNhanVien, maHoaDon, maHanhKhach) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = connectDB.getConnection();
        	 PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, lichSu.getIDloaiGiaoDich()); 
            ps.setTimestamp(2, Timestamp.valueOf(lichSu.getNgayGiaoDich())); 
            ps.setString(3, lichSu.getLyDo()); 
            ps.setBigDecimal(4, lichSu.getPhiXuLy()); 
            ps.setString(5, lichSu.getMaVe()); 
            ps.setString(6, lichSu.getMaNhanVien()); 
            ps.setString(7, lichSu.getMaHoaDon()); 
            ps.setString(8, lichSu.getMaHanhKhach()); 

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
//	DOC
    public List<LichSuVe> timLichSuTheoMaVe(String maVe) {
        List<LichSuVe> danhSachLS = new ArrayList<>();
        
        String sql = "SELECT * FROM LichSuVe WHERE maVe = ?";
        try (Connection con = connectDB.getConnection();
        	 PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maVe);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    danhSachLS.add(layLichSuVeTuRS(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachLS;
    }
    
    public List<LichSuVe> timLichSuTheoMaNhanVien(String maNhanVien) {
        List<LichSuVe> danhSachLS = new ArrayList<>();
        
        String sql = "SELECT * FROM LichSuVe WHERE maNhanVien = ?";
        try (Connection con = connectDB.getConnection();
        	 PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maNhanVien);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    danhSachLS.add(layLichSuVeTuRS(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachLS;
    }
    public List<LichSuVe> layTatCaLichSu() {
        List<LichSuVe> danhSachLS = new ArrayList<>();
        String sql = "SELECT * FROM LichSuVe";
        try (Connection con = connectDB.getConnection();
        	 PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                danhSachLS.add(layLichSuVeTuRS(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachLS;
    }
}
