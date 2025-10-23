package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import connectDB.connectDB;
import entity.HanhKhach;

public class HanhKhachDAO {
	
	private HanhKhach layHanhKhachTuResultSet(ResultSet rs) throws SQLException {
		
        String maHK = rs.getString("maHanhKhach"); 
        String hoTen = rs.getString("hoTen"); 
        String cmndCccd = rs.getString("cmndCccd"); 
        String soDT = rs.getString("soDienThoai"); 
        
        Date sqlNgaySinh = rs.getDate("ngaySinh");
        LocalDate ngaySinh = null;
        if (sqlNgaySinh != null) { 
            ngaySinh = sqlNgaySinh.toLocalDate();
        }
        String maUuDai = rs.getString("maUuDai");
        try {
        	 HanhKhach hk = new HanhKhach(maHK, hoTen, cmndCccd, soDT, ngaySinh, maUuDai);
        	 return hk;	 
		} catch (Exception e) {
			// TODO: handle exception
			throw new SQLException("Lỗi khi tạo đối tượng Ve từ ResultSet: " + e.getMessage(), e);
		}

    }
	
	//Them
    public boolean themHanhKhach(HanhKhach hk) throws SQLException {
        String sql = "INSERT INTO HanhKhach (maHanhKhach, hoTen, cmndCccd, soDienThoai, ngaySinh, maUuDai) VALUES (?, ?, ?, ?, ?, ?)";

        try(
            Connection con = connectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, hk.getMaKH()); 
            ps.setString(2, hk.getHoTen());
            ps.setString(3, hk.getCmndCccd());
            ps.setString(4, hk.getSoDT()); 
            
            ps.setDate(5, Date.valueOf(hk.getNgaySinh()));
            
            ps.setString(6, hk.getMaUuDai()); 

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
        	ex.printStackTrace();
        	return false;
        }
    }

    //DOC
    public List<HanhKhach> layDanhSachHanhKhach() throws SQLException {
        List<HanhKhach> danhSachHK = new ArrayList<>();
        String sql = "SELECT * FROM HanhKhach";

        try (
            Connection con = connectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()){
                danhSachHK.add(layHanhKhachTuResultSet(rs));
            }
        } catch (SQLException ex) {
        	ex.printStackTrace();
        }
        return danhSachHK;
    }
    public HanhKhach layHanhKhachTheoMa(String maHK) throws SQLException {
        String sql = "SELECT * FROM HanhKhach WHERE maHanhKhach = ?";
        HanhKhach hk = null;
        try (
            Connection con = connectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHK);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                   hk = layHanhKhachTuResultSet(rs);
                }
            }
        } catch (SQLException ex) {
        	ex.printStackTrace();
        }
		return hk;
    }
    public HanhKhach layHanhKhachTheoCMND(String cmndCccd) throws SQLException {
        String sql = "SELECT * FROM HanhKhach WHERE cmndCccd = ?";
        HanhKhach hk = null;
        
        try (
            Connection con = connectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, cmndCccd);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    hk = layHanhKhachTuResultSet(rs); 
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();;
        }
        return hk;
    }
    
    public HanhKhach layHanhKhachTheoSDT(String soDT) throws SQLException {
        String sql = "SELECT * FROM HanhKhach WHERE soDienThoai = ?";
        HanhKhach hk = null;
        
        try (
            Connection con = connectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, soDT);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    hk = layHanhKhachTuResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi tìm kiếm hành khách theo SĐT: " + ex.getMessage());
            throw ex;
        }
        return hk;
    }
    public List<HanhKhach> timHanhKhachTheoHoTen(String hoTen) throws SQLException {
        List<HanhKhach> danhSachHK = new ArrayList<>();
        String sql = "SELECT * FROM HanhKhach WHERE hoTen LIKE ?"; 
        try (
            Connection con = connectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, "%" + hoTen + "%"); 
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    danhSachHK.add(layHanhKhachTuResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi tìm kiếm hành khách theo Họ tên: " + ex.getMessage());
            throw ex;
        }
        return danhSachHK;
    }
//  cap nhat
    public boolean capNhatHanhKhach(HanhKhach hk) throws SQLException {
        String sql = "UPDATE HanhKhach SET hoTen = ?, cmndCccd = ?, soDienThoai = ?, ngaySinh = ?, maUuDai = ? WHERE maHanhKhach = ?";

        try(
            Connection con = connectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, hk.getHoTen());
            ps.setString(2, hk.getCmndCccd());
            ps.setString(3, hk.getSoDT());
            ps.setDate(4, Date.valueOf(hk.getNgaySinh()));
            ps.setString(5, hk.getMaUuDai());
            
            ps.setString(6, hk.getMaKH()); 

            return ps.executeUpdate() > 0;
            
        } catch (SQLException ex) {
        	ex.printStackTrace();
        	return false;
        }
    }
//  xoa
    public boolean xoaHanhKhach(String maHK) throws SQLException {
        String sql = "DELETE FROM HanhKhach WHERE maHanhKhach = ?";

        try (
            Connection con = connectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHK);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException ex) {
        	ex.printStackTrace();
        	return false;
        }
    }
    
}
