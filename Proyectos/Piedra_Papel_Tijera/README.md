# TP0-WARM-UP - Piedra, Papel o Tijera

Proyecto Java con Maven para jugar **Piedra, Papel o Tijera** contra la computadora mediante una interfaz gráfica en Swing.

## Directorio del proyecto

El proyecto quedó armado sobre la estructura estándar de Maven dentro de esta carpeta:

```text
PiedraPapelTijera
├── .vscode
│   └── settings.json
├── pom.xml
├── README.md
├── src
│   ├── main
│   │   └── java
│   │        ├── dominio
│   │        ├── infraestructura
│   │        └── interfaz
│   └── test
│       └── java
│            ├── dominio
│            └── integracion
└── .gitignore
```

## Objetivos del diseño

- Separar responsabilidades para respetar SOLID.
- Favorecer clases pequeñas, fáciles de extender y probar.
- Evitar herencia entre clases concretas.
- Modelar la lógica con colaboración entre objetos en lugar de condicionales distribuidos.

## Estructura funcional

- `dominio`: jugadas, resultados, jugadores, rondas, marcador, estado del juego y motor principal.
- `infraestructura`: implementación aleatoria de un `Player` para la computadora.
- `interfaz`: ventana principal, controlador y presentación del estado.
- `src/test/java`: pruebas unitarias y de integración.

## Ejecutar la aplicación

```bash
mvn exec:java
```

## Ejecutar los tests

```bash
mvn test
```

## Principios de diseño aplicados

- **Single Responsibility**: cada clase resuelve una necesidad concreta.
- **Open/Closed**: cualquier `Player` puede incorporarse al juego sin modificar el motor principal.
- **Liskov Substitution**: cualquier implementación de `Player` puede reemplazar a otra sin romper el comportamiento esperado.
- **Interface Segregation**: `Player` expone una única operación simple: `getMove()`.
- **Dependency Inversion**: el juego depende de la abstracción `Player`, no de una implementación concreta.

## Tests incluidos

- Tests unitarios para reglas de jugadas con `vs(...)`.
- Test unitario para uso extensible mediante `Player`.
- Tests unitarios para el marcador y la presentación del estado.
- Test de integración para el flujo principal del juego.