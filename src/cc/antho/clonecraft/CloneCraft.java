package cc.antho.clonecraft;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.esotericsoftware.minlog.Log;
import com.esotericsoftware.minlog.Log.Logger;

import cc.antho.clonecraft.client.CloneCraftClient;
import cc.antho.clonecraft.core.Util;
import cc.antho.clonecraft.server.CloneCraftServer;

public final class CloneCraft {
	
	static CloneCraftFrame ccFrame;

	private CloneCraft() {

	}

	public static final void main(final String[] args) {

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

}
