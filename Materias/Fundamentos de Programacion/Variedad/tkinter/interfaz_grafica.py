import tkinter as tk

# Crear la ventana
ventana = tk.Tk()
ventana.title("Cuadros de texto centrados con etiquetas arriba-izquierda")

# Función para mostrar el texto ingresado
def mostrar_texto():
    texto1 = entrada1.get()
    texto2 = entrada2.get()
    resultado.config(text=f"Texto 1: {texto1}\nTexto 2: {texto2}")

# Crear el frame
frame = tk.Frame(ventana)
frame.pack()

# Crear etiquetas
etiqueta1 = tk.Label(frame, text="Ingresar texto 1:")
etiqueta1.grid(row=0, column=0, sticky=tk.W, pady=(20,5))

etiqueta2 = tk.Label(frame, text="Ingresar texto 2:")
etiqueta2.grid(row=1, column=0, sticky=tk.W, pady=5)

# Crear los cuadros de texto
entrada1 = tk.Entry(frame)
entrada1.grid(row=0, column=1, padx=5, pady=5)

entrada2 = tk.Entry(frame)
entrada2.grid(row=1, column=1, padx=5, pady=5)

# Crear el botón
boton = tk.Button(frame, text="Mostrar Texto", command=mostrar_texto)
boton.grid(row=2, columnspan=2, pady=(10,20))

# Crear un área para mostrar el resultado
resultado = tk.Label(frame, text="")
resultado.grid(row=3, columnspan=2)

# Centrar el frame en la ventana
frame.grid_columnconfigure(0, weight=1)
frame.grid_columnconfigure(1, weight=1)
frame.grid_rowconfigure(2, weight=1)

# Ejecutar el bucle principal de la ventana
ventana.mainloop()