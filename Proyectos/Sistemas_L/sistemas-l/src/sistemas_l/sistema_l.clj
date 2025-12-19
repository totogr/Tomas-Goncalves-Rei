(ns sistemas-l.sistema-l
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn aplicar-reglas [reglas cadena]
  (apply str (map #(get reglas % (str %)) cadena)))

(defn generar-systema-l [axioma reglas n]
  (nth (iterate #(aplicar-reglas reglas %) axioma) n))

(defn leer-sistema-l [archivo]
  (let [lineas (line-seq (io/reader archivo))
        angulo (Double/parseDouble (first lineas))
        axioma (second lineas)
        reglas (into {}
                     (map (fn [linea]
                            (let [[pred suc] (str/split linea #" " 2)]
                              [(first pred) suc]))
                          (drop 2 lineas)))]
    {:angulo angulo :axioma axioma :reglas reglas}))