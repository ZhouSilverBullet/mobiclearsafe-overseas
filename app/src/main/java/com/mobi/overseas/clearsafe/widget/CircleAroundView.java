package com.mobi.overseas.clearsafe.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.mobi.overseas.clearsafe.R;

public class CircleAroundView extends FrameLayout {

    /* renamed from: A */
    private int f12214A;

    /* renamed from: B */
    private int f12215B;

    /* renamed from: C */
    private int f12216C;

    /* renamed from: D */
    private int f12217D;

    /* renamed from: E */
    private PorterDuffXfermode f12218E;

    /* renamed from: a */
    private Bitmap f12219a;

    /* renamed from: b */
    private Path f12220b;

    /* renamed from: c */
    private Path f12221c;

    /* renamed from: d */
    private Path f12222d;

    /* renamed from: e */
    private Path f12223e;

    /* renamed from: f */
    private int f12224f;

    /* renamed from: g */
    private int f12225g;

    /* renamed from: h */
    private int f12226h;

    /* renamed from: i */
    private int f12227i;

    /* renamed from: j */
    private int f12228j;

    /* renamed from: k */
    private int f12229k;

    /* renamed from: l */
    private int f12230l;

    /* renamed from: m */
    private LinearGradient f12231m;

    /* renamed from: n */
    private Matrix f12232n;

    /* renamed from: o */
    private Paint f12233o;

    /* renamed from: p */
    private int f12234p;

    /* renamed from: q */
    private PathMeasure f12235q;

    /* renamed from: r */
    private RectF f12236r;

    /* renamed from: s */
    private float f12237s;

    /* renamed from: t */
    private int f12238t;

    /* renamed from: u */
    private Path f12239u;

    /* renamed from: v */
    private int f12240v;

    /* renamed from: w */
    private int f12241w;

    /* renamed from: x */
    private int f12242x;

    /* renamed from: y */
    private int f12243y;

    /* renamed from: z */
    private int f12244z;

    public CircleAroundView(Context context) {
        this(context, null);
    }

    public CircleAroundView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CircleAroundView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        m15429a(context, attributeSet);
    }

    /* renamed from: a */
    private void m15429a(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, new int[]{R.attr.around_unit, R.attr.around_unit});
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(1, 20);
        int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(0, 20);
        obtainStyledAttributes.recycle();
        this.f12232n = new Matrix();
        this.f12233o = new Paint(5);
        this.f12233o.setStyle(Style.STROKE);
        this.f12233o.setStrokeJoin(Join.ROUND);
        this.f12233o.setStrokeWidth((float) dimensionPixelSize);
        this.f12237s = (float) dimensionPixelSize2;
        this.f12218E = new PorterDuffXfermode(Mode.DST_IN);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.f12217D = i;
        this.f12226h = i2;
        this.f12235q = new PathMeasure();
        float f = (float) i2;
        this.f12236r = new RectF(0.0f, 0.0f, (float) i, f);
        this.f12239u = new Path();
        this.f12220b = new Path();
        this.f12221c = new Path();
        this.f12222d = new Path();
        this.f12223e = new Path();
        Path path = this.f12239u;
        RectF rectF = this.f12236r;
        float f2 = this.f12237s;
        path.addRoundRect(rectF, f2, f2, Direction.CW);
        this.f12235q.setPath(this.f12239u, true);
        this.f12234p = (int) this.f12235q.getLength();
        this.f12240v = 0;
        int i5 = this.f12234p;
        this.f12227i = i5 / 8;
        this.f12244z = this.f12240v + this.f12227i;
        this.f12214A = i5;
        this.f12228j = i5 / 8;
        this.f12241w = this.f12214A - this.f12228j;
        this.f12242x = (i5 / 2) - (i5 / 8);
        this.f12229k = i5 / 4;
        this.f12215B = this.f12242x + this.f12229k;
        this.f12243y = 0;
        this.f12230l = 0;
        this.f12216C = this.f12243y + this.f12230l;
        this.f12238t = (int) (((float) i5) * 0.01f);
        Paint paint = this.f12233o;
        LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, 0.0f, f, new int[]{Color.parseColor("#FF64A1"), Color.parseColor("#A643FF"), Color.parseColor("#64EBFF"), Color.parseColor("#FFFE39"), Color.parseColor("#FF9964")}, new float[]{0.2f, 0.4f, 0.6f, 0.8f, 1.0f}, TileMode.REPEAT);
        this.f12231m = linearGradient;
        paint.setShader(this.f12231m);
        this.f12219a = m15427a(getWidth(), getHeight());
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.f12236r != null) {
            m15431a(this.f12235q);
            m15432b();
            m15428a();
            m15430a(canvas);
            m15433c();
            invalidate();
        }
    }

    /* renamed from: a */
    private void m15428a() {
        int i = this.f12224f;
        int i2 = this.f12238t;
        this.f12224f = i + i2;
        this.f12225g += i2;
        this.f12232n.setTranslate((float) this.f12224f, (float) this.f12225g);
        this.f12231m.setLocalMatrix(this.f12232n);
    }

    /* renamed from: b */
    private void m15432b() {
        int i = this.f12244z;
        int i2 = this.f12234p;
        if (i >= i2) {
            this.f12214A = i2;
            this.f12241w = this.f12240v;
            this.f12240v = 0;
            this.f12244z = 1;
        }
        if (this.f12241w >= this.f12234p) {
            this.f12240v += this.f12238t;
        }
        int i3 = this.f12244z;
        int i4 = this.f12238t;
        this.f12244z = i3 + i4;
        this.f12241w += i4;
        this.f12242x += i4;
        this.f12215B = this.f12242x + this.f12229k;
        if (this.f12215B >= this.f12234p) {
            this.f12216C += i4;
        }
        if (this.f12242x >= this.f12234p) {
            this.f12216C = 0;
            this.f12243y = 0;
            this.f12242x = 0;
            this.f12215B = this.f12242x + this.f12229k;
        }
    }

    /* renamed from: a */
    private void m15430a(Canvas canvas) {
        int saveLayer = canvas.saveLayer(0.0f, 0.0f, (float) this.f12217D, (float) this.f12226h, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawPath(this.f12220b, this.f12233o);
        canvas.drawPath(this.f12221c, this.f12233o);
        canvas.drawPath(this.f12222d, this.f12233o);
        canvas.drawPath(this.f12223e, this.f12233o);
        this.f12233o.setXfermode(this.f12218E);
        canvas.drawBitmap(this.f12219a, 0.0f, 0.0f, this.f12233o);
        this.f12233o.setXfermode(null);
        canvas.restoreToCount(saveLayer);
    }

    /* renamed from: a */
    private void m15431a(PathMeasure pathMeasure) {
        pathMeasure.getSegment((float) this.f12240v, (float) this.f12244z, this.f12220b, true);
        pathMeasure.getSegment((float) this.f12241w, (float) this.f12214A, this.f12221c, true);
        pathMeasure.getSegment((float) this.f12242x, (float) this.f12215B, this.f12222d, true);
        pathMeasure.getSegment((float) this.f12243y, (float) this.f12216C, this.f12223e, true);
    }

    /* renamed from: c */
    private void m15433c() {
        this.f12220b.reset();
        this.f12221c.reset();
        this.f12222d.reset();
        this.f12223e.reset();
        this.f12220b.lineTo(0.0f, 0.0f);
        this.f12221c.lineTo(0.0f, 0.0f);
        this.f12222d.lineTo(0.0f, 0.0f);
        this.f12223e.lineTo(0.0f, 0.0f);
    }

    /* renamed from: a */
    private Bitmap m15427a(int i, int i2) {
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_4444);
        Canvas canvas = new Canvas(createBitmap);
        Path path = new Path();
        RectF rectF = new RectF(0.0f, 0.0f, (float) i, (float) i2);
        float f = this.f12237s;
        path.addRoundRect(rectF, f, f, Direction.CW);
        Paint paint = new Paint(1);
        paint.setColor(Color.parseColor("#0000ff"));
        canvas.drawPath(path, paint);
        return createBitmap;
    }
}