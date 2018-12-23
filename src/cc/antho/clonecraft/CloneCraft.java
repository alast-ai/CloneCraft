package cc.antho.clonecraft;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import cc.antho.clonecraft.client.CloneCraftClient;
import cc.antho.clonecraft.client.CloneCraftGame;
import cc.antho.clonecraft.core.Config;
import cc.antho.clonecraft.core.Util;
import cc.antho.clonecraft.core.log.Logger;
import cc.antho.clonecraft.core.log.LoggerImpl;
import cc.antho.clonecraft.server.CloneCraftServer;

public final class CloneCraft {
	
	static CloneCraftFrame ccFrame;

	private CloneCraft() {

	}

	public static final void main(final String[] args) {

		Logger.logger = new LoggerImpl();
		Logger.info("Starting CloneCraft");

		Logger.debug("Setting look and feel to system");
		Util.setLookAndFeel();

		Logger.debug("Loading config");
		Config.loadConfig();
		
		ccFrame = new CloneCraftFrame("CloneCraft");
		ccFrame.getClientConnectButton().addActionListener(new ActionListener() {

			public final void actionPerformed(final ActionEvent e) {

				try {

					final int tcp = Integer.parseInt(ccFrame.getClientHostPortTCP().getText());
					final int udp = Integer.parseInt(ccFrame.getClientHostPortUDP().getText());
					CloneCraftClient.address = ccFrame.getClientHostAddress().getText();
					CloneCraftClient.tcp = tcp;
					CloneCraftClient.udp = udp;
					CloneCraftGame.main(ccFrame.getClientDebuggerEnabled().isSelected());
					
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

					CloneCraftServer.startThread(tcp, udp, ppf);

					ccFrame.getServerStopButton().setEnabled(true);
					ccFrame.getServerStartButton().setEnabled(false);

				} catch (final NumberFormatException e2) {

					e2.printStackTrace();

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

}
