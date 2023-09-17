package com.cc.ecassist.utils;

import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * 文件工具类
 *
 * @author congcong.jiang
 * @date 2022/11/16 10:42
 */
public class FileUtils {

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
