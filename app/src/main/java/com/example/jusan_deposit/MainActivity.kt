package com.example.jusan_deposit

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {
    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarPercentAmount: SeekBar
    private lateinit var tvPercentage: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvSumAmount: TextView
    private lateinit var tvPercDesc: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarPercentAmount = findViewById(R.id.seekBarPercentAmount)
        tvPercentage = findViewById(R.id.tvPercentage)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvSumAmount = findViewById(R.id.tvSumAmount)
        tvPercDesc = findViewById(R.id.tvPercDesc)

        seekBarPercentAmount.progress = INITIAL_TIP_PERCENT
        tvPercentage.text = "$INITIAL_TIP_PERCENT%"
        updTipDesc(INITIAL_TIP_PERCENT)

        seekBarPercentAmount.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tvPercentage.text = "$progress%"
                computeTipAndSum()
                updTipDesc(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })

        etBaseAmount.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipAndSum()
            }

        })
    }

    private fun updTipDesc(p: Int) {
        val tipDesc = when(p){
            in 0..9 -> "Пенсионный"
            in 10..14 -> "Оптимальный"
            in 15..19 -> "Комфорт"
            in 20..24 -> "Бизнесмен"
            else -> "Максимум"
        }
        tvPercDesc.text = tipDesc
        val color = ArgbEvaluator().evaluate(
            seekBarPercentAmount.progress.toFloat() / seekBarPercentAmount.max,
            ContextCompat.getColor(this, R.color.worst),
            ContextCompat.getColor(this, R.color.best)
        ) as Int
        tvPercDesc.setTextColor(color)
    }

    private fun computeTipAndSum() {
        if(etBaseAmount.text.isEmpty()){
            tvTipAmount.text = ""
            tvSumAmount.text = ""
            return
        }
        //1.
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarPercentAmount.progress
        //2.
        val tipAmount = baseAmount * tipPercent / 100
        val sumAmount = baseAmount + tipAmount
        //3.
        tvTipAmount.text = "%.2f".format(tipAmount)
        tvSumAmount.text = "%.2f".format(sumAmount)
    }
}