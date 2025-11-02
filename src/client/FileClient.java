
package client;

import crypto.AESUtils;
import utils.HashUtils;
import javax.crypto.SecretKey;
import java.io.*;
import java.net.*;



public class FileClient {
    private static final String PASSWORD = "SuperSecretKey";        // shld match server

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("[usage]: java client.FileClient <server_ip> <port> <file_path>");
            return;
        }


        String serverIp = args[0];
        int port = Integer.parseInt(args[1]);
        String filePath = args[2];
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("[error]: file not found: " + filePath);
            return;
        }

        try (Socket socket = new Socket(serverIp, port);
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
             DataInputStream dis = new DataInputStream(socket.getInputStream())) {

            dos.writeUTF(file.getName());
            dos.writeLong(file.length());

            SecretKey key = AESUtils.generateKey(PASSWORD);
            AESUtils.encryptFile(key, file, dos);

            String serverHash = dis.readUTF();
            String localHash = HashUtils.computeSHA256(filePath);

            System.out.println("file sent successfully.");
            System.out.println("server SHA256: " + serverHash);
            System.out.println("local SHA256: " + localHash);
            System.out.println(localHash.equals(serverHash) ? "integrity verified" : "hash mismatch");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
