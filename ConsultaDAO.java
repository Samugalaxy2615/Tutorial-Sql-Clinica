package com.example.clinica.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class ConsultaDAO {

    private DBHelper dbHelper;

    public ConsultaDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void insertarConsulta(String idPaciente, String idMedico, String sintomas, String diagnostico, String tratamiento) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("id_paciente", idPaciente);
        valores.put("id_medico", idMedico);
        valores.put("sintomas", sintomas);
        valores.put("diagnostico", diagnostico);
        valores.put("tratamiento", tratamiento);
        db.insert("Consulta", null, valores);
        db.close();
    }
}
