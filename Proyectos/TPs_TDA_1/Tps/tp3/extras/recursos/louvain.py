class Louvain:
    def __init__(self, grafo):
        self.grafo = grafo
        self.comunidades = {v: v for v in self.grafo.obtener_vertices()}
        self.peso_total_aristas = self.calcular_total_peso()
        self.grado_total_nodos = self.calcular_grados()

        self.suma_de_grados_nodos = {}
        self.suma_peso_aristas_internas = {}

        for v in self.grafo.obtener_vertices():
            c = self.comunidades[v]
            self.suma_de_grados_nodos[c] = self.suma_de_grados_nodos.get(c, 0) + self.grado_total_nodos[v]
            self.suma_peso_aristas_internas[c] = self.suma_peso_aristas_internas.get(c, 0) + self.grafo.vertices[v].get(v, 0)

    # Calcula la suma total de los pesos de todas las aristas del grafo
    def calcular_total_peso(self):
        total = 0
        for v in self.grafo.obtener_vertices():
            for w, peso in self.grafo.vertices[v].items():
                total += peso
        return total / 2

    # Calcula el grado para cada nodo.
    def calcular_grados(self):
        grado = {}
        for v in self.grafo.obtener_vertices():
            grado[v] = sum(self.grafo.vertices[v].values())
        return grado

    # Calcula la suma de pesos de aristas desde el nodo v hacia todos los nodos que están en la comunidad dada.
    def peso_aristas_hacia_comunidad(self, v, comunidad):
        suma = 0
        for w, peso in self.grafo.vertices[v].items():
            if self.comunidades[w] == comunidad:
                suma += peso
        return suma

    # Calcula la modularidad actual de la partición.
    def modularidad(self):
        Q = 0
        for c in set(self.comunidades.values()):
            peso_aristas_c = self.suma_peso_aristas_internas.get(c, 0)
            suma_grados_c = self.suma_de_grados_nodos.get(c, 0)
            Q += (peso_aristas_c / (2*self.peso_total_aristas)) - (suma_grados_c / (2*self.peso_total_aristas))**2
        return Q

    # Optimizacion de la partición actual moviendo nodos para aumentar la modularidad.
    def mejorar_particion(self):
        mejora = True
        historial_particiones = set()

        while mejora:
            particion_actual = frozenset((v, c) for v, c in self.comunidades.items())
            if particion_actual in historial_particiones:
                # Ciclo repetido
                break
            historial_particiones.add(particion_actual)

            mejora = False
            for v in self.grafo.obtener_vertices():
                comunidad_actual = self.comunidades[v]
                grado_total_v = self.grado_total_nodos[v]

                # Sacar al nodo v de su comunidad actual
                self.suma_de_grados_nodos[comunidad_actual] -= grado_total_v
                grado_total_v_en_comunidad = self.peso_aristas_hacia_comunidad(v, comunidad_actual)
                self.suma_peso_aristas_internas[comunidad_actual] -= 2 * grado_total_v_en_comunidad + self.grafo.vertices[v].get(v, 0)

                # Evaluar a que comunidad adyacente mover el nodo v
                comunidades_vecinas = set(self.comunidades[w] for w in self.grafo.adyacentes(v))
                comunidades_vecinas.add(comunidad_actual) 

                # Calculo de cambio de modularidad
                mejor_delta = 0
                mejor_comunidad = comunidad_actual
                for c in comunidades_vecinas:
                    if c == comunidad_actual:
                        continue
                    suma_pesos_entre_v_c = self.peso_aristas_hacia_comunidad(v, c)
                    suma_grados_c = self.suma_de_grados_nodos.get(c, 0)

                    # Funcion de modularidad
                    delta_Q = (suma_pesos_entre_v_c - (grado_total_v * suma_grados_c) / (2 * self.peso_total_aristas)) / (2 * self.peso_total_aristas)
                    
                    if delta_Q > mejor_delta:
                        mejor_delta = delta_Q
                        mejor_comunidad = c

                # Decidir si mover el nodo v o dejarlo donde estaba
                if mejor_comunidad != comunidad_actual:
                    self.comunidades[v] = mejor_comunidad
                    mejora = True
                    self.suma_de_grados_nodos[mejor_comunidad] = self.suma_de_grados_nodos.get(mejor_comunidad, 0) + grado_total_v
                    suma_pesos_entre_v_c_mejor = self.peso_aristas_hacia_comunidad(v, mejor_comunidad)
                    self.suma_peso_aristas_internas[mejor_comunidad] = self.suma_peso_aristas_internas.get(mejor_comunidad, 0) + 2 * suma_pesos_entre_v_c_mejor + self.grafo.vertices[v].get(v, 0)
                else:
                    self.suma_de_grados_nodos[comunidad_actual] += grado_total_v
                    self.suma_peso_aristas_internas[comunidad_actual] += 2 * grado_total_v_en_comunidad + self.grafo.vertices[v].get(v, 0)

    def ejecutar(self):
        self.mejorar_particion()
