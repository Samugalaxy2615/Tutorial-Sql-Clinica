package com.example.clinica.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "clinica.db";
    public static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Paciente (id TEXT PRIMARY KEY, nombre TEXT, edad INTEGER, historial TEXT)");
        db.execSQL("CREATE TABLE Medico (id TEXT PRIMARY KEY, nombre TEXT, especialidad TEXT)");
        db.execSQL("CREATE TABLE Consulta (id INTEGER PRIMARY KEY AUTOINCREMENT, id_paciente TEXT, id_medico TEXT, sintomas TEXT, diagnostico TEXT, tratamiento TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Paciente");
        db.execSQL("DROP TABLE IF EXISTS Medico");
        db.execSQL("DROP TABLE IF EXISTS Consulta");
        onCreate(db);
    }
}
