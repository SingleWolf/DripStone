package com.walker.core.log;

import android.content.Context;
import android.text.TextUtils;

import com.walker.core.store.sp.SPHelper;

import java.io.File;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author Walker
 * @Date 2020-03-26 15:13
 * @Summary 日志打印到文件
 */
public abstract class BaseLogger {
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    protected Context mContext;
    private String mLogDirPath;
    private long mMaxFileSize;

    private static final String FIRST_LOG_FILE_NAME_MAIN = "log1.txt";
    private static final String SECOND_LOG_FILE_NAME_MAIN = "log2.txt";
    private static final String SP_KEY_CUR_LOG_FILE_FLAG_MAIN = "cur_log_file_flag";

    private RandomAccessFile mLogRandomAccessFile;
    private static final long DEFAULT_MAX_FILE_LENGTH = 1 * 1024 * 1024; //单个文件限制大小
    private int CHECK_SIZE_TIMES = 10; //每写入10次，检查一下文件大小是否超出限制
    private String FIRST_FILE_NAME = FIRST_LOG_FILE_NAME_MAIN; //第一个日志文件的名称
    private String SECOND_FILE_NAME = SECOND_LOG_FILE_NAME_MAIN; //第二个日志文件的名称
    private String SP_KEY_CUR_LOG_FILE_FLAG = SP_KEY_CUR_LOG_FILE_FLAG_MAIN; //存储当前操作的文件标识的key
    private int curLogFileFlag = 0; //当前操作的文件标志位，0-log1.txt  1-log2.txt;
    private long curWriteTime = 0; //当前已写入次数

    private LinkedBlockingQueue<String> cacheList = new LinkedBlockingQueue<>();


    public BaseLogger(Context context) {
        mContext = context;
        init();
    }

    private void init() {
        mLogDirPath = getLogDirPath();
        mMaxFileSize = getMaxFileSize();
        if (TextUtils.isEmpty(mLogDirPath)) {
            mLogDirPath = mContext.getExternalFilesDir("DripLog").getAbsolutePath() + File.separator + "log";
        }
        if (mMaxFileSize < DEFAULT_MAX_FILE_LENGTH) {
            mMaxFileSize = DEFAULT_MAX_FILE_LENGTH;
        }
    }

    public void start() {
        LoggerTaskThread loggerTaskThread = new LoggerTaskThread();
        loggerTaskThread.setName("DripLogger");
        loggerTaskThread.start();
    }

    protected abstract String getLogDirPath();

    protected abstract long getMaxFileSize();

    private String getTime() {
        return mDateFormat.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 获取日志文件目录
     * 如果不存在，则创建目录
     */
    private File getDirFile(Context context) {
        if (context == null) {
            return null;
        }
        File file = new File(mLogDirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 删除日志文件
     */
    public void tryDeleteLog(Context context) {
        File oldFile = new File(mLogDirPath);
        if (oldFile != null && oldFile.exists()) {
            oldFile.deleteOnExit();
        }
    }

    public void writeDisk(String tag, String message, String level) {
        boolean ret = cacheList.offer(new StringBuilder().append("[")
                .append(getTime()).append(": ")
                .append(level).append("/")
                .append(tag).append("] ")
                .append(message).toString());
    }

    private void doWriteDisk(String msg) {
        try {
            if (mLogRandomAccessFile != null) {
                byte[] msgByte = msg.getBytes("UTF-8");
                if (curWriteTime % CHECK_SIZE_TIMES == 0) {
                    if (mLogRandomAccessFile.length() + msgByte.length > mMaxFileSize) {
                        //切换文件
                        curLogFileFlag = curLogFileFlag == 0 ? 1 : 0;
                        switchLogRandomAccessFile(curLogFileFlag == 0 ? FIRST_FILE_NAME : SECOND_FILE_NAME);
                        SPHelper.get().setInt(SP_KEY_CUR_LOG_FILE_FLAG, curLogFileFlag);
                    }
                    curWriteTime = 0;
                }
                mLogRandomAccessFile.write(msgByte);
                mLogRandomAccessFile.writeBytes("\r\n");
                curWriteTime++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            mLogRandomAccessFile = null;
        }
    }

    private void switchLogRandomAccessFile(String fileName) {
        if (mContext == null) {
            return;
        }
        File file = getDirFile(mContext);
        try {
            File newLogFile = new File(file.getAbsolutePath() + File.separator + fileName);
            if (newLogFile.exists()) {
                newLogFile.delete();
            }
            newLogFile.createNewFile();
            mLogRandomAccessFile = new RandomAccessFile(newLogFile, "rw");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class LoggerTaskThread extends Thread {
        @Override
        public void run() {
            try {
                File rootDir = getDirFile(mContext);
                try {
                    curLogFileFlag = SPHelper.get().getInt(SP_KEY_CUR_LOG_FILE_FLAG, 0);
                    String fileName = curLogFileFlag == 0 ? FIRST_FILE_NAME : SECOND_FILE_NAME;
                    File newLogFile = new File(rootDir.getAbsolutePath() + File.separator + fileName);
                    if (!newLogFile.exists()) {
                        newLogFile.createNewFile();
                    }
                    mLogRandomAccessFile = new RandomAccessFile(newLogFile, "rw");
                    mLogRandomAccessFile.seek(mLogRandomAccessFile.length());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                while (!Thread.interrupted()) {
                    doWriteDisk(cacheList.take());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
