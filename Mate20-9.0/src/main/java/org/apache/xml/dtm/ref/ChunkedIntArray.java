package org.apache.xml.dtm.ref;

import java.io.PrintStream;
import java.util.Objects;
import org.apache.xml.res.XMLErrorResources;
import org.apache.xml.res.XMLMessages;

final class ChunkedIntArray {
    static final int chunkalloc = 1024;
    static final int lowbits = 10;
    static final int lowmask = 1023;
    ChunksVector chunks = new ChunksVector();
    final int[] fastArray = new int[1024];
    int lastUsed = 0;
    final int slotsize = 4;

    class ChunksVector {
        final int BLOCKSIZE = 64;
        int[][] m_map = new int[64][];
        int m_mapSize = 64;
        int pos = 0;

        ChunksVector() {
        }

        /* access modifiers changed from: package-private */
        public final int size() {
            return this.pos;
        }

        /* access modifiers changed from: package-private */
        public void addElement(int[] value) {
            if (this.pos >= this.m_mapSize) {
                int orgMapSize = this.m_mapSize;
                while (this.pos >= this.m_mapSize) {
                    this.m_mapSize += 64;
                }
                int[][] newMap = new int[this.m_mapSize][];
                System.arraycopy(this.m_map, 0, newMap, 0, orgMapSize);
                this.m_map = newMap;
            }
            this.m_map[this.pos] = value;
            this.pos++;
        }

        /* access modifiers changed from: package-private */
        public final int[] elementAt(int pos2) {
            return this.m_map[pos2];
        }
    }

    ChunkedIntArray(int slotsize2) {
        Objects.requireNonNull(this);
        if (4 >= slotsize2) {
            Objects.requireNonNull(this);
            if (4 > slotsize2) {
                PrintStream printStream = System.out;
                StringBuilder sb = new StringBuilder();
                sb.append("*****WARNING: ChunkedIntArray(");
                sb.append(slotsize2);
                sb.append(") wasting ");
                Objects.requireNonNull(this);
                sb.append(4 - slotsize2);
                sb.append(" words per slot");
                printStream.println(sb.toString());
            }
            this.chunks.addElement(this.fastArray);
            return;
        }
        throw new ArrayIndexOutOfBoundsException(XMLMessages.createXMLMessage(XMLErrorResources.ER_CHUNKEDINTARRAY_NOT_SUPPORTED, new Object[]{Integer.toString(slotsize2)}));
    }

    /* access modifiers changed from: package-private */
    public int appendSlot(int w0, int w1, int w2, int w3) {
        int newoffset = (this.lastUsed + 1) * 4;
        int chunkpos = newoffset >> 10;
        int slotpos = newoffset & lowmask;
        if (chunkpos > this.chunks.size() - 1) {
            this.chunks.addElement(new int[1024]);
        }
        int[] chunk = this.chunks.elementAt(chunkpos);
        chunk[slotpos] = w0;
        chunk[slotpos + 1] = w1;
        chunk[slotpos + 2] = w2;
        chunk[slotpos + 3] = w3;
        int i = this.lastUsed + 1;
        this.lastUsed = i;
        return i;
    }

    /* access modifiers changed from: package-private */
    public int readEntry(int position, int offset) throws ArrayIndexOutOfBoundsException {
        if (offset < 4) {
            int position2 = position * 4;
            int slotpos = position2 & lowmask;
            return this.chunks.elementAt(position2 >> 10)[slotpos + offset];
        }
        throw new ArrayIndexOutOfBoundsException(XMLMessages.createXMLMessage(XMLErrorResources.ER_OFFSET_BIGGER_THAN_SLOT, null));
    }

    /* access modifiers changed from: package-private */
    public int specialFind(int startPos, int position) {
        int ancestor = startPos;
        while (ancestor > 0) {
            int ancestor2 = ancestor * 4;
            int slotpos = ancestor2 & lowmask;
            ancestor = this.chunks.elementAt(ancestor2 >> 10)[slotpos + 1];
            if (ancestor == position) {
                break;
            }
        }
        if (ancestor <= 0) {
            return position;
        }
        return -1;
    }

    /* access modifiers changed from: package-private */
    public int slotsUsed() {
        return this.lastUsed;
    }

    /* access modifiers changed from: package-private */
    public void discardLast() {
        this.lastUsed--;
    }

    /* access modifiers changed from: package-private */
    public void writeEntry(int position, int offset, int value) throws ArrayIndexOutOfBoundsException {
        if (offset < 4) {
            int position2 = position * 4;
            int slotpos = position2 & lowmask;
            this.chunks.elementAt(position2 >> 10)[slotpos + offset] = value;
            return;
        }
        throw new ArrayIndexOutOfBoundsException(XMLMessages.createXMLMessage(XMLErrorResources.ER_OFFSET_BIGGER_THAN_SLOT, null));
    }

    /* access modifiers changed from: package-private */
    public void writeSlot(int position, int w0, int w1, int w2, int w3) {
        int position2 = position * 4;
        int chunkpos = position2 >> 10;
        int slotpos = position2 & lowmask;
        if (chunkpos > this.chunks.size() - 1) {
            this.chunks.addElement(new int[1024]);
        }
        int[] chunk = this.chunks.elementAt(chunkpos);
        chunk[slotpos] = w0;
        chunk[slotpos + 1] = w1;
        chunk[slotpos + 2] = w2;
        chunk[slotpos + 3] = w3;
    }

    /* access modifiers changed from: package-private */
    public void readSlot(int position, int[] buffer) {
        int position2 = position * 4;
        int chunkpos = position2 >> 10;
        int slotpos = position2 & lowmask;
        if (chunkpos > this.chunks.size() - 1) {
            this.chunks.addElement(new int[1024]);
        }
        System.arraycopy(this.chunks.elementAt(chunkpos), slotpos, buffer, 0, 4);
    }
}
