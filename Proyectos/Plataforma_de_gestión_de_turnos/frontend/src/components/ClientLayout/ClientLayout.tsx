import React, { useState, useRef, useEffect } from "react";
import { useLocation } from "wouter";
import { useToken } from "@/services/TokenContext";
import { useUpdateClientPreferences } from "@/services/BookingServices";
import styles from "./ClientLayout.module.css";

export const ClientLayout = ({ children }: React.PropsWithChildren) => {
  const [tokenState, setToken] = useToken();
  const [, navigate] = useLocation();
  const [menuOpen, setMenuOpen] = useState(false);
  const menuRef = useRef<HTMLDivElement>(null);

  const [receivesReminders, setReceivesReminders] = useState(true);
  const { mutate: updatePreferences, isPending } = useUpdateClientPreferences();

  const firstName = tokenState.state === "LOGGED_IN" ? tokenState.tokens.firstName : "";
  const lastName = tokenState.state === "LOGGED_IN" ? tokenState.tokens.lastName : "";
  const initials = `${firstName.charAt(0)}${lastName.charAt(0)}`.toUpperCase();
  const fullName = `${firstName} ${lastName}`.trim();

  const logOut = () => {
    setToken({ state: "LOGGED_OUT" });
    navigate("/");
  };

  const handleToggle = () => {
    const newValue = !receivesReminders;
    setReceivesReminders(newValue);
    updatePreferences(newValue, {
      onError: () => {
        setReceivesReminders(!newValue);
        alert("Error al actualizar preferencias");
      }
    });
  };

  useEffect(() => {
    const handleClick = (e: MouseEvent) => {
      if (menuRef.current && !menuRef.current.contains(e.target as Node)) {
        setMenuOpen(false);
      }
    };
    document.addEventListener("mousedown", handleClick);
    return () => document.removeEventListener("mousedown", handleClick);
  }, []);

  return (
      <div className={styles.root}>
        <aside className={styles.sidebar}>
          <div className={styles.sidebarHeader}>
            <span className={styles.logo}>Tur<span className={styles.logoAccent}>nos</span></span>
          </div>

          <nav className={styles.nav}>
            <NavItem href="/bookings/me" icon="ti-calendar" label="Mis turnos" />
            <NavItem href="/professionals/" icon="ti-search" label="Buscar profesionales" />
          </nav>

          <div className={styles.sidebarFooter} ref={menuRef}>
            <button
                type="button"
                className={styles.avatarBtn}
                onClick={() => setMenuOpen((o) => !o)}
                aria-label="Menú de usuario"
            >
              {initials}
            </button>
            <div className={styles.userInfo}>
              <span className={styles.userName}>{fullName}</span>
              <span className={styles.userRole}>cliente</span>
            </div>

            {menuOpen && (
                <div className={styles.dropdown}>
                  <div className={styles.dropdownToggleContainer}>
                    <label className={styles.toggleLabel}>
                      <span>Notificaciones</span>
                      <input
                          type="checkbox"
                          checked={receivesReminders}
                          onChange={handleToggle}
                          disabled={isPending}
                      />
                    </label>
                  </div>
                  <hr className={styles.dropdownDivider} />
                  <button type="button" className={styles.dropdownItem} onClick={logOut}>
                    <i className="ti-logout" aria-hidden="true" />
                    Cerrar sesión
                  </button>
                </div>
            )}
          </div>
        </aside>

        <main className={styles.content}>{children}</main>
      </div>
  );
};

type NavItemProps = { href: string; icon: string; label: string };

const NavItem = ({ href, icon, label }: NavItemProps) => {
  const [location] = useLocation();
  const isActive = location === href || location.startsWith(href + "/");

  return (
      <a href={href} className={`${styles.navItem} ${isActive ? styles.navItemActive : ""}`}>
        <i className={`ti ${icon}`} aria-hidden="true" />
        {label}
      </a>
  );
};