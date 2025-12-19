(ns sistemas-l.core
  (:gen-class)
  (:require [sistemas-l.sistema-l :refer [leer-sistema-l generar-systema-l]]
            [sistemas-l.tortuga :refer [tortuga]]
            [sistemas-l.svg :refer [guardar-svg DISTANCIA ANGULO-INICIAL]]))

(defn -main [& args]
  (let [[archivo-sl cant-iteraciones archivo-svg] args
        n (Integer/parseInt cant-iteraciones)
        {:keys [angulo axioma reglas]} (leer-sistema-l archivo-sl)
        cadena (generar-systema-l axioma reglas n)
        lineas (tortuga cadena DISTANCIA ANGULO-INICIAL angulo)]
    (println "Cantidad de lineas generadas:" (count lineas))
    (guardar-svg archivo-svg lineas)))
