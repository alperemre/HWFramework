package android_maps_conflict_avoidance.com.google.googlenav.map;

import android_maps_conflict_avoidance.com.google.common.Config;
import android_maps_conflict_avoidance.com.google.common.Log;
import android_maps_conflict_avoidance.com.google.common.io.IoUtil;
import android_maps_conflict_avoidance.com.google.common.io.PersistentStore.PersistentStoreException;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

class FlashRecord {
    private static final Object lastCacheDataLock = null;
    private static byte[] lastCachedData;
    private static FlashRecord lastFlashRecord;
    private final Vector flashEntries;
    private int recordId;
    private boolean unverified;

    static {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: android_maps_conflict_avoidance.com.google.googlenav.map.FlashRecord.<clinit>():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:113)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
Caused by: jadx.core.utils.exceptions.DecodeException:  in method: android_maps_conflict_avoidance.com.google.googlenav.map.FlashRecord.<clinit>():void
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:46)
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:98)
	... 7 more
Caused by: java.lang.IllegalArgumentException: bogus opcode: 0073
	at com.android.dx.io.OpcodeInfo.get(OpcodeInfo.java:1197)
	at com.android.dx.io.OpcodeInfo.getFormat(OpcodeInfo.java:1212)
	at com.android.dx.io.instructions.DecodedInstruction.decode(DecodedInstruction.java:72)
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:43)
	... 8 more
*/
        /*
        // Can't load method instructions.
        */
        throw new UnsupportedOperationException("Method not decompiled: android_maps_conflict_avoidance.com.google.googlenav.map.FlashRecord.<clinit>():void");
    }

    public FlashRecord() {
        this.unverified = false;
        this.recordId = -1;
        this.flashEntries = new Vector();
    }

    public boolean addEntry(FlashEntry entry) {
        if (numEntries() >= 255 || this.recordId != -1) {
            return false;
        }
        this.flashEntries.addElement(entry);
        entry.setFlashRecord(this);
        return true;
    }

    public FlashEntry getEntry(Tile location) {
        for (int i = 0; i < this.flashEntries.size(); i++) {
            FlashEntry entry = getEntry(i);
            if (entry.getTile().equals(location)) {
                return entry;
            }
        }
        return null;
    }

    public FlashEntry getEntry(int index) {
        return (FlashEntry) this.flashEntries.elementAt(index);
    }

    public int numEntries() {
        return this.flashEntries.size();
    }

    public int getDataSize() {
        int size = 1;
        for (int i = 0; i < this.flashEntries.size(); i++) {
            size += getEntry(i).getByteSize();
        }
        return size;
    }

    public int getRecordId() {
        return this.recordId;
    }

    boolean isSaved() {
        return this.recordId != -1;
    }

    void setUnsaved() {
        this.recordId = -1;
    }

    public long getScore(long currentTime) {
        int numEntries = this.flashEntries.size();
        long scores = 0;
        if (numEntries <= 0) {
            return Long.MAX_VALUE;
        }
        for (int i = 0; i < numEntries; i++) {
            FlashEntry entry = getEntry(i);
            scores += MapService.getScore(entry.getTile(), currentTime, entry.getLastAccessTime());
        }
        return scores / ((long) numEntries);
    }

    public String toString() {
        return super.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FlashRecord)) {
            return false;
        }
        FlashRecord flashRecord = (FlashRecord) o;
        if (this.recordId != flashRecord.recordId) {
            return false;
        }
        int numEntries = numEntries();
        if (numEntries != flashRecord.numEntries()) {
            return false;
        }
        Vector otherEntries = flashRecord.flashEntries;
        for (int i = 0; i < numEntries; i++) {
            if (!((FlashEntry) this.flashEntries.elementAt(i)).equals(otherEntries.elementAt(i))) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        return this.recordId;
    }

    public static FlashRecord readFromCatalog(DataInput is) throws IOException {
        int entries = is.readUnsignedByte();
        int recordIndex = is.readInt();
        FlashRecord flashRecord = new FlashRecord();
        int entry = 0;
        while (entry < entries) {
            if (flashRecord.addEntry(FlashEntry.readFromCatalog(is))) {
                entry++;
            } else {
                throw new IOException("FlashRecord full");
            }
        }
        flashRecord.unverified = true;
        flashRecord.recordId = recordIndex;
        return flashRecord;
    }

    public void writeToCatalog(DataOutput dos) throws IOException {
        if (this.recordId != -1) {
            int numEntries = numEntries();
            dos.writeByte(numEntries);
            dos.writeInt(this.recordId);
            for (int index = 0; index < numEntries; index++) {
                getEntry(index).writeToCatalog(dos);
            }
            return;
        }
        throw new IllegalStateException("Can't write unsaved FlashRecord");
    }

    private MapTile loadTileFromDataEntry(byte[] recordBlock, Tile desiredTile) {
        MapTile mapTile = null;
        if (recordBlock == null || recordBlock.length == 0) {
            return null;
        }
        try {
            DataInput dis = IoUtil.createDataInputFromBytes(recordBlock);
            int entries = dis.readUnsignedByte();
            if (entries != numEntries()) {
                return null;
            }
            int i = 0;
            while (i < entries) {
                MapTile currentTile = MapTile.read(dis);
                if (this.unverified && !new FlashEntry(currentTile).equals(this.flashEntries.elementAt(i))) {
                    return null;
                }
                if (currentTile.getLocation().equals(desiredTile)) {
                    mapTile = currentTile;
                    if (!this.unverified) {
                        break;
                    }
                }
                i++;
            }
            this.unverified = false;
            return mapTile;
        } catch (IOException e) {
            Log.logQuietThrowable("FLASH", e);
        }
    }

    byte[] createDataEntry(Hashtable tileMap) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(72000);
        DataOutputStream dos = new DataOutputStream(baos);
        int numEntries = numEntries();
        dos.writeByte(numEntries);
        for (int i = 0; i < numEntries; i++) {
            MapTile mapTile = (MapTile) tileMap.get(getEntry(i).getTile());
            if (mapTile == null) {
                return null;
            }
            mapTile.write(dos);
        }
        return baos.toByteArray();
    }

    public MapTile loadTile(String blockName, Tile desiredTile) {
        if (this.recordId == -1) {
            return null;
        }
        byte[] recordBlock = null;
        synchronized (lastCacheDataLock) {
            if (equals(lastFlashRecord)) {
                recordBlock = lastCachedData;
            }
        }
        if (recordBlock == null) {
            recordBlock = Config.getInstance().getPersistentStore().readBlock(blockName);
            if (!(recordBlock == null || recordBlock.length == 0)) {
                synchronized (lastCacheDataLock) {
                    lastCachedData = recordBlock;
                    lastFlashRecord = this;
                }
            }
        }
        if (recordBlock != null) {
            return loadTileFromDataEntry(recordBlock, desiredTile);
        }
        return null;
    }

    void writeRecord(String blockName, int recordId, byte[] recordData) throws PersistentStoreException, IllegalStateException {
        if (this.recordId == -1) {
            Config.getInstance().getPersistentStore().writeBlockX(recordData, blockName);
            this.recordId = recordId;
            return;
        }
        throw new IllegalStateException("already saved");
    }

    static void clearDataCache() {
        synchronized (lastCacheDataLock) {
            lastCachedData = null;
            lastFlashRecord = null;
        }
    }
}
