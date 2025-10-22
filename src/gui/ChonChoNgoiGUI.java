package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import entity.ChoNgoi;
import entity.ChuyenTau;
import entity.NhanVien;
import entity.ToaTau;

public class ChonChoNgoiGUI extends GiaoDienChinh {
	private ChuyenTau chuyenTauDuocChon;
	private NhanVien nvHienTai;
	
	
    private JComboBox<String> toaComboBox;
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel seatPanel;
    private JButton bookButton;
    private JPanel seatDisplayPanel;

    private Color primaryColor;
    private Color darkBg;
   

    public ChonChoNgoiGUI(ChuyenTau ct, NhanVien nv) {
        this.chuyenTauDuocChon = ct;
        this.nvHienTai = nv;
        initializeComponents();
        setupLayout();
        setupListeners();
        
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        initializeComponents();
        setupLayout();
        setupListeners();
        setTitle("Chọn Chỗ Ngồi Tàu Lửa");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        // Màu thông thường: Trắng, xám nhạt, xanh dương nhạt cho accent
        primaryColor = Color.BLACK; // Đen cho text chính
        darkBg = new Color(220, 220, 220); // Light gray cho footer
      

        // Panel chọn toa (phía trên)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(new Color(103, 192, 144));
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Chọn toa
        JLabel toaLabel = new JLabel("Chọn Toa:");
        toaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        toaLabel.setForeground(primaryColor);
        String[] toaTypes = {"Toa Ghế 1", "Toa Ghế 2", "Toa Ghế 3", "Toa Giường Nằm 1", "Toa Giường Nằm 2"};
        toaComboBox = new JComboBox<>(toaTypes);
        toaComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        toaComboBox.setBackground(Color.WHITE);
        toaComboBox.setForeground(primaryColor);
        toaComboBox.setPreferredSize(new Dimension(250, 30));

        topPanel.add(toaLabel);
        topPanel.add(toaComboBox);

        // Panel bên trái (thông tin hoặc menu)
        leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setPreferredSize(new Dimension(200, 0));
        leftPanel.setBorder(new EtchedBorder(Color.GRAY, new Color(74, 140, 103)));
        JLabel leftTitle = new JLabel("Thông Tin", SwingConstants.CENTER);
        leftTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        leftTitle.setForeground(primaryColor);
        leftPanel.add(leftTitle, BorderLayout.NORTH);
        JTextArea infoArea = new JTextArea("Chọn toa để xem chỗ ngồi.\n\nMàu xanh nhạt: Trống\nMàu đỏ: Đã chọn");
        infoArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoArea.setBackground(Color.WHITE);
        infoArea.setForeground(primaryColor);
        infoArea.setEditable(false);
        leftPanel.add(new JScrollPane(infoArea), BorderLayout.CENTER);

        // Panel toa (giữa) - Sẽ cập nhật động
        seatDisplayPanel = new JPanel(new BorderLayout());
        seatDisplayPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Ban đầu hiển thị thông báo
        JLabel initialLabel = new JLabel("Chọn toa để hiển thị chỗ ngồi.", SwingConstants.CENTER);
        initialLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        initialLabel.setForeground(primaryColor);
        seatDisplayPanel.add(initialLabel, BorderLayout.CENTER);

        // Seat panel wrapper
        seatPanel = new JPanel(new BorderLayout());
        JLabel seatTitle = new JLabel("Chỗ Ngồi Toa", SwingConstants.CENTER);
        seatTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        seatTitle.setForeground(primaryColor);
        seatPanel.add(seatTitle, BorderLayout.NORTH);
        seatPanel.add(seatDisplayPanel, BorderLayout.CENTER);

        // Footer với nút đặt vé
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(darkBg);
        footerPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        bookButton = new JButton("Đặt Vé");
        bookButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        bookButton.setBackground(new Color(103, 192, 144));
        bookButton.setForeground(Color.WHITE);
        bookButton.setPreferredSize(new Dimension(150, 40));
        footerPanel.add(bookButton, BorderLayout.EAST);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(seatPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        bookButton.addActionListener(this);
    }

    // Tải icon cho ghế (thay đổi đường dẫn đến file seat.png trong project)
    private Icon getSeatIcon() {
        ImageIcon original = chinhKichThuoc("/img/car-seat.png", 40, 30);
     
        return original;
    }

    // Tải icon cho giường nằm (thay đổi đường dẫn đến file bed.png trong project)
    private Icon getBerthIcon() {
        ImageIcon original = chinhKichThuoc("/img/single-bed.png", 40, 30);
        return original;
    }

    private JPanel createSeatPanel(String toaName, int soLuongCho, String loaiCho, Color availableColor, Color selectedColor) {
        JPanel toaPanel = new JPanel(new BorderLayout());
        toaPanel.setBorder(new TitledBorder(new LineBorder(Color.GRAY, 2), toaName + " - " + loaiCho, TitledBorder.CENTER, TitledBorder.TOP));

        // Panel chính với layout dọc để kiểm soát chiều cao lối đi
        JPanel mainGrid = new JPanel();
        mainGrid.setLayout(new BoxLayout(mainGrid, BoxLayout.Y_AXIS));

        int numPerAisle = soLuongCho / 2;
        int rows = 2; // Chia thành 2 dãy nhỏ (2 hàng)
        int cols = numPerAisle / rows; // Số cột cho mỗi dãy

        // Dãy top: 2 hàng nhỏ
        JPanel topAisle = new JPanel(new GridLayout(rows, cols, 5, 5));
        for (int i = 1; i <= numPerAisle; i++) {
            JButton seatButton = new JButton(getSeatIcon()); // Sử dụng icon ghế
            seatButton.setBackground(availableColor);
            seatButton.setBorder(BorderFactory.createEmptyBorder());
            seatButton.setBorderPainted(false);
            seatButton.setFocusPainted(false);
            seatButton.setContentAreaFilled(true);
            seatButton.setPreferredSize(new Dimension(50, 40)); // Điều chỉnh kích thước button cho icon
            seatButton.addActionListener(e -> {
                if (seatButton.getBackground() == availableColor) {
                    seatButton.setBackground(selectedColor);
                } else {
                    seatButton.setBackground(availableColor);
                }
            });
            topAisle.add(seatButton);
        }

        // Lối đi: Panel rỗng cao nhỏ
        JPanel aisle = new JPanel(new BorderLayout());
        JLabel lblLoidi = new JLabel("Lối đi --->",SwingConstants.CENTER);
        lblLoidi.setFont(new Font("Segoe UI",Font.PLAIN,18));
        lblLoidi.setForeground(new Color(229, 115, 115));
        aisle.add(lblLoidi,BorderLayout.CENTER);
        aisle.setBackground(Color.LIGHT_GRAY);
        aisle.setPreferredSize(new Dimension(0, 100));
        aisle.setMaximumSize(new Dimension(Short.MAX_VALUE, 100));

        // Dãy bottom: 2 hàng nhỏ
        JPanel bottomAisle = new JPanel(new GridLayout(rows, cols, 5, 5));
        for (int i = numPerAisle + 1; i <= soLuongCho; i++) {
            JButton seatButton = new JButton(getSeatIcon()); // Sử dụng icon ghế
            seatButton.setBackground(availableColor);
            seatButton.setBorder(BorderFactory.createEmptyBorder());
            seatButton.setBorderPainted(false);
            seatButton.setFocusPainted(false);
            seatButton.setContentAreaFilled(true);
            seatButton.setPreferredSize(new Dimension(50, 40)); // Điều chỉnh kích thước button cho icon
            seatButton.addActionListener(e -> {
                if (seatButton.getBackground() == availableColor) {
                    seatButton.setBackground(selectedColor);
                } else {
                    seatButton.setBackground(availableColor);
                }
            });
            bottomAisle.add(seatButton);
        }

        mainGrid.add(topAisle);
        mainGrid.add(aisle);
        mainGrid.add(bottomAisle);

        // Wrap trong scroll pane (ngang cho dãy rộng)
        JScrollPane scrollPane = new JScrollPane(mainGrid);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        toaPanel.add(scrollPane, BorderLayout.CENTER);
        return toaPanel;
    }
    // Hàm tạo panel cho toa giường nằm: các khoang theo chiều ngang, dựa trên số lượng chỗ
    private JPanel createBerthPanel(String toaName, int soLuongCho, String loaiCho, Color availableColor, Color selectedColor) {
        JPanel toaPanel = new JPanel(new BorderLayout());
        toaPanel.setBorder(new TitledBorder(new LineBorder(Color.GRAY, 2), toaName + " - " + loaiCho, TitledBorder.CENTER, TitledBorder.TOP));

        int numKhoangFull = soLuongCho / 4;
        int choDu = soLuongCho % 4;
        int numKhoang = numKhoangFull + (choDu > 0 ? 1 : 0);

        // Panel labels tầng bên trái (ngoài tất cả khoang)
        JPanel leftLabels = new JPanel(new GridLayout(2, 1, 0, 5));
        leftLabels.setPreferredSize(new Dimension(60, 0));
        JLabel lowerLabel = new JLabel("Tầng 1", SwingConstants.CENTER);
        lowerLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lowerLabel.setForeground(primaryColor);
        JLabel upperLabel = new JLabel("Tầng 2", SwingConstants.CENTER);
        upperLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        upperLabel.setForeground(primaryColor);
        leftLabels.add(lowerLabel);
        leftLabels.add(upperLabel);

        // Panel chứa các khoang theo chiều ngang
        JPanel compartmentsPanel = new JPanel(new GridLayout(1, numKhoang, 10, 0)); // 1 hàng, numKhoang cột

        int currentIndex = 1;
        for (int k = 1; k <= numKhoang; k++) {
            // Mỗi khoang là một panel với title và chỗ tương ứng
            JPanel khoangPanel = new JPanel(new BorderLayout());
            khoangPanel.setBorder(new TitledBorder(new LineBorder(Color.GRAY, 1), "Khoang " + k, TitledBorder.CENTER, TitledBorder.TOP));

            int soChoKhoang = (k <= numKhoangFull) ? 4 : choDu;
            int rows = 2;
            int cols = soChoKhoang / rows + (soChoKhoang % rows > 0 ? 1 : 0); // Điều chỉnh cột cho khoang cuối

            JPanel berthGrid = new JPanel(new GridLayout(rows, cols, 5, 5));
            for (int i = 0; i < soChoKhoang; i++) {
                JButton berthButton = new JButton(getBerthIcon()); // Sử dụng icon giường
                berthButton.setBackground(Color.WHITE);
               
                berthButton.setBorderPainted(false);
                berthButton.setFocusPainted(false);
                berthButton.setContentAreaFilled(true);
                berthButton.setPreferredSize(new Dimension(30, 20)); // Giảm chiều cao button
                berthButton.addActionListener(e -> {
                    if (berthButton.getBackground() == availableColor) {
                        berthButton.setBackground(selectedColor);
                    } else {
                        berthButton.setBackground(availableColor);
                    }
                });
                berthGrid.add(berthButton);
                currentIndex++;
            }

            // Thêm các panel rỗng nếu cần để lấp đầy grid
            while (berthGrid.getComponentCount() < rows * cols) {
                JPanel empty = new JPanel();
                empty.setBackground(Color.WHITE);
                empty.setPreferredSize(new Dimension(50, 40));
                berthGrid.add(empty);
            }

            khoangPanel.add(berthGrid, BorderLayout.CENTER);
            compartmentsPanel.add(khoangPanel);
        }

        // Wrapper để đặt labels bên trái và compartments bên phải
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(leftLabels, BorderLayout.WEST);
        wrapper.add(compartmentsPanel, BorderLayout.CENTER);

        // Wrap trong scroll pane ngang
        JScrollPane scrollPane = new JScrollPane(wrapper);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        toaPanel.add(scrollPane, BorderLayout.CENTER);
        return toaPanel;
    }

    private void setupLayout() {
        add(mainPanel);
    }

    private void setupListeners() {
        // Listener cho chọn toa
        toaComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedToa = (String) toaComboBox.getSelectedItem();
                String type = selectedToa.contains("Ghế") ? "seat" : "berth";
                String loaiCho = selectedToa.contains("Ghế") ? "Ghế" : "Giường Nằm";
                int soLuongCho;

                // Tạo và hiển thị panel chỗ ngồi dựa trên loại
                JPanel newSeatPanel;
                Color available = new Color(173, 216, 230);
                Color selected = Color.RED;
                if (type.equals("seat")) {
                    soLuongCho = 20;
                    newSeatPanel = createSeatPanel(selectedToa, soLuongCho, loaiCho, Color.white, selected);
                } else {
                    soLuongCho = 28;
                    newSeatPanel = createBerthPanel(selectedToa, soLuongCho, loaiCho, Color.white, selected);
                }

                seatDisplayPanel.removeAll();
                seatDisplayPanel.add(newSeatPanel, BorderLayout.CENTER);
                seatDisplayPanel.revalidate();
                seatDisplayPanel.repaint();

                // Cập nhật left panel
                updateLeftPanel(selectedToa, soLuongCho, loaiCho);

                // Bật nút đặt vé
                bookButton.setEnabled(true);
            }
        });
    }

    private void updateLeftPanel(String toa, int soLuongCho, String loaiCho) {
        // Cập nhật text area bên trái
        Component[] components = leftPanel.getComponents();
        for (Component c : components) {
            if (c instanceof JScrollPane) {
                JScrollPane scroll = (JScrollPane) c;
                if (scroll.getViewport().getView() instanceof JTextArea) {
                    JTextArea area = (JTextArea) scroll.getViewport().getView();
                    StringBuilder text = new StringBuilder("Toa: " + toa + "\n");
                    text.append("Loại: ").append(loaiCho).append(" (").append(soLuongCho).append(" chỗ)\n");
                    if (loaiCho.contains("Ghế")) {
                        text.append("Cấu hình: 2 dãy ghế\n");
                    } else {
                        int numKhoang = soLuongCho / 4 + (soLuongCho % 4 > 0 ? 1 : 0);
                        text.append("Cấu hình: ").append(numKhoang).append(" khoang, mỗi khoang 4 chỗ (khoang cuối có thể ít hơn)\n");
                    }
                    text.append("\nMàu xanh nhạt: Trống\nMàu đỏ: Đã chọn");
                    area.setText(text.toString());
                }
            }
        }
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
        });
    }
    private List<ChoNgoi> collectSelectedSeats(ChuyenTau ct) {
        // Đây là placeholder. Trong thực tế, bạn cần logic phức tạp hơn.
        List<ChoNgoi> seats = new ArrayList<>();
        // Ví dụ: Giả định ghế A-01 (IDloaiGhe=1, Trống) và chuyến tàu đã chọn
        ChoNgoi cn = new ChoNgoi("A-01", 1, "Trống", new ToaTau("TOA-01")); 
        seats.add(cn);
        return seats;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == bookButton) {
            // 1. THU THẬP: Lấy danh sách chỗ ngồi đã chọn
            // ********* Cần triển khai hàm này để lấy đối tượng ChoNgoi thực tế *********
            List<ChoNgoi> selectedSeats = collectSelectedSeats(chuyenTauDuocChon); 

            if (selectedSeats.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn chỗ ngồi.");
                return;
            }

            // 2. CHUYỂN MÀN HÌNH: Mở màn hình nhập thông tin khách hàng
            ThongTinKhachHangGUI tthk = new ThongTinKhachHangGUI(
                chuyenTauDuocChon, 
                selectedSeats, 
                nvHienTai
            );
            tthk.setVisible(true);
            this.dispose(); // Đóng màn hình chọn chỗ ngồi
        }
        // ...
    }
	
}
