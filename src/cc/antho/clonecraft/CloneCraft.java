package cc.antho.clonecraft;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import cc.antho.clonecraft.client.CloneCraftClient;
import cc.antho.clonecraft.client.CloneCraftGame;
import cc.antho.clonecraft.core.ConnectionDefaults;
import cc.antho.clonecraft.core.Util;
import cc.antho.clonecraft.core.log.Logger;
import cc.antho.clonecraft.core.log.LoggerImpl;
import cc.antho.clonecraft.server.CloneCraftServer;

public final class CloneCraft {

	private CloneCraft() {

	}

	public static final void main(final String[] args) {

		Logger.logger = new LoggerImpl();
		Logger.info("Starting CloneCraft");

		Logger.debug("Setting look and feel to system");
		Util.setLookAndFeel();

		final JTabbedPane tabbedPane = new JTabbedPane();

		final GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5, 5, 5, 5);

		{

			final JPanel panel = new JPanel(new GridBagLayout());
			tabbedPane.addTab("Client", panel);

			final JLabel lblAddress = new JLabel("Host Address");
			constraints.gridx = 0;
			constraints.gridy = 0;
			panel.add(lblAddress, constraints);

			final JTextField txtAddress = new JTextField(ConnectionDefaults.ADDRESS, 10);
			constraints.gridx = 1;
			constraints.gridy = 0;
			panel.add(txtAddress, constraints);

			final JLabel lblPortTcp = new JLabel("Host Port (TCP)");
			constraints.gridx = 0;
			constraints.gridy = 1;
			panel.add(lblPortTcp, constraints);

			final JTextField txtPortTcp = new JTextField(ConnectionDefaults.TCP_PORT + "", 10);
			constraints.gridx = 1;
			constraints.gridy = 1;
			panel.add(txtPortTcp, constraints);

			final JLabel lblPortUdp = new JLabel("Host Port (UDP)");
			constraints.gridx = 0;
			constraints.gridy = 2;
			panel.add(lblPortUdp, constraints);

			final JTextField txtPortUdp = new JTextField(ConnectionDefaults.UDP_PORT + "", 10);
			constraints.gridx = 1;
			constraints.gridy = 2;
			panel.add(txtPortUdp, constraints);

			final JCheckBox chkboxWithDebuffer = new JCheckBox("Launch with debugger");
			constraints.gridwidth = 2;
			constraints.gridx = 0;
			constraints.gridy = 3;
			constraints.anchor = GridBagConstraints.CENTER;
			panel.add(chkboxWithDebuffer, constraints);
			constraints.anchor = GridBagConstraints.WEST;
			constraints.gridwidth = 1;

			final JButton btnConnect = new JButton("Connect");
			constraints.anchor = GridBagConstraints.CENTER;
			constraints.gridwidth = 2;
			constraints.gridx = 0;
			constraints.gridy = 4;
			panel.add(btnConnect, constraints);

			// Reset these vales for next use
			constraints.anchor = GridBagConstraints.WEST;
			constraints.gridwidth = 1;

			btnConnect.addActionListener(new ActionListener() {

				public final void actionPerformed(final ActionEvent e) {

					try {

						final int tcp = Integer.parseInt(txtPortTcp.getText());
						final int udp = Integer.parseInt(txtPortUdp.getText());
						CloneCraftClient.address = txtAddress.getText();
						CloneCraftClient.tcp = tcp;
						CloneCraftClient.udp = udp;
						CloneCraftGame.main(chkboxWithDebuffer.isSelected());

					} catch (final NumberFormatException e2) {

					}

				}

			});

		}

		{

			final JPanel panel = new JPanel(new GridBagLayout());
			tabbedPane.addTab("Server", panel);

			final JLabel lblPortTcp = new JLabel("Port (TCP)");
			constraints.gridx = 0;
			constraints.gridy = 0;
			panel.add(lblPortTcp, constraints);

			final JTextField txtPortTcp = new JTextField(ConnectionDefaults.TCP_PORT + "", 10);
			constraints.gridx = 1;
			constraints.gridy = 0;
			panel.add(txtPortTcp, constraints);

			final JLabel lblPortUdp = new JLabel("Port (UDP)");
			constraints.gridx = 0;
			constraints.gridy = 1;
			panel.add(lblPortUdp, constraints);

			final JTextField txtPortUdp = new JTextField(ConnectionDefaults.UDP_PORT + "", 10);
			constraints.gridx = 1;
			constraints.gridy = 1;
			panel.add(txtPortUdp, constraints);

			final JLabel lblPlayerPkt = new JLabel("Player packet frequency");
			constraints.gridx = 0;
			constraints.gridy = 2;
			panel.add(lblPlayerPkt, constraints);

			final JTextField txtPlayerPkt = new JTextField(ConnectionDefaults.PLAYER_PACKET_FREQUENCY + "", 10);
			constraints.gridx = 1;
			constraints.gridy = 2;
			panel.add(txtPlayerPkt, constraints);

			constraints.anchor = GridBagConstraints.CENTER;

			final JButton buttonStart = new JButton("Start");
			buttonStart.setEnabled(true);
			constraints.gridx = 0;
			constraints.gridy = 3;
			panel.add(buttonStart, constraints);

			final JButton buttonStop = new JButton("Stop");
			buttonStop.setEnabled(false);
			constraints.gridx = 1;
			constraints.gridy = 3;
			panel.add(buttonStop, constraints);

			buttonStart.addActionListener(new ActionListener() {

				public final void actionPerformed(final ActionEvent e) {

					try {

						final int tcp = Integer.parseInt(txtPortTcp.getText());
						final int udp = Integer.parseInt(txtPortUdp.getText());
						final float ppf = Float.parseFloat(txtPlayerPkt.getText());

						CloneCraftServer.startThread(tcp, udp, ppf);

						buttonStop.setEnabled(true);
						buttonStart.setEnabled(false);

					} catch (final NumberFormatException e2) {

						e2.printStackTrace();

					}

				}

			});

			buttonStop.addActionListener(new ActionListener() {

				public final void actionPerformed(final ActionEvent e) {

					CloneCraftServer.thread.interrupt();

					buttonStop.setEnabled(false);
					buttonStart.setEnabled(true);

				}

			});

		}

		{

			final JFrame f = new JFrame("CloneCraft");
			f.setMinimumSize(new Dimension(300, 250));
			f.setPreferredSize(new Dimension(640, 480));
			f.setSize(new Dimension(640, 480));
			f.setLocationRelativeTo(null);
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setLayout(new BorderLayout());
			f.add(tabbedPane, BorderLayout.CENTER);
			f.setVisible(true);
			Logger.info("Main UI shown");

		}

	}

}
