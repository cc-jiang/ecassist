package com.cc.ecassist.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件工具类
 *
 * @author congcong.jiang
 * @date 2022/11/16 10:42
 */
@Slf4j
public class FileUtils extends org.apache.commons.io.FileUtils {

    /**
     * zip压缩文件夹下的文件
     * @param sourcePath
     * @param zipFilePath
     */
    public static void zipDirectory(String sourcePath, String zipFilePath) {
        try {
            FileOutputStream fos = new FileOutputStream(zipFilePath);
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            File fileToZip = new File(sourcePath);

            // 压缩文件或目录
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, childFile.getName(), zipOut);
            }

            zipOut.close();
            fos.close();
            System.out.println("文件已压缩至 " + zipFilePath);
        } catch (IOException e) {
            log.error("压缩失败", e);
            throw new RuntimeException(e);
        }
    }

    public static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
            }
            zipOut.closeEntry();
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }

        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    /**
     * 删除文件夹
     * @param path
     */
    public static void deleteDir(String path) {
        File file = new File(path);
        try {
            deleteDirectory(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 新建文件夹
     * @param path
     */
    public static void createDir(String path) {
        File file = new File(path);
        file.mkdirs();
    }

    public static void createAndWriteFile(String path, String content) throws IOException {
        createAndWriteFile(path, content, false);
    }

    public static void createAndWriteFile(String path, String content, boolean append) throws IOException {
        File fileRes = new File(path);
        if (!fileRes.exists()) {
            fileRes.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(fileRes, append);
        fileWriter.write(content);
        fileWriter.close();
    }

    public static void createExcel(String path, Workbook workbook) throws IOException {
        File fileRes = new File(path);
        if (!fileRes.exists()) {
            fileRes.createNewFile();
        }
        try (FileOutputStream outputStream = new FileOutputStream(fileRes)) {
            workbook.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Scanner getScanner(String path) throws FileNotFoundException {
        File file = new File(path);
        return new Scanner(file);
    }
}
