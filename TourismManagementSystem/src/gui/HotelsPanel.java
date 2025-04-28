package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JComboBox;
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

import dao.HotelBookingDAO;
import dao.HotelDAO;
import models.Hotel;
import models.HotelBooking;
import models.User;
import utils.DateUtils;

public class HotelsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private User user;
    private JTable hotelsTable;
    private DefaultTableModel tableModel;
    private JTextArea descriptionArea;
    
    // Date selection components
    private JComboBox<Integer> checkInDay, checkInMonth, checkInYear;
    private JComboBox<Integer> checkOutDay, checkOutMonth, checkOutYear;
    
    private JSpinner roomsSpinner;
    private JTextField totalPriceField;
    private JButton bookButton;
    private Hotel selectedHotel;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    public HotelsPanel(User user) {
        this.user = user;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(new TitledBorder("Hotels"));
        
        // Create table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
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
        
        // Create details panel
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(new TitledBorder("Hotel Details & Booking"));
        
        // Description panel
        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        descPanel.add(new JLabel("Description:"), BorderLayout.NORTH);
        descPanel.add(descScrollPane, BorderLayout.CENTER);
        
        detailsPanel.add(descPanel, BorderLayout.NORTH);
        
        // Booking panel
        JPanel bookingPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        bookingPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Check-in date components
        bookingPanel.add(new JLabel("Check-in Date (DD/MM/YYYY):"));
        JPanel checkInPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        // Create day, month, year dropdowns for check-in
        checkInDay = new JComboBox<>();
        for (int i = 1; i <= 31; i++) checkInDay.addItem(i);
        
        checkInMonth = new JComboBox<>();
        for (int i = 1; i <= 12; i++) checkInMonth.addItem(i);
        
        checkInYear = new JComboBox<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i <= currentYear + 2; i++) checkInYear.addItem(i);
        
        checkInPanel.add(checkInDay);
        checkInPanel.add(new JLabel("/"));
        checkInPanel.add(checkInMonth);
        checkInPanel.add(new JLabel("/"));
        checkInPanel.add(checkInYear);
        bookingPanel.add(checkInPanel);
        
        // Check-out date components
        bookingPanel.add(new JLabel("Check-out Date (DD/MM/YYYY):"));
        JPanel checkOutPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        // Create day, month, year dropdowns for check-out
        checkOutDay = new JComboBox<>();
        for (int i = 1; i <= 31; i++) checkOutDay.addItem(i);
        
        checkOutMonth = new JComboBox<>();
        for (int i = 1; i <= 12; i++) checkOutMonth.addItem(i);
        
        checkOutYear = new JComboBox<>();
        for (int i = currentYear; i <= currentYear + 2; i++) checkOutYear.addItem(i);
        
        checkOutPanel.add(checkOutDay);
        checkOutPanel.add(new JLabel("/"));
        checkOutPanel.add(checkOutMonth);
        checkOutPanel.add(new JLabel("/"));
        checkOutPanel.add(checkOutYear);
        bookingPanel.add(checkOutPanel);
        
        bookingPanel.add(new JLabel("Number of Rooms:"));
        roomsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        bookingPanel.add(roomsSpinner);
        
        bookingPanel.add(new JLabel("Total Price:"));
        totalPriceField = new JTextField();
        totalPriceField.setEditable(false);
        bookingPanel.add(totalPriceField);
        
        bookingPanel.add(new JLabel(""));
        bookButton = new JButton("Book Hotel");
        bookButton.setEnabled(false);
        bookingPanel.add(bookButton);
        
        detailsPanel.add(bookingPanel, BorderLayout.CENTER);
        
        add(detailsPanel, BorderLayout.SOUTH);
        
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
        
        // Add action listeners to update price when date or rooms change
        ActionListener dateChangeListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTotalPrice();
            }
        };
        
        checkInDay.addActionListener(dateChangeListener);
        checkInMonth.addActionListener(dateChangeListener);
        checkInYear.addActionListener(dateChangeListener);
        checkOutDay.addActionListener(dateChangeListener);
        checkOutMonth.addActionListener(dateChangeListener);
        checkOutYear.addActionListener(dateChangeListener);
        
        roomsSpinner.addChangeListener(e -> updateTotalPrice());
        
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookHotel();
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
            descriptionArea.setText(selectedHotel.getDescription());
            updateTotalPrice();
            bookButton.setEnabled(selectedHotel.getAvailableRooms() > 0);
        }
    }
    
    private Date getCheckInDate() {
        try {
            String dateStr = checkInYear.getSelectedItem() + "-" + 
                             checkInMonth.getSelectedItem() + "-" + 
                             checkInDay.getSelectedItem();
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
    
    private Date getCheckOutDate() {
        try {
            String dateStr = checkOutYear.getSelectedItem() + "-" + 
                             checkOutMonth.getSelectedItem() + "-" + 
                             checkOutDay.getSelectedItem();
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
    
    private void updateTotalPrice() {
        if (selectedHotel != null) {
            Date checkIn = getCheckInDate();
            Date checkOut = getCheckOutDate();
            
            if (checkIn == null || checkOut == null) {
                totalPriceField.setText("Invalid dates");
                bookButton.setEnabled(false);
                return;
            }
            
            if (checkOut.before(checkIn)) {
                totalPriceField.setText("Invalid dates");
                bookButton.setEnabled(false);
                return;
            }
            
            long diffInMillies = Math.abs(checkOut.getTime() - checkIn.getTime());
            long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            
            if (diffInDays == 0) diffInDays = 1; // Minimum 1 day
            
            int rooms = (int) roomsSpinner.getValue();
            double totalPrice = selectedHotel.getPricePerNight() * diffInDays * rooms;
            totalPriceField.setText(String.format("%.2f", totalPrice));
            bookButton.setEnabled(selectedHotel.getAvailableRooms() >= rooms);
        }
    }
    
    private void bookHotel() {
        if (selectedHotel == null) {
            JOptionPane.showMessageDialog(this, "Please select a hotel first", "Booking Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Date checkInDate = getCheckInDate();
        Date checkOutDate = getCheckOutDate();
        
        if (checkInDate == null || checkOutDate == null) {
            JOptionPane.showMessageDialog(this, "Please select valid check-in and check-out dates", "Booking Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (checkOutDate.before(checkInDate)) {
            JOptionPane.showMessageDialog(this, "Check-out date cannot be before check-in date", "Booking Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if check-in date is in the future
        Date today = new Date();
        if (checkInDate.before(today)) {
            JOptionPane.showMessageDialog(this, "Check-in date must be in the future", "Booking Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int rooms = (int) roomsSpinner.getValue();
        if (rooms > selectedHotel.getAvailableRooms()) {
            JOptionPane.showMessageDialog(this, "Not enough rooms available", "Booking Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        long diffInMillies = Math.abs(checkOutDate.getTime() - checkInDate.getTime());
        long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        
        if (diffInDays == 0) diffInDays = 1; // Minimum 1 day
        
        double totalPrice = selectedHotel.getPricePerNight() * diffInDays * rooms;
        
        // Confirm booking
        int choice = JOptionPane.showConfirmDialog(this, 
                "Confirm booking for " + selectedHotel.getName() + "?\n" +
                "Check-in: " + dateFormat.format(checkInDate) + "\n" +
                "Check-out: " + dateFormat.format(checkOutDate) + "\n" +
                "Rooms: " + rooms + "\n" +
                "Total Price: $" + String.format("%.2f", totalPrice),
                "Confirm Booking", JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            // Create booking
            HotelBooking booking = new HotelBooking();
            booking.setUserId(user.getUserId());
            booking.setHotelId(selectedHotel.getHotelId());
            booking.setBookingDate(new Date());
            booking.setCheckInDate(checkInDate);
            booking.setCheckOutDate(checkOutDate);
            booking.setNumRooms(rooms);
            booking.setTotalPrice(totalPrice);
            booking.setStatus("CONFIRMED");
            HotelBookingDAO bookingDAO = new HotelBookingDAO();
            boolean success = bookingDAO.createHotelBooking(booking);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Hotel booking successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadHotels();  // Refresh hotels to update available rooms
                clearBookingForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to book hotel", "Booking Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearBookingForm() {
        // Reset date selections to current date
        Calendar cal = Calendar.getInstance();
        checkInDay.setSelectedItem(cal.get(Calendar.DAY_OF_MONTH));
        checkInMonth.setSelectedItem(cal.get(Calendar.MONTH) + 1);
        checkInYear.setSelectedItem(cal.get(Calendar.YEAR));
        
        checkOutDay.setSelectedItem(cal.get(Calendar.DAY_OF_MONTH));
        checkOutMonth.setSelectedItem(cal.get(Calendar.MONTH) + 1);
        checkOutYear.setSelectedItem(cal.get(Calendar.YEAR));
        
        roomsSpinner.setValue(1);
        totalPriceField.setText("");
        bookButton.setEnabled(false);
        selectedHotel = null;
        hotelsTable.clearSelection();
        descriptionArea.setText("");
    }
}

            
            
