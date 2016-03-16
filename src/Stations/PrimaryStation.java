package Stations;

import com.sun.javaws.exceptions.InvalidArgumentException;
import hdlc.ControlDataInterface;
import hdlc.HdlcFrame;
import hdlc.SFrameControlData;
import hdlc.UFrameControlData;
import util.StringUtil;

import java.net.*;
import java.io.*;
import java.util.Objects;

public class PrimaryStation {	
	
    public static void main(String[] args) throws IOException, InvalidArgumentException {
        
        //
        // sockets and other variables declaration
        //
        // maximum number of clients connected: 10
        //
               
        ServerSocket serverSocket = null;
        Socket[] client_sockets;
        client_sockets = new Socket[10];
        PrintWriter[] s_out;
        s_out = new PrintWriter[10];
        BufferedReader[] s_in;
        s_in = new BufferedReader[10];
        
        int[] ns; // send sequence number
        ns = new int[10];
        
        int[] nr; // receive sequence number
        nr = new int[10];
        
        String inputLine = null;
        String outputLine = null;
        
        //
        //get port number from the command line
        //
        int nPort = 4444; // default port number
        //nPort = Integer.parseInt(args[0]);        
        
        String flag = "01111110";
        String[] address;
        address = new String[10];
        int[] clientID;
        clientID = new int[10];
        
        String control = null;
        String information = "";
        
        boolean bListening = true;
        
        String[] sMessages; // frame buffer
        sMessages = new String[20];
        int nMsg = 0;        
        
        
        boolean bAlive = false;
		
        String response = null; // control field of the input
        //
        // initialize some var's for array handling
        //
        int s_count = 0;
        int i = 0;       
        
        //
        // create server socket
        //
        try {
            serverSocket = new ServerSocket(nPort);
            
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + args[0]);
            System.exit(-1);
        }
        
        //
        // this variable defines how many clients are connected
        //
        int nClient = 0;
                
        //
        // set timeout on the socket so the program does not
        // hang up
        //
        serverSocket.setSoTimeout(1000);
        
        //
        // main server loop
        //
        while (bListening){
        	
        	try {        		
        		//
        		// trying to listen to the socket to accept
        		// clients
        		// if there is nobody to connect, exception will be
        		// thrown - set by setSoTimeout()
        		//
        		client_sockets[s_count]=serverSocket.accept();
        		
        		//
        		// connection got accepted
        		//
        		
        		if (client_sockets[s_count]!=null){
        		
        			System.out.println("Connection from " + client_sockets[s_count].getInetAddress() + " accepted.");
        			        			
        			System.out.println("accepted client");
        			s_out[s_count] = new PrintWriter(client_sockets[s_count].getOutputStream(),true);
        			s_in[s_count] = new BufferedReader(new InputStreamReader(client_sockets[s_count].getInputStream()));
					
        			clientID[s_count] = s_count+1;		
        			
					address[s_count] = "00000000"+Integer.toBinaryString(clientID[s_count]);
					int len = address[s_count].length();					
					address[s_count] = address[s_count].substring(len-8);				
					        			
        			System.out.println("client address: " + address[s_count]);
        			
        			// send client address to the new client
        			s_out[s_count].println(address[s_count]);
        			
        			//
                	// initialization
                	// 
        			
        			// ===========================================================
        			// insert codes here to send SNRM message
        			//
					String  frame = new HdlcFrame("", address[s_count], new UFrameControlData(UFrameControlData.TYPE_SNRM)).toString();
					s_out[s_count].println(frame);
        			System.out.println("Sent SNRM to station " + clientID[s_count] + " ... " + frame);
            		// ===============================================================
        			
            		// recv UA message
            		inputLine = s_in[s_count].readLine();
            		response = inputLine.substring(16, 24);
            		System.out.println(inputLine + "---" + response);
            		if(response.equals("11000110") || response.equals("11001110")) {
            			System.out.println("Received UA from station " + clientID[s_count]);
            		}
            		else {
            			System.out.println("UA error -- station " + clientID[s_count]);
            		}       
        			
            		// initialize ns and nr
            		ns[s_count] = -1;
            		nr[s_count] = 0;
            		
            		//            		 
        			// increment count of clients
        			//
        			s_count++;
        			nClient = s_count;
        			bAlive = true;
        		}
        	}
        	catch (InterruptedIOException e) {} catch (InvalidArgumentException e) {
				e.printStackTrace();
			}

			for (i=0;i<s_count;i++) {

        		// ==============================================================
        		// insert codes here to send “RR,*,P” msg
				if (s_out[i] != null) {
					ns[i]++;
					String frame = new HdlcFrame("", address[i], new SFrameControlData(ns[i], true, SFrameControlData.TYPE_RR)).toString();
					s_out[i].println(frame);

					System.out.println("Sent < RR,*,P > to station " + clientID[i] + "..." + frame);
				}

        		// ==============================================================
        		
        		
        		// recv response from the client
        		inputLine = s_in[i].readLine();
        		
        		if(inputLine != null) {		
        		
        			// get control field of the response frame
        			response = inputLine.substring(16, 24);
					System.out.println(response);
        		
        			if(response.substring(0,4).equals("1000")) {
        				// recv “RR,*,F”, no data to send from B
        				System.out.println("Receive RR, *, F from station " + clientID[i]);
        			}
        			else if(response.substring(0, 1).equals("0")) {
						String addr = inputLine.substring(8, 16);
                		// ==============================================================
        				// insert codes here to handle the frame “I, *, *” received

						if ("00000000".equals(addr)) {
							//if the frame is to the primary station; consume it
							String data = inputLine.substring(24, inputLine.length() - 24);
							String decoded = StringUtil.binaryDecode(data);
							System.out.println("Consuming " + data + " -- " + decoded);
						} else {
							//if the frame is to the secondary station; buffer the frame to send
							System.out.println("Relaying message " + inputLine);
							sMessages[nMsg] = inputLine;
							nMsg++;
						}
                		// ==============================================================
        			}
        		}
        	}
        	
    		// ==============================================================
        	// insert codes here to send frames in the buffer
			for (int k = 0; k < nMsg; k++) {
				String addr = sMessages[k].substring(8,16);
				int j = 0;
				for (String ad : address) {
					if (ad.equals(addr)) {
						s_out[j].println(sMessages[k]);
						System.out.println("Relaying message to " + addr + " - " + sMessages[k]);
						sMessages[k] = null;
						break;
					}
					j++;
				}
				if (sMessages[k] != null) {
					System.out.println("Error relaying message to " + addr + " no such client " + sMessages[k]);
				}
			}
        	        		
        	// send I frame
        		
    		// ==============================================================
			
		//
		// stop server automatically when
		// all clients disconnect
		//
		// no active clients
		//
			if (!bAlive && s_count > 0){
				System.out.println("All clients are disconnected - stopping");
				bListening = false;
			}
			
		}// end of while loop
		
		//
		// close all sockets
		//
		
		for (i=0;i<s_count;i++){
			client_sockets[i].close();
		}
        
        serverSocket.close();
        
    }// end main 
}// end of class PrimaryStation
