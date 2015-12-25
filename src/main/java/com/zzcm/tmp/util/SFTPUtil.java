package com.zzcm.tmp.util;

import com.jcraft.jsch.*;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by Administrator on 2015/12/23.
 */
public class SFTPUtil {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SFTPUtil.class);

    private static final int BUFFER = 8192;

    public static void download(String host,String user,String pwd,String srcDir,String srcFile,String dst){
        ChannelSftp channelSftp = null;
        try {
            channelSftp = connectSFTP(host,user,pwd,null,null,22);
            download(srcDir,srcFile,dst,channelSftp);
        }finally {
            disconnected(channelSftp);
        }
    }

    public static void downloadZip(String host,String user,String pwd,String srcDir,String srcFile,String dst){
        ChannelSftp channelSftp = null;
        try {
            channelSftp = connectSFTP(host,user,pwd,null,null,22);
            downloadZip(srcDir, srcFile, dst, channelSftp);
        }finally {
            disconnected(channelSftp);
        }
    }

    public static void downloadGZip(String host,String user,String pwd,String srcDir,String srcFile,String dst){
        ChannelSftp channelSftp = null;
        try {
            channelSftp = connectSFTP(host,user,pwd,null,null,22);
            downloadGZip(srcDir, srcFile, dst, channelSftp);
        }finally {
            disconnected(channelSftp);
        }
    }

    /**
     * 获取连接
     * @return channel
     */
    private static ChannelSftp connectSFTP(String host,String username,String password,String privateKey,String passphrase,int port) {
        JSch jsch = new JSch();
        Channel channel = null;
        try {
            if (privateKey!= null && !"".equals(privateKey)) {
                //使用密钥验证方式，密钥可以使有口令的密钥，也可以是没有口令的密钥
                if (passphrase != null && "".equals(passphrase)) {
                    jsch.addIdentity(privateKey, passphrase);
                } else {
                    jsch.addIdentity(privateKey);
                }
            }
            Session session = jsch.getSession(username, host, port);
            if (password != null && !"".equals(password)) {
                session.setPassword(password);
            }
            //具体config中需要配置那些内容，请参照sshd服务器的配置文件/etc/ssh/sshd_config的配置
            Properties sshConfig = new Properties();
            //设置不用检查hostKey
            //如果设置成“yes”，ssh就不会自动把计算机的密匙加入“$HOME/.ssh/known_hosts”文件，
            //并且一旦计算机的密匙发生了变化，就拒绝连接。
            sshConfig.put("StrictHostKeyChecking", "no");// do not verify host key
            //UseDNS指定，sshd的是否应该看远程主机名，检查解析主机名的远程IP地址映射到相同的IP地址。
            //默认值是 “yes” 此处是由于我们SFTP服务器的DNS解析有问题，则把UseDNS设置为“no”
            //sshConfig.put("UseDNS", "no");
            session.setConfig(sshConfig);
            // session.setTimeout(timeout);
            session.setServerAliveInterval(92000);
            session.connect();
            //参数sftp指明要打开的连接是sftp连接
            channel = session.openChannel("sftp");
            channel.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return (ChannelSftp) channel;
    }

    /**
     * 上传文件
     *
     * @param directory
     *            上传的目录
     * @param uploadFile
     *            要上传的文件
     * @param sftp
     */
    private void upload(String directory, String uploadFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            File file = new File(uploadFile);
            sftp.put(new FileInputStream(file), file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件
     *
     * @param directory
     *            下载目录
     * @param downloadFile
     *            下载的文件
     * @param saveFile
     *            存在本地的路径
     * @param sftp
     */
    private static void download(String directory, String downloadFile,
                                String saveFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            sftp.get(downloadFile,saveFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件
     *
     * @param directory
     *            下载目录
     * @param downloadFile
     *            下载的文件
     * @param saveFile
     *            存在本地的路径
     * @param sftp
     */
    private static void downloadGZip(String directory, String downloadFile,
                                    String saveFile, ChannelSftp sftp) {
        InputStream inputStream = null;
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            sftp.cd(directory);
            inputStream = sftp.get(downloadFile);
            File saveDir = new File(saveFile);
            if(!saveDir.exists()){
                saveDir.mkdirs();
            }
            File file = new File(saveDir,downloadFile+".gz");
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();

            bos = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
            bis = new BufferedInputStream(inputStream);
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                bos.write(data, 0, count);
            }
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(bis);
            close(inputStream);
            close(bos);
        }
    }

    /**
     * 下载文件
     *
     * @param directory
     *            下载目录
     * @param downloadFile
     *            下载的文件
     * @param saveFile
     *            存在本地的路径
     * @param sftp
     */
    private static void downloadZip(String directory, String downloadFile,
                                String saveFile, ChannelSftp sftp) {
        InputStream inputStream = null;
        FileOutputStream fos = null;
        CheckedOutputStream cos = null;
        ZipOutputStream zos = null;
        BufferedInputStream bis = null;
        try {
            sftp.cd(directory);
            inputStream = sftp.get(downloadFile);
            File saveDir = new File(saveFile);
            if(!saveDir.exists()){
                saveDir.mkdirs();
            }
            File file = new File(saveDir,downloadFile+".zip");
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();

            fos = new FileOutputStream(file);
            cos = new CheckedOutputStream(fos,new CRC32());
            zos = new ZipOutputStream(cos);

            bis = new BufferedInputStream(inputStream);
            ZipEntry entry = new ZipEntry(file.getName());
            zos.putNextEntry(entry);
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                zos.write(data, 0, count);
            }
            zos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(zos);
            close(cos);
            close(fos);
            close(bis);
            close(inputStream);
        }
    }

    private static void close(InputStream in){
        if(null!=in){
            try {
                in.close();
                in = null;
            } catch (IOException e) {
            }
        }
    }

    private static void close(OutputStream out){
        if(null!=out){
            try {
                out.close();
                out = null;
            } catch (IOException e) {
            }
        }
    }


    /**
     * 删除文件
     *
     * @param directory
     *            要删除文件所在目录
     * @param deleteFile
     *            要删除的文件
     * @param sftp
     */
    private void delete(String directory, String deleteFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void disconnected(ChannelSftp sftp){
        if (sftp != null) {
            try {
                sftp.getSession().disconnect();
            } catch (JSchException e) {
                e.printStackTrace();
            }
            sftp.disconnect();
        }
    }
}
