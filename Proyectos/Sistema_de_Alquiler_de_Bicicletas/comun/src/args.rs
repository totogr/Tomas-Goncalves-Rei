//! Parsing mínimo de argumentos de línea de comandos, sin crates externas.

/// Devuelve el valor que sigue a una bandera `--nombre valor` en los argumentos
/// del proceso, o `None` si la bandera no está presente o no tiene valor.
///
/// Si el token siguiente es **otra bandera** (empieza con `--`), se considera que
/// la bandera no tiene valor. Así, un comando mal escrito como
/// `--config --estado archivo.json` no toma `--estado` como valor de `--config`
/// (que terminaba en un críptico "NotSuchFile" al intentar abrir un archivo
/// llamado "--estado"); en su lugar falla con "falta el argumento --config".
pub fn flag(nombre: &str) -> Option<String> {
    let args: Vec<String> = std::env::args().collect();
    let pos = args.iter().position(|a| a == nombre)?;
    let valor = args.get(pos + 1)?;
    if valor.starts_with("--") {
        return None;
    }
    Some(valor.clone())
}
