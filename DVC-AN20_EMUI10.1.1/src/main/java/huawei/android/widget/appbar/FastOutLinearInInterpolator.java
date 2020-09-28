package huawei.android.widget.appbar;

import huawei.android.widget.LookupTableInterpolator;

public class FastOutLinearInInterpolator extends LookupTableInterpolator {
    private static final float[] VALUES = {0.0f, 1.0E-4f, 2.0E-4f, 5.0E-4f, 8.0E-4f, 0.0013f, 0.0018f, 0.0024f, 0.0032f, 0.004f, 0.0049f, 0.0059f, 0.0069f, 0.0081f, 0.0093f, 0.0106f, 0.012f, 0.0135f, 0.0151f, 0.0167f, 0.0184f, 0.0201f, 0.022f, 0.0239f, 0.0259f, 0.0279f, 0.03f, 0.0322f, 0.0345f, 0.0368f, 0.0391f, 0.0416f, 0.0441f, 0.0466f, 0.0492f, 0.0519f, 0.0547f, 0.0574f, 0.0603f, 0.0632f, 0.0662f, 0.0692f, 0.0722f, 0.0754f, 0.0785f, 0.0817f, 0.085f, 0.0884f, 0.0917f, 0.0952f, 0.0986f, 0.1021f, 0.1057f, 0.1093f, 0.113f, 0.1167f, 0.1205f, 0.1243f, 0.1281f, 0.132f, 0.1359f, 0.1399f, 0.1439f, 0.148f, 0.1521f, 0.1562f, 0.1604f, 0.1647f, 0.1689f, 0.1732f, 0.1776f, 0.182f, 0.1864f, 0.1909f, 0.1954f, 0.1999f, 0.2045f, 0.2091f, 0.2138f, 0.2184f, 0.2232f, 0.2279f, 0.2327f, 0.2376f, 0.2424f, 0.2473f, 0.2523f, 0.2572f, 0.2622f, 0.2673f, 0.2723f, 0.2774f, 0.2826f, 0.2877f, 0.2929f, 0.2982f, 0.3034f, 0.3087f, 0.3141f, 0.3194f, 0.3248f, 0.3302f, 0.3357f, 0.3412f, 0.3467f, 0.3522f, 0.3578f, 0.3634f, 0.369f, 0.3747f, 0.3804f, 0.3861f, 0.3918f, 0.3976f, 0.4034f, 0.4092f, 0.4151f, 0.421f, 0.4269f, 0.4329f, 0.4388f, 0.4448f, 0.4508f, 0.4569f, 0.463f, 0.4691f, 0.4752f, 0.4814f, 0.4876f, 0.4938f, 0.5f, 0.5063f, 0.5126f, 0.5189f, 0.5252f, 0.5316f, 0.538f, 0.5444f, 0.5508f, 0.5573f, 0.5638f, 0.5703f, 0.5768f, 0.5834f, 0.59f, 0.5966f, 0.6033f, 0.6099f, 0.6166f, 0.6233f, 0.6301f, 0.6369f, 0.6436f, 0.6505f, 0.6573f, 0.6642f, 0.671f, 0.678f, 0.6849f, 0.6919f, 0.6988f, 0.7059f, 0.7129f, 0.7199f, 0.727f, 0.7341f, 0.7413f, 0.7484f, 0.7556f, 0.7628f, 0.77f, 0.7773f, 0.7846f, 0.7919f, 0.7992f, 0.8066f, 0.814f, 0.8214f, 0.8288f, 0.8363f, 0.8437f, 0.8513f, 0.8588f, 0.8664f, 0.874f, 0.8816f, 0.8892f, 0.8969f, 0.9046f, 0.9124f, 0.9201f, 0.928f, 0.9358f, 0.9437f, 0.9516f, 0.9595f, 0.9675f, 0.9755f, 0.9836f, 0.9918f, 1.0f};

    public FastOutLinearInInterpolator() {
        super(VALUES);
    }
}
