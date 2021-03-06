/*
 * Class MessagePage
 * 
 * Version: 1.0
 *
 * 11.06.2013
 * 
 * This Class will implement our Interface Page and other Methods which will
 * be used to create our Message Page to interact with the users.
 *
 * Copyright ZHAW 2013
 */

package ch.zhaw.multiChannel.view;

import ch.zhaw.multiChannel.controller.Controller;
import ch.zhaw.multiChannel.model.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;


public class MessagePage implements Page {

	private Controller controller;
	private String timeOfDay = new String();

	private JButton sendButton = new JButton("Nachricht senden");
	protected JCheckBox timeshiftBox = new JCheckBox("Zeit versetzt senden?");
	protected JComboBox timeComboBox;
	private JLabel receiverLabel = new JLabel("Empfänger:");
	private JLabel receiverInfoLabel = new JLabel("(separiert durch semikolon)");
	protected JTextArea messageText = new JTextArea(15, 40);
	protected JTextField receiverText = new JTextField(15);
	protected JTextField dateTextField = new JTextField(20);

	private JPanel lowerPanel = new JPanel();
	private JPanel mainPanel = new JPanel();
	private JPanel middlePanel = new JPanel();
	private JPanel upperPanel = new JPanel();

	protected MinimumSizedFrame mainFrame = new MinimumSizedFrame();

	public MessagePage(Controller controller) {

		this.controller = controller;
	}

	/**
	 * It creates the User Interface
	 */
	public void show(String title) {

		timeComboBox = new JComboBox(getTimeList());
		timeComboBox.setEditable(false);
		dateTextField.setText("sofort senden");
		dateTextField.setEditable(false);
		upperPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		upperPanel.add(receiverLabel);
		upperPanel.add(receiverText);
		upperPanel.add(receiverInfoLabel);

		middlePanel.setLayout(new BorderLayout());
		JScrollPane scrollMessageText = new JScrollPane(messageText);
		middlePanel.add(scrollMessageText, BorderLayout.CENTER);

		JPanel sendTimePanel = new JPanel();
		sendTimePanel.setLayout(new FlowLayout());
		loadSendTimePanel(lowerPanel);

		mainPanel.setSize(600, 700);
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(BorderLayout.NORTH, upperPanel);
		mainPanel.add(BorderLayout.CENTER, middlePanel);
		mainPanel.add(BorderLayout.SOUTH, lowerPanel);

		mainFrame.getContentPane().add(mainPanel);
		mainFrame.setSize(600, 500);
		mainFrame.setMinimumSize(new Dimension(470, 200));
		mainFrame.setVisible(true);
		mainFrame.setTitle(title);
		mainFrame.pack();

		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		timeshiftBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent datum) {

				if (timeshiftBox.isSelected()) {
					dateTextField.setText(new DatePicker(mainFrame).setPickedDate());
					timeComboBox.setEditable(true);
				} else {
					dateTextField.setText("Zeitversetzt senden?");
					timeComboBox.setEditable(false);
				}
			}
		});

		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				controller.sendMessageRequest();
			}
		});
	}


	protected void loadSendTimePanel(JPanel panel) {

		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.add(timeshiftBox);
		panel.add(dateTextField);
		dateTextField.setColumns(7);
		panel.add(timeComboBox);
		panel.add(sendButton);
	}

	public boolean isVisible() {

		return mainFrame.isVisible();
	}

	public Message getMessage() {

		String[] receivers = receiverText.getText().split(";");
		String message = messageText.getText();
		if (!timeshiftBox.isSelected()) {
			return new Message(receivers, message);
		} else {
			String date = dateTextField.getText();
			String time = timeComboBox.getSelectedItem().toString();
			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyyHH:mm");
			try {
				return new Message(receivers, message, format.parse(dateTextField.getText() + timeComboBox.getSelectedItem()));
			} catch (ParseException e) {
				showError(String.format("Ung�ltiges Datum: %s %s", date, time));
				return null;
			}
		}
	}

	public void close() {

		mainFrame.dispose();
	}

	public void showError(String errorMessage) {

		JOptionPane.showMessageDialog(mainFrame, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public void showNotification(String errorMessage) {

		JOptionPane.showMessageDialog(mainFrame, errorMessage, "Danke", JOptionPane.INFORMATION_MESSAGE);
	}

	private Vector<String> getTimeList() {

		Vector<String> timelist = new Vector<String>();

		for (int h = 0; h < 24; h++) {
			for (int min = 0; min < 60; min = min + 5) {
				String stunde = Integer.toString(h);
				if (stunde.length() == 1) {
					stunde = "0" + stunde;
				}
				String minuten = Integer.toString(min);
				if (minuten.length() == 1) {
					minuten = "0" + minuten;
				}
				timeOfDay = stunde + ":" + minuten;
				timelist.add(timeOfDay);
			}
		}
		return timelist;
	}
}