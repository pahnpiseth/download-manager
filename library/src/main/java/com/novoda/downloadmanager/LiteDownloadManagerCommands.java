package com.novoda.downloadmanager;

import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import java.io.File;
import java.util.List;

public interface LiteDownloadManagerCommands {

    /**
     * Downloads a given batch of files.
     *
     * @param batch to download.
     */
    void download(Batch batch);

    /**
     * Pauses the {@link Batch} with the associated {@link DownloadBatchId}.
     *
     * @param downloadBatchId of the batch to pause.
     */
    void pause(DownloadBatchId downloadBatchId);

    /**
     * Resumes the {@link Batch} with the associated {@link DownloadBatchId}.
     *
     * @param downloadBatchId of the batch to resume.
     */
    void resume(DownloadBatchId downloadBatchId);

    /**
     * Deletes the {@link Batch} with the associated {@link DownloadBatchId}.
     *
     * @param downloadBatchId of the batch to delete.
     */
    void delete(DownloadBatchId downloadBatchId);

    /**
     * Adds a {@link DownloadBatchStatusCallback} to the internal list of callbacks
     * to be notified on {@link DownloadBatchStatus} changes.
     *
     * @param downloadBatchCallback to add to the internal list of callbacks.
     */
    void addDownloadBatchCallback(DownloadBatchStatusCallback downloadBatchCallback);

    /**
     * Removes a {@link DownloadBatchStatusCallback} from the internal list of callbacks,
     * removing notifications on {@link DownloadBatchStatus} changes.
     *
     * @param downloadBatchCallback to remove from the internal list of callbacks.
     */
    void removeDownloadBatchCallback(DownloadBatchStatusCallback downloadBatchCallback);

    /**
     * Retrieves all stored batches from the database and queues them ready for download,
     * repopulating the internal map.
     *
     * @param callback to be notified when all downloads are queued.
     */
    void submitAllStoredDownloads(AllStoredDownloadsSubmittedCallback callback);

    /**
     * Retrieves a list of {@link DownloadBatchStatus} synchronously. Clients should
     * specify their own Threading mechanism.
     *
     * @return a list of {@link DownloadBatchStatus}.
     */
    @WorkerThread
    List<DownloadBatchStatus> getAllDownloadBatchStatuses();

    /**
     * Retrieves a list of {@link DownloadBatchStatus} passing to the {@link AllBatchStatusesCallback}.
     *
     * @param callback to pass the list of {@link DownloadBatchStatus} to.
     */
    void getAllDownloadBatchStatuses(AllBatchStatusesCallback callback);

    /**
     * Retrieves {@link DownloadFileStatus} with the associated {@link DownloadBatchId} and {@link DownloadFileId}.
     * Will return null if a {@link DownloadFileStatus} is not found with the matching {@link DownloadBatchId} and {@link DownloadFileId}.
     * This call is synchronous, clients should specify their own Threading mechanism.
     *
     * @param downloadBatchId of the {@link DownloadFileStatus} to retrieve.
     * @param downloadFileId  of the {@link DownloadFileStatus} to retrieve.
     * @return {@link DownloadFileStatus} or null.
     */
    @Nullable
    @WorkerThread
    DownloadFileStatus getDownloadFileStatusWithMatching(DownloadBatchId downloadBatchId, DownloadFileId downloadFileId);

    /**
     * Retrieves {@link DownloadFileStatus} with the associated {@link DownloadBatchId} and {@link DownloadFileId}.
     * Will return null if a {@link DownloadFileStatus} is not found with the matching {@link DownloadBatchId} and {@link DownloadFileId}.
     *
     * @param downloadBatchId of the {@link DownloadFileStatus} to retrieve.
     * @param downloadFileId  of the {@link DownloadFileStatus} to retrieve.
     * @param callback        to pass the {@link DownloadFileStatus} to.
     */
    void getDownloadFileStatusWithMatching(DownloadBatchId downloadBatchId, DownloadFileId downloadFileId, DownloadFileStatusCallback callback);

    /**
     * Changes the {@link ConnectionType} that must be satisfied in order to download.
     * Will pause all current downloads that are violating this condition.
     *
     * @param allowedConnectionType that must be satisfied in order to download.
     */
    void updateAllowedConnectionType(ConnectionType allowedConnectionType);

    /**
     * Retrieves the root directory used to store downloads.
     *
     * @return a {@link File} representing the root directory for downloads.
     */
    File getDownloadsDir();

    /**
     * Adds a {@link CompletedDownloadBatch} to the download manager.
     * This call is synchronous, clients should specify their own Threading mechanism.
     *
     * @param completedDownloadBatch to add to the download manager.
     * @return whether the addition was successful.
     */
    @WorkerThread
    boolean addCompletedBatch(CompletedDownloadBatch completedDownloadBatch);
}
