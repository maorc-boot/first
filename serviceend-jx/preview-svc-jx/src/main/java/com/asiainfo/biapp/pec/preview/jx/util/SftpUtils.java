package com.asiainfo.biapp.pec.preview.jx.util;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

@Slf4j
public class SftpUtils {

    /**
     * 连接sftp服务器
     *
     * @param host     主机
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public ChannelSftp connect(String host, int port, String username,
                               String password,String encoding) {
        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
            Session sshSession = jsch.getSession(username, host, port);
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            sftp = (ChannelSftp) sshSession.openChannel("sftp");
            sftp.connect();
            if (StringUtils.isNotBlank(encoding)){
                sftp.setFilenameEncoding(encoding);
            }
        } catch (Exception e) {
            log.error("连接sftp异常" , e);
        }
        return sftp;
    }

    /**
     * 上传文件
     *
     * @param directory  上传的目录
     * @param uploadFile 要上传的文件
     * @param sftp
     */
    public boolean upload(String directory, String uploadFile, ChannelSftp sftp) {
        FileInputStream fis=null;
        try {
            sftp.cd(directory);
            File file = new File(uploadFile);
            fis=new FileInputStream(file);
            sftp.put(fis, file.getName());
        } catch (Exception e) {
            log.error("error on upload!", e);
            return false;
        }finally {
            IOUtils.closeQuietly(fis);
        }
        return true;
    }

    /**
     * 批量上传文件
     *
     * @param directory  上传的目录
     * @param files 要上传的文件
     * @param sftp
     */
    public boolean BuchUpload(String directory,  List<String> files, ChannelSftp sftp) {
        FileInputStream fis=null;
        try {
            sftp.cd(directory);
            for (String f : files) {
                File file = new File(f);
                fis=new FileInputStream(file);
                sftp.put(fis, file.getName());
            }
        } catch (Exception e) {
            log.error("error on upload!", e);
            return false;
        }finally {
            IOUtils.closeQuietly(fis);
        }
        return true;
    }

    /**
     * 获取本地文件夹下所有文件名
     * @param path
     * @return
     */
    public List<String> getFileName(String path) {
        File f = new File(path);//获取路径
        if (!f.exists()) {
            System.out.println(path + " not exists");//不存在就输出
            return null;
        }
        List<String> files = new ArrayList<String>();
        File fa[] = f.listFiles();//用数组接收
        for (int i = 0; i < fa.length; i++) {//循环遍历
            File fs = fa[i];//获取数组中的第i个
            if (fs.isFile()) {
                files.add(path+"\\"+fs.getName());//如果是目录就输出
            }
        }
        return files;
    }
    /**
     * 批量下载文件
     *
     * @param directory    下载目录
     * @param files 下载的文件
     * @param saveFile     存在本地的路径
     * @param sftp
     */
    public boolean buchDownload(String directory,List<String> files,
                            String saveFile, ChannelSftp sftp) {
        boolean flag = true;
        FileOutputStream out = null;
        try {
            sftp.cd(directory);
            File file = new File(saveFile);
            for (String s : files) {
                out = new FileOutputStream(file+"/"+s);
                sftp.get(s, out);
            }
        } catch (Exception e) {
            flag = false;
            log.error("下载文件异常", e);
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("close error", e);
                }
            }
        }
        return flag;
    }

    /**
     * 获取目标文件夹所有文件名
     * @param ftpath
     * @param sftp
     * @param endsWith
     * @return
     * @throws SftpException
     * @throws UnsupportedEncodingException
     */
    public ArrayList<String> listFiles(String ftpath, ChannelSftp sftp,String endsWith) throws SftpException, UnsupportedEncodingException {
        ArrayList<String> files = new ArrayList<String>();
        sftp.cd(ftpath);
        Vector<String> lss = sftp.ls("*");
        for (int i = 0; i < lss.size(); i++) {
            Object obj = lss.elementAt(i);
            if (obj instanceof ChannelSftp.LsEntry) {
                ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) obj;

                if (true && !entry.getAttrs().isDir()) {
                    String st = new String(entry.getFilename().getBytes(), "UTF-8");
                    if (StringUtils.isNotBlank(endsWith) && st.endsWith(endsWith)) {//获取后缀为endsWith的某一类文件
                        files.add(entry.getFilename());
                    }
                    if (true && entry.getAttrs().isDir()) {
                        if (!entry.getFilename().equals(".") && !entry.getFilename().equals("..")) {
                            files.add(entry.getFilename());
                        }
                    }
                }
            }
        }
        return files;
    }

    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件
     * @param saveFile     存在本地的路径
     * @param sftp
     */
    public boolean download(String directory, String downloadFile,
                            String saveFile, ChannelSftp sftp) {
        boolean flag = true;
        FileOutputStream out = null;
        try {
            sftp.cd(directory);
            File file = new File(saveFile);
            out = new FileOutputStream(file);
            sftp.get(downloadFile, out);
        } catch (Exception e) {
            flag = false;
            log.error("下载文件异常", e);
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("close error", e);
                }
            }
        }
        return flag;
    }

    /**
     * 删除文件
     *
     * @param directory  要删除文件所在目录
     * @param deleteFile 要删除的文件
     * @param sftp
     */
    public void delete(String directory, String deleteFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
        } catch (Exception e) {
            System.out.println("删除文件异常" + e);
        }
    }

    /**
     * 删除文件夹
     * param folderPath 文件夹完整绝对路径
     * @param folderPath
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除指定文件夹下所有文件
     * @param path 文件夹完整绝对路径
     * @return
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
               // delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    /**
     * Description:断开FTP连接 <br>
     * @throws JSchException 
     */
    public void disconnect(ChannelSftp sftp) throws JSchException {
        if (null != sftp.getSession()) {
            sftp.getSession().disconnect();
        }
        if (null != sftp) {
            sftp.disconnect();
        }
    }

    /**
     * 判断sftp目录是否存在
     *
     * @param sftpClient
     * @param dir
     * @return
     */
    public boolean isDirExist(ChannelSftp sftpClient, String dir) {
        try {
            sftpClient.cd(dir);
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    
}