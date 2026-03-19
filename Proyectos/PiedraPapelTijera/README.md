# Piedra, Papel o Tijera

Proyecto Java con Maven para jugar **Piedra, Papel o Tijera** contra la computadora mediante una interfaz gráfica en Swing.

## Directorio del proyecto

El proyecto quedó armado sobre la estructura estándar de Maven dentro de esta carpeta:

```text
Proyectos/PiedraPapelTijera
├── .vscode
│   ├── extensions.json
│   ├── launch.json
│   └── settings.json
├── pom.xml
├── README.md
├── src
│   ├── main
│   │   └── java
│   │       └── ar/fiuba/piedrapapeltijera
│   │           ├── dominio
│   │           ├── infraestructura
│   │           └── interfaz
│   └── test
│       └── java
│           └── ar/fiuba/piedrapapeltijera
│               ├── dominio
│               └── integracion
└── .gitignore
```

## Objetivos del diseño

- Mantener nombres expresivos en español.
- Separar responsabilidades para respetar SOLID.
- Favorecer clases pequeñas, fáciles de extender y probar.
- Evitar herencia entre clases concretas.
- Modelar la lógica con colaboración entre objetos en lugar de condicionales distribuidos.

## Estructura funcional

- `dominio`: reglas, ronda, marcador, estado del juego y motor principal.
- `infraestructura`: implementación aleatoria de la jugada de la computadora.
- `interfaz`: ventana principal, controlador y textos de presentación.
- `src/test/java`: pruebas unitarias y de integración.

## Requisitos

- Java 17 o superior.
- Maven 3.9 o superior.
- Visual Studio Code con extensiones de Java si querés trabajar desde el editor.

## Ejecutar la aplicación

```bash
mvn exec:java
```

## Ejecutar los tests

```bash
mvn test
```

## Configuración de Visual Studio Code

### 1. Abrir la carpeta correcta

Abrí directamente la carpeta `Proyectos/PiedraPapelTijera` como workspace de VS Code para que detecte automáticamente el `pom.xml`.

### 2. Instalar las extensiones recomendadas

El proyecto ya incluye `.vscode/extensions.json`, así que VS Code debería sugerirte automáticamente:

- **Extension Pack for Java**
- **Maven for Java**
- **Java Test Runner**

### 3. Configurar el JDK

Asegurate de tener un JDK 17 instalado y que `JAVA_HOME` apunte a ese JDK.

Ejemplo en Windows:

```powershell
setx JAVA_HOME "C:\\Program Files\\Java\\jdk-17"
```

Ejemplo en Linux o macOS:

```bash
export JAVA_HOME=/ruta/al/jdk-17
```

### 4. Esperar la importación del proyecto

Cuando abras la carpeta, VS Code debería:

- detectar `pom.xml`
- descargar dependencias
- indexar clases y tests
- habilitar el panel de Maven y Testing

### 5. Ejecutar el juego

Tenés dos formas:

- desde **Run and Debug**, usando la configuración incluida `Abrir PiedraPapelTijera`
- desde la terminal integrada con `mvn exec:java`

### 6. Ejecutar los tests

También tenés dos formas:

- desde el panel **Testing** de VS Code
- desde la terminal con `mvn test`

## Principios de diseño aplicados

- **Single Responsibility**: cada clase resuelve una necesidad concreta.
- **Open/Closed**: la fuente de jugada de la computadora puede cambiar sin modificar el motor del juego.
- **Liskov Substitution**: cualquier implementación de `FuenteDeJugadaDeLaComputadora` puede usarse en el juego.
- **Interface Segregation**: la interfaz de la fuente expone una única operación simple.
- **Dependency Inversion**: el juego depende de una abstracción para obtener la jugada de la computadora.

## Tests incluidos

- Tests unitarios para la tabla de resultados.
- Tests unitarios para el marcador.
- Tests unitarios para la presentación del estado.
- Test de integración para el flujo principal del juego.
