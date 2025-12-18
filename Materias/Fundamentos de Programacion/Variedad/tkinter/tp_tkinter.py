from tkinter import *

def ventana_nueva():
    ventana = Tk()
    ventana.title('Login Grupo 11')
    ventana.resizable(0,0)
    ventana.geometry('300x130')
    ventana.config(bg='blue')
    ventana.iconbitmap("tp.ico")

    ventana.mainloop()


def main():
    ventana_nueva()

main()