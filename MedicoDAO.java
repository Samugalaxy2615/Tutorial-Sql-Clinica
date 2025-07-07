package com.example.clinica.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class MedicoDAO {

    private DBHelper dbHelper;

    public MedicoDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void insertarMedico(String id, String nombre, String especialidad) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("id", id);
        valores.put("nombre", nombre);
        valores.put("especialidad", especialidad);
        db.insert("Medico", null, valores);
        db.close();
    }
}
