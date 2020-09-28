package vendor.huawei.hardware.radio.V1_1;

import android.hardware.radio.V1_0.CallState;
import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;
import vendor.huawei.hardware.radio.V1_0.RILImsCallDetails;

public final class RILImsCallEx {
    public byte als;
    public RILImsCallDetails callDetails = new RILImsCallDetails();
    public int imsDomain;
    public int index;
    public int isECOnference;
    public byte isMT;
    public byte isMpty;
    public byte isVoice;
    public byte isVoicePrivacy;
    public String name = new String();
    public int namePresentation;
    public String number = new String();
    public int numberPresentation;
    public int peerVideoSupport;
    public int state;
    public int toa;

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != RILImsCallEx.class) {
            return false;
        }
        RILImsCallEx other = (RILImsCallEx) otherObject;
        if (this.state == other.state && this.index == other.index && this.toa == other.toa && this.isMpty == other.isMpty && this.isMT == other.isMT && this.als == other.als && this.isVoice == other.isVoice && this.isVoicePrivacy == other.isVoicePrivacy && HidlSupport.deepEquals(this.number, other.number) && this.numberPresentation == other.numberPresentation && HidlSupport.deepEquals(this.name, other.name) && this.namePresentation == other.namePresentation && HidlSupport.deepEquals(this.callDetails, other.callDetails) && this.isECOnference == other.isECOnference && this.peerVideoSupport == other.peerVideoSupport && this.imsDomain == other.imsDomain) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.state))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.index))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.toa))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.isMpty))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.isMT))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.als))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.isVoice))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.isVoicePrivacy))), Integer.valueOf(HidlSupport.deepHashCode(this.number)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.numberPresentation))), Integer.valueOf(HidlSupport.deepHashCode(this.name)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.namePresentation))), Integer.valueOf(HidlSupport.deepHashCode(this.callDetails)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.isECOnference))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.peerVideoSupport))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.imsDomain))));
    }

    public final String toString() {
        return "{" + ".state = " + CallState.toString(this.state) + ", .index = " + this.index + ", .toa = " + this.toa + ", .isMpty = " + ((int) this.isMpty) + ", .isMT = " + ((int) this.isMT) + ", .als = " + ((int) this.als) + ", .isVoice = " + ((int) this.isVoice) + ", .isVoicePrivacy = " + ((int) this.isVoicePrivacy) + ", .number = " + this.number + ", .numberPresentation = " + this.numberPresentation + ", .name = " + this.name + ", .namePresentation = " + this.namePresentation + ", .callDetails = " + this.callDetails + ", .isECOnference = " + this.isECOnference + ", .peerVideoSupport = " + this.peerVideoSupport + ", .imsDomain = " + this.imsDomain + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        readEmbeddedFromParcel(parcel, parcel.readBuffer(88), 0);
    }

    public static final ArrayList<RILImsCallEx> readVectorFromParcel(HwParcel parcel) {
        ArrayList<RILImsCallEx> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16);
        int _hidl_vec_size = _hidl_blob.getInt32(8);
        HwBlob childBlob = parcel.readEmbeddedBuffer((long) (_hidl_vec_size * 88), _hidl_blob.handle(), 0, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            RILImsCallEx _hidl_vec_element = new RILImsCallEx();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, (long) (_hidl_index_0 * 88));
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.state = _hidl_blob.getInt32(_hidl_offset + 0);
        this.index = _hidl_blob.getInt32(_hidl_offset + 4);
        this.toa = _hidl_blob.getInt32(_hidl_offset + 8);
        this.isMpty = _hidl_blob.getInt8(_hidl_offset + 12);
        this.isMT = _hidl_blob.getInt8(_hidl_offset + 13);
        this.als = _hidl_blob.getInt8(_hidl_offset + 14);
        this.isVoice = _hidl_blob.getInt8(_hidl_offset + 15);
        this.isVoicePrivacy = _hidl_blob.getInt8(_hidl_offset + 16);
        this.number = _hidl_blob.getString(_hidl_offset + 24);
        parcel.readEmbeddedBuffer((long) (this.number.getBytes().length + 1), _hidl_blob.handle(), _hidl_offset + 24 + 0, false);
        this.numberPresentation = _hidl_blob.getInt32(_hidl_offset + 40);
        this.name = _hidl_blob.getString(_hidl_offset + 48);
        parcel.readEmbeddedBuffer((long) (this.name.getBytes().length + 1), _hidl_blob.handle(), _hidl_offset + 48 + 0, false);
        this.namePresentation = _hidl_blob.getInt32(_hidl_offset + 64);
        this.callDetails.readEmbeddedFromParcel(parcel, _hidl_blob, _hidl_offset + 68);
        this.isECOnference = _hidl_blob.getInt32(_hidl_offset + 76);
        this.peerVideoSupport = _hidl_blob.getInt32(_hidl_offset + 80);
        this.imsDomain = _hidl_blob.getInt32(_hidl_offset + 84);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(88);
        writeEmbeddedToBlob(_hidl_blob, 0);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<RILImsCallEx> _hidl_vec) {
        HwBlob _hidl_blob = new HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8, _hidl_vec_size);
        _hidl_blob.putBool(12, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 88);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, (long) (_hidl_index_0 * 88));
        }
        _hidl_blob.putBlob(0, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(0 + _hidl_offset, this.state);
        _hidl_blob.putInt32(4 + _hidl_offset, this.index);
        _hidl_blob.putInt32(8 + _hidl_offset, this.toa);
        _hidl_blob.putInt8(12 + _hidl_offset, this.isMpty);
        _hidl_blob.putInt8(13 + _hidl_offset, this.isMT);
        _hidl_blob.putInt8(14 + _hidl_offset, this.als);
        _hidl_blob.putInt8(15 + _hidl_offset, this.isVoice);
        _hidl_blob.putInt8(16 + _hidl_offset, this.isVoicePrivacy);
        _hidl_blob.putString(24 + _hidl_offset, this.number);
        _hidl_blob.putInt32(40 + _hidl_offset, this.numberPresentation);
        _hidl_blob.putString(48 + _hidl_offset, this.name);
        _hidl_blob.putInt32(64 + _hidl_offset, this.namePresentation);
        this.callDetails.writeEmbeddedToBlob(_hidl_blob, 68 + _hidl_offset);
        _hidl_blob.putInt32(76 + _hidl_offset, this.isECOnference);
        _hidl_blob.putInt32(80 + _hidl_offset, this.peerVideoSupport);
        _hidl_blob.putInt32(84 + _hidl_offset, this.imsDomain);
    }
}
