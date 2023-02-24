package com.ezio.plugin.navigator.domain;

import com.intellij.openapi.actionSystem.DataContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 检索调用它的上下文信息
 *
 * @author: Ezio
 * created on 2020/3/4
 */
public class NodeDataContext implements DataContext {

    private RestServiceItem restServiceItem;

    public NodeDataContext(RestServiceItem restServiceItem) {
        this.restServiceItem = restServiceItem;
    }

    @Nullable
    @Override
    public RestServiceItem getData(@NotNull String dataId) {
        return restServiceItem;
    }

    public RestServiceItem getData() {
        return restServiceItem;
    }
}
