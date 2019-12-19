package com.zone.util.file;

import java.io.*;

/**
 * @ClassName FileUtil
 * @Author zone
 * @Date 2018/12/29  23:19
 * @Version 1.0
 * @Description
 */
public class FileUtil {

    public static void uploadFile(byte[] file,String filePath,String fileName) throws IOException {
        File targetFile=new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    public static void writeFile(String data){
        byte[] sourceData=data.getBytes();
        String filePath="E:/Temp/";
        String fileName="text.txt";
        if(sourceData!=null){
            try {
            File file=new File(filePath+fileName);
            if(!file.exists()) {//文件不存在则创建文件，先创建目录
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
                FileOutputStream fileOutputStream=new FileOutputStream(file);//文件输出流将数据写入文件
                fileOutputStream.write(sourceData);
                System.out.println("保存文件成功");
                fileOutputStream.close();
            }
                 catch (Exception e) {
                    e.printStackTrace();
                }

        }
    }
    public static String readFile(String filepath){
        StringBuilder stringBuilder=new StringBuilder();
        try {
            FileReader reader=new FileReader(filepath);
            BufferedReader br=new BufferedReader(reader);
            String line;
            //网友推荐更加简洁的写法
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
                //stringBuilder.append(line+"\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
