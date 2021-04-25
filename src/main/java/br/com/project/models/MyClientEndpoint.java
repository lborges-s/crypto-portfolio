package br.com.project.models;

import java.io.IOException;
import java.net.URI;

import javax.swing.JOptionPane;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.PongMessage;
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
		System.out.println("Conexão fechada!");
		userSession = null;
	}

	@OnError
	public void onError(Throwable t) {
		System.out.println("Erro websocket > " + t.getMessage());
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		if (this.messageHandler != null) {
			this.messageHandler.handleMessage(message);
		}
	}

//	@OnMessage
//	protected void sendPongMessage(PongMessage message) throws IOException {
//		System.out.println("sendPongMessage");
//		userSession.getBasicRemote().sendPong(message.getApplicationData());
//	}

	public void addMessageHandler(MessageHandler msgHandler) {
		this.messageHandler = msgHandler;
	}

	public static interface MessageHandler {

		public void handleMessage(String message);
	}

}
