/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.lealone.storage.aose;

import java.util.HashMap;
import java.util.Map;

import org.lealone.db.PluginManager;
import org.lealone.storage.StorageBuilder;
import org.lealone.storage.StorageEngine;
import org.lealone.storage.StorageSetting;
import org.lealone.storage.page.PageOperationHandlerFactory;

public class AOStorageBuilder extends StorageBuilder {

    private static final HashMap<String, AOStorage> cache = new HashMap<>();

    public AOStorageBuilder() {
        this(null, null);
    }

    public AOStorageBuilder(Map<String, String> defaultConfig) {
        this(defaultConfig, null);
    }

    public AOStorageBuilder(Map<String, String> defaultConfig, PageOperationHandlerFactory pohFactory) {
        // 如果pohFactory为null，优先使用StorageEngine配置的，若没有再创建新的，避免嵌入式场景创建出多个pohFactory
        StorageEngine se = PluginManager.getPlugin(StorageEngine.class, AOStorageEngine.NAME);
        if (pohFactory == null)
            pohFactory = se.getPageOperationHandlerFactory();
        if (pohFactory == null)
            pohFactory = PageOperationHandlerFactory.create(defaultConfig);
        if (defaultConfig != null)
            config.putAll(defaultConfig);
        config.put(StorageSetting.POH_FACTORY.name(), pohFactory);
        se.setPageOperationHandlerFactory(pohFactory);
    }

    @Override
    public AOStorage openStorage() {
        String storagePath = (String) config.get(StorageSetting.STORAGE_PATH.name());
        AOStorage storage = cache.get(storagePath);
        if (storage == null) {
            synchronized (cache) {
                storage = cache.get(storagePath);
                if (storage == null) {
                    storage = new AOStorage(config);
                    cache.put(storagePath, storage);
                }
            }
        }
        return storage;
    }
}
