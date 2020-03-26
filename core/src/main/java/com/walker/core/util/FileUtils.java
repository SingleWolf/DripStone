package com.walker.core.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @date on 2018/4/17 0017 上午 10:57
 * @author Walker
 * @email feitianwumu@163.com
 * @desc  文件操作相关的工具类
 */
public class FileUtils {
    /**
     * 读取文件为字符串
     *
     * @param file 文件
     * @return 文件内容字符串
     * @throws IOException 读写异常
     */
    public static String onRead(File file) throws IOException {
        String text = null;
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            text = onRead(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return text;
    }

    /**
     * 读取输入流为字符串,最常见的是网络请求
     *
     * @param is 输入流
     * @return 输入流内容字符串
     * @throws IOException 读写异常
     */
    public static String onRead(InputStream is) throws IOException {
        StringBuffer strbuffer = new StringBuffer();
        String line;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is));
            while ((line = reader.readLine()) != null) {
                strbuffer.append(line).append("\r\n");
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return strbuffer.toString();
    }

    /**
     * 把字符串写入到文件中
     *
     * @param file 被写入的目标文件
     * @param str  要写入的字符串内容
     * @throws IOException 读写异常
     */
    public static void onWrite(File file, String str) throws IOException {
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(new FileOutputStream(file));
            out.write(str.getBytes());
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }


    /**
     * 删除文件或者文件夹，默认保留根目录
     *
     * @param directory 目标文件
     */
    public static void onDel(File directory) {
        onDel(directory, false);
    }

    /**
     * 删除文件或者文件夹
     *
     * @param directory 目标文件
     * @param keepRoot  是否保留根目录
     */
    public static void onDel(File directory, boolean keepRoot) {
        if (directory != null && directory.exists()) {
            if (directory.isDirectory()) {
                for (File subDirectory : directory.listFiles()) {
                    onDel(subDirectory, false);
                }
            }

            if (!keepRoot) {
                directory.delete();
            }
        }
    }

    /**
     * 重命名文件
     *
     * @param oldFile 原始文件
     * @param newFile 更名后的文件
     */

    public static void onRename(File oldFile, File newFile) {
        if (oldFile.exists()) {
            if (!newFile.exists()) {
                try {
                    newFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            oldFile.renameTo(newFile);
        }
    }

    /**
     * 解压文件
     *
     * @param zipFilePath 压缩文件路径
     * @param destPath    解压文件路径
     * @throws IOException 读写异常
     */
    public static void onUnzip(String zipFilePath, String destPath) throws IOException {
        // check or create dest folder
        File destFile = new File(destPath);
        if (!destFile.exists()) {
            destFile.mkdirs();
        }

        // start unzip
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry zipEntry;
        String zipEntryName;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            zipEntryName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                File folder = new File(destPath + File.separator + zipEntryName);
                folder.mkdirs();
            } else {
                File file = new File(destPath + File.separator + zipEntryName);
                if (file != null && !file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                while ((len = zipInputStream.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        zipInputStream.close();
    }

    /**
     * 压缩文件
     *
     * @param srcFileString 待压缩的文件夹或文件路径
     * @param zipFileString 压缩包的路径
     * @throws Exception 异常
     */
    public static void onZip(String srcFileString, String zipFileString)
            throws Exception {
        // create ZIP
        ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(
                zipFileString));
        // create the file
        File file = new File(srcFileString);
        // compress
        onZipFiles(file.getParent() + File.separator, file.getName(), outZip);
        // finish and close
        outZip.finish();
        outZip.close();
    }

    /**
     * 压缩文件
     *
     * @param folderString   文件目录
     * @param fileString     文件名
     * @param zipOutputSteam 压缩流
     * @throws Exception 异常
     */
    private static void onZipFiles(String folderString, String fileString,
                                   ZipOutputStream zipOutputSteam) throws Exception {
        if (zipOutputSteam == null) {
            return;
        }
        File file = new File(folderString + fileString);
        if (file.isFile()) {
            ZipEntry zipEntry = new ZipEntry(fileString);
            FileInputStream inputStream = new FileInputStream(file);
            zipOutputSteam.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[4096];
            while ((len = inputStream.read(buffer)) != -1) {
                zipOutputSteam.write(buffer, 0, len);
            }
            zipOutputSteam.closeEntry();
        } else {
            // folder
            String[] fileList = file.list();
            // no child file and compress
            if (fileList.length <= 0) {
                ZipEntry zipEntry = new ZipEntry(fileString + File.separator);
                zipOutputSteam.putNextEntry(zipEntry);
                zipOutputSteam.closeEntry();
            }
            // child files and recursion
            for (String element : fileList) {
                onZipFiles(folderString, fileString + File.separator + element, zipOutputSteam);
            }// end of for
        }
    }

}
