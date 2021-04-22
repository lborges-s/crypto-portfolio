package br.com.project.models;

import java.net.URI;
import java.util.concurrent.CountDownLatch;

import javax.swing.JOptionPane;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

@ClientEndpoint
public class MyClientEndpoint {

	Session userSession = null;
	private MessageHandler messageHandler;

	public MyClientEndpoint(URI endpointURI) {
		try {
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			container.connectToServer(this, endpointURI);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}

	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		System.out.println("Tamo conectado");
		userSession = session;
	}

	@OnClose
	public void onClose(CloseReason c) {
		System.out.println("Conex�o fechada!");
		userSession = null;
	}

	@OnError
	public void onError(Throwable t) {
		
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		if (this.messageHandler != null) {
			this.messageHandler.handleMessage(message);
		}
	}

	public void addMessageHandler(MessageHandler msgHandler) {
		this.messageHandler = msgHandler;
	}

	public static interface MessageHandler {

		public void handleMessage(String message);
	}

}
