package com.example.cardealer.service;

import com.example.cardealer.entity.auth.Dealer;
import com.example.cardealer.exceptions.file.FtpConnectionException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class FtpService {

    @Value("${ftp.server}")
    private String FTP_SERVER;
    @Value("${ftp.username}")
    private String FTP_USERNAME;
    @Value("${ftp.password}")
    private String FTP_PASSWORD;
    @Value("${ftp.origin}")
    private String FTP_ORIGIN_DIRECTORY;
    @Value("${ftp.port}")
    private int FTP_PORT;

    private FTPClient getFtpConnection() throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(FTP_SERVER, FTP_PORT);
        ftpClient.login(FTP_USERNAME, FTP_PASSWORD);

        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        return ftpClient;
    }

    public void uploadFileToFtp(MultipartFile file, Dealer dealer, String nextId) throws FtpConnectionException, IOException {
        FTPClient ftpClient = null;
        try {
            ftpClient = getFtpConnection();
            String remoteFilePath = FTP_ORIGIN_DIRECTORY + "/" + dealer.getId() + "/" + nextId + "/" + file.getOriginalFilename();
            boolean uploaded = streamFile(file,ftpClient,remoteFilePath);
            if (!uploaded) {
                createFolder(ftpClient, dealer, nextId);
                boolean folderCreated = ftpClient.changeWorkingDirectory(FTP_ORIGIN_DIRECTORY + "/" + dealer.getId() + "/" + nextId);
                if (folderCreated) {
                    uploaded = streamFile(file, ftpClient, remoteFilePath);
                    if (!uploaded) {
                        throw new FtpConnectionException("Cannot upload file on ftp server");
                    }
                } else {
                    throw new FtpConnectionException("Cannot create directory on ftp server");
                }
            }
        } catch (IOException e) {
            throw new FtpConnectionException(e);
        } finally {
            if (ftpClient != null && ftpClient.isConnected()) {
                try {
                    ftpClient.logout();
                    ftpClient.disconnect();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void createFolder(FTPClient client, Dealer dealer, String nextId) throws IOException {
        client.makeDirectory(FTP_ORIGIN_DIRECTORY + "/" + dealer.getId());
        client.makeDirectory(FTP_ORIGIN_DIRECTORY + "/" + dealer.getId() + "/" + nextId);
    }

    private boolean streamFile(MultipartFile file,FTPClient ftpClient,String remoteFilePath) throws IOException {
        InputStream inputStream = file.getInputStream();
        boolean uploaded = ftpClient.storeFile(remoteFilePath, inputStream);
        inputStream.close();
        return uploaded;
    }

    public boolean deleteFile(String path) throws IOException {
        FTPClient ftpClient = getFtpConnection();
        boolean deleted = ftpClient.deleteFile(FTP_ORIGIN_DIRECTORY + "/" + path);
        ftpClient.logout();
        ftpClient.disconnect();
        return deleted;
    }
}
