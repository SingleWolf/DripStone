package com.walker.core.util;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.DrawableRes;

import java.io.File;
import java.io.FileDescriptor;
import java.io.InputStream;

/**
 * @Author Walker
 * @Date 2020-08-14 13:22
 * @Summary 图片相关
 */
public class ImageUtils {
    /**
     * Bitmap to bytes.
     *
     * @param bitmap The bitmap.
     * @return bytes
     */
    public static byte[] bitmap2Bytes(final Bitmap bitmap) {
        return com.blankj.utilcode.util.ImageUtils.bitmap2Bytes(bitmap);
    }

    /**
     * Bitmap to bytes.
     *
     * @param bitmap  The bitmap.
     * @param format  The format of bitmap.
     * @param quality The quality.
     * @return bytes
     */
    public static byte[] bitmap2Bytes(final Bitmap bitmap, final Bitmap.CompressFormat format, int quality) {
        return com.blankj.utilcode.util.ImageUtils.bitmap2Bytes(bitmap, format, quality);
    }

    /**
     * Bytes to bitmap.
     *
     * @param bytes The bytes.
     * @return bitmap
     */
    public static Bitmap bytes2Bitmap(final byte[] bytes) {
        return com.blankj.utilcode.util.ImageUtils.bytes2Bitmap(bytes);
    }

    /**
     * Drawable to bitmap.
     *
     * @param drawable The drawable.
     * @return bitmap
     */
    public static Bitmap drawable2Bitmap(final Drawable drawable) {
        return com.blankj.utilcode.util.ImageUtils.drawable2Bitmap(drawable);
    }

    /**
     * Bitmap to drawable.
     *
     * @param bitmap The bitmap.
     * @return drawable
     */
    public static Drawable bitmap2Drawable(final Bitmap bitmap) {
        return com.blankj.utilcode.util.ImageUtils.bitmap2Drawable(bitmap);
    }

    /**
     * Drawable to bytes.
     *
     * @param drawable The drawable.
     * @return bytes
     */
    public static byte[] drawable2Bytes(final Drawable drawable) {
        return com.blankj.utilcode.util.ImageUtils.drawable2Bytes(drawable);
    }

    /**
     * Drawable to bytes.
     *
     * @param drawable The drawable.
     * @param format   The format of bitmap.
     * @return bytes
     */
    public static byte[] drawable2Bytes(final Drawable drawable, final Bitmap.CompressFormat format, int quality) {
        return com.blankj.utilcode.util.ImageUtils.drawable2Bytes(drawable, format, quality);
    }

    /**
     * Bytes to drawable.
     *
     * @param bytes The bytes.
     * @return drawable
     */
    public static Drawable bytes2Drawable(final byte[] bytes) {
        return com.blankj.utilcode.util.ImageUtils.bytes2Drawable(bytes);
    }

    /**
     * View to bitmap.
     *
     * @param view The view.
     * @return bitmap
     */
    public static Bitmap view2Bitmap(final View view) {
        return com.blankj.utilcode.util.ImageUtils.view2Bitmap(view);
    }

    /**
     * Return bitmap.
     *
     * @param file The file.
     * @return bitmap
     */
    public static Bitmap getBitmap(final File file) {
        return com.blankj.utilcode.util.ImageUtils.getBitmap(file);
    }

    /**
     * Return bitmap.
     *
     * @param file      The file.
     * @param maxWidth  The maximum width.
     * @param maxHeight The maximum height.
     * @return bitmap
     */
    public static Bitmap getBitmap(final File file, final int maxWidth, final int maxHeight) {
        return com.blankj.utilcode.util.ImageUtils.getBitmap(file, maxWidth, maxHeight);
    }

    /**
     * Return bitmap.
     *
     * @param filePath The path of file.
     * @return bitmap
     */
    public static Bitmap getBitmap(final String filePath) {
        return com.blankj.utilcode.util.ImageUtils.getBitmap(filePath);
    }

    /**
     * Return bitmap.
     *
     * @param filePath  The path of file.
     * @param maxWidth  The maximum width.
     * @param maxHeight The maximum height.
     * @return bitmap
     */
    public static Bitmap getBitmap(final String filePath, final int maxWidth, final int maxHeight) {
        return com.blankj.utilcode.util.ImageUtils.getBitmap(filePath, maxWidth, maxHeight);
    }

    /**
     * Return bitmap.
     *
     * @param is The input stream.
     * @return bitmap
     */
    public static Bitmap getBitmap(final InputStream is) {
        return com.blankj.utilcode.util.ImageUtils.getBitmap(is);
    }

    /**
     * Return bitmap.
     *
     * @param is        The input stream.
     * @param maxWidth  The maximum width.
     * @param maxHeight The maximum height.
     * @return bitmap
     */
    public static Bitmap getBitmap(final InputStream is, final int maxWidth, final int maxHeight) {
        return com.blankj.utilcode.util.ImageUtils.getBitmap(is, maxWidth, maxHeight);
    }

    /**
     * Return bitmap.
     *
     * @param data   The data.
     * @param offset The offset.
     * @return bitmap
     */
    public static Bitmap getBitmap(final byte[] data, final int offset) {
        return com.blankj.utilcode.util.ImageUtils.getBitmap(data, offset);

    }

    /**
     * Return bitmap.
     *
     * @param data      The data.
     * @param offset    The offset.
     * @param maxWidth  The maximum width.
     * @param maxHeight The maximum height.
     * @return bitmap
     */
    public static Bitmap getBitmap(final byte[] data,
                                   final int offset,
                                   final int maxWidth,
                                   final int maxHeight) {
        return com.blankj.utilcode.util.ImageUtils.getBitmap(data, offset, maxWidth, maxHeight);
    }

    /**
     * Return bitmap.
     *
     * @param resId The resource id.
     * @return bitmap
     */
    public static Bitmap getBitmap(@DrawableRes final int resId) {
        return com.blankj.utilcode.util.ImageUtils.getBitmap(resId);
    }

    /**
     * Return bitmap.
     *
     * @param resId     The resource id.
     * @param maxWidth  The maximum width.
     * @param maxHeight The maximum height.
     * @return bitmap
     */
    public static Bitmap getBitmap(@DrawableRes final int resId,
                                   final int maxWidth,
                                   final int maxHeight) {
        return com.blankj.utilcode.util.ImageUtils.getBitmap(resId, maxWidth, maxHeight);
    }

    /**
     * Return bitmap.
     *
     * @param fd The file descriptor.
     * @return bitmap
     */
    public static Bitmap getBitmap(final FileDescriptor fd) {
        return com.blankj.utilcode.util.ImageUtils.getBitmap(fd);
    }

    /**
     * Return bitmap.
     *
     * @param fd        The file descriptor
     * @param maxWidth  The maximum width.
     * @param maxHeight The maximum height.
     * @return bitmap
     */
    public static Bitmap getBitmap(final FileDescriptor fd,
                                   final int maxWidth,
                                   final int maxHeight) {
        return com.blankj.utilcode.util.ImageUtils.getBitmap(fd, maxWidth, maxHeight);
    }

}
