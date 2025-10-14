package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import connectDB.connectDB;
import entity.Ve;

public class VeDAO {
	private Connection con;
	
	public VeDAO() {
		con = connectDB.getConnection();
	}
	public boolean themVe(Ve ve) throws Exception {
		String sql = "INSERT INTO Ve (maVe, ngayDat, giaVeGoc, giaThanhToan, trangThai, maChoNgoi, maChuyenTau, maHanhKhach, maKhuyenMai, maNhanVien)"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement ps = con.prepareStatement(sql)){
			ps.setString(1, ve.getMaVe());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
			String ngayDatString = ve.getNgayDat().format(formatter);
			ps.setString(2, ngayDatString);
			ps.setBigDecimal(3, ve.getGiaVeGoc());
			ps.setBigDecimal(4, ve.getGiaThanhToan());
			ps.setString(5, ve.getTrangThai());
			ps.setString(6, ve.getMaChoNgoi().getMaChoNgoi());
			ps.setString(7, ve.getMaChuyenTau().getMaChuyenTau());
			ps.setString(8, ve.getMaHanhkhach().getMaKH());
			
			if(ve.getMaKhuyenMai() != null) {
				ps.setString(9, ve.getMaKhuyenMai().getMaKhuyenMai());
			}else {
				ps.setNull(9, java.sql.Types.VARCHAR);
			}
			ps.setString(10, ve.getMaNhanVien().getMaNhanVien());
			int row = ps.executeUpdate();
			return row > 0;
			
			
		}catch(SQLException e) {
			System.err.println("Lỗi khi thêm Vé vào CSDL: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("Không thể thêm thông tin Vé", e);
		}
	}
	public static void main(String[] args) throws Exception {
		VeDAO ve = new VeDAO();
		Ve testve = new Ve("MV", null, null, null, null, null, null, null, null, null);
		ve.themVe(testve);
	}
}
