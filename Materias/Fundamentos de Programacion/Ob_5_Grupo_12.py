#CAROLA SCIAINI 111406

from tkinter import *
from tkinter import messagebox

diccionario_datos = {
    "Carola Sciaini": "123",
    "Daniel Molina": "456",
    "Jazmin Fabrizio": "789",
    "Jeremias Bermudez": "101",
    "Ailen Gangale": "102"
}

def obtener_usuarios_claves():
    return diccionario_datos
    
def mensaje_error():
    messagebox.showerror('','Alguno de los datos es incorrecto')

def mensaje_acceso_correcto():
    messagebox.showinfo('','Usuario y Clave correcto')

def validar_ingreso(nombre, clave):
    diccionario_datos = obtener_usuarios_claves()
    if nombre in diccionario_datos and diccionario_datos[nombre] == clave:
        mensaje_acceso_correcto()
    else:
        mensaje_error()

def registrar_usuarios(nuevo_usuario, nueva_clave):
    diccionario_datos = obtener_usuarios_claves()
    if nuevo_usuario in diccionario_datos:
        messagebox.showerror("Error","Usuario ya registrado")
    else:
        diccionario_datos[nuevo_usuario] = nueva_clave
        messagebox.showinfo('Éxito', 'Usuario registrado con éxito')


def nuevos_usuarios(diccionario_datos):
    raiz = Tk()
    raiz.title("Registrar usuario")
    raiz.resizable(0,0)
    raiz.geometry("300x130")
    raiz.iconbitmap("IMG_Grupo_Nro12.ico")
    raiz.config(bg="pink")

    miframe = Frame(raiz)
    miframe.pack()

    cuadronuevousuario=Entry(miframe)
    cuadronuevousuario.grid(row=0, column=1, padx= 5, pady = 5)

    cuadronuevacontraseña=Entry(miframe)
    cuadronuevacontraseña.grid(row=1, column=1, padx= 5, pady = 5)
    cuadronuevacontraseña.config(justify="center", show="*")

    nuevousuariolabel=Label(miframe, text="Nuevo Usuario Alumno:")
    nuevousuariolabel.grid(row=0, column=0, sticky="w", padx= 5, pady = 5)
    nuevousuariolabel.config(justify="center")

    nuevaclavelabel=Label(miframe, text="Clave:")
    nuevaclavelabel.grid(row=1, column=0, sticky="w", padx= 5, pady = 5)
    nuevaclavelabel.config(justify="center")

    botonregistrar = Button(miframe, text="Registrar", command=lambda: registrar_usuarios(cuadronuevousuario.get(), cuadronuevacontraseña.get()))
    botonregistrar.grid(row=3, columnspan=2, pady=5)


    raiz.mainloop()

def Login(diccionario_datos):
  raiz = Tk()
  raiz.title("Login doce")
  raiz.resizable(0,0)
  raiz.geometry("300x130")
  raiz.iconbitmap("IMG_Grupo_Nro12.ico")
  raiz.config(bg="pink")

  miframe = Frame(raiz)
  miframe.pack()

  cuadrousuario=Entry(miframe)
  cuadrousuario.grid(row=0, column=1, padx= 5, pady = 5)

  cuadrocontraseña=Entry(miframe)
  cuadrocontraseña.grid(row=1, column=1, padx= 5, pady = 5)
  cuadrocontraseña.config(justify="center", show="*")

  usuariolabel=Label(miframe, text="Usuario Alumno:")
  usuariolabel.grid(row=0, column=0, sticky="w", padx= 5, pady = 5)
  usuariolabel.config(justify="center")

  clavelabel=Label(miframe, text="Clave:")
  clavelabel.grid(row=1, column=0, sticky="w", padx= 5, pady = 5)
  clavelabel.config(justify="center")

  botonEnviar=Button(miframe, text="Enviar", command=lambda:validar_ingreso(cuadrousuario.get(),cuadrocontraseña.get()))
  botonEnviar.grid(row=3, columnspan=2, pady=5)

  botonregistrar=Button(miframe, text="Registrar", command=lambda: nuevos_usuarios(diccionario_datos))
  botonregistrar.grid(row=4, columnspan=2, pady=5)

  raiz.mainloop()

def main():
    diccionario_datos = obtener_usuarios_claves()
    Login(diccionario_datos)

main()