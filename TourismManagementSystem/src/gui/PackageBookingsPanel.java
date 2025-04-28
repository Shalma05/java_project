package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import dao.BookingDAO;
import models.Booking;
import models.User;

public class PackageBookingsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private User user;
    private JTable bookingsTable;
    private DefaultTableModel tableModel;
    private JButton cancelButton;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    public PackageBookingsPanel(User user) {
        this.user = user;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(new TitledBorder("My Package Bookings"));
        
        // Create table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create table model
        String[] columnNames = {"Booking ID", "Package", "Booking Date", "Travel Date", "Travelers", "Total Price ($)", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        bookingsTable = new JTable(tableModel);
        bookingsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        add(tablePanel, BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        cancelButton = new JButton("Cancel Selected Booking");
        buttonPanel.add(cancelButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Load bookings
        loadBookings();
        
        // Add listeners
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelBooking();
            }
        });
    }
    
    private void loadBookings() {
        // Clear table
        tableModel.setRowCount(0);
        
        // Load bookings from database
        BookingDAO bookingDAO = new BookingDAO();
        List<Booking> bookings = bookingDAO.getBookingsByUserId(user.getUserId());
        
        for (Booking booking : bookings) {
            Object[] row = {
                booking.getBookingId(),
                booking.getPackageName(),
                dateFormat.format(booking.getBookingDate()),
                dateFormat.format(booking.getTravelDate()),
                booking.getNumTravelers(),
                String.format("%.2f", booking.getTotalPrice()),
                booking.getStatus()
            };
            tableModel.addRow(row);
        }
    }
    
    private void cancelBooking() {
        int selectedRow = bookingsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a booking to cancel", "Cancel Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int bookingId = (int) tableModel.getValueAt(selectedRow, 0);
        String status = (String) tableModel.getValueAt(selectedRow, 6);
        
        if (status.equals("CANCELLED")) {
            JOptionPane.showMessageDialog(this, "This booking is already cancelled", "Cancel Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Confirm cancellation
        int choice = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to cancel this booking?",
                "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            BookingDAO bookingDAO = new BookingDAO();
            boolean success = bookingDAO.cancelBooking(bookingId);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Booking cancelled successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadBookings();  // Refresh bookings
            } else {
                JOptionPane.showMessageDialog(this, "Failed to cancel booking", "Cancel Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
