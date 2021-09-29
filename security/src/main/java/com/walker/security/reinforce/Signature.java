package com.walker.security.reinforce;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Signature {
    public static void signature(File unsignedApk, File signedApk) throws InterruptedException, IOException {
        String cmd[] = {"jarsigner",  "-sigalg", "MD5withRSA",
                "-digestalg", "SHA1",
                "-keystore", "/Users/walker/develop/Android/DripStone/keystore/drip.jks",
                "-storepass", "drip1234",
                "-keypass", "drip1234",
                "-signedjar", signedApk.getAbsolutePath(),
                unsignedApk.getAbsolutePath(),
                "drip"};
        Process process = Runtime.getRuntime().exec(cmd);
        System.out.println("start sign");
//        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//        String line;
//        while ((line = reader.readLine()) != null)
//            System.out.println("tasklist: " + line);
        try {
            int waitResult = process.waitFor();
            System.out.println("waitResult: " + waitResult);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        }
        System.out.println("process.exitValue() " + process.exitValue() );
        if (process.exitValue() != 0) {
        	InputStream inputStream = process.getErrorStream();
        	int len;
        	byte[] buffer = new byte[2048];
        	ByteArrayOutputStream bos = new ByteArrayOutputStream();
        	while((len=inputStream.read(buffer)) != -1){
        		bos.write(buffer,0,len);
        	}
        	System.out.println(new String(bos.toByteArray(),"utf-8"));
            throw new RuntimeException("Ç©ÃûÖ´ÐÐÊ§°Ü");
        }
        System.out.println("finish signed");
        process.destroy();
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        File unsignedApk = new File("/Users/walker/Desktop/jiagu/icbcoa_4.2.9_sec.apk");
        File signedApk = new File("/Users/walker/Desktop/jiagu/icbcoa_4.2.9.apk");

        String cmd[] = {"jarsigner",  "-sigalg", "MD5withRSA",
                "-digestalg", "SHA1",
                "-keystore", "/Users/walker/Desktop/jiagu/echat.jks",
                "-storepass", "ruaho1234",
                "-keypass", "ruaho1234",
                "-signedjar", signedApk.getAbsolutePath(),
                unsignedApk.getAbsolutePath(),
                "codesign"};
        Process process = Runtime.getRuntime().exec(cmd);
        System.out.println("start sign");
        try {
            int waitResult = process.waitFor();
            System.out.println("waitResult: " + waitResult);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        }
        System.out.println("process.exitValue() " + process.exitValue() );
        if (process.exitValue() != 0) {
            InputStream inputStream = process.getErrorStream();
            int len;
            byte[] buffer = new byte[2048];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while((len=inputStream.read(buffer)) != -1){
                bos.write(buffer,0,len);
            }
            System.out.println(new String(bos.toByteArray(),"utf-8"));
            throw new RuntimeException("Ç©ÃûÖ´ÐÐÊ§°Ü");
        }
        System.out.println("finish signed");
        process.destroy();
    }
}
