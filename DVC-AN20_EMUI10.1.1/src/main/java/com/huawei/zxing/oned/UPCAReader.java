package com.huawei.zxing.oned;

import com.huawei.zxing.BarcodeFormat;
import com.huawei.zxing.BinaryBitmap;
import com.huawei.zxing.ChecksumException;
import com.huawei.zxing.DecodeHintType;
import com.huawei.zxing.FormatException;
import com.huawei.zxing.NotFoundException;
import com.huawei.zxing.Result;
import com.huawei.zxing.common.BitArray;
import java.util.Map;

public final class UPCAReader extends UPCEANReader {
    private final UPCEANReader ean13Reader = new EAN13Reader();

    @Override // com.huawei.zxing.oned.UPCEANReader
    public Result decodeRow(int rowNumber, BitArray row, int[] startGuardRange, Map<DecodeHintType, ?> hints) throws NotFoundException, FormatException, ChecksumException {
        return maybeReturnResult(this.ean13Reader.decodeRow(rowNumber, row, startGuardRange, hints));
    }

    @Override // com.huawei.zxing.oned.UPCEANReader, com.huawei.zxing.oned.OneDReader
    public Result decodeRow(int rowNumber, BitArray row, Map<DecodeHintType, ?> hints) throws NotFoundException, FormatException, ChecksumException {
        return maybeReturnResult(this.ean13Reader.decodeRow(rowNumber, row, hints));
    }

    @Override // com.huawei.zxing.Reader, com.huawei.zxing.oned.OneDReader
    public Result decode(BinaryBitmap image) throws NotFoundException, FormatException {
        return maybeReturnResult(this.ean13Reader.decode(image));
    }

    @Override // com.huawei.zxing.Reader, com.huawei.zxing.oned.OneDReader
    public Result decode(BinaryBitmap image, Map<DecodeHintType, ?> hints) throws NotFoundException, FormatException {
        return maybeReturnResult(this.ean13Reader.decode(image, hints));
    }

    /* access modifiers changed from: package-private */
    @Override // com.huawei.zxing.oned.UPCEANReader
    public BarcodeFormat getBarcodeFormat() {
        return BarcodeFormat.UPC_A;
    }

    /* access modifiers changed from: protected */
    @Override // com.huawei.zxing.oned.UPCEANReader
    public int decodeMiddle(BitArray row, int[] startRange, StringBuilder resultString) throws NotFoundException {
        return this.ean13Reader.decodeMiddle(row, startRange, resultString);
    }

    private static Result maybeReturnResult(Result result) throws FormatException {
        String text = result.getText();
        if (text.charAt(0) == '0') {
            return new Result(text.substring(1), null, result.getResultPoints(), BarcodeFormat.UPC_A);
        }
        throw FormatException.getFormatInstance();
    }
}
