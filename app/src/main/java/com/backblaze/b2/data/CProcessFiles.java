package com.backblaze.b2.data;

import com.backblaze.b2.client.B2StorageClient;
import com.backblaze.b2.client.B2StorageClientFactory;
import com.backblaze.b2.client.contentHandlers.B2ContentMemoryWriter;
import com.backblaze.b2.client.exceptions.B2Exception;
import com.backblaze.b2.client.structures.B2Bucket;
import com.backblaze.b2.client.structures.B2FileVersion;
import com.backblaze.b2.util.B2ExecutorUtils;
import com.example.b2.viewmodels.AppKeyViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.backblaze.b2.util.B2ExecutorUtils.createThreadFactory;


public class CProcessFiles {

    private static final String APP_NAME = "FileCharCount";
    private static final String VERSION = "0.0.1";
    private static final String USER_AGENT = APP_NAME + "/" + VERSION;
    private static String APP_KEY_ID= AppKeyViewModel.getAppKeyId();
    private static String APP_KEY=AppKeyViewModel.getAppKey();
    private CFileDao dao;
    private static char cToCount = 'a';

    public CProcessFiles(  CFileDao v_dao) {
        dao = v_dao;
    }
    // Interface for sending error message to Main Activity
    public interface OnErrorListener {
        public void onError(String errorMessage);
    }

    private OnErrorListener onErrorListener;

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    public void ProcessNow () {
        final ExecutorService executor  = Executors.newFixedThreadPool(10, createThreadFactory("B2Sample-executor-%02d"));
        APP_KEY_ID = AppKeyViewModel.getAppKeyId();
        APP_KEY= AppKeyViewModel.getAppKey();

        try {
            // Obtain a handle on the client
            B2StorageClient client = B2StorageClientFactory.createDefaultFactory()
                    .create(APP_KEY_ID, APP_KEY, USER_AGENT);
            // Obtain Bucket List
            List<B2Bucket> bucketList = client.listBuckets().getBuckets();
            CFile cfile1 = null;
            for ( B2Bucket bucket: bucketList ) {
                System.out.println("------------------------------");
                for (B2FileVersion file : client.fileNames(bucket.getBucketId())) {
                    try {

                        B2ContentMemoryWriter sink = B2ContentMemoryWriter.build();
                        // Download file content into memory
                        client.downloadById(file.getFileId(), sink);
                        String content  = new String(sink.getBytes());
                        Long aCount = content.chars().filter(c -> c == cToCount).count();
                        System.out.println (  aCount +" | "+file.getFileName()   );
                        Long timestamp = System.nanoTime();
                        cfile1 = new CFile(file.getFileName(), aCount, timestamp);
                        dao.insert(cfile1);

                    }catch (Exception e1 ) {
                        onErrorListener.onError(e1.toString());
                    }
                    System.out.println("------------------------------");
                }
            }

        } catch (B2Exception e) {
            onErrorListener.onError(e.toString());
            e.printStackTrace();
        } finally {
            B2ExecutorUtils.shutdownAndAwaitTermination(executor, 10, 10);
        }

    }

}
