package com.github.lemon.osc.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.github.lemon.osc.cache.Cache;
import com.github.lemon.osc.cache.CacheDao;
import com.github.lemon.osc.cache.StorageDrive;
import com.github.lemon.osc.cache.StorageDriveDao;
import com.github.lemon.osc.cache.VodCollect;
import com.github.lemon.osc.cache.VodCollectDao;
import com.github.lemon.osc.cache.VodRecord;
import com.github.lemon.osc.cache.VodRecordDao;


/**
 * 类描述:
 *
 * @author pj567
 * @since 2020/5/15
 */
@Database(entities = {Cache.class, VodRecord.class, VodCollect.class, StorageDrive.class}, version = 2)
public abstract class AppDataBase extends RoomDatabase {
    public abstract CacheDao getCacheDao();

    public abstract VodRecordDao getVodRecordDao();

    public abstract VodCollectDao getVodCollectDao();

    public abstract StorageDriveDao getStorageDriveDao();
}
