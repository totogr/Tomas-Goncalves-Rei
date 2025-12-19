(ns sistemas-l.tortuga)

(def POSICION-INICIAL-X 500)
(def POSICION-INICIAL-Y 500)

(defn grados-a-radianes [grados]
  (* (/ Math/PI 180) grados))

(defn avanzar [x y angulo distancia]
  (let [radianes (grados-a-radianes (- angulo 90))]
    [(+ x (* distancia (Math/cos radianes)))
     (+ y (* distancia (Math/sin radianes)))]))

(defn tortuga [cadena distancia angulo-inicial angulo-giro]
  (loop [secuencia (seq cadena)
         estado {:x POSICION-INICIAL-X
                 :y POSICION-INICIAL-Y
                 :angulo angulo-inicial
                 :pila []
                 :lineas []}]
    (if (empty? secuencia)
      (:lineas estado)
      (let [{:keys [x y angulo pila lineas]} estado
            c (first secuencia)]
        (case c
          \F (let [[nx ny] (avanzar x y angulo distancia)]
               (recur (rest secuencia) {:x nx :y ny :angulo angulo
                                        :pila pila
                                        :lineas (conj lineas [x y nx ny])}))
          \+ (recur (rest secuencia) (assoc estado :angulo (- angulo angulo-giro)))
          \- (recur (rest secuencia) (assoc estado :angulo (+ angulo angulo-giro)))
          \[ (recur (rest secuencia) (assoc estado :pila (conj pila [x y angulo])))
          \] (let [[nx ny na] (peek pila)]
               (recur (rest secuencia) (assoc estado :x nx :y ny :angulo na :pila (pop pila))))
          (recur (rest secuencia) estado))))))