package org.springframework.transaction.support;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.sql.DataSource;

public abstract class TransactionSynchronizationManager {

    private static final ThreadLocal<Map<DataSource, Connection>> resources = new ThreadLocal<>();

    private TransactionSynchronizationManager() {}

    public static Connection getResource(final DataSource key) {
        final Map<DataSource, Connection> connectionByDataSource = resources.get();
        if (Objects.isNull(connectionByDataSource)) {
            return null;
        }
        return connectionByDataSource.get(key);
    }

    public static void bindResource(final DataSource key, final Connection value) {
        final Map<DataSource, Connection> connectionMap = resources.get();
        if (Objects.isNull(connectionMap)) {
            resources.set(new HashMap<>(Map.of(key, value)));
            return;
        }

        connectionMap.put(key, value);
    }

    public static Connection unbindResource(final DataSource key) {
        final Connection removedConnection = resources.get().remove(key);
        if (resources.get().isEmpty()) {
            resources.remove();
        }
        return removedConnection;

    }
}
