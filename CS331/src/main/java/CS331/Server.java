/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CS331;
import java.io.*;
import java.net.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author janet
 */
public class Server { 

    public static void main(String agrs[])
    {

        try {
            ServerSocket mySocket = new ServerSocket(6666);
            Socket connectedClient = mySocket.accept();            
            BufferedReader br = new BufferedReader(new InputStreamReader(connectedClient.getInputStream()));
            PrintStream ps = new PrintStream(connectedClient.getOutputStream());
            
            ArrayList<String> clients = new ArrayList<>();

            String inputData = "";
             while (!(inputData = br.readLine()).equals("exit")) {   
                System.out.println("inside while loop! " + inputData);
                if(inputData.startsWith("NewClient"))
                {
 
                    String clientID = inputData.split(" ")[1];
                    if(clients.contains(clientID))
                    {
                        ps.println(clientID + " already exists.");
                    }
                    else
                    {
                        clients.add(clientID);
                        ps.println("Welcome to our file system, you have been added as a new client.");

                    }
                }
                
                else if(inputData.startsWith("CreateDirectory"))
                {
                    String newDirectory = inputData;
                    String client = newDirectory.split("[ ;]")[1]; 
                    String directoryName = newDirectory.split(" ")[2]; 

                    if(clients.contains(client))
                    {
                        try {
                            Path directory = Paths.get(directoryName);
                            Files.createDirectories(directory); 
                            ps.println("The directory is successfully created."); 

                        } catch (IOException e) {
                            System.out.println("Failed to create directory! " + e); 
                        }
                    }
                    else
                    {
                        ps.println("This " + client + " does not exist."); 

                    }
                }
                
                else if(inputData.startsWith("MoveFile"))
                {
                    String client = inputData.split("[ ;]")[1]; 
                    String directoryName = inputData.split("[ ;]")[2]; 
                    String file = inputData.split(" ")[3];
          
                    Path D = Paths.get(directoryName);
                    Path newDirectory = D.toAbsolutePath();
                    Path f = Paths.get(file);
                    Path currentDir = f.toAbsolutePath();
                    if(clients.contains(client))
                    {
                        try {                    

                            if(Files.exists(newDirectory))
                            {
                                Files.move(currentDir, newDirectory.resolve(currentDir.getFileName())); 
                                ps.println("The file is successfully moved under your directory.");
                                
                            }
                            else
                            {
                                ps.println("The directory does not exist for this client."); 
                            } 
                          
                        } catch (IOException e) {
                            System.out.println("Failed to move file! " + e); 
                        }
                    }
                  
                    else
                    {
                        ps.println("This " + client + " does not exist."); 

                    }

                }
                else if(inputData.startsWith("DeleteFile"))
                {
                    String client = inputData.split("[ ;]")[1]; 
                    String directoryName = inputData.split("[ ;]")[2]; 
                    String file = inputData.split(" ")[3];
      
                    Path dir = Paths.get(directoryName);
                    Path f = dir.resolve(file);
                    
                    if(clients.contains(client))
                    {
                        try{
                            if((Files.exists(dir)))
                            {
                                Files.delete(f);
                                ps.println("The file was successfully removed from this directory.");
                            }
                            else
                            {
                           ps.println("The directory does not exist for this client."); 
                            }
                                   
                        } catch (IOException e) {
                            System.out.println("Failed to delete file! " + e); 
                        }
                    
                    }
                    else
                    {
                        ps.println("This " + client + " does not exist."); 

                    }

                }
                else
                {
                    ps.println("Unsupported Command");
                } 
            }
            
            ps.println("Thanks for using our remote file system.");
            ps.close();
            br.close();
            mySocket.close();
            connectedClient.close();
        } catch (IOException exc) {
            System.out.println("Error :" + exc.toString());
            
        }
    }
    
}
