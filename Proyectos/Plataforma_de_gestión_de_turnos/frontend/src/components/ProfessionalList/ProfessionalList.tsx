import { useMemo, useState } from "react";

import { ClientLayout } from "@/components/ClientLayout/ClientLayout";
import { ProfessionalCard } from "@/components/ProfessionalCard/ProfessionalCard";
import { ProfessionalSummary } from "@/models/Professional";

import styles from "./ProfessionalList.module.css";

type Props = {
  professionals: ProfessionalSummary[] | undefined;
  isLoading: boolean;
  onSelect: (id: number) => void;
};

export const ProfessionalList = ({ professionals, isLoading, onSelect }: Props) => {
  const [search, setSearch] = useState("");
  const [selectedSpecialty, setSelectedSpecialty] = useState("");

  const specialties = useMemo(() => {
    if (!professionals) return [];
    const unique = new Set(
      professionals.map((p) => p.specialty).filter((s): s is string => s !== null && s !== "")
    );
    return Array.from(unique).sort();
  }, [professionals]);

  const filtered = useMemo(() => {
    if (!professionals) return [];
    const term = search.toLowerCase().trim();
    return professionals.filter((p) => {
      const matchesSearch =
        term === "" ||
        `${p.firstName} ${p.lastName}`.toLowerCase().includes(term) ||
        (p.specialty ?? "").toLowerCase().includes(term);
      const matchesSpecialty =
        selectedSpecialty === "" || p.specialty === selectedSpecialty;
      return matchesSearch && matchesSpecialty;
    });
  }, [professionals, search, selectedSpecialty]);

  const hasActiveFilters = search.trim() !== "" || selectedSpecialty !== "";

  return (
    <ClientLayout>
      <div className={styles.page}>
        <div className={styles.header}>
          <h1 className={styles.title}>Profesionales</h1>
          <p className={styles.subtitle}>Elegí con quién querés reservar un turno</p>
        </div>

        <div className={styles.filters}>
          <div className={styles.searchWrapper}>
            <SearchIcon />
            <input
              type="text"
              placeholder="Buscar por nombre o rubro..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className={styles.searchInput}
            />
          </div>
          <select
            value={selectedSpecialty}
            onChange={(e) => setSelectedSpecialty(e.target.value)}
            className={styles.select}
          >
            <option value="">Todos los rubros</option>
            {specialties.map((s) => (
              <option key={s} value={s}>
                {s}
              </option>
            ))}
          </select>
        </div>

        {isLoading || !professionals || professionals.length === 0 || filtered.length === 0 ? (
          <div className={styles.listCard}>
            {isLoading ? (
              <EmptyState message="Cargando profesionales..." />
            ) : !professionals || professionals.length === 0 ? (
              <EmptyState message="No hay profesionales disponibles por el momento." />
            ) : (
              <EmptyState
                message="No se encontraron profesionales con esos criterios."
                showClear
                onClear={() => {
                  setSearch("");
                  setSelectedSpecialty("");
                }}
              />
            )}
          </div>
        ) : (
          <div className={styles.listGrid}>
            {filtered.map((professional) => (
              <ProfessionalCard
                key={professional.id}
                professional={professional}
                onClick={() => onSelect(professional.id)}
              />
            ))}
          </div>
        )}

        {!isLoading && hasActiveFilters && filtered.length > 0 && (
          <p className={styles.resultsCount}>
            {filtered.length} {filtered.length === 1 ? "profesional encontrado" : "profesionales encontrados"}
          </p>
        )}
      </div>
    </ClientLayout>
  );
};

const SearchIcon = () => (
  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
    <circle cx="11" cy="11" r="8" />
    <path d="m21 21-4.35-4.35" />
  </svg>
);

type EmptyStateProps = {
  message: string;
  showClear?: boolean;
  onClear?: () => void;
};

const EmptyState = ({ message, showClear, onClear }: EmptyStateProps) => (
  <div className={styles.empty}>
    <div className={styles.emptyIcon}>
      <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5">
        <circle cx="11" cy="11" r="8" />
        <path d="m21 21-4.35-4.35" />
      </svg>
    </div>
    <span className={styles.emptyTitle}>{message}</span>
    {showClear && onClear && (
      <button type="button" className={styles.clearButton} onClick={onClear}>
        Limpiar filtros
      </button>
    )}
  </div>
);