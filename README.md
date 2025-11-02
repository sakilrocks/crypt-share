# crypt-share

It is a command line application for secure, encrypted file transfers between a server and multiple clients.
It uses AES encryption, multi-threaded networking, and integrity verification to ensure your files are transmitted safely and efficiently.

---

## Features

- AES-256 Encryption – All files are encrypted before transfer
- Multi-client Support – The server can handle multiple clients concurrently using threads
- Integrity Check – SHA-256 verification after transfer
- Progress Display – Real time CLI progress feedback

---

## Security

- AES/ECB/PKCS5Padding for symmetric encryption
- Secure random key generation
- Optional public/private key setup for key exchange
- SHA-256 for file integrity verification

---

## Project Structure

```
crypt-share/
├── src/
│   ├── server/
│   │   └── FileServer.java
│   ├── client/
│   │   └── FileClient.java
│   ├── crypto/
│   │   └── AESUtils.java
│   └── utils/
│       └── HashUtils.java
└──  README.md
```

---

## How It Works

1. The server listens on a specified port for incoming connections
2. The client connects to the server, encrypts the file using AES, and starts transferring it
3. The server decrypts and saves the file on its end
4. Both ends verify file integrity using SHA-256 hashing

---

## Usage

- Start the Server
```
javac src/server/FileServer.java
java server.FileServer 5000
```

- Send a File (Client)
```
javac src/client/FileClient.java
java client.FileClient 127.0.0.1 5000 myfile.txt
```
