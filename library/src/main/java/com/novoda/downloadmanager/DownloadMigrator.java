package com.novoda.downloadmanager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;

public class DownloadMigrator {

    private final Context applicationContext;
    private final Handler handler;
    private final NotificationChannelCreator notificationChannelCreator;
    private final NotificationCreator<MigrationStatus> notificationCreator;

    DownloadMigrator(Context context,
                     Handler handler,
                     NotificationChannelCreator notificationChannelCreator,
                     NotificationCreator<MigrationStatus> notificationCreator) {
        this.applicationContext = context.getApplicationContext();
        this.handler = handler;
        this.notificationChannelCreator = notificationChannelCreator;
        this.notificationCreator = notificationCreator;
    }

    public void startMigration(final MigrationCallback migrationCallback) {
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                DownloadMigrationService migrationService = ((LiteDownloadMigrationService.MigrationDownloadServiceBinder) binder).getService();
                migrationService.setNotificationChannelCreator(notificationChannelCreator);
                migrationService.setNotificationCreator(notificationCreator);

                MigrationCallback mainThreadReportingMigrationCallback = new MigrationCallback() {
                    @Override
                    public void onUpdate(final MigrationStatus migrationStatus) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                migrationCallback.onUpdate(migrationStatus);
                            }
                        });
                    }
                };

                migrationService.startMigration(mainThreadReportingMigrationCallback);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                // do nothing.
            }
        };
        Intent serviceIntent = new Intent(applicationContext, LiteDownloadMigrationService.class);
        applicationContext.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        applicationContext.startService(serviceIntent);
    }

}
