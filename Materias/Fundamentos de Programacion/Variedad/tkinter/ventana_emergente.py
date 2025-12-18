from tkinter import *
from tkinter import messagebox

raiz = Tk()

def ventana_emergente():
    messagebox.showinfo("Nombre de Usuario", "El usuario se llama Tomas")

def alerta_emergente():
    messagebox.showwarning("Alerta","Te  llamas Tomas")

def salir_emergente():
    # valor = messagebox.askquestion("Salir", "¿Quiere salir de la app?")
    # if valor == "yes":
    #     raiz.destroy()
    valor = messagebox.askokcancel("Salir", "¿Quiere salir de la app?")
    if valor == True:
        raiz.destroy()

def cerrar_emergente():
    valor = messagebox.askretrycancel("Reintentar", "No es posible cerrar documento bloqueado")
    if valor == True:
        raiz.destroy()


frame_label = Frame(raiz, width=200, height=300)
frame_label.pack(fill="both", expand=1)

boton = Button(frame_label, text="Informacion", command=ventana_emergente)
boton.grid( padx=5, pady=10)
boton = Button(frame_label, text="Alerta", command=alerta_emergente)
boton.grid(padx=5, pady=10)
boton = Button(frame_label, text="Salir", command=salir_emergente)
boton.grid(padx=5, pady=10)
boton = Button(frame_label, text="Cerrar", command=cerrar_emergente)
boton.grid(padx=5, pady=10)











raiz.mainloop() 