(ns sistemas-l.svg
  (:require [clojure.java.io :as io])
  (:import (java.util Locale)))

(def DISTANCIA 3)
(def ANGULO-INICIAL 0)

(def MARGEN-SVG 20)
(def SVG-ANCHO 1200)
(def SVG-ALTO 1200)
(def COLOR-LINEA "black")
(def GROSOR-LINEA 0.5)

(defn calcular-limites [lineas]
  (let [xs (mapcat (fn [[x1 _ x2 _]] [x1 x2]) lineas)
        ys (mapcat (fn [[_ y1 _ y2]] [y1 y2]) lineas)]
    {:min-x (double (apply min xs))
     :max-x (double (apply max xs))
     :min-y (double (apply min ys))
     :max-y (double (apply max ys))}))

(defn guardar-svg [archivo lineas]
  (let [{:keys [min-x max-x min-y max-y]} (calcular-limites lineas)
        min-x (- min-x MARGEN-SVG)
        min-y (- min-y MARGEN-SVG)
        max-x (+ max-x MARGEN-SVG)
        max-y (+ max-y MARGEN-SVG)
        ancho (- max-x min-x)
        alto (- max-y min-y)]
    (println (format "Rango X: %.2f - %.2f" min-x max-x))
    (println (format "Rango Y: %.2f - %.2f" min-y max-y))
    (with-open [w (io/writer archivo)]
      (.write w "<?xml version=\"1.0\" standalone=\"no\"?>\n")
      (.write w (String/format Locale/US
                               "<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" width=\"%d\" height=\"%d\" viewBox=\"%f %f %f %f\">\n"
                               (into-array Object [SVG-ANCHO SVG-ALTO min-x min-y ancho alto])))
      (doseq [[x1 y1 x2 y2] lineas]
        (.write w (String/format Locale/US
                                 (str "<line x1=\"%f\" y1=\"%f\" x2=\"%f\" y2=\"%f\" stroke=\"" COLOR-LINEA "\" stroke-width=\"" GROSOR-LINEA "\" />\n")
                                 (into-array Object [(double x1) (double y1) (double x2) (double y2)]))))
      (.write w "</svg>\n"))))