package cc.antho.clonecraft;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import cc.antho.clonecraft.core.Config;
import lombok.Getter;

public class CloneCraftFrame extends JFrame {

	private static final long serialVersionUID = 1L;

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

		clientHostAddress = new JTextField(Config.ADDRESS, 10);
		constraints.gridx = 1;
		constraints.gridy = 0;
		panel.add(clientHostAddress, constraints);

		final JLabel lblPortTcp = new JLabel("Host Port (TCP)");
		constraints.gridx = 0;
		constraints.gridy = 1;
		panel.add(lblPortTcp, constraints);

		clientHostPortTCP = new JTextField(Config.TCP_PORT + "", 10);
		constraints.gridx = 1;
		constraints.gridy = 1;
		panel.add(clientHostPortTCP, constraints);

		final JLabel lblPortUdp = new JLabel("Host Port (UDP)");
		constraints.gridx = 0;
		constraints.gridy = 2;
		panel.add(lblPortUdp, constraints);

		clientHostPortUDP = new JTextField(Config.UDP_PORT + "", 10);
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

		serverHostPortTCP = new JTextField(Config.TCP_PORT + "", 10);
		constraints.gridx = 1;
		constraints.gridy = 0;
		panel.add(serverHostPortTCP, constraints);

		final JLabel lblPortUdp = new JLabel("Port (UDP)");
		constraints.gridx = 0;
		constraints.gridy = 1;
		panel.add(lblPortUdp, constraints);

		serverHostPortUDP = new JTextField(Config.UDP_PORT + "", 10);
		constraints.gridx = 1;
		constraints.gridy = 1;
		panel.add(serverHostPortUDP, constraints);

		final JLabel lblPlayerPkt = new JLabel("Player packet frequency");
		constraints.gridx = 0;
		constraints.gridy = 2;
		panel.add(lblPlayerPkt, constraints);

		serverPlayerPacketFrequency = new JTextField(Config.PLAYER_PACKET_FREQUENCY + "", 10);
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

	public CloneCraftFrame(final String title) {
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(300, 250));
		setSize(new Dimension(640, 480));
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
