from tkinter import *

raiz = Tk()

frame_label = Frame(raiz, width=200, height=300)
frame_label.pack(fill="both", expand=1)

label = Label(frame_label, text="Formulario: ", fg="red", font=("Comic Sans MS", 18)) #Creo un texto
label.grid(row=0, column=1) #Agrego el texto con posiciones de tablas

mi_nombre = StringVar()

cuadro_nombre = Entry(frame_label, textvariable=mi_nombre) #Creo cuadro de texto con Entry
cuadro_nombre.grid(row=1, column=1, padx=20, pady=10)
cuadro_nombre.config(justify="center")

cuadro_apellido = Entry(frame_label)
cuadro_apellido.grid(row=2, column=1, padx=20, pady=10)
cuadro_apellido.config(justify="right")

cuadro_dni = Entry(frame_label)
cuadro_dni.grid(row=3, column=1, padx=20, pady=10)

cuadro_contraseña = Entry(frame_label)
cuadro_contraseña.grid(row=4, column=1, padx=20, pady=10)
cuadro_contraseña.config(show="*")


nombre_label= Label(frame_label, text="Nombre: ")
nombre_label.grid(row=1, column=0, sticky="w", padx=5, pady=10)

apellido_label= Label(frame_label, text="Apellido: ")
apellido_label.grid(row=2, column=0, sticky="w", padx=5, pady=10)

dni_label= Label(frame_label, text="DNI: ")
dni_label.grid(row=3, column=0, sticky="e", padx=5, pady=10)

contraseña_label= Label(frame_label, text="Contraseña: ")
contraseña_label.grid(row=4, column=0, sticky="e", padx=5, pady=10)

def codigo_boton():
    mi_nombre.set("Tomas")

boton = Button(frame_label, text="Enviar", command=codigo_boton)
boton.grid(row=5, column=1, sticky="e", padx=5, pady=10)
 







raiz.mainloop() 

