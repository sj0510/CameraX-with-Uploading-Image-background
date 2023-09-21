package com.sj.saurabh.utils;

import java.lang.System;

/**
 * @author Saurabh Kumar 27-03-2023
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 \u00122\u00020\u0001:\u0001\u0012B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\fJ\u0013\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\fJ\u001b\u0010\u000f\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u0005\u001a\u00020\u0006H\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0013"}, d2 = {"Lcom/sj/saurabh/utils/MediaStoreUtils;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "mediaStoreCollection", "Landroid/net/Uri;", "getMediaStoreCollection", "()Landroid/net/Uri;", "getImages", "", "Lcom/sj/saurabh/utils/MediaStoreFile;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLatestImageFilename", "", "getMediaStoreImageCursor", "Landroid/database/Cursor;", "(Landroid/net/Uri;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"})
public final class MediaStoreUtils {
    private final android.content.Context context = null;
    @org.jetbrains.annotations.Nullable()
    private final android.net.Uri mediaStoreCollection = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.sj.saurabh.utils.MediaStoreUtils.Companion Companion = null;
    private static final java.lang.String imageDataColumnIndex = "_data";
    private static final java.lang.String imageIdColumnIndex = "_id";
    
    public MediaStoreUtils(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.net.Uri getMediaStoreCollection() {
        return null;
    }
    
    private final java.lang.Object getMediaStoreImageCursor(android.net.Uri mediaStoreCollection, kotlin.coroutines.Continuation<? super android.database.Cursor> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getLatestImageFilename(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getImages(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.sj.saurabh.utils.MediaStoreFile>> continuation) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\b\n\u0000\u0012\u0004\b\u0005\u0010\u0002R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/sj/saurabh/utils/MediaStoreUtils$Companion;", "", "()V", "imageDataColumnIndex", "", "getImageDataColumnIndex$annotations", "imageIdColumnIndex", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @kotlin.Suppress(names = {"DEPRECATION"})
        @java.lang.Deprecated()
        private static void getImageDataColumnIndex$annotations() {
        }
    }
}