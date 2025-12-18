from tkinter import*
from tkinter import messagebox

def codigo_boton():
    mi_nombre.set("Hecho por: Carola Sciaini")    
    nombre = validar_nom_ap(cuadro_nombre.get())
    apellido = validar_nom_ap(cuadro_apellido.get())
    mail = validar_email(cuadro_email.get())
    ventana = nombre and apellido and mail
    ventana_emergente(ventana)

def validar_nom_ap(campo):
        num=["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"]
        if len(campo) < 25:
                validada=True
                i=0
                while i<len(campo) and validada:
                        if campo[i] in num:
                                validada=False
                        i+=1
        else:
               validada=False

        return validada

def validar_email(campo):
        if len(campo) < 20:
                validada=True
                if "@" in campo:
                        if campo[0] == "@" or campo[-1] == "@":
                                validada = False
                else:
                       validada = False
        else:
              validada=False

        return validada

def ventana_emergente(a):
        if a==False:
              messagebox.showinfo("Campo Invalido", "Algun campo ingresado no es valido, intente nuevamente")
        else:
               messagebox.showinfo("Campos Valido", "Se recibieron los campos correctamente")
                   
raiz = Tk()

raiz.title ("Ingreso de Datos")

raiz.geometry("500x300")

raiz.resizable(0,0)

mi_frame= Frame (raiz, width= 500, height= 300)
mi_frame.pack()

mi_nombre = StringVar()

cuadro_nombre = Entry (mi_frame)
cuadro_nombre.grid(row=0, column=1)

cuadro_apellido = Entry (mi_frame)
cuadro_apellido.grid(row=1, column=1)

cuadro_usuario = Entry (mi_frame)
cuadro_usuario.grid(row=2, column=1)

cuadro_email = Entry (mi_frame)
cuadro_email.grid(row=3, column=1)

nombre_label=Label (mi_frame, text ="Ingrese se Nombre").grid(row= 0, column=0)

apellido_label=Label (mi_frame, text ="Ingrese se Apellido").grid(row= 1, column= 0)

usuario_label=Label (mi_frame, text ="Ingrese se Usuario").grid(row= 2, column= 0)

email_label=Label (mi_frame, text= "Ingrese se Email").grid(row= 3, column= 0)

minombre_label=Label(mi_frame, textvariable= mi_nombre)
minombre_label.grid(row=4, column=1)

boton_envio= Button (raiz, text= "Enviar", command= codigo_boton)
boton_envio.pack()


raiz.mainloop()