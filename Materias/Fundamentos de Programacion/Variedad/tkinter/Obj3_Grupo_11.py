#Padron: 111405, Tomas Goncalves Rei

from tkinter import *

def crear_login():

    login = Tk()

    login.title('Login Grupo 11')
    login.resizable(0,0)
    login.geometry('300x130')
    login.iconbitmap("tp.ico")
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

    botonIngreso = Button(miFrame, text='Ingresar')
    botonIngreso.place(x=120,y=100)

    login.mainloop()


def main():
    crear_login()

main()