package com.psiqueylogosac.mispacientes

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

class Preferencias(context: Context)
{

    private var prefs = context.getSharedPreferences ("ajustes", AppCompatActivity.MODE_PRIVATE)

    var usuarioId : String?
        get() = prefs.getString("usuarioId",null)
        set(value) = prefs.edit().putString("usuarioId",value).apply()



}