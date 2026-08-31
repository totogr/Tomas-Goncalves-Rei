# 10 · NFV — Virtualización de funciones de red

> 🧩 **Guía de estudio para llegar a la clase con el tema masticado.**
> Basada en la PPT 10 de la cátedra (*NFV — Virtualización de Funciones de Red*) y el temario del plan de estudios.

---

## 🎯 En una frase

**NFV (Network Functions Virtualization)** es la idea de tomar funciones de red que antes vivían en **hardware dedicado** (routers, firewalls físicos) y correrlas como **software sobre servidores genéricos** — el mismo salto que llevó de las máquinas físicas a las **VMs, la nube y los contenedores**.

---

## 🧭 ¿Por qué importa / dónde encaja?

Es la mirada **moderna y de tendencia** de la materia: cómo evolucionó la infraestructura de red hacia lo **definido por software**. Conecta redes con **cloud, DevOps y Kubernetes**, temas centrales de la industria hoy. Muestra hacia dónde va todo lo anterior.

---

## 💡 La idea con una analogía

Antes, cada función de red era un **electrodoméstico dedicado**: una tostadora para tostar, una licuadora para licuar (un equipo por tarea). **NFV es la Thermomix**: un solo hardware genérico que, con **software distinto**, hace de tostadora, licuadora o lo que necesites. Ganás flexibilidad y ahorrás espacio/plata, a cambio de un poco más de complejidad para orquestar todo.

---

## 🗺️ La evolución hacia NFV

```mermaid
flowchart LR
    A["Purpose-built<br/>hardware dedicado<br/>1 equipo = 1 función"] --> B["Virtualización (VMs)<br/>varios SO en un servidor<br/>hipervisor (VMware, KVM)"]
    B --> C["Nube<br/>infraestructura elástica<br/>bajo demanda (AWS, Azure)"]
    C --> D["Contenedores<br/>ligeros y portables<br/>(Docker)"]
    D --> E["Cloud native<br/>orquestado<br/>(Kubernetes, OpenStack)"]
```

---

## 📊 Conceptos clave

### Las eras de la infraestructura

| Era | Idea | Aislamiento |
| --- | --- | --- |
| **Purpose-built** | Hardware específico por función | Físico (1 caja = 1 función) |
| **Virtualización (VMs)** | Varios SO sobre un mismo hardware vía **hipervisor** | A nivel de SO |
| **Nube** | Infraestructura elástica, bajo demanda; modelo **CapEx → OpEx** | Servicios gestionados |
| **Contenedores** | Empaquetar app + dependencias en unidades ligeras (**Docker**) | Por contenedor |
| **Cloud native** | Contenedores orquestados (**Kubernetes**, **OpenStack**) | Automático y escalable |

### Los tres "drivers" de la red (recap de la clase)

| Driver | Qué prioriza |
| --- | --- |
| **Voice driven** | Redes TDM tradicionales (telefonía) |
| **Data driven** | Redes IP |
| **Deterministic driven** | Plataformas basadas en software, **QoS predecible** (latencia, jitter, pérdidas bajo control) |

### VM vs. contenedor (la distinción clave)

- **VM**: incluye un **sistema operativo completo** → más pesada, más aislada.
- **Contenedor**: comparte el SO del host, solo empaqueta la app y sus dependencias → **más liviano y portable**.
- Beneficios de virtualizar: mejor uso del hardware, aislamiento de cargas, escalabilidad simplificada.

---

## ❓ Preguntas para autoevaluarte

1. ¿Qué propone **NFV** frente al hardware de red dedicado?
2. Ordená la evolución: contenedores, VMs, hardware dedicado, cloud native.
3. ¿Qué diferencia hay entre una **VM** y un **contenedor**?
4. ¿Qué significa el cambio de modelo **CapEx → OpEx** que trajo la nube?
5. ¿Qué es **Kubernetes** y qué rol cumple en la era cloud native?
6. ¿Qué prioriza un enfoque **deterministic driven** (pista: QoS)?

---

## 📌 Qué prestar atención en la clase

- El hilo **hardware dedicado → VMs → nube → contenedores → orquestación**.
- La distinción **VM vs. contenedor** (peso, aislamiento) — suele tomarse.
- Cómo NFV se apoya en **hardware genérico (COTS)** y software.
- Los nombres de herramientas (**Docker, Kubernetes, OpenStack**): saber qué es cada una a grandes rasgos.

---

<sub>⚙️ Guía basada en la PPT 10 de la cátedra (Volpi / Giorgi / Llasat).</sub>
