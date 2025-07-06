# Tutorial: Aplicar persistencia con SQLite en una app de clínica Android

Este tutorial describe cómo implementar persistencia de datos mediante **SQLite** en una aplicación Android desarrollada con Java. Se reemplaza la serialización con almacenamiento estructurado y duradero a través de bases de datos.

---

## Introducción

El modelo actual de la app utiliza archivos `.dat` para guardar listas de `Paciente`, `Medico` y `Consulta`. Este tutorial adapta la arquitectura para trabajar con **SQLite**, usando clases auxiliares como `SQLiteOpenHelper`.

---

## Paso 1: Crear la clase `ClinicaDBHelper`

Crear una nueva clase que extienda `SQLiteOpenHelper`. Esto manejará la creación y actualización de la base de datos.

```java
public class ClinicaDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "clinica.db";
    private static final int DB_VERSION = 1;

    public ClinicaDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Paciente (id TEXT PRIMARY KEY, nombre TEXT, edad INTEGER, historial TEXT)");
        db.execSQL("CREATE TABLE Medico (id TEXT PRIMARY KEY, nombre TEXT, especialidad TEXT)");
        db.execSQL("CREATE TABLE Consulta (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            idPaciente TEXT,
            idMedico TEXT,
            sintomas TEXT,
            diagnostico TEXT,
            tratamiento TEXT,
            FOREIGN KEY(idPaciente) REFERENCES Paciente(id),
            FOREIGN KEY(idMedico) REFERENCES Medico(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Consulta");
        db.execSQL("DROP TABLE IF EXISTS Medico");
        db.execSQL("DROP TABLE IF EXISTS Paciente");
        onCreate(db);
    }
}
```

---

## Paso 2: Insertar pacientes, médicos y consultas

Ejemplo para insertar un `Paciente`:

```java
public void guardarPaciente(SQLiteDatabase db, Paciente p) {
    ContentValues values = new ContentValues();
    values.put("id", p.getIdentificacion());
    values.put("nombre", p.getNombre());
    values.put("edad", p.getEdad());
    values.put("historial", p.getHistorial());
    db.insert("Paciente", null, values);
}
```

Ejemplo para insertar un `Medico`:

```java
public void guardarMedico(SQLiteDatabase db, Medico m) {
    ContentValues values = new ContentValues();
    values.put("id", m.getIdentificacion());
    values.put("nombre", m.getNombre());
    values.put("especialidad", m.getEspecialidad());
    db.insert("Medico", null, values);
}
```

Ejemplo para insertar una `Consulta`:

```java
public void guardarConsulta(SQLiteDatabase db, Consulta c) {
    ContentValues values = new ContentValues();
    values.put("idPaciente", c.getPaciente().getIdentificacion());
    values.put("idMedico", c.getMedico().getIdentificacion());
    values.put("sintomas", c.getSintomas());
    values.put("diagnostico", c.getDiagnostico());
    values.put("tratamiento", c.getTratamiento());
    db.insert("Consulta", null, values);
}
```

---

## Paso 3: Consultar datos

Obtener todos los pacientes:

```java
public List<Paciente> obtenerPacientes(SQLiteDatabase db) {
    List<Paciente> pacientes = new ArrayList<>();
    Cursor cursor = db.rawQuery("SELECT * FROM Paciente", null);
    while (cursor.moveToNext()) {
        String id = cursor.getString(0);
        String nombre = cursor.getString(1);
        int edad = cursor.getInt(2);
        String historial = cursor.getString(3);
        pacientes.add(new Paciente(nombre, edad, historial, id));
    }
    cursor.close();
    return pacientes;
}
```

Obtener el historial de un paciente:

```java
public List<Consulta> obtenerHistorialPaciente(SQLiteDatabase db, String idPaciente) {
    List<Consulta> historial = new ArrayList<>();
    Cursor cursor = db.rawQuery("SELECT * FROM Consulta WHERE idPaciente = ?", new String[]{idPaciente});
    while (cursor.moveToNext()) {
        String idMedico = cursor.getString(2);
        String sintomas = cursor.getString(3);
        String diagnostico = cursor.getString(4);
        String tratamiento = cursor.getString(5);

        // Se puede usar métodos auxiliares para obtener médico y paciente
        Paciente paciente = buscarPacientePorId(db, idPaciente);
        Medico medico = buscarMedicoPorId(db, idMedico);

        historial.add(new Consulta(paciente, medico, sintomas, diagnostico, tratamiento));
    }
    cursor.close();
    return historial;
}
```

---

## Paso 4: Conectar la base de datos con las Activities

Modificar las activities como `RegistrarPacienteActivity` para que en lugar de usar `Clinica.cargarDesdeArchivo`, utilicen una instancia de `ClinicaDBHelper` y obtengan el `SQLiteDatabase`:

```java
ClinicaDBHelper dbHelper = new ClinicaDBHelper(getApplicationContext());
SQLiteDatabase db = dbHelper.getWritableDatabase();
guardarPaciente(db, paciente);
```

---

## Paso 5: Consideraciones

* Las entidades (`Paciente`, `Medico`, `Consulta`) deben mantenerse sin cambios.
* La clase `Clinica` se puede reemplazar por `ClinicaDBHelper`, eliminando la serialización.
* Se debe cerrar el cursor y la base de datos si no se usa `try-with-resources`.
* Asegurar que los ID no se repitan para evitar errores de clave primaria.

---

## Resultado esperado

Una vez implementado:

* Al registrar un paciente, médico o consulta, estos se guardarán en la base de datos.
* Al cerrar y volver a abrir la app, los datos seguirán disponibles.
* Las pantallas de consulta, historial y listado funcionarán sin depender de archivos `.dat`.



