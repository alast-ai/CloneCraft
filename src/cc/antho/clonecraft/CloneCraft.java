package cc.antho.clonecraft;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JLabel;

import cc.antho.clonecraft.client.CloneCraftClient;
import cc.antho.clonecraft.core.ConnectionDefaults;
import cc.antho.clonecraft.core.Util;
import cc.antho.clonecraft.server.CloneCraftServer;

public final class CloneCraft {

	private CloneCraft() {
		
	}

	public static final void main(final String[] args) {

		Util.setLookAndFeel();

		JFrame f = new JFrame("CloneCraft");
		f.setSize(200, 170);
		f.setLocation(300, 200);
		JTabbedPane tabbedPane = new JTabbedPane();
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5, 5, 5, 5);
		
		{
			
			JPanel panel = new JPanel(new GridBagLayout());
			tabbedPane.addTab("Client", panel);
			
			final JLabel lblAddress = new JLabel("Host Address");
			constraints.gridx = 0;
			constraints.gridy = 0;
			panel.add(lblAddress, constraints);
			
			final JTextField txtAddress = new JTextField(ConnectionDefaults.ADDRESS, 10);
			constraints.gridx = 1;
			constraints.gridy = 0;
			panel.add(txtAddress, constraints);
			
			final JLabel lblPort = new JLabel("Host Port");
			constraints.gridx = 0;
			constraints.gridy = 1;
			panel.add(lblPort, constraints);
			
			final JTextField txtPort = new JTextField(ConnectionDefaults.TCP_PORT+"", 10);
			constraints.gridx = 1;
			constraints.gridy = 1;
			panel.add(txtPort, constraints);
			
			final JButton btnConnect = new JButton("Connect");
			constraints.anchor = GridBagConstraints.EAST;
			constraints.gridx = 1;
			constraints.gridy = 2;
			panel.add(btnConnect, constraints);
			
			btnConnect.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					CloneCraftClient.main(txtAddress.getText(), Integer.parseInt(txtPort.getText()));
					
				}
				
			});
			
		}
		
		{
			
			JPanel panel = new JPanel(new GridBagLayout());
			tabbedPane.addTab("Server", panel);
			
			final JButton serverStart = new JButton("Start");
			final JButton serverStop = new JButton("Stop");
			serverStop.setEnabled(false);
			serverStart.setEnabled(true);
			
			constraints.anchor = GridBagConstraints.EAST;
			constraints.gridx = 0;
			constraints.gridy = 0;
			panel.add(serverStart, constraints);
			
			serverStart.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					if(CloneCraftServer.instance != null && CloneCraftServer.instance.started) return;
					if(CloneCraftServer.instance == null) CloneCraftServer.main();
					
					serverStop.setEnabled(true);
					serverStart.setEnabled(false);
				}
				
			});
			
			constraints.anchor = GridBagConstraints.EAST;
			constraints.gridx = 1;
			constraints.gridy = 0;
			panel.add(serverStop, constraints);
			
			serverStop.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					CloneCraftServer.instance.getServer().stop();
					serverStop.setEnabled(false);
					synchronized(CloneCraftServer.instance.getServer().getUpdateThread()) {
						
						System.out.println("Server Stopped");
						CloneCraftServer.instance.started = false;
						serverStart.setEnabled(true);
						
					}
	
				}
				
			});
		}

		f.add(tabbedPane);
		f.setVisible(true);
		f.setResizable(false);

	}

}
