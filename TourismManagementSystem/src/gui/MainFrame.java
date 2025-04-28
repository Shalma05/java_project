package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import models.User;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private User currentUser;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    
    // Panel names
    private static final String PROFILE_PANEL = "PROFILE";
    private static final String PACKAGES_PANEL = "PACKAGES";
    private static final String PACKAGE_BOOKINGS_PANEL = "PACKAGE_BOOKINGS";
    private static final String HOTELS_PANEL = "HOTELS";
    private static final String HOTEL_BOOKINGS_PANEL = "HOTEL_BOOKINGS";
    private static final String ADMIN_PANEL = "ADMIN";
    
    public MainFrame(User user) {
        this.currentUser = user;
        
        setTitle("Tourism Management System - Welcome " + user.getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Create navigation panel
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JButton profileBtn = new JButton("My Profile");
        JButton packagesBtn = new JButton("Tour Packages");
        JButton packageBookingsBtn = new JButton("My Package Bookings");
        JButton hotelsBtn = new JButton("Hotels");
        JButton hotelBookingsBtn = new JButton("My Hotel Bookings");
        JButton adminBtn = new JButton("Admin Panel");
        JButton logoutBtn = new JButton("Logout");
        
        navPanel.add(profileBtn);
        navPanel.add(packagesBtn);
        navPanel.add(packageBookingsBtn);
        navPanel.add(hotelsBtn);
        navPanel.add(hotelBookingsBtn);
        
        if (user.isAdmin()) {
            navPanel.add(adminBtn);
        }
        
        navPanel.add(logoutBtn);
        
        mainPanel.add(navPanel, BorderLayout.NORTH);
        
        // Create card panel for different views
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Add panels to card layout
        cardPanel.add(new ProfilePanel(currentUser), PROFILE_PANEL);
        cardPanel.add(new PackagesPanel(currentUser), PACKAGES_PANEL);
        cardPanel.add(new PackageBookingsPanel(currentUser), PACKAGE_BOOKINGS_PANEL);
        cardPanel.add(new HotelsPanel(currentUser), HOTELS_PANEL);
        cardPanel.add(new HotelBookingsPanel(currentUser), HOTEL_BOOKINGS_PANEL);
        
        if (user.isAdmin()) {
            cardPanel.add(new AdminPanel(), ADMIN_PANEL);
        }
        
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        
        // Add action listeners
        profileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, PROFILE_PANEL);
            }
        });
        
        packagesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, PACKAGES_PANEL);
            }
        });
        
        packageBookingsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, PACKAGE_BOOKINGS_PANEL);
            }
        });
        
        hotelsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, HOTELS_PANEL);
            }
        });
        
        hotelBookingsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, HOTEL_BOOKINGS_PANEL);
            }
        });
        
        if (user.isAdmin()) {
            adminBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cardPanel, ADMIN_PANEL);
                }
            });
        }
        
        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        
        add(mainPanel);
        
        // Show profile panel by default
        cardLayout.show(cardPanel, PROFILE_PANEL);
        
        setVisible(true);
    }
    
    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new LoginFrame();
                    dispose();
                }
            });
        }
    }
    
    public void refreshPanel(String panelName) {
        if (panelName.equals(PROFILE_PANEL)) {
            cardPanel.remove(0);
            cardPanel.add(new ProfilePanel(currentUser), PROFILE_PANEL, 0);
        } else if (panelName.equals(PACKAGES_PANEL)) {
            cardPanel.remove(1);
            cardPanel.add(new PackagesPanel(currentUser), PACKAGES_PANEL, 1);
        } else if (panelName.equals(PACKAGE_BOOKINGS_PANEL)) {
            cardPanel.remove(2);
            cardPanel.add(new PackageBookingsPanel(currentUser), PACKAGE_BOOKINGS_PANEL, 2);
        } else if (panelName.equals(HOTELS_PANEL)) {
            cardPanel.remove(3);
            cardPanel.add(new HotelsPanel(currentUser), HOTELS_PANEL, 3);
        } else if (panelName.equals(HOTEL_BOOKINGS_PANEL)) {
            cardPanel.remove(4);
            cardPanel.add(new HotelBookingsPanel(currentUser), HOTEL_BOOKINGS_PANEL, 4);
        }
        
        cardLayout.show(cardPanel, panelName);
    }
}

