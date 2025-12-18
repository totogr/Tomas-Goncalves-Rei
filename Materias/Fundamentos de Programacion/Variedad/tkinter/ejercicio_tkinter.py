from tkinter import *
from tkinter import messagebox

raiz = Tk()
raiz.title("Ingreso de Datos")
raiz.resizable(False, False)
raiz.geometry("500x300")

def mostrar_datos():
    nom_datos = ingreso_nombre.get()
    ape_datos = ingreso_apellido.get()
    mail_datos = ingreso_email.get()
    if (validar(nom_datos) and validar(ape_datos)) == False:
        messagebox.showerror("Error", "Nombre o apellido invalido")
    elif validar_mail(mail_datos) == False:
        messagebox.showerror("Error", "Mail invalido")
    else:
        messagebox.showinfo("Confirmacion", "Datos recibidos correctamente")

def validar(cadena):
    validada = True
    i = 0
    if len(cadena) < 26 and len(cadena) > 0:
        while i < len(cadena) and validada:
            if cadena[i].isalpha() or " ":
                validada
            else:
                validada = False
            i += 1
    else: 
        validada = False
    return validada

def validar_mail(mail):
    validada = True
    i = 0
    contador = 0
    if len(mail) < 21 and mail[0] != "@" and mail[-1] != "@":
        while i < len(mail) and validada:
            if mail[i] == "@" and contador == 0:
                contador += 1
            elif contador > 1:
                validada = False
            i += 1
    else:
        validada = False
    if contador == 0:
        validada = False
    return validada

datos = Frame(raiz, background="blue2")
datos.pack(fill="both", expand=1)

titulo = Label(datos, text="Ingresa los Datos", font=("Comic Sans MS", 20), background="blue2", fg="white")
titulo.pack()

nombre = Label(datos, text="Nombre:", font=("Comic Sans MS",10), background="blue2", fg="white")
nombre.pack()

ingreso_nombre = Entry(datos, font=("Comic Sans MS", 10))
ingreso_nombre.pack()

apellido = Label(datos, text="Apellido:", font=("Comic Sans MS",10), background="blue2", fg="white")
apellido.pack()

ingreso_apellido = Entry(datos, font=("Comic Sans MS", 10))
ingreso_apellido.pack()

email = Label(datos, text="Email:", font=("Comic Sans MS",10), background="blue2", fg="white")
email.pack()

ingreso_email = Entry(datos, font=("Comic Sans MS", 10))
ingreso_email.pack()


boton_envio = Button(datos, text="Envio", command=mostrar_datos, bg="chartreuse2")
boton_envio.pack(pady=10)

nom_ape = Label(datos, text="Hecho por Tomas", font=("Comic Sans MS",10), background="blue2", fg="white" )
nom_ape.pack()

raiz.mainloop()