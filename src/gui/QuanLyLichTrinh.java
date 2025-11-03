package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import control.QuanLyLichTrinhController;
import dao.GaDAO;
import dao.LichTrinhDAO;
import entity.Ga;
import entity.LichTrinh;

public class QuanLyLichTrinh extends GiaoDienChinh {

    private JTable tblLichTrinh;
    private DefaultTableModel modelLT;
    private JTextField txtMaLichTrinh, txtTenLichTrinh, txtKhoangCach;
    private JComboBox<String> cbGaDi, cbGaDen;
    private JButton btnThem, btnSua, btnXoa, btnReset, btnExport, btnTroVe, btnTim;
    private QuanLyLichTrinhController controller;
    private LichTrinhDAO lichTrinhDAO = new LichTrinhDAO();
    private GaDAO gaDAO = new GaDAO();

    // Trạng thái
    private boolean dangNhap = false;
    private boolean dangSua = false;
	private QuanLyLichTrinh view;

    public QuanLyLichTrinh() throws Exception {
        super();
        controller = new QuanLyLichTrinhController(this);
        setTitle("Quản lý lịch trình");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBackground(new Color(245, 247, 250));

        // ===== TITLE =====
        JLabel lblTitle = new JLabel("QUẢN LÝ LỊCH TRÌNH", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 44));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(103, 192, 144));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        pnlMain.add(lblTitle, BorderLayout.NORTH);

        // ===== LEFT PANEL =====
        JPanel pnlLeft = new JPanel(new BorderLayout(10, 10));
        pnlLeft.setPreferredSize(new Dimension(540, 0));
        pnlLeft.setBackground(new Color(245, 247, 250));

        TitledBorder titleBorder = BorderFactory.createTitledBorder("Nhập Thông Tin Tra Cứu");
        titleBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 24));
        pnlLeft.setBorder(titleBorder);

        // ===== FORM =====
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        pnlForm.setBackground(Color.WHITE);

        Font lblFont = new Font("Segoe UI", Font.BOLD, 19);
        Font txtFont = new Font("Segoe UI", Font.PLAIN, 20);

        JLabel lblMaLT = new JLabel("Mã LT:");
        JLabel lblTenLT = new JLabel("Tên lịch trình:");
        JLabel lblGaDi = new JLabel("Ga đi:");
        JLabel lblGaDen = new JLabel("Ga đến:");
        JLabel lblKhoangCach = new JLabel("Khoảng cách (km):");

        JLabel[] labels = {lblMaLT, lblTenLT, lblGaDi, lblGaDen, lblKhoangCach};
        for (JLabel lbl : labels) {
            lbl.setFont(lblFont);
            lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        txtMaLichTrinh = new JTextField(); txtMaLichTrinh.setEnabled(false);
        txtTenLichTrinh = new JTextField();
        txtKhoangCach = new JTextField();
        cbGaDi = new JComboBox<>();
        cbGaDen = new JComboBox<>();

        JTextField[] textFields = {txtMaLichTrinh, txtTenLichTrinh, txtKhoangCach};
        for (JTextField txt : textFields) {
            txt.setFont(txtFont);
            txt.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(150, 150, 150)),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
        }
        cbGaDi.setFont(txtFont); cbGaDen.setFont(txtFont);
        cbGaDi.setPreferredSize(new Dimension(200, 40)); cbGaDen.setPreferredSize(new Dimension(200, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        for (JLabel lbl : labels) {
            gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3; gbc.anchor = GridBagConstraints.EAST;
            pnlForm.add(lbl, gbc);
            gbc.gridx = 1; gbc.weightx = 0.7; gbc.anchor = GridBagConstraints.WEST;
            if (row == 0) pnlForm.add(txtMaLichTrinh, gbc);
            else if (row == 1) pnlForm.add(txtTenLichTrinh, gbc);
            else if (row == 2) pnlForm.add(cbGaDi, gbc);
            else if (row == 3) pnlForm.add(cbGaDen, gbc);
            else if (row == 4) pnlForm.add(txtKhoangCach, gbc);
            row++;
        }

        // ===== BUTTONS =====
        btnThem = taoButton("Thêm", new Color(46, 204, 113), "/img/plus2.png");
        btnSua = taoButton("Sửa", new Color(187, 102, 83), "/img/repair.png");
        btnXoa = taoButton("Xóa", new Color(231, 76, 60), "/img/trash-bin.png");
        btnReset = taoButton("Làm mới", new Color(52, 152, 219), "/img/undo2.png");
        btnExport = taoButton("Xuất Excel", new Color(241, 196, 15), "/img/export2.png");
        btnTim = taoButton("Tìm", new Color(155, 89, 182), "/img/magnifying-glass.png");

        JPanel pnlButtons = new JPanel(new GridLayout(3, 2, 10, 10));
        pnlButtons.setBackground(new Color(245, 247, 250));
        pnlButtons.add(btnThem); pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa); pnlButtons.add(btnReset);
        pnlButtons.add(btnExport); pnlButtons.add(btnTim);

        pnlLeft.add(pnlForm, BorderLayout.CENTER);
        pnlLeft.add(pnlButtons, BorderLayout.SOUTH);

        // ===== RIGHT PANEL =====
        JPanel pnlRight = new JPanel(new BorderLayout(10, 10));
        pnlRight.setBackground(Color.WHITE);

        String[] colHeader = {"Mã LT", "Tên lịch trình", "Ga đi", "Ga đến", "Khoảng cách (km)"};
        modelLT = new DefaultTableModel(colHeader, 0);
        tblLichTrinh = new JTable(modelLT);
        tblLichTrinh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblLichTrinh.setRowHeight(28);
        tblLichTrinh.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tblLichTrinh.setSelectionBackground(new Color(58, 111, 67));

        JScrollPane scroll = new JScrollPane(tblLichTrinh);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        TitledBorder tblBorder = BorderFactory.createTitledBorder("Danh Sách Lịch Trình");
        tblBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 24));
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBorder(tblBorder);
        pnlTable.add(scroll, BorderLayout.CENTER);
        pnlRight.add(pnlTable, BorderLayout.CENTER);

        // ===== FOOTER =====
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        btnTroVe = taoButton("Trở về", new Color(41, 128, 185), "");
        btnTroVe.setForeground(Color.WHITE);
        btnTroVe.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btnTroVe.setPreferredSize(new Dimension(160, 60));
        pnlFooter.add(btnTroVe, BorderLayout.WEST);

        // ===== ADD ALL =====
        pnlMain.add(pnlLeft, BorderLayout.WEST);
        pnlMain.add(pnlRight, BorderLayout.CENTER);
        pnlMain.add(pnlFooter, BorderLayout.SOUTH);
        add(pnlMain);

        // Load data
        loadCombosFromDB();
        loadDataFromDB();

        // Attach listeners
        attachListeners();

        enableFormFields(false);
    }

    private void attachListeners() {
        // === THÊM: NHẤN 2 LẦN ===
        btnThem.addActionListener(e -> {
            if (!dangNhap && !dangSua) {
                controller.them();
                dangNhap = true;
                btnThem.setText("Xác nhận thêm");
                btnThem.setIcon(chinhKichThuoc("/img/check.png", 24, 24));
            } else if (dangNhap) {
                try {
                    controller.xuLyThem();
                    dangNhap = false;
                    btnThem.setText("Thêm");
                    btnThem.setIcon(chinhKichThuoc("/img/plus2.png", 24, 24));
                } catch (Exception ex) {
                    showMessage("Lỗi thêm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // === SỬA: NHẤN 2 LẦN ===
        btnSua.addActionListener(e -> {
            if (!dangSua && !dangNhap) {
                controller.sua();
                dangSua = true;
                btnSua.setText("Xác nhận sửa");
                btnSua.setIcon(chinhKichThuoc("/img/check.png", 24, 24));
            } else if (dangSua) {
                try {
                    controller.xuLySua();
                    dangSua = false;
                    btnSua.setText("Sửa");
                    btnSua.setIcon(chinhKichThuoc("/img/repair.png", 24, 24));
                } catch (Exception ex) {
                    showMessage("Lỗi sửa: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // === HỦY KHI LÀM MỚI ===
        btnReset.addActionListener(e -> {
            dangNhap = false;
            dangSua = false;
            btnThem.setText("Thêm");
            btnThem.setIcon(chinhKichThuoc("/img/plus2.png", 24, 24));
            btnSua.setText("Sửa");
            btnSua.setIcon(chinhKichThuoc("/img/repair.png", 24, 24));
            try {
				view.refreshData();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        });

        btnXoa.addActionListener(controller);
        btnExport.addActionListener(controller);
        btnTim.addActionListener(controller);
        btnTroVe.addActionListener(controller);

        tblLichTrinh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
					controller.handleTableSelection();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
    }

    public JButton taoButton(String text, Color bg, String iconPath) {
        JButton btn = new JButton(text, chinhKichThuoc(iconPath, 24, 24));
        btn.setBackground(bg);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    public static ImageIcon chinhKichThuoc(String duongDan, int rong, int cao) {
        URL iconUrl = QuanLyLichTrinh.class.getResource(duongDan);
        if (iconUrl == null) return null;
        ImageIcon icon = new ImageIcon(iconUrl);
        Image img = icon.getImage().getScaledInstance(rong, cao, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private void loadCombosFromDB() {
        cbGaDi.removeAllItems(); cbGaDen.removeAllItems();
        List<Ga> listGa = gaDAO.getAllGa();
        for (Ga ga : listGa) {
            String item = ga.getTenGa() + " - " + ga.getDiaChi() + " (" + ga.getMaGa() + ")";
            cbGaDi.addItem(item); cbGaDen.addItem(item);
        }
    }

    public void loadDataFromDB() throws Exception {
        modelLT.setRowCount(0);
        List<LichTrinh> list = lichTrinhDAO.getAllLichTrinh();
        for (LichTrinh lt : list) {
            modelLT.addRow(new Object[]{
                lt.getMaLichTrinh(),
                lt.getTenLichTrinh(),
                lt.getMaGaDi().getTenGa() + " (" + lt.getMaGaDi().getMaGa() + ")",
                lt.getMaGaDen().getTenGa() + " (" + lt.getMaGaDen().getMaGa() + ")",
                String.format("%,.0f km", lt.getKhoangCach())
            });
        }
    }

    public void loadFormData(LichTrinh lt) {
        txtMaLichTrinh.setText(lt.getMaLichTrinh());
        txtTenLichTrinh.setText(lt.getTenLichTrinh());
        txtKhoangCach.setText(String.valueOf(lt.getKhoangCach()));
        setComboSelection(cbGaDi, lt.getMaGaDi().getMaGa());
        setComboSelection(cbGaDen, lt.getMaGaDen().getMaGa());
    }

    private void setComboSelection(JComboBox<String> cb, String maGa) {
        for (int i = 0; i < cb.getItemCount(); i++) {
            String item = cb.getItemAt(i);
            if (item.contains("(" + maGa + ")")) {
                cb.setSelectedIndex(i);
                break;
            }
        }
    }

    public void resetForm() {
        txtMaLichTrinh.setText("");
        txtTenLichTrinh.setText("");
        txtKhoangCach.setText("");
        cbGaDi.setSelectedIndex(0);
        cbGaDen.setSelectedIndex(0);
        tblLichTrinh.clearSelection();
    }

    public void enableFormFields(boolean b) {
        txtTenLichTrinh.setEnabled(b);
        txtKhoangCach.setEnabled(b);
        cbGaDi.setEnabled(b);
        cbGaDen.setEnabled(b);
    }

    public String getSelectedMaLichTrinh() {
        int row = tblLichTrinh.getSelectedRow();
        return row >= 0 ? (String) modelLT.getValueAt(row, 0) : null;
    }

    public void refreshData() throws Exception {
        loadDataFromDB();
        resetForm();
    }

    public void showMessage(String msg, String title, int type) {
        JOptionPane.showMessageDialog(this, msg, title, type);
    }

    // ===== GETTERS =====
    public JTextField getTxtTenLichTrinh() { return txtTenLichTrinh; }
    public JTextField getTxtKhoangCach() { return txtKhoangCach; }
    public JComboBox<String> getCbGaDi() { return cbGaDi; }
    public JComboBox<String> getCbGaDen() { return cbGaDen; }
    public DefaultTableModel getModelLT() { return modelLT; }

    public static void main(String[] args) throws Exception {
        LookAndFeelManager.setNimbusLookAndFeel();
        new QuanLyLichTrinh().setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {}
}