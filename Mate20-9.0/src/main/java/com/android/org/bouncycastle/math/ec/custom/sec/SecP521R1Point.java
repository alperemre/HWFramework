package com.android.org.bouncycastle.math.ec.custom.sec;

import com.android.org.bouncycastle.math.ec.ECCurve;
import com.android.org.bouncycastle.math.ec.ECFieldElement;
import com.android.org.bouncycastle.math.ec.ECPoint;
import com.android.org.bouncycastle.math.raw.Nat;

public class SecP521R1Point extends ECPoint.AbstractFp {
    public SecP521R1Point(ECCurve curve, ECFieldElement x, ECFieldElement y) {
        this(curve, x, y, false);
    }

    public SecP521R1Point(ECCurve curve, ECFieldElement x, ECFieldElement y, boolean withCompression) {
        super(curve, x, y);
        boolean z = false;
        if ((x == null) == (y == null ? true : z)) {
            this.withCompression = withCompression;
            return;
        }
        throw new IllegalArgumentException("Exactly one of the field elements is null");
    }

    SecP521R1Point(ECCurve curve, ECFieldElement x, ECFieldElement y, ECFieldElement[] zs, boolean withCompression) {
        super(curve, x, y, zs);
        this.withCompression = withCompression;
    }

    /* access modifiers changed from: protected */
    public ECPoint detach() {
        return new SecP521R1Point(null, getAffineXCoord(), getAffineYCoord());
    }

    public ECPoint add(ECPoint b) {
        int[] S2;
        int[] U2;
        int[] S1;
        int[] U1;
        int[] t4;
        SecP521R1FieldElement Y3;
        ECPoint eCPoint = b;
        if (isInfinity()) {
            return eCPoint;
        }
        if (b.isInfinity()) {
            return this;
        }
        if (this == eCPoint) {
            return twice();
        }
        ECCurve curve = getCurve();
        SecP521R1FieldElement X1 = (SecP521R1FieldElement) this.x;
        SecP521R1FieldElement Y1 = (SecP521R1FieldElement) this.y;
        SecP521R1FieldElement X2 = (SecP521R1FieldElement) b.getXCoord();
        SecP521R1FieldElement Y2 = (SecP521R1FieldElement) b.getYCoord();
        SecP521R1FieldElement Z1 = (SecP521R1FieldElement) this.zs[0];
        SecP521R1FieldElement Z2 = (SecP521R1FieldElement) eCPoint.getZCoord(0);
        int[] t1 = Nat.create(17);
        int[] t2 = Nat.create(17);
        int[] t3 = Nat.create(17);
        int[] t42 = Nat.create(17);
        boolean Z1IsOne = Z1.isOne();
        if (Z1IsOne) {
            U2 = X2.x;
            S2 = Y2.x;
        } else {
            S2 = t3;
            SecP521R1Field.square(Z1.x, S2);
            U2 = t2;
            SecP521R1Field.multiply(S2, X2.x, U2);
            SecP521R1Field.multiply(S2, Z1.x, S2);
            SecP521R1Field.multiply(S2, Y2.x, S2);
        }
        int[] S22 = S2;
        boolean Z2IsOne = Z2.isOne();
        if (Z2IsOne) {
            U1 = X1.x;
            S1 = Y1.x;
        } else {
            S1 = t42;
            SecP521R1Field.square(Z2.x, S1);
            U1 = t1;
            SecP521R1Field.multiply(S1, X1.x, U1);
            SecP521R1Field.multiply(S1, Z2.x, S1);
            SecP521R1Field.multiply(S1, Y1.x, S1);
        }
        int[] U12 = U1;
        SecP521R1FieldElement secP521R1FieldElement = X1;
        int[] H = Nat.create(17);
        SecP521R1Field.subtract(U12, U2, H);
        int[] R = t2;
        SecP521R1Field.subtract(S1, S22, R);
        int[] S23 = S22;
        if (!Nat.isZero(17, H)) {
            int[] HSquared = t3;
            SecP521R1Field.square(H, HSquared);
            int[] iArr = U2;
            int[] U22 = Nat.create(17);
            SecP521R1Field.multiply(HSquared, H, U22);
            SecP521R1FieldElement secP521R1FieldElement2 = Y1;
            int[] V = t3;
            SecP521R1Field.multiply(HSquared, U12, V);
            SecP521R1Field.multiply(S1, U22, t1);
            int[] iArr2 = U12;
            SecP521R1FieldElement X3 = new SecP521R1FieldElement(t42);
            int[] S12 = S1;
            SecP521R1Field.square(R, X3.x);
            int[] HSquared2 = HSquared;
            SecP521R1Field.add(X3.x, U22, X3.x);
            SecP521R1Field.subtract(X3.x, V, X3.x);
            SecP521R1Field.subtract(X3.x, V, X3.x);
            SecP521R1FieldElement Y32 = new SecP521R1FieldElement(U22);
            int[] G = U22;
            SecP521R1Field.subtract(V, X3.x, Y32.x);
            SecP521R1Field.multiply(Y32.x, R, t2);
            SecP521R1Field.subtract(t2, t1, Y32.x);
            SecP521R1FieldElement Z3 = new SecP521R1FieldElement(H);
            if (!Z1IsOne) {
                Y3 = Y32;
                t4 = t42;
                SecP521R1Field.multiply(Z3.x, Z1.x, Z3.x);
            } else {
                Y3 = Y32;
                t4 = t42;
            }
            if (!Z2IsOne) {
                SecP521R1Field.multiply(Z3.x, Z2.x, Z3.x);
            }
            ECFieldElement[] zs = {Z3};
            int[] iArr3 = R;
            int[] iArr4 = S12;
            int[] iArr5 = S23;
            int[] S24 = HSquared2;
            SecP521R1FieldElement Y33 = Y3;
            SecP521R1FieldElement secP521R1FieldElement3 = Z3;
            int[] iArr6 = G;
            int[] iArr7 = t4;
            SecP521R1FieldElement secP521R1FieldElement4 = Y33;
            int[] t43 = t2;
            SecP521R1Point secP521R1Point = new SecP521R1Point(curve, X3, secP521R1FieldElement4, zs, this.withCompression);
            return secP521R1Point;
        } else if (Nat.isZero(17, R)) {
            return twice();
        } else {
            return curve.getInfinity();
        }
    }

    public ECPoint twice() {
        if (isInfinity()) {
            return this;
        }
        ECCurve curve = getCurve();
        SecP521R1FieldElement Y1 = (SecP521R1FieldElement) this.y;
        if (Y1.isZero()) {
            return curve.getInfinity();
        }
        SecP521R1FieldElement X1 = (SecP521R1FieldElement) this.x;
        SecP521R1FieldElement Z1 = (SecP521R1FieldElement) this.zs[0];
        int[] t1 = Nat.create(17);
        int[] t2 = Nat.create(17);
        int[] Y1Squared = Nat.create(17);
        SecP521R1Field.square(Y1.x, Y1Squared);
        int[] T = Nat.create(17);
        SecP521R1Field.square(Y1Squared, T);
        boolean Z1IsOne = Z1.isOne();
        int[] Z1Squared = Z1.x;
        if (!Z1IsOne) {
            Z1Squared = t2;
            SecP521R1Field.square(Z1.x, Z1Squared);
        }
        int[] Z1Squared2 = Z1Squared;
        SecP521R1Field.subtract(X1.x, Z1Squared2, t1);
        int[] M = t2;
        SecP521R1Field.add(X1.x, Z1Squared2, M);
        SecP521R1Field.multiply(M, t1, M);
        Nat.addBothTo(17, M, M, M);
        SecP521R1Field.reduce23(M);
        int[] S = Y1Squared;
        SecP521R1Field.multiply(Y1Squared, X1.x, S);
        Nat.shiftUpBits(17, S, 2, 0);
        SecP521R1Field.reduce23(S);
        Nat.shiftUpBits(17, T, 3, 0, t1);
        SecP521R1Field.reduce23(t1);
        SecP521R1FieldElement X3 = new SecP521R1FieldElement(T);
        SecP521R1Field.square(M, X3.x);
        SecP521R1Field.subtract(X3.x, S, X3.x);
        SecP521R1Field.subtract(X3.x, S, X3.x);
        SecP521R1FieldElement Y3 = new SecP521R1FieldElement(S);
        SecP521R1FieldElement X32 = X3;
        SecP521R1Field.subtract(S, X3.x, Y3.x);
        SecP521R1Field.multiply(Y3.x, M, Y3.x);
        SecP521R1Field.subtract(Y3.x, t1, Y3.x);
        SecP521R1FieldElement Z3 = new SecP521R1FieldElement(M);
        SecP521R1FieldElement Y32 = Y3;
        SecP521R1Field.twice(Y1.x, Z3.x);
        if (!Z1IsOne) {
            int[] iArr = S;
            SecP521R1Field.multiply(Z3.x, Z1.x, Z3.x);
        }
        SecP521R1FieldElement secP521R1FieldElement = Z3;
        int[] iArr2 = M;
        ECFieldElement[] eCFieldElementArr = {Z3};
        int[] iArr3 = Z1Squared2;
        SecP521R1Point secP521R1Point = new SecP521R1Point(curve, X32, Y32, eCFieldElementArr, this.withCompression);
        return secP521R1Point;
    }

    public ECPoint twicePlus(ECPoint b) {
        if (this == b) {
            return threeTimes();
        }
        if (isInfinity()) {
            return b;
        }
        if (b.isInfinity()) {
            return twice();
        }
        if (this.y.isZero()) {
            return b;
        }
        return twice().add(b);
    }

    public ECPoint threeTimes() {
        if (isInfinity() || this.y.isZero()) {
            return this;
        }
        return twice().add(this);
    }

    /* access modifiers changed from: protected */
    public ECFieldElement two(ECFieldElement x) {
        return x.add(x);
    }

    /* access modifiers changed from: protected */
    public ECFieldElement three(ECFieldElement x) {
        return two(x).add(x);
    }

    /* access modifiers changed from: protected */
    public ECFieldElement four(ECFieldElement x) {
        return two(two(x));
    }

    /* access modifiers changed from: protected */
    public ECFieldElement eight(ECFieldElement x) {
        return four(two(x));
    }

    /* access modifiers changed from: protected */
    public ECFieldElement doubleProductFromSquares(ECFieldElement a, ECFieldElement b, ECFieldElement aSquared, ECFieldElement bSquared) {
        return a.add(b).square().subtract(aSquared).subtract(bSquared);
    }

    public ECPoint negate() {
        if (isInfinity()) {
            return this;
        }
        SecP521R1Point secP521R1Point = new SecP521R1Point(this.curve, this.x, this.y.negate(), this.zs, this.withCompression);
        return secP521R1Point;
    }
}
