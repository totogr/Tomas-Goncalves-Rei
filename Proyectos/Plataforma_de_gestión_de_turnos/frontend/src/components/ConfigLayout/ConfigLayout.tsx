import styles from "./ConfigLayout.module.css";

type Props = {
    activeTab: "servicios" | "horarios" | "bloqueo-horarios" | "bloqueo-clientes";
    children: React.ReactNode;
};

export function ConfigLayout({ activeTab, children }: Props) {
    return (
        <div>
            <div style={{ marginBottom: 32 }}>
                <h1 className={styles.title}>Configuración</h1>
                <p className={styles.subtitle}>Ajustá tu negocio y preferencias</p>
            </div>

            <div className={styles.tabs}>
                <a href="/professional/config/servicios" className={`${styles.tab} ${activeTab === "servicios" ? styles.active : ""}`}>Servicios</a>
                <a href="/professional/config/horarios" className={`${styles.tab} ${activeTab === "horarios" ? styles.active : ""}`}>Horarios</a>
                <a href="/professional/config/bloqueo-horarios" className={`${styles.tab} ${activeTab === "bloqueo-horarios" ? styles.active : ""}`}>Bloquear horarios</a>
                <a href="/professional/config/bloqueo-clientes" className={`${styles.tab} ${activeTab === "bloqueo-clientes" ? styles.active : ""}`}>Bloquear clientes</a>
            </div>

            {children}
        </div>
    );
}