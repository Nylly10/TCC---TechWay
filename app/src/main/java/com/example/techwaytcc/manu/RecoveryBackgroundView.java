package com.example.techwaytcc.manu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class RecoveryBackgroundView extends View {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // Cores do protótipo
    private static final int COLOR_LILAC = 0xFFD9D3E1;
    private static final int COLOR_NAVY = 0xFF10182D;

    public RecoveryBackgroundView(Context context) {
        super(context);
        configurar();
    }

    public RecoveryBackgroundView(
            Context context,
            AttributeSet attrs
    ) {
        super(context, attrs);
        configurar();
    }

    public RecoveryBackgroundView(
            Context context,
            AttributeSet attrs,
            int defStyleAttr
    ) {
        super(context, attrs, defStyleAttr);
        configurar();
    }

    private void configurar() {

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        float w = getWidth();
        float h = getHeight();

        // =====================================================
        // FUNDO LILÁS
        // =====================================================

        paint.setColor(COLOR_LILAC);

        canvas.drawRect(
                0,
                0,
                w,
                h,
                paint
        );


        // =====================================================
        // FORMA AZUL-MARINHO
        //
        // A forma abaixo foi construída para reproduzir
        // as duas curvas principais do protótipo:
        //
        // 1. Curva diagonal superior
        // 2. Curva diagonal inferior
        //
        // O azul fica como uma grande área contínua
        // no centro da tela.
        // =====================================================

        Path navy = new Path();


        // -----------------------------------------------------
        // INÍCIO DA CURVA SUPERIOR
        //
        // No protótipo:
        // o lilás ocupa o canto superior esquerdo
        // e o azul começa aproximadamente na região
        // esquerda/meio da tela.
        // -----------------------------------------------------

        navy.moveTo(
                0,
                h * 0.345f
        );


        // -----------------------------------------------------
        // CURVA SUPERIOR - PRIMEIRO TRECHO
        // -----------------------------------------------------

        navy.cubicTo(
                w * 0.08f,
                h * 0.335f,
                w * 0.18f,
                h * 0.245f,
                w * 0.28f,
                h * 0.145f
        );


        // -----------------------------------------------------
        // CURVA SUPERIOR - SEGUNDO TRECHO
        // -----------------------------------------------------

        navy.cubicTo(
                w * 0.39f,
                h * 0.045f,
                w * 0.55f,
                0,
                w * 0.72f,
                0
        );


        // -----------------------------------------------------
        // TOPO / CANTO DIREITO
        // -----------------------------------------------------

        navy.cubicTo(
                w * 0.84f,
                0,
                w * 0.94f,
                h * 0.035f,
                w,
                h * 0.12f
        );


        // -----------------------------------------------------
        // LADO DIREITO
        //
        // O azul continua até aproximadamente a metade
        // inferior da tela.
        // -----------------------------------------------------

        navy.lineTo(
                w,
                h * 0.615f
        );


        // -----------------------------------------------------
        // CURVA INFERIOR - PRIMEIRO TRECHO
        // -----------------------------------------------------

        navy.cubicTo(
                w * 0.94f,
                h * 0.615f,
                w * 0.90f,
                h * 0.635f,
                w * 0.865f,
                h * 0.685f
        );


        // -----------------------------------------------------
        // CURVA INFERIOR - SEGUNDO TRECHO
        // -----------------------------------------------------

        navy.cubicTo(
                w * 0.81f,
                h * 0.765f,
                w * 0.76f,
                h * 0.835f,
                w * 0.68f,
                h * 0.895f
        );


        // -----------------------------------------------------
        // CURVA INFERIOR - TERCEIRO TRECHO
        // -----------------------------------------------------

        navy.cubicTo(
                w * 0.59f,
                h * 0.945f,
                w * 0.49f,
                h * 0.975f,
                w * 0.37f,
                h
        );


        // -----------------------------------------------------
        // BASE
        // -----------------------------------------------------

        navy.lineTo(
                0,
                h
        );


        // -----------------------------------------------------
        // FECHA A FORMA ATÉ O PONTO INICIAL
        // -----------------------------------------------------

        navy.close();


        // =====================================================
        // DESENHAR AZUL
        // =====================================================

        paint.setColor(COLOR_NAVY);

        canvas.drawPath(
                navy,
                paint
        );
    }
}