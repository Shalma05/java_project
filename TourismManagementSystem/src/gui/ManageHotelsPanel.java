package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import dao.HotelDAO;
import models.Hotel;

public class ManageHotelsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTable hotelsTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextField locationField;
    private JTextArea descriptionArea;
    private JSpinner priceSpinner;
    private JSpinner ratingSpinner;
    private JSpinner roomsSpinner;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private Hotel selectedHotel;
    
    public ManageHotelsPanel() {
        setLayout(new BorderLayout(10, 10));
        
        // Create table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new TitledBorder("Hotels"));
        
        // Create table model
        String[] columnNames = {"ID", "Name", "Location", "Price/Night ($)", "Rating", "Available Rooms"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        hotelsTable = new JTable(tableModel);
        hotelsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(hotelsTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        add(tablePanel, BorderLayout.CENTER);
        
        // Create form panel
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBorder(new TitledBorder("Hotel Details"));
        
        JPanel fieldsPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        fieldsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        fieldsPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        fieldsPanel.add(nameField);
        
        fieldsPanel.add(new JLabel("Location:"));
        locationField = new JTextField();
        fieldsPanel.add(locationField);
        
        fieldsPanel.add(new JLabel("Price per Night ($):"));
        priceSpinner = new JSpinner(new SpinnerNumberModel(100.0, 10.0, 10000.0, 10.0));
        fieldsPanel.add(priceSpinner);
        
        fieldsPanel.add(new JLabel("Rating (1-5):"));
        ratingSpinner = new JSpinner(new SpinnerNumberModel(3.0, 1.0, 5.0, 0.5));
        fieldsPanel.add(ratingSpinner);
        
        fieldsPanel.add(new JLabel("Available Rooms:"));
        roomsSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        fieldsPanel.add(roomsSpinner);
        
        fieldsPanel.add(new JLabel("Description:"));
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        fieldsPanel.add(descScrollPane);
        
        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        addButton = new JButton("Add Hotel");
        updateButton = new JButton("Update Hotel");
        deleteButton = new JButton("Delete Hotel");
        clearButton = new JButton("Clear Form");
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        formPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(formPanel, BorderLayout.SOUTH);
        
        // Load hotels
        loadHotels();
        
        // Add listeners
        hotelsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = hotelsTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        int hotelId = (int) tableModel.getValueAt(selectedRow, 0);
                        displayHotelDetails(hotelId);
                    }
                }
            }
        });
        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addHotel();
            }
        });
        
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateHotel();
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteHotel();
            }
        });
        
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
    }
    
    private void loadHotels() {
        // Clear table
        tableModel.setRowCount(0);
        
        // Load hotels from database
        HotelDAO hotelDAO = new HotelDAO();
        List<Hotel> hotels = hotelDAO.getAllHotels();
        
        for (Hotel hotel : hotels) {
            Object[] row = {
                hotel.getHotelId(),
                hotel.getName(),
                hotel.getLocation(),
                hotel.getPricePerNight(),
                hotel.getRating(),
                hotel.getAvailableRooms()
            };
            tableModel.addRow(row);
        }
    }
    
    private void displayHotelDetails(int hotelId) {
        HotelDAO hotelDAO = new HotelDAO();
        selectedHotel = hotelDAO.getHotelById(hotelId);
        
        if (selectedHotel != null) {
            nameField.setText(selectedHotel.getName());
            locationField.setText(selectedHotel.getLocation());
            priceSpinner.setValue(selectedHotel.getPricePerNight());
            ratingSpinner.setValue(selectedHotel.getRating());
            roomsSpinner.setValue(selectedHotel.getAvailableRooms());
            descriptionArea.setText(selectedHotel.getDescription());
            
            updateButton.setEnabled(true);
            deleteButton.setEnabled(true);
        }
    }
    
    private void addHotel() {
        String name = nameField.getText();
        String location = locationField.getText();
        double price = (double) priceSpinner.getValue();
        double rating = (double) ratingSpinner.getValue();
        int rooms = (int) roomsSpinner.getValue();
        String description = descriptionArea.getText();
        
        // Validate input
        if (name.isEmpty() || location.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create hotel object
        Hotel hotel = new Hotel();
        hotel.setName(name);
        hotel.setLocation(location);
        hotel.setPricePerNight(price);
        hotel.setRating(rating);
        hotel.setAvailableRooms(rooms);
        hotel.setDescription(description);
        
        // Add hotel to database
        HotelDAO hotelDAO = new HotelDAO();
        boolean success = hotelDAO.addHotel(hotel);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Hotel added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadHotels();  // Refresh hotels
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add hotel", "Add Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateHotel() {
        if (selectedHotel == null) {
            JOptionPane.showMessageDialog(this, "Please select a hotel to update", "Update Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String name = nameField.getText();
        String location = locationField.getText();
        double price = (double) priceSpinner.getValue();
        double rating = (double) ratingSpinner.getValue();
        int rooms = (int) roomsSpinner.getValue();
        String description = descriptionArea.getText();
        
        // Validate input
        if (name.isEmpty() || location.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Update hotel object
        selectedHotel.setName(name);
        selectedHotel.setLocation(location);
        selectedHotel.setPricePerNight(price);
        selectedHotel.setRating(rating);
        selectedHotel.setAvailableRooms(rooms);
        selectedHotel.setDescription(description);
        
        // Update hotel in database
        HotelDAO hotelDAO = new HotelDAO();
        boolean success = hotelDAO.updateHotel(selectedHotel);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Hotel updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadHotels();  // Refresh hotels
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update hotel", "Update Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteHotel() {
        if (selectedHotel == null) {
            JOptionPane.showMessageDialog(this, "Please select a hotel to delete", "Delete Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Confirm deletion
        int choice = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete hotel " + selectedHotel.getName() + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            HotelDAO hotelDAO = new HotelDAO();
            boolean success = hotelDAO.deleteHotel(selectedHotel.getHotelId());
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Hotel deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadHotels();  // Refresh hotels
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete hotel", "Delete Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearForm() {
        selectedHotel = null;
        nameField.setText("");
        locationField.setText("");
        priceSpinner.setValue(100.0);
        ratingSpinner.setValue(3.0);
        roomsSpinner.setValue(10);
        descriptionArea.setText("");
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        hotelsTable.clearSelection();
    }
}
