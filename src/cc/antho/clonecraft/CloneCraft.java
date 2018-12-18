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

	    JFrame f = new JFrame("A JFrame");
	    f.setSize(200, 170);
	    f.setLocation(300, 200);
	    JTabbedPane tabbedPane = new JTabbedPane();
	    
	    GridBagConstraints constraints = new GridBagConstraints();
	    constraints.anchor = GridBagConstraints.WEST;
	    constraints.insets = new Insets(5, 5, 5, 5);
	    
	    {
	        JPanel panel = new JPanel(new GridBagLayout());
	        
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
		    
		    final JButton senpai = new JButton("Connect");
		    constraints.anchor = GridBagConstraints.EAST;
		    constraints.gridx = 1;
	        constraints.gridy = 2;      
		    panel.add(senpai, constraints);
		    senpai.addActionListener(new ActionListener() {
		        @Override
		        public void actionPerformed(ActionEvent e) {
		        	CloneCraftClient.main(txtAddress.getText(), Integer.parseInt(txtPort.getText()));
		        }
		    });
		    
	        tabbedPane.addTab("Client", panel);
	    }
	    
	    {
	        JPanel panel = new JPanel(new GridBagLayout());
		    
		    final JButton senpaiStart = new JButton("Start");
		    final JButton senpaiStop = new JButton("Stop");
		    senpaiStop.setEnabled(false);
		    senpaiStart.setEnabled(true);
		    
		    constraints.anchor = GridBagConstraints.EAST;
		    constraints.gridx = 0;
	        constraints.gridy = 0;      
		    panel.add(senpaiStart, constraints);
		    senpaiStart.addActionListener(new ActionListener() {
	
		        @Override
		        public void actionPerformed(ActionEvent e) {
	        		if(CloneCraftServer.instance != null && CloneCraftServer.instance.started) return;
		        	
				    if(CloneCraftServer.instance == null) CloneCraftServer.main();
				    
				    senpaiStop.setEnabled(true);
				    senpaiStart.setEnabled(false);
	
		        }
		    });
		    
		    constraints.anchor = GridBagConstraints.EAST;
		    constraints.gridx = 1;
	        constraints.gridy = 0;      
		    panel.add(senpaiStop, constraints);
		    senpaiStop.addActionListener(new ActionListener() {
		        @Override
		        public void actionPerformed(ActionEvent e) {
		        	CloneCraftServer.instance.getServer().stop();
				    senpaiStop.setEnabled(false);
		        	synchronized(CloneCraftServer.instance.getServer().getUpdateThread()) {
			        	System.out.println("Server Stopped");
			        	CloneCraftServer.instance.started = false;
					    senpaiStart.setEnabled(true);
		        	}
	
		        }
		    });
		    
	        tabbedPane.addTab("Server", panel);
	    }

    	f.add(tabbedPane);
	    f.setVisible(true);
	    f.setResizable(false);

	}

}
