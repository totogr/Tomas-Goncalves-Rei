import os
import sys

sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))
import heapq
from backtracking.backtracking import tribu_del_agua_backtracking
from extras.generadores.generador_1 import generar_maestros

def exactas_conocidas():
    maestros_17_5 = [("Huu", 523),
     ("Misu", 36),
    ("Wei", 665),
    ("Yakone", 582),
    ("Rafa", 90),
    ("Senna", 427),
    ("Tho", 369),
    ("Amon", 392),
    ("Sangok", 742),
    ("Amon I", 662),
    ("Senna I", 704),
    ("Sangok I", 938),
    ("Hama", 315),
    ("Unalaq", 413),
    ("Yakone I", 839),
    ("Misu I", 761),
    ("Ming-Hua", 479)]
    sol_17_5 = 15974095
    maestros_17_7 = [("Hama", 849),
        ("Sangok", 747),
        ("Pakku", 76),
        ("Siku", 761),
        ("Pakku I", 776),
        ("Pakku II", 988),
        ("Wei", 806),
        ("Unalaq", 794),
        ("Eska", 250),
        ("La", 390),
        ("Hasook", 11),
        ("Desna", 67),
        ("Rafa", 916),
        ("Wei I", 281),
        ("Tonraq", 502),
        ("Yue", 604),
        ("Hasook I", 122)]
    sol_17_7 = 11513230
    maestros_17_10 = [("wei", 437),
        ("Yue", 901),
        ("Rafa", 778),
        ("Tho", 237),
        ("Desna", 322),
        ("Misu", 582),
        ("Sura", 142),
        ("Amon", 429),
        ("Hama", 426),
        ("Siku", 689),
        ("Yue I", 147),
        ("Siku I", 222),
        ("Sangok", 463),
        ("Pakku", 114),
        ("Pakku I", 713),
        ("Eska", 93),
        ("Wei I", 645)]
    sol_17_10 = 5427764
    maestros_18_6 = [("Huu", 51),
        ("Kya", 413),
        ("Kya I", 324),
        ("Hama", 437),
        ("Amon", 469),
        ("Yue", 180),
        ("Tho", 646),
        ("Hama I", 532),
        ("Yue I", 785),
        ("Senna", 475),
        ("Sangok", 134),
        ("Kuruk", 417),
        ("Yue II", 845),
        ("Misu", 44),
        ("Pakku", 324),
        ("Yakone", 525),
        ("Misu I", 898),
        ("Kuruk I", 371)]
    sol_18_6 = 10322822
    maestros_18_8 = [("Amon", 455),
        ("Rafa", 724),
        ("Tho", 703),
        ("Yakone", 734),
        ("Wei", 290),
        ("Ming-Hua", 218),
        ("Tho I", 448),
        ("Siku", 743),
        ("Kya", 636),
        ("Katara", 182),
        ("Sura", 838),
        ("Desna", 306),
        ("Hama", 382),
        ("Amon I", 413),
        ("Rafa I", 733),
        ("Amon II", 881),
        ("Senna", 316),
        ("Kya I", 777)]
    sol_18_8 = 11971097
    maestros_20_4 = [("Sura", 392),
        ("Rafa", 575),
        ("Tonraq", 468),
        ("Katara", 187),
        ("Sura I", 936),
        ("Siku", 442),
        ("Tho", 843),
        ("Hasook", 26),
        ("Hama", 346),
        ("Ming-Hua", 576),
        ("Kya", 264),
        ("Wei", 733),
        ("Tonraq I", 414),
        ("Sangok", 872),
        ("Sangok I", 126),
        ("Siku I", 902),
        ("Wei I", 86),
        ("Sangok II", 84),
        ("Yue", 451),
        ("Tho I", 460)]
    sol_20_4 = 21081875
    maestros_20_5 = [("Huu",964),
        ("Wei",483),
        ("Misu",624),
        ("Hasook",556),
        ("Huu I",679),
        ("Kuruk",783),
        ("Yakone",769),
        ("Misu I",75),
        ("Desna",961),
        ("Huu II",225),
        ("Misu II",174),
        ("Sura",373),
        ("Yue",178),
        ("Hasook I",635),
        ("Ming-Hua",88),
        ("Tonraq",326),
        ("Huu III",41),
        ("Yue I",260),
        ("Siku",670),
        ("Tonraq I",309)]
    sol_20_5 = 16828799
    maestros_20_8 = [("Ming-Hua",153),
        ("Yakone",792),
        ("Tho",756),
        ("Rafa",576),
        ("Unalaq",874),
        ("Wei",934),
        ("Katara",121),
        ("Hama",637),
        ("Sura",239),
        ("Pakku",365),
        ("La",30),
        ("Amon",455),
        ("Ming-Hua I",451),
        ("Amon I",316),
        ("Tonraq",786),
        ("Sangok",193),
        ("Kuruk",893),
        ("Hama I",57),
        ("Sura I",153),
        ("Misu",775)]
    sol_20_8 = 19896411
    maestros_optimos = [(5, maestros_17_5, sol_17_5),
        (7, maestros_17_7, sol_17_7),
        (10, maestros_17_10, sol_17_10),
        (6, maestros_18_6, sol_18_6),
        (8, maestros_18_8, sol_18_8),
        (4, maestros_20_4, sol_20_4),
        (5, maestros_20_5, sol_20_5),
        (8, maestros_20_8, sol_20_8)]
    
    r = []
    
    for k, maestros, sol in maestros_optimos:
        sol_aprox, _ = aprox(k, maestros)
        relacion = obtener_relacion(sol, sol_aprox)
        r.append(relacion)
    return r


def generar_cota_empirica():
    relaciones = exactas_conocidas()
    for i in range(1, 13):
        for j in range(1, 5):
            n = i
            k = j + 1
            maestros = generar_maestros(n, (1, 100))
            sol_aprox, _ = aprox(k, maestros)
            sol_exacta, _ = tribu_del_agua_backtracking(k, maestros)
            relacion = obtener_relacion(sol_exacta, sol_aprox)
            relaciones.append(relacion)
        
    return max(relaciones)
                


def peor_caso():
    maestros = [("M1", 3), ("M2", 3), ("M3", 2), ("M4", 2), ("M5", 2)]
    k = 2
    sol_exacta, _ = tribu_del_agua_backtracking(k, maestros)
    sol_aprox, _ = aprox(k, maestros)
    return obtener_relacion(sol_exacta, sol_aprox)
    

def obtener_relacion(sol_exacta, sol_aprox):
    return sol_aprox / sol_exacta

def aprox(k, maestros):
        
    maestros = sorted(maestros, key=lambda x: x[1], reverse=True)
    heap = []
    for i in range(k):
        heapq.heappush(heap, (0,i, []))
    
    for nombre, poder in maestros:
        poder_actual, indice_desempate, nombres = heapq.heappop(heap)
        poder_actual += poder
        nombres.append(nombre)
        heapq.heappush(heap, (poder_actual, indice_desempate, nombres))
    
    costo_total = 0
    grupos = []
    for poder, _, nombres in heap:
        costo_total += (poder ** 2)
        grupos.append(nombres)
    
    return costo_total, grupos

def main():
    r_obtenidos = []
    for i in range(50000):
        r = generar_cota_empirica()
        r_obtenidos.append(r)
        print("iteracion " + str(i+1) + " terminada" )
    r = max(r_obtenidos)
    print(f"Cota empirica obtenida: {r}, cota_peor_caso: {peor_caso()}")

if __name__ == "__main__":
    main()