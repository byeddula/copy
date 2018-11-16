
package com.vanguard.copyfiles;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FileUtils {
    
    private static Pattern pattern = Pattern.compile("_201702(01|02|03|04|05|06|07)_");
    
    /**
     * Reads the file contents from the folder
     * @param sftp Secure File Transfer Protocol object
     * @param path Path of the folder
     * @return Vector of LsEntry objects
     * @throws SftpException 
     */
    protected Vector<ChannelSftp.LsEntry> getFiles(ChannelSftp sftp, String path) throws SftpException{
        return sftp.ls(path);
    }
    /**
     * Iterates over individual LsEntry objects and finds whether a given entry
     * is Dir or File. If it is a file copy it else iterate over its sub directories to get all the files.
     * @param entries Vector of LEntry objects
     * @param sftp Secure File Transfer Protocol object
     * @param sourcePath Source path to get file contents.
     * @throws SftpException 
     */
    protected void checkFiles(Vector<ChannelSftp.LsEntry> entries, ChannelSftp sftp, String sourcePath, String destPath) throws SftpException{
 
            for(ChannelSftp.LsEntry entry: entries){
                if(entry.getAttrs().isReg() && validateFileName(entry.getFilename())){
                	String destination = destPath + File.separator + entry.getFilename();
                    File file = new File(destination);
                        Path path = file.toPath();
                        System.out.println(destination);
                        sftp.get(buildPath(sourcePath, entry.getFilename()),path.toString());
                 }else if(entry.getAttrs().isDir() && !entry.getFilename().equals(".") && !entry.getFilename().equals("..")) {
                    checkFiles(getFiles(sftp, buildPath(sourcePath, entry.getFilename())), sftp, buildPath(sourcePath, entry.getFilename()),destPath);
                }
                    
            }
    
    }
   
    /**
     * Forms a canonical path fro a given path and fileName
     * @param source Source path
     * @param fileName Name of the File or Dir 
     * @return Absolute path 
     */
    protected String buildPath(String source, String fileName){ 
      return (source !=null && source.equals("/")) ? fileName : (source +"/"+fileName);
    
    }
    
    /**
     * @param fileName
     * @return True -> If the fileName matches the pattern.
     */
    protected boolean validateFileName(String fileName){
       Matcher matcher = pattern.matcher(fileName);
       return true;
    }
    
    
}