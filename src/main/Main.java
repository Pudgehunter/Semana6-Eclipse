package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.gson.Gson;

import model.User;
import processing.core.PApplet;

public class Main extends PApplet {
	
	private Socket socket;
	private BufferedReader reader;
	private BufferedWriter writer;
	private String listUser, listPassword;
	private int pantalla;
	private boolean onActive;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main("main.Main");
	}
	
	public void settings() {
		size(500,500);
	}

	public void setup() {
		pantalla = 0;
		listUser = "Yoshiki Elizabeth Daniel Nataly Paula Gabriel Andres Sebastian Laura Isabella";
		listPassword = "yos12 eli12 dan12 nat12 pau12 gab12 and12 seb12 lau12 isa12";
		onActive = true;
		initServer();	
	}
	
	public void draw() {
		switch(pantalla) {
		case 0: 
			background(0);
			text("Ingrese su usuario y contraseña desde su teléfono movil",100,250);
			break;
		case 1:
			background(0);
			text("Bienvenido",width/2,height/2);
			break;
		}
	}
	
	public void initServer() {
		new Thread(
				() -> {
					try {
						//1. Esperar una conexión
						ServerSocket server = new ServerSocket(5000);
						System.out.println("Esperando conexión");
						socket = server.accept();
						
						//3. Conectados
						System.out.println("Cliente conectado");
						
						InputStream is = socket.getInputStream();
						InputStreamReader isr = new InputStreamReader(is);
						reader = new BufferedReader(isr);
						
						OutputStream os = socket.getOutputStream();
						OutputStreamWriter osw = new OutputStreamWriter(os);
						writer = new BufferedWriter(osw);
						
						while(onActive == true) {
							System.out.println("Esperando...");
							String line = reader.readLine();
							System.out.println("Recibido");
							//Validación de si tienen el mismo usuario o contraseña.
							

							String[] dividirListUser = listUser.split(" ");
							String[] dividirListPassword = listPassword.split(" ");

							Gson gson = new Gson();
							
							User userJson = gson.fromJson(line, User.class);
							System.out.println(userJson.getDescription());
							
							
							for (int i = 0; i < dividirListUser.length; i++) {
								if(dividirListUser[i].equals(userJson.getUsuario()) && dividirListPassword[i].equals(userJson.getPassword())) {
									
									pantalla = 1;
									System.out.println("si funciona");
									sendUser("cambio de pantalla");
									onActive = false;
									
								} else if(onActive == true) {
									
									pantalla = 0;
									sendUser("Fail");
									System.out.println("no funciona");
								}
							}
						}
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				).start();
	}
	
	public void sendUser(String msg) {
		new Thread(
				() -> {
					try {
						 writer.write(msg+"\n");
	                     writer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				).start();
	}
	
}
