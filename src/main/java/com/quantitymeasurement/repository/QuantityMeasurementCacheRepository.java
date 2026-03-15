package com.quantitymeasurement.repository;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.quantitymeasurement.exception.QuantityMeasurementException;
import com.quantitymeasurement.model.QuantityMeasurementEntity;

public class QuantityMeasurementCacheRepository
        implements IQuantityMeasurementRepository {

    private static final String FILE_NAME = "quantity-measurement-history.ser";

    private static QuantityMeasurementCacheRepository instance;

    private final List<QuantityMeasurementEntity> cache;
    private final File storageFile;

    private QuantityMeasurementCacheRepository() {
        this.cache = new ArrayList<>();
        this.storageFile = new File(FILE_NAME);
        loadFromDisk();
    }

    public static synchronized QuantityMeasurementCacheRepository getInstance() {
        if (instance == null) {
            instance = new QuantityMeasurementCacheRepository();
        }
        return instance;
    }

    @Override
    public synchronized void save(QuantityMeasurementEntity entity) {

        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        cache.add(entity);
        saveToDisk(entity);
    }

    @Override
    public synchronized List<QuantityMeasurementEntity> findAll() {
        return new ArrayList<>(cache);
    }

    @Override
    public synchronized void clear() {

        cache.clear();

        if (storageFile.exists() && !storageFile.delete()) {
            throw new QuantityMeasurementException(
                    "Failed to clear repository storage file.");
        }
    }

    private void saveToDisk(QuantityMeasurementEntity entity) {

        try {

            boolean append = storageFile.exists() && storageFile.length() > 0;

            try (FileOutputStream fos = new FileOutputStream(storageFile, true);
                 ObjectOutputStream oos = append
                         ? new AppendableObjectOutputStream(fos)
                         : new ObjectOutputStream(fos)) {

                oos.writeObject(entity);
            }

        } catch (IOException e) {

            throw new QuantityMeasurementException(
                    "Failed to save repository data.", e);
        }
    }

    private void loadFromDisk() {

        if (!storageFile.exists() || storageFile.length() == 0) {
            return;
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(storageFile))) {

            cache.clear();

            while (true) {

                try {

                    Object obj = ois.readObject();

                    if (obj instanceof QuantityMeasurementEntity) {
                        cache.add((QuantityMeasurementEntity) obj);
                    }

                } catch (EOFException eof) {
                    break;
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            cache.clear();
        }
    }

    private static class AppendableObjectOutputStream
            extends ObjectOutputStream {

        public AppendableObjectOutputStream(OutputStream out)
                throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            reset();
        }
    }
}