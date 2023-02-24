package com.ezio.plugin.utils;

import com.ezio.plugin.navigator.domain.RestServiceItem;
import com.intellij.openapi.actionSystem.DataKey;

import java.util.HashMap;
import java.util.List;

/**
 * Here be dragons !
 *
 * @author: Ezio
 * created on 2020/3/3
 */
public class RestApiDataKeys {


    public static final DataKey<List<RestServiceItem>> SERVICE_ITEMS = DataKey.create("SERVICE_ITEM");
    public static final HashMap<String, RestServiceItem> ITEM_HASH_MAP = new HashMap<>();
    public static final String KEY = "item";

    public static void put(RestServiceItem item) {
        ITEM_HASH_MAP.put(KEY, item);
    }

    public static RestServiceItem get() {
        return ITEM_HASH_MAP.get(KEY);
    }

    private RestApiDataKeys() {
    }
}
