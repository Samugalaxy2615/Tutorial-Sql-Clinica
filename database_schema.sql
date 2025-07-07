-- Base de datos simulada para la Clínica

CREATE TABLE Paciente (
    id TEXT PRIMARY KEY,
    nombre TEXT,
    edad INTEGER,
    historial TEXT
);

CREATE TABLE Medico (
    id TEXT PRIMARY KEY,
    nombre TEXT,
    especialidad TEXT
);

CREATE TABLE Consulta (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_paciente TEXT,
    id_medico TEXT,
    sintomas TEXT,
    diagnostico TEXT,
    tratamiento TEXT
);
