package com.cherrrnikov.chatapp.client;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatClientGUI extends JFrame {
    private JTextArea messageArea;
    private JTextField textField;
    private ChatClient chatClient;
    private JButton exitButton;

    public ChatClientGUI() {
        super("Chat Application");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Styling variables
        Color backgroundColor = new Color(240, 240, 240); // Light gray background
        Color buttonColor = new Color(75, 75, 75); // Darker gray for buttons
        Color textColor = new Color(50, 50, 50); // Almost black for text
        Font textFont = new Font("Arial", Font.PLAIN, 14);
        Font buttonFont = new Font("Arial", Font.BOLD, 12);

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setBackground(backgroundColor);
        messageArea.setForeground(buttonColor);
        messageArea.setFont(textFont);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        add(scrollPane, BorderLayout.CENTER);

        // Prompt for username
        String name = JOptionPane.showInputDialog(
                this,
                "Enter your name: ",
                "Name Entry",
                JOptionPane.PLAIN_MESSAGE
        );
        this.setTitle("Chat Application - " + name);

        // Apply styles to the text field
        textField = new JTextField();
        textField.setFont(textFont);
        textField.setForeground(textColor);
        textField.setBackground(backgroundColor);
        textField.addActionListener(e -> {
            String message = "[" +
                    new SimpleDateFormat("HH:mm:ss").format(new Date())  +
                    "]" +
                    name +
                    ": " +
                    textField.getText();

            chatClient.sendMessage(message);
            textField.setText("");
        });
        add(textField, BorderLayout.SOUTH);

        // Apply styles to the exit button and initialize it
        exitButton = new JButton("Exit");
        exitButton.setFont(buttonFont);
        exitButton.setBackground(buttonColor);
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(e -> {
            // Send a departure message to the server
            String departuredMessage = name + " has left the chat.";
            chatClient.sendMessage(departuredMessage);

            // Delay to ensure the message is sent before exiting
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }

            // If I use this method, all my testing windows are closing instead of one.
            // because I have only one system on my PC. Idk how to change it.
//            System.exit(0);
        });

        // Creating a bottom panel to hold the text field and exit button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(backgroundColor); // Apply background color to the panel
        bottomPanel.add(textField, BorderLayout.CENTER);
        bottomPanel.add(exitButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // Initialize and start the ChatClient
        try {
            this.chatClient = new ChatClient("localhost", 5000, this::onMessageReceived);
            chatClient.startClient();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Error connecting to the server",
                    "Connection error",
                    JOptionPane.ERROR_MESSAGE
            );
            System.exit(1);
        }
    }

    private void onMessageReceived(String message) {
        SwingUtilities.invokeLater(() -> messageArea.append(message + "\n"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChatClientGUI().setVisible(true));
        SwingUtilities.invokeLater(() -> new ChatClientGUI().setVisible(true));
        SwingUtilities.invokeLater(() -> new ChatClientGUI().setVisible(true));
    }
}
