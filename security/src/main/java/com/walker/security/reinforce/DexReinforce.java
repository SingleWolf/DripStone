package com.walker.security.reinforce;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;


public class DexReinforce {

    public static void main(String[] args) throws Exception {

    	byte[] mainDexData; //�洢Դapk�е�Դdex�ļ�
    	byte[] aarData;     // �洢���еĿ�dex�ļ�
    	byte[] mergeDex;    // �洢��dex ��Դdex �ĺϲ�����dex�ļ�

    	File tempFileApk = new File("security/source/apk/temp");
    	if (tempFileApk.exists()) {
			File[]files = tempFileApk.listFiles();
			for(File file: files){
				if (file.isFile()) {
					file.delete();
				}
			}
		}

    	File tempFileAar = new File("security/source/aar/temp");
    	if (tempFileAar.exists()) {
    		File[]files = tempFileAar.listFiles();
			for(File file: files){
				if (file.isFile()) {
					file.delete();
				}
			}
		}

        /**
         * ��һ�� ����ԭʼapk ����dex
         *
         */
        AES.init(AES.DEFAULT_PWD);
        //��ѹapk
        File apkFile = new File("security/source/apk/app-release.apk");
        File newApkFile = new File(apkFile.getParent() + File.separator + "temp");
        if(!newApkFile.exists()) {
        	newApkFile.mkdirs();
        }
        File mainDexFile = AES.encryptAPKFile(apkFile,newApkFile);
        if (newApkFile.isDirectory()) {
			File[] listFiles = newApkFile.listFiles();
			for (File file : listFiles) {
				if (file.isFile()) {
					if (file.getName().endsWith(".dex")) {
						String name = file.getName();
						System.out.println("rename step1:"+name);
						int cursor = name.indexOf(".dex");
						String newName = file.getParent()+ File.separator + name.substring(0, cursor) + "_" + ".dex";
						System.out.println("rename step2:"+newName);
						file.renameTo(new File(newName));
					}
				}
			}
		}


    	 /**
         * �ڶ��� ����aar ��ÿ�dex
         */
    	File aarFile = new File("security/source/aar/shell.aar");
        File aarDex  = Dx.jar2Dex(aarFile);
//        aarData = Utils.getBytes(aarDex);   //��dex�ļ�����byte ����

        File tempMainDex = new File(newApkFile.getPath() + File.separator + "classes.dex");
        if (!tempMainDex.exists()) {
			tempMainDex.createNewFile();
		}
//        System.out.println("MyMain" + tempMainDex.getAbsolutePath());
        FileOutputStream fos = new FileOutputStream(tempMainDex);
        byte[] fbytes = Utils.getBytes(aarDex);
        fos.write(fbytes);
        fos.flush();
        fos.close();


        /**
         * ��3�� ���ǩ��
         */
        File unsignedApk = new File("security/result/apk-unsigned.apk");
        unsignedApk.getParentFile().mkdirs();
//        File disFile = new File(apkFile.getAbsolutePath() + File.separator+ "temp");
        Zip.zip(newApkFile, unsignedApk);
        //���ò���Ͳ����Զ�ʹ��ԭapk��ǩ��...
        File signedApk = new File("security/result/apk-signed.apk");
        Signature.signature(unsignedApk, signedApk);
    }


    private static File getMainDexFile(File apkFile) {
        File disFile = new File(apkFile.getAbsolutePath() + "unzip");
        Zip.unZip(apkFile, disFile);
        File[] files = disFile.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith(".dex")) {
                    return true;
                }
                return false;
            }
        });
        for (File file : files) {
            if (file.getName().endsWith("classes.dex")) {
                return file;
            }
        }
        return null;
    }
}
