package com.example.clinica.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class PacienteDAO {

    private DBHelper dbHelper;

    public PacienteDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void insertarPaciente(String id, String nombre, int edad, String historial) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("id", id);
        valores.put("nombre", nombre);
        valores.put("edad", edad);
        valores.put("historial", historial);
        db.insert("Paciente", null, valores);
        db.close();
    }
}
