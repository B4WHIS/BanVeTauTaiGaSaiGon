package control;

import dao.HanhKhachDAO;
import dao.LichSuVeDAO;
import dao.UuDaiDAO;
import dao.VeDAO;

public class QuanLyVeControl {
	private VeDAO veDao;
	private HanhKhachDAO hanhKhachDao;
	private UuDaiDAO uuDaiDao;
	private LichSuVeDAO lichSuVeDao;
	
	public QuanLyVeControl() {
		veDao = new VeDAO();
		hanhKhachDao = new HanhKhachDAO();
		uuDaiDao = new UuDaiDAO();
		lichSuVeDao = new LichSuVeDAO();
		
		
	}
	
	
}
