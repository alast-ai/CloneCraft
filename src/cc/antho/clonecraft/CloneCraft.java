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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.esotericsoftware.minlog.Log;
import com.esotericsoftware.minlog.Log.Logger;

import cc.antho.clonecraft.client.CloneCraftClient;
import cc.antho.clonecraft.core.ConnectionDefaults;
import cc.antho.clonecraft.core.Util;
import cc.antho.clonecraft.server.CloneCraftServer;
import lombok.Getter;

public final class CloneCraft {
	
	static CloneCraftFrame ccFrame;

	private CloneCraft() {

	}

	public static final void main(final String[] args) {

//		Log.setLogger(new Logger() {
//			 
//			public void log(final int level, final String category, final String message, final Throwable ex) {
//				
//				System.out.println("[" + category + "] " + message);
//			
//			}
//		
//		});

		Util.setLookAndFeel();
		
		ccFrame = new CloneCraftFrame("CloneCraft");
		ccFrame.getClientConnectButton().addActionListener(new ActionListener() {

			public final void actionPerformed(final ActionEvent e) {

				try {

					final int tcp = Integer.parseInt(ccFrame.getClientHostPortTCP().getText());
					final int udp = Integer.parseInt(ccFrame.getClientHostPortUDP().getText());
					CloneCraftClient.main(ccFrame.getClientHostAddress().getText(), tcp, udp, ccFrame.getClientDebuggerEnabled().isSelected());

				} catch (final NumberFormatException e2) {

				}

			}

		});

		ccFrame.getServerStartButton().addActionListener(new ActionListener() {

			public final void actionPerformed(final ActionEvent e) {

				try {

					final int tcp = Integer.parseInt(ccFrame.getServerHostPortTCP().getText());
					final int udp = Integer.parseInt(ccFrame.getServerHostPortUDP().getText());
					final float ppf = Float.parseFloat(ccFrame.getServerPlayerPacketFrequency().getText());

					CloneCraftServer.main(tcp, udp, ppf);

					ccFrame.getServerStopButton().setEnabled(true);
					ccFrame.getServerStartButton().setEnabled(false);

				} catch (final NumberFormatException e2) {

				}

			}

		});

		ccFrame.getServerStopButton().addActionListener(new ActionListener() {

			public final void actionPerformed(final ActionEvent e) {

				CloneCraftServer.thread.interrupt();

				ccFrame.getServerStopButton().setEnabled(false);
				ccFrame.getServerStartButton().setEnabled(true);

			}

		});


	}
	
	static private class CloneCraftFrame extends JFrame {
		final private GridBagConstraints constraints = new GridBagConstraints();

		@Getter public JTextField clientHostAddress;
		@Getter public JTextField clientHostPortTCP;
		@Getter public JTextField clientHostPortUDP;
		@Getter public JCheckBox clientDebuggerEnabled;
		@Getter public JButton clientConnectButton;
		
		@Getter public JTextField serverHostPortTCP;
		@Getter public JTextField serverHostPortUDP;
		@Getter public JTextField serverPlayerPacketFrequency;
		@Getter public JButton serverStartButton;
		@Getter public JButton serverStopButton;
		
		private final JPanel createClientPanel() {
			final JPanel panel = new JPanel(new GridBagLayout());

			final JLabel lblAddress = new JLabel("Host Address");
			constraints.gridx = 0;
			constraints.gridy = 0;
			panel.add(lblAddress, constraints);

			clientHostAddress = new JTextField(ConnectionDefaults.ADDRESS, 10);
			constraints.gridx = 1;
			constraints.gridy = 0;
			panel.add(clientHostAddress, constraints);

			final JLabel lblPortTcp = new JLabel("Host Port (TCP)");
			constraints.gridx = 0;
			constraints.gridy = 1;
			panel.add(lblPortTcp, constraints);

			clientHostPortTCP = new JTextField(ConnectionDefaults.TCP_PORT + "", 10);
			constraints.gridx = 1;
			constraints.gridy = 1;
			panel.add(clientHostPortTCP, constraints);

			final JLabel lblPortUdp = new JLabel("Host Port (UDP)");
			constraints.gridx = 0;
			constraints.gridy = 2;
			panel.add(lblPortUdp, constraints);

			clientHostPortUDP = new JTextField(ConnectionDefaults.UDP_PORT + "", 10);
			constraints.gridx = 1;
			constraints.gridy = 2;
			panel.add(clientHostPortUDP, constraints);

			clientDebuggerEnabled = new JCheckBox("Launch with debugger");
			constraints.gridwidth = 2;
			constraints.gridx = 0;
			constraints.gridy = 3;
			constraints.anchor = GridBagConstraints.CENTER;
			panel.add(clientDebuggerEnabled, constraints);
			
			constraints.anchor = GridBagConstraints.WEST;
			constraints.gridwidth = 1;

			clientConnectButton = new JButton("Connect");
			constraints.anchor = GridBagConstraints.CENTER;
			constraints.gridwidth = 2;
			constraints.gridx = 0;
			constraints.gridy = 4;
			panel.add(clientConnectButton, constraints);

			return panel;
		}
		
		private final JPanel createServerPanel() {
			final JPanel panel = new JPanel(new GridBagLayout());
			
			final JLabel lblPortTcp = new JLabel("Port (TCP)");
			constraints.gridx = 0;
			constraints.gridy = 0;
			panel.add(lblPortTcp, constraints);

			serverHostPortTCP = new JTextField(ConnectionDefaults.TCP_PORT + "", 10);
			constraints.gridx = 1;
			constraints.gridy = 0;
			panel.add(serverHostPortTCP, constraints);

			final JLabel lblPortUdp = new JLabel("Port (UDP)");
			constraints.gridx = 0;
			constraints.gridy = 1;
			panel.add(lblPortUdp, constraints);

			serverHostPortUDP = new JTextField(ConnectionDefaults.UDP_PORT + "", 10);
			constraints.gridx = 1;
			constraints.gridy = 1;
			panel.add(serverHostPortUDP, constraints);

			final JLabel lblPlayerPkt = new JLabel("Player packet frequency");
			constraints.gridx = 0;
			constraints.gridy = 2;
			panel.add(lblPlayerPkt, constraints);

			serverPlayerPacketFrequency = new JTextField(ConnectionDefaults.PLAYER_PACKET_FREQUENCY + "", 10);
			constraints.gridx = 1;
			constraints.gridy = 2;
			panel.add(serverPlayerPacketFrequency, constraints);

			constraints.anchor = GridBagConstraints.CENTER;

			serverStartButton = new JButton("Start");
			serverStartButton.setEnabled(true);
			constraints.gridx = 0;
			constraints.gridy = 4;
			panel.add(serverStartButton, constraints);

			serverStopButton = new JButton("Stop");
			serverStopButton.setEnabled(false);
			constraints.gridx = 1;
			constraints.gridy = 4;
			panel.add(serverStopButton, constraints);
			
			return panel;
		}

		public CloneCraftFrame(String title) {
			super(title);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setMinimumSize(new Dimension(300, 250));
			setPreferredSize(new Dimension(640, 480));
			setLocationRelativeTo(null);
			setLayout(new BorderLayout());
			
			constraints.anchor = GridBagConstraints.WEST;
			constraints.insets = new Insets(5, 5, 5, 5);

			final JTabbedPane tabbedPane = new JTabbedPane();
			tabbedPane.add("Server", createServerPanel());
			
			constraints.anchor = GridBagConstraints.WEST; // reset for client panel
			tabbedPane.insertTab("Client", null, createClientPanel(), null, 0);
			
			add(tabbedPane, BorderLayout.CENTER);
			tabbedPane.setSelectedIndex(0);
			setVisible(true);

		}
	}

}
