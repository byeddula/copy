/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vanguard.copyfiles;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Vector;
import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import javax.swing.text.DefaultEditorKit;
        
        
public class ExecuteMain {
    
    public static void main(String[] args) throws Exception {

    String path = "/";
    String destPath = "/mnt";
    Session session = null;
    ChannelSftp sftp = null;
    try{
    JSch jsch = new JSch();
    // hostname='test.rebex.net',username='demo',password=pass_att
    session = jsch.getSession("demo", "test.rebex.net");
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword("password");
        session.connect();
    sftp = (ChannelSftp)session.openChannel("sftp");
        sftp.connect();
    FileUtils utils = new FileUtils();
        utils.checkFiles(utils.getFiles(sftp, path), sftp, path, destPath);
    }catch(Exception ex){
     throw ex;
    }finally{
        sftp.disconnect();
        session.disconnect();
    }
    }
}