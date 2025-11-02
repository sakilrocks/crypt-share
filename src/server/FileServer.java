
package server;

import crypto.AESUtils;
import utils.HashUtils;
import javax.crypto.SecretKey;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;



public class FileServer {
    private static final String PASSWORD = "SuperSecretKey";         // shld match client   

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("[usage]: java server.FileServer <port>");
            return;
        }

        int port = Integer.parseInt(args[0]);
        ExecutorService executor = Executors.newFixedThreadPool(4);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("CryptShare started on port: " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                executor.submit(() -> handleClient(socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }


    private static void handleClient(Socket socket) {
        try (DataInputStream dis = new DataInputStream(socket.getInputStream());
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {

            String fileName = dis.readUTF();
            long fileSize = dis.readLong();

            System.out.println("[receiving]: " + fileName + " (" + fileSize + " bytes)");

            File outputFile = new File("received_" + fileName);
            SecretKey key = AESUtils.generateKey(PASSWORD);

            AESUtils.decryptFile(key, dis, outputFile);

            String hash = HashUtils.computeSHA256(outputFile.getAbsolutePath());
            dos.writeUTF(hash);

            System.out.println("[received]: " + outputFile.getName());
            System.out.println("SHA256: " + hash);
        } catch (Exception e) {
            System.err.println("[error]: " + e.getMessage());
        }
    }

}