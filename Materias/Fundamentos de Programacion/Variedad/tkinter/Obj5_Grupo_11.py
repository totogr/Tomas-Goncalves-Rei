#Padron: 111405, Tomas Goncalves Rei

from tkinter import *
from tkinter import messagebox

def obtener_usuarios_claves():

    return {
        'Juan Ignacio Moore' : '112479',
        'Tomas Goncalves Rei' : '111405',
        'Ulises Distefano' : '111883',
        'Marie Colette Henry' : '45085568',
        'Agustin Nadim Neme' : '110136'
    }

def mensaje_error():
    messagebox.showerror('','Alguno de los datos es incorrecto')
     
def mensaje_acceso_correcto():
    messagebox.showinfo('','Usuario y Clave correcto')

def destroy(ventana):
    ventana.destroy()

def validar_ingreso(nombre, clave, diccionario_datos):
    if nombre in diccionario_datos and diccionario_datos[nombre] == clave:
        mensaje_acceso_correcto()
    else:
        mensaje_error()

def registro_usuarios(nombre, clave, ventana, dicc_usuarios):
    if nombre in dicc_usuarios:
        messagebox.showerror('','Usuario Duplicado')
    else:
        dicc_usuarios[nombre] = clave
        messagebox.showinfo('','Usuario creado correctamente')
        destroy(ventana)


def crear_registro(dicc_usuarios):
    registro = Tk()

    registro.title('Registro Grupo 11')
    registro.resizable(0,0)
    registro.geometry('300x130')
    #registro.iconbitmap('tp.ico')

    frameRegistro = Frame(registro)
    frameRegistro.config(bg='blue')
    frameRegistro.pack(fill="both", expand=1)

    registroAlumno = Label(frameRegistro, text='Usuario:')
    registroAlumno.grid(row=0,column=0,padx=10,pady=10, sticky='w')

    registroPsw = Label(frameRegistro, text='Clave:')
    registroPsw.grid(row=1,column=0,padx=10,pady=10, sticky='w')

    entradaAlumno = Entry(frameRegistro)
    entradaAlumno.grid(row=0,column=1,padx=10,pady=10)

    entradaPws = Entry(frameRegistro)
    entradaPws.grid(row=1,column=1,padx=10,pady=10)
    entradaPws.config(show='*')

    botonRegistro = Button(frameRegistro,  text='Registrar Usuario', command=lambda:registro_usuarios(entradaAlumno.get(), entradaPws.get(), registro, dicc_usuarios))
    botonRegistro.place(x=90,y=100)

    botonVolver = Button(frameRegistro,  text='Volver', command=lambda:destroy(registro))
    botonVolver.place(x=250,y=100)

    registro.mainloop()
              

def crear_login(dicc_usuarios):

    login = Tk()

    login.title('Login Grupo 11')
    login.resizable(0,0)
    login.geometry('300x130')
    #login.iconbitmap('tp.ico')

    miFrame = Frame(login)
    miFrame.config(bg='blue')
    miFrame.pack(fill="both", expand=1)
    
    labelAlumno = Label(miFrame, text='Usuario Alumno:')
    labelAlumno.grid(row=0,column=0,padx=10,pady=10, sticky='w')

    labelClave = Label(miFrame, text='Clave')
    labelClave.grid(row=1,column=0,padx=10,pady=10, sticky='w')

    entradaAlumno = Entry(miFrame)
    entradaAlumno.grid(row=0,column=1,padx=10,pady=10)

    entradaClave = Entry(miFrame)
    entradaClave.grid(row=1,column=1,padx=10,pady=10)
    entradaClave.config(show='*')

    botonIngreso = Button(miFrame, text='Ingresar', command=lambda:validar_ingreso(entradaAlumno.get(),entradaClave.get(), dicc_usuarios))
    botonIngreso.place(x=120,y=100)

    botonRegistro = Button(miFrame, text='Registrar', command=lambda:crear_registro(dicc_usuarios))
    botonRegistro.place(x=190,y=100)

    login.mainloop()

def main():
    dicc_usuarios = obtener_usuarios_claves()
    crear_login(dicc_usuarios)

main()