package com.github.xpenatan.gdx.backends.teavm.preloader;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entries;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.github.xpenatan.gdx.backends.teavm.AssetLoaderListener;
import com.github.xpenatan.gdx.backends.teavm.TeaApplication;
import com.github.xpenatan.gdx.backends.teavm.TeaApplicationConfiguration;
import com.github.xpenatan.gdx.backends.teavm.TeaFileHandle;
import com.github.xpenatan.gdx.backends.teavm.dom.DataTransferWrapper;
import com.github.xpenatan.gdx.backends.teavm.dom.DocumentWrapper;
import com.github.xpenatan.gdx.backends.teavm.dom.DragEventWrapper;
import com.github.xpenatan.gdx.backends.teavm.dom.EventListenerWrapper;
import com.github.xpenatan.gdx.backends.teavm.dom.EventWrapper;
import com.github.xpenatan.gdx.backends.teavm.dom.FileListWrapper;
import com.github.xpenatan.gdx.backends.teavm.dom.FileReaderWrapper;
import com.github.xpenatan.gdx.backends.teavm.dom.FileWrapper;
import com.github.xpenatan.gdx.backends.teavm.dom.HTMLCanvasElementWrapper;
import com.github.xpenatan.gdx.backends.teavm.dom.HTMLDocumentWrapper;
import com.github.xpenatan.gdx.backends.teavm.dom.HTMLImageElementWrapper;
import com.github.xpenatan.gdx.backends.teavm.dom.typedarray.ArrayBufferWrapper;
import com.github.xpenatan.gdx.backends.teavm.dom.typedarray.Int8ArrayWrapper;
import com.github.xpenatan.gdx.backends.teavm.dom.typedarray.TypedArrays;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import org.teavm.jso.browser.Window;

/**
 * @author xpenatan
 */
public class Preloader {
    public ObjectMap<String, Void> directories = new ObjectMap<>();
    public ObjectMap<String, Blob> images = new ObjectMap<>();
    public ObjectMap<String, Blob> audio = new ObjectMap<>();
    public ObjectMap<String, String> texts = new ObjectMap<>();
    public ObjectMap<String, Blob> binaries = new ObjectMap<>();
    public Array<Asset> assets = new Array<>();
    public int assetTotal = -1;

    private static final String ASSET_FOLDER = "assets/";

    public static class Asset {
        public Asset(String url, AssetType type, long size, String mimeType) {
            this.url = url;
            this.type = type;
            this.size = size;
            this.mimeType = mimeType;
        }

        public boolean succeed;
        public boolean failed;
        public boolean isLoading;
        public long loaded;
        public final String url;
        public final AssetType type;
        public final long size;
        public final String mimeType;
    }

    public final String baseUrl;

    public Preloader(String newBaseURL, HTMLCanvasElementWrapper canvas, TeaApplication teaApplication) {
        baseUrl = newBaseURL;

        setupFileDrop(canvas, teaApplication);
    }

    private void setupFileDrop(HTMLCanvasElementWrapper canvas, TeaApplication teaApplication) {
        TeaApplicationConfiguration config = teaApplication.getConfig();
        if(config.windowListener != null) {
            HTMLDocumentWrapper document = canvas.getOwnerDocument();
            document.addEventListener("dragenter", new EventListenerWrapper() {
                @Override
                public void handleEvent(EventWrapper evt) {
                    evt.preventDefault();
                }
            }, false);
            document.addEventListener("dragover", new EventListenerWrapper() {
                @Override
                public void handleEvent(EventWrapper evt) {
                    evt.preventDefault();
                }
            }, false);
            document.addEventListener("drop", new EventListenerWrapper() {
                @Override
                public void handleEvent(EventWrapper evt) {
                    evt.preventDefault();
                    DragEventWrapper event = (DragEventWrapper)evt;

                    DataTransferWrapper dataTransfer = event.getDataTransfer();

                    FileListWrapper files = dataTransfer.getFiles();

                    int length = files.getLength();
                    if(length > 0) {
                        Array<String> droppedFiles = new Array<>();
                        for(int i = 0; i < length; i++) {
                            FileWrapper itemWrapper = files.get(i);
                            String name = itemWrapper.getName();
                            AssetType type = AssetFilter.getType(name);
                            FileReaderWrapper fileReader = FileReaderWrapper.create();
                            fileReader.addEventListener("load", new EventListenerWrapper() {
                                @Override
                                public void handleEvent(EventWrapper evt) {
                                    FileReaderWrapper target = (FileReaderWrapper)evt.getTarget();
                                    Object obj = null;

                                    if(type == AssetType.Binary || type == AssetType.Audio) {
                                        ArrayBufferWrapper arrayBuffer = target.getResultAsArrayBuffer();
                                        Int8ArrayWrapper data = TypedArrays.getInstance().createInt8Array(arrayBuffer);
                                        obj = new Blob(arrayBuffer, data);
                                    }
                                    else if(type == AssetType.Image) {
                                        DocumentWrapper document = (DocumentWrapper)Window.current().getDocument();
                                        final HTMLImageElementWrapper image = (HTMLImageElementWrapper)document.createElement("img");
                                        String baseUrl = target.getResultAsString();
                                        image.setSrc(baseUrl);

                                        ArrayBufferWrapper arrayBuffer = target.getResultAsArrayBuffer();
                                        Int8ArrayWrapper data = TypedArrays.getInstance().createInt8Array(arrayBuffer);
                                        Blob blob = new Blob(arrayBuffer, data);
                                        blob.setImage(image);
                                        obj = blob;
                                    }
                                    else if(type == AssetType.Text) {
                                        obj = target.getResultAsString();
                                    }

                                    if(obj != null) {
                                        putAssetInMap(type, name, obj);
                                        droppedFiles.add(name);
                                        if(droppedFiles.size == length) {
                                            teaApplication.postRunnable(new Runnable() {
                                                @Override
                                                public void run() {
                                                    String[] array = droppedFiles.toArray();
                                                    config.windowListener.filesDropped(array);
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                            if(type == AssetType.Binary || type == AssetType.Audio) {
                                fileReader.readAsArrayBuffer(itemWrapper);
                            }
                            else if(type == AssetType.Image) {
                                fileReader.readAsDataURL(itemWrapper);
                            }
                            else if(type == AssetType.Text) {
                                fileReader.readAsText(itemWrapper);
                            }
                        }
                    }
                }
            });
        }
    }

    public String getAssetUrl() {
        return baseUrl + ASSET_FOLDER;
    }

    public void preload(boolean loadAssets, final String assetFileUrl) {
        AssetDownloader.getInstance().loadText(true, getAssetUrl() + assetFileUrl, new AssetLoaderListener<String>() {
            @Override
            public void onProgress(double amount) {
            }

            @Override
            public void onFailure(String url) {
                System.out.println("ErrorLoading: " + assetFileUrl);
            }

            @Override
            public boolean onSuccess(String url, String result) {
                String[] lines = result.split("\n");
                for(String line : lines) {
                    String[] tokens = line.split(":");
                    if(tokens.length != 4) {
                        throw new GdxRuntimeException("Invalid assets description file.");
                    }
                    AssetType type = AssetType.Text;
                    if(tokens[0].equals("i")) type = AssetType.Image;
                    if(tokens[0].equals("b")) type = AssetType.Binary;
                    if(tokens[0].equals("a")) type = AssetType.Audio;
                    if(tokens[0].equals("d")) type = AssetType.Directory;
                    long size = Long.parseLong(tokens[2]);
                    if(type == AssetType.Audio && !AssetDownloader.getInstance().isUseBrowserCache()) {
                        size = 0;
                    }
                    assets.add(new Asset(tokens[1].trim(), type, size, tokens[3]));
                }
                assetTotal = assets.size;

                if(loadAssets) {
                    for(int i = 0; i < assets.size; i++) {
                        final Asset asset = assets.get(i);
                        loadSingleAsset(asset);
                    }
                }
                return false;
            }
        });
    }

    public int loadAssets() {
        int assetsToDownload = 0;
        for(int i = 0; i < assets.size; i++) {
            final Asset asset = assets.get(i);
            if(loadSingleAsset(asset)) {
                assetsToDownload++;
            }
        }
        return assetsToDownload;
    }

    public boolean loadSingleAsset(Asset asset) {
        if(contains(asset.url)) {
            asset.loaded = asset.size;
            asset.succeed = true;
            asset.failed = false;
            asset.isLoading = false;
            return false;
        }

        if(!asset.isLoading) {
            asset.failed = false;
            asset.succeed = false;
            asset.isLoading = true;
            loadAsset(true, asset.url, asset.type, asset.mimeType, new AssetLoaderListener<Object>() {
                @Override
                public void onProgress(double amount) {
                    asset.loaded = (long)amount;
                }

                @Override
                public void onFailure(String url) {
                    asset.failed = true;
                    asset.isLoading = false;
                }

                @Override
                public boolean onSuccess(String url, Object result) {
                    asset.succeed = true;
                    asset.isLoading = false;
                    return false;
                }
            });
            return true;
        }
        return false;
    }

    public Asset getAsset(String path) {
        for(int i = 0; i < assets.size; i++) {
            final Asset asset = assets.get(i);
            String url = asset.url;
            if(path.equals(url)) {
                return asset;
            }
        }
        return null;
    }

    public void loadBinaryAsset(boolean async, final String url, AssetLoaderListener<Blob> listener) {
        AssetDownloader.getInstance().load(async, getAssetUrl() + url, AssetType.Binary, null, new AssetLoaderListener<Blob>() {
            @Override
            public void onProgress(double amount) {
                listener.onProgress(amount);
            }

            @Override
            public void onFailure(String urll) {
                listener.onFailure(urll);
            }

            @Override
            public boolean onSuccess(String urll, Blob result) {
                putAssetInMap(AssetType.Binary, url, result);
                listener.onSuccess(urll, result);
                return false;
            }
        });
    }

    public void loadAsset(boolean async, final String url, final AssetType type, final String mimeType, final AssetLoaderListener<Object> listener) {
        AssetDownloader.getInstance().load(async, getAssetUrl() + url, type, mimeType, new AssetLoaderListener<Object>() {
            @Override
            public void onProgress(double amount) {
                listener.onProgress(amount);
            }

            @Override
            public void onFailure(String urll) {
                listener.onFailure(urll);
            }

            @Override
            public boolean onSuccess(String urll, Object result) {
                putAssetInMap(type, url, result);
                listener.onSuccess(urll, result);
                return false;
            }
        });
    }

    public void loadScript(boolean async, final String url, final AssetLoaderListener<Object> listener) {
        AssetDownloader.getInstance().loadScript(async, baseUrl + url, listener);
    }

    protected void putAssetInMap(AssetType type, String url, Object result) {
        switch(type) {
            case Text:
                texts.put(url, (String)result);
                break;
            case Image:
                images.put(url, (Blob)result);
                break;
            case Binary:
                binaries.put(url, (Blob)result);
                break;
            case Audio:
                audio.put(url, (Blob)result);
                break;
            case Directory:
                directories.put(url, null);
                break;
        }
    }

    public InputStream read(String url) {
        if(texts.containsKey(url)) {
            try {
                return new ByteArrayInputStream(texts.get(url).getBytes("UTF-8"));
            }
            catch(UnsupportedEncodingException e) {
                return null;
            }
        }
        if(images.containsKey(url)) {
            return new ByteArrayInputStream(new byte[1]); // FIXME, sensible?
        }
        if(binaries.containsKey(url)) {
            return binaries.get(url).read();
        }
        if(audio.containsKey(url)) {
            return new ByteArrayInputStream(new byte[1]); // FIXME, sensible?
        }
        return null;
    }

    public boolean contains(String file) {
        return texts.containsKey(file) || images.containsKey(file) || binaries.containsKey(file) || audio.containsKey(file)
                || directories.containsKey(file);
    }

    public boolean isText(String url) {
        return texts.containsKey(url);
    }

    public boolean isImage(String url) {
        return images.containsKey(url);
    }

    public boolean isBinary(String url) {
        return binaries.containsKey(url);
    }

    public boolean isAudio(String url) {
        return audio.containsKey(url);
    }

    public boolean isDirectory(String url) {
        return directories.containsKey(url);
    }

    private boolean isChild(String filePath, String directory) {
        return filePath.startsWith(directory + "/");
    }

    public FileHandle[] list(final String file) {
        return getMatchedAssetFiles(new FilePathFilter() {
            @Override
            public boolean accept(String path) {
                return isChild(path, file);
            }
        });
    }

    public FileHandle[] list(final String file, final FileFilter filter) {
        return getMatchedAssetFiles(new FilePathFilter() {
            @Override
            public boolean accept(String path) {
                return isChild(path, file) && filter.accept(new File(path));
            }
        });
    }

    public FileHandle[] list(final String file, final FilenameFilter filter) {
        return getMatchedAssetFiles(new FilePathFilter() {
            @Override
            public boolean accept(String path) {
                return isChild(path, file) && filter.accept(new File(file), path.substring(file.length() + 1));
            }
        });
    }

    public FileHandle[] list(final String file, final String suffix) {
        return getMatchedAssetFiles(new FilePathFilter() {
            @Override
            public boolean accept(String path) {
                return isChild(path, file) && path.endsWith(suffix);
            }
        });
    }

    public long length(String file) {
        if(texts.containsKey(file)) {
            return texts.get(file).getBytes(StandardCharsets.UTF_8).length;
        }
        if(images.containsKey(file)) {
            return 1; // FIXME, sensible?
        }
        if(binaries.containsKey(file)) {
            return binaries.get(file).length();
        }
        if(audio.containsKey(file)) {
            return audio.get(file).length();
        }
        return 0;
    }

    private interface FilePathFilter {
        boolean accept(String path);
    }

    private FileHandle[] getMatchedAssetFiles(FilePathFilter filter) {
        Array<FileHandle> files = new Array<>();
        for(String file : texts.keys()) {
            if(filter.accept(file)) {
                files.add(new TeaFileHandle(this, file, FileType.Internal));
            }
        }
        for(String file : images.keys()) {
            if(filter.accept(file)) {
                files.add(new TeaFileHandle(this, file, FileType.Internal));
            }
        }
        for(String file : binaries.keys()) {
            if(filter.accept(file)) {
                files.add(new TeaFileHandle(this, file, FileType.Internal));
            }
        }
        for(String file : audio.keys()) {
            if(filter.accept(file)) {
                files.add(new TeaFileHandle(this, file, FileType.Internal));
            }
        }
        FileHandle[] filesArray = new FileHandle[files.size];
        System.arraycopy(files.items, 0, filesArray, 0, filesArray.length);
        return filesArray;
    }

    public int getQueue() {
        return AssetDownloader.getInstance().getQueue();
    }

    public void printLoadedAssets() {
        System.out.println("### Text Assets: ");
        printKeys(texts);
        System.out.println("##########");
        System.out.println("### Image Assets: ");
        printKeys(images);
        System.out.println("##########");
    }

    private void printKeys(ObjectMap<String, ?> map) {
        Entries<String, ?> iterator = map.iterator();

        while(iterator.hasNext) {
            Entry<String, ?> next = iterator.next();
            String key = next.key;
            System.out.println("Key: " + key);
        }
    }
}
