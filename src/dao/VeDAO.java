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
    
    //THÊM
    public boolean themVe(Ve ve) throws SQLException {
    	String sql = "INSERT INTO Ve (maVe, ngayDat, giaVeGoc, giaThanhToan, maChoNgoi, maChuyenTau, maHanhKhach"
    			+ "maKhuyenMai, maNhanVien)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    	try(Connection con = connectDB.getConnection();
    		PreparedStatement ps = con.prepareStatement(sql);){	
			ps.setString(1, ve.getMaVe());
			ps.setTimestamp(2, Timestamp.valueOf(ve.getNgayDat()));
			ps.setBigDecimal(3, ve.getGiaVeGoc());
			ps.setBigDecimal(4, ve.getGiaThanhToan());
			ps.setString(5, ve.getMaChoNgoi().getMaChoNgoi());
			ps.setString(6, ve.getMaChuyenTau().getMaChuyenTau());
			ps.setString(7, ve.getMaHanhkhach().getMaKH());
			
			if(ve.getMaKhuyenMai() != null) {
				ps.setString(8, ve.getMaKhuyenMai().getMaKhuyenMai());
			}else {
				ps.setNull(8, Types.VARCHAR);
			}
			ps.setString(9, ve.getMaNhanVien().getMaNhanVien());
			
			return ps.executeUpdate() > 0;
			
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
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
}
