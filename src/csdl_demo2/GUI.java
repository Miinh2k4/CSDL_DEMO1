package csdl_demo2;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class GUI {

    // Text fields
    JTextField txtProductID = new JTextField();
    JTextField txtProductName = new JTextField();
    JTextField txtPrice = new JTextField();
    JTextField txtQuantity = new JTextField();
    JTextField txtDescription = new JTextField();

    // Buttons
    JButton btnThem = new JButton("THÊM");
    JButton btnXoa = new JButton("XÓA");
    JButton btnSua = new JButton("SỬA");
    JButton btnThoat = new JButton("THOÁT");

    // ComboBox
    JComboBox<String> cbCateID = new JComboBox<>();

    // Table columns
    public String[] col = {"Product ID", "Product Name", "Category Name", "Price", "Quantity", "Description"};
    public DefaultTableModel model = new DefaultTableModel(col, 0);
    public JTable table = new JTable(model);

    // Database credentials
    String DB_URL = "jdbc:mysql://localhost:3306/CSDLDK1";
    String USER_NAME = "root";
    String PASSWORD = "";

    private int index = -1;

    public void initUI() {
        // Frame
        JFrame frame = new JFrame("Product Management");
        frame.setSize(830, 420);
        frame.setLayout(null);

        // Labels and text fields
        JLabel lbProductID = new JLabel("Product ID:");
        lbProductID.setBounds(20, 20, 80, 25);
        frame.add(lbProductID);
        txtProductID.setBounds(100, 20, 80, 25);
        frame.add(txtProductID);

        JLabel lbProductName = new JLabel("Product Name:");
        lbProductName.setBounds(20, 60, 100, 25);
        frame.add(lbProductName);
        txtProductName.setBounds(120, 60, 220, 25);
        frame.add(txtProductName);

        JLabel lbPrice = new JLabel("Price:");
        lbPrice.setBounds(20, 100, 80, 25);
        frame.add(lbPrice);
        txtPrice.setBounds(100, 100, 80, 25);
        frame.add(txtPrice);

        JLabel lbQuantity = new JLabel("Quantity:");
        lbQuantity.setBounds(20, 150, 80, 25);
        frame.add(lbQuantity);
        txtQuantity.setBounds(100, 150, 80, 25);
        frame.add(txtQuantity);

        JLabel lbDescription = new JLabel("Description:");
        lbDescription.setBounds(20, 200, 80, 25);
        frame.add(lbDescription);
        txtDescription.setBounds(100, 200, 220, 25);
        frame.add(txtDescription);

        JLabel lbCateID = new JLabel("Category ID:");
        lbCateID.setBounds(20, 250, 80, 25);
        frame.add(lbCateID);
        cbCateID.setBounds(100, 250, 150, 25);
        frame.add(cbCateID);

        // Table and scroll pane
        JScrollPane pane = new JScrollPane(table);
        JPanel panel = new JPanel();
        panel.add(pane);
        panel.setBounds(350, 20, 450, 300);
        frame.add(panel);

        // Buttons
        btnThem.setBounds(100, 300, 90, 25);
        frame.add(btnThem);

        btnSua.setBounds(230, 300, 90, 25);
        frame.add(btnSua);

        btnXoa.setBounds(100, 340, 90, 25);
        frame.add(btnXoa);

        btnThoat.setBounds(230, 340, 90, 25);
        frame.add(btnThoat);

        // Button state
        btnXoa.setEnabled(false);
        btnSua.setEnabled(false);

        // Frame visibility
        frame.setVisible(true);

        // Table event listener
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                index = table.getSelectedRow();
                txtProductID.setText(table.getValueAt(index, 0).toString());
                txtProductName.setText(table.getValueAt(index, 1).toString());
                cbCateID.setSelectedItem(table.getValueAt(index, 2).toString());
                txtPrice.setText(table.getValueAt(index, 3).toString());
                txtQuantity.setText(table.getValueAt(index, 4).toString());
                txtDescription.setText(table.getValueAt(index, 5).toString());
                btnXoa.setEnabled(true);
                btnSua.setEnabled(true);
            }
        });

        // Add button event listener
        btnThem.addActionListener(e -> {
            try {
                if (validateInputs()) {
                    String query = "INSERT INTO product (ProductID, ProductName, CateID, Price, Quantity, Description) VALUES (?, ?, ?, ?, ?, ?)";
                    try (Connection cnn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
                         PreparedStatement pstm = cnn.prepareStatement(query)) {
                        pstm.setInt(1, Integer.parseInt(txtProductID.getText().trim()));
                        pstm.setString(2, txtProductName.getText().trim());
                        pstm.setInt(3, Integer.parseInt(cbCateID.getSelectedItem().toString().trim()));
                        pstm.setDouble(4, Double.parseDouble(txtPrice.getText().trim()));
                        pstm.setInt(5, Integer.parseInt(txtQuantity.getText().trim()));
                        pstm.setString(6, txtDescription.getText().trim());
                        int result = pstm.executeUpdate();
                        if (result == 1) {
                            model.addRow(new Object[]{
                                txtProductID.getText(),
                                txtProductName.getText(),
                                cbCateID.getSelectedItem().toString(),
                                txtPrice.getText(),
                                txtQuantity.getText(),
                                txtDescription.getText()
                            });
                            clearForm();
                        }
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });

        // Edit button event listener
        btnSua.addActionListener(e -> {
            try {
                if (validateInputs()) {
                    String query = "UPDATE product SET ProductName=?, CateID=?, Price=?, Quantity=?, Description=? WHERE ProductID=?";
                    try (Connection cnn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
                         PreparedStatement pstm = cnn.prepareStatement(query)) {
                        pstm.setString(1, txtProductName.getText().trim());
                        pstm.setInt(2, Integer.parseInt(cbCateID.getSelectedItem().toString().trim()));
                        pstm.setDouble(3, Double.parseDouble(txtPrice.getText().trim()));
                        pstm.setInt(4, Integer.parseInt(txtQuantity.getText().trim()));
                        pstm.setString(5, txtDescription.getText().trim());
                        pstm.setInt(6, Integer.parseInt(txtProductID.getText().trim()));
                        int rowAffected = pstm.executeUpdate();
                        if (rowAffected == 1) {
                            JOptionPane.showMessageDialog(null, "Update Complete");
                            loadData();
                            clearForm();
                        }
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });

        // Delete button event listener
        btnXoa.addActionListener(e -> {
            try {
                String query = "DELETE FROM product WHERE ProductID=?";
                try (Connection cnn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
                     PreparedStatement pstm = cnn.prepareStatement(query)) {
                    pstm.setInt(1, Integer.parseInt(txtProductID.getText().trim()));
                    pstm.executeUpdate();
                    model.removeRow(index);
                    clearForm();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });

        // Exit button event listener
        btnThoat.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "Bạn có muốn thoát?", "Xác nhận thoát", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // Load data to table and combobox
        loadData();
        loadComboboxData();
    }

    // Validate inputs
    private boolean validateInputs() throws Exception {
        if (txtProductID.getText().isEmpty()) throw new Exception("Product ID không được để trống!");
        if (txtProductName.getText().isEmpty()) throw new Exception("Product Name không được để trống!");
        if (txtPrice.getText().isEmpty()) throw new Exception("Price không được để trống!");
        if (txtQuantity.getText().isEmpty()) throw new Exception("Quantity không được để trống!");
        if (txtDescription.getText().isEmpty()) throw new Exception("Description không được để trống!");
        return true;
    }

    // Clear form
    private void clearForm() {
        txtProductID.setText(null);
        txtProductName.setText(null);
        txtPrice.setText(null);
        txtQuantity.setText(null);
        txtDescription.setText(null);
        cbCateID.setSelectedIndex(-1);
        index = -1;
        btnXoa.setEnabled(false);
        btnSua.setEnabled(false);
    }

    // Load data to table
    public void loadData() {
        try (Connection cnn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
             Statement stm = cnn.createStatement()) {
            ResultSet rs = stm.executeQuery("SELECT ProductID, ProductName, CateID, Price, Quantity, Description FROM product");
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("ProductID"),
                    rs.getString("ProductName"),
                    rs.getInt("CateID"),
                    rs.getDouble("Price"),
                    rs.getInt("Quantity"),
                    rs.getString("Description")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    // Load categories to combobox
    public void loadComboboxData() {
        try (Connection cnn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
             Statement stm = cnn.createStatement()) {
            ResultSet rs = stm.executeQuery("SELECT CateID FROM category");
            cbCateID.removeAllItems();
            while (rs.next()) {
                cbCateID.addItem(rs.getString("CateID"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new GUI().initUI();
    }
}
