import { render, screen, fireEvent } from "@testing-library/react";
import type React from "react";
import { describe, expect, test, vi } from "vitest";

import { ProfessionalList } from "./ProfessionalList";

vi.mock("@/components/ClientLayout/ClientLayout", () => ({
  ClientLayout: ({ children }: { children: React.ReactNode }) => <div>{children}</div>,
}));

vi.mock("@/components/ProfessionalCard/ProfessionalCard", () => ({
  ProfessionalCard: ({ professional, onClick }: { professional: { id: number; firstName: string; lastName: string }; onClick: () => void }) => (
    <div
      data-testid={`professional-card-${professional.id}`}
      onClick={onClick}
      style={{ cursor: "pointer" }}
    >
      {professional.firstName} {professional.lastName}
    </div>
  ),
}));

const professionals = [
  { id: 1, firstName: "Ana", lastName: "García", specialty: "Peluquería", rating: 4.5, reviewCount: 10 },
  { id: 2, firstName: "Carlos", lastName: "López", specialty: "Barbería", rating: null, reviewCount: null },
  { id: 3, firstName: "Laura", lastName: "Martínez", specialty: "Peluquería", rating: 3.0, reviewCount: 5 },
];

describe("ProfessionalList", () => {
  test("Muestra el título de la sección", () => {
    render(<ProfessionalList professionals={professionals} isLoading={false} onSelect={vi.fn()} />);
    expect(screen.getByText("Profesionales")).toBeVisible();
  });

  test("Renderiza una card por cada profesional", () => {
    render(<ProfessionalList professionals={professionals} isLoading={false} onSelect={vi.fn()} />);
    expect(screen.getByTestId("professional-card-1")).toBeVisible();
    expect(screen.getByTestId("professional-card-2")).toBeVisible();
    expect(screen.getByTestId("professional-card-3")).toBeVisible();
  });

  test("Muestra mensaje de carga cuando isLoading es true", () => {
    render(<ProfessionalList professionals={undefined} isLoading={true} onSelect={vi.fn()} />);
    expect(screen.getByText("Cargando profesionales...")).toBeVisible();
  });

  test("Muestra mensaje vacío cuando no hay profesionales", () => {
    render(<ProfessionalList professionals={[]} isLoading={false} onSelect={vi.fn()} />);
    expect(screen.getByText("No hay profesionales disponibles por el momento.")).toBeVisible();
  });

  test("Llama a onSelect con el id correcto al hacer click en una card", () => {
    const onSelect = vi.fn();
    render(<ProfessionalList professionals={professionals} isLoading={false} onSelect={onSelect} />);
    fireEvent.click(screen.getByTestId("professional-card-2"));
    expect(onSelect).toHaveBeenCalledWith(2);
  });

  test("Filtra profesionales por nombre", () => {
    render(<ProfessionalList professionals={professionals} isLoading={false} onSelect={vi.fn()} />);
    fireEvent.change(screen.getByPlaceholderText(/Buscar/), { target: { value: "Ana" } });
    expect(screen.getByTestId("professional-card-1")).toBeVisible();
    expect(screen.queryByTestId("professional-card-2")).toBeNull();
    expect(screen.queryByTestId("professional-card-3")).toBeNull();
  });

  test("Filtra profesionales por especialidad", () => {
    render(<ProfessionalList professionals={professionals} isLoading={false} onSelect={vi.fn()} />);
    fireEvent.change(screen.getByRole("combobox"), { target: { value: "Barbería" } });
    expect(screen.queryByTestId("professional-card-1")).toBeNull();
    expect(screen.getByTestId("professional-card-2")).toBeVisible();
    expect(screen.queryByTestId("professional-card-3")).toBeNull();
  });

  test("Muestra mensaje cuando los filtros no tienen resultados", () => {
    render(<ProfessionalList professionals={professionals} isLoading={false} onSelect={vi.fn()} />);
    fireEvent.change(screen.getByPlaceholderText(/Buscar/), { target: { value: "zzz" } });
    expect(screen.getByText("No se encontraron profesionales con esos criterios.")).toBeVisible();
  });

  test("Limpiar filtros restablece los resultados", () => {
    render(<ProfessionalList professionals={professionals} isLoading={false} onSelect={vi.fn()} />);
    fireEvent.change(screen.getByPlaceholderText(/Buscar/), { target: { value: "zzz" } });
    fireEvent.click(screen.getByText("Limpiar filtros"));
    expect(screen.getByTestId("professional-card-1")).toBeVisible();
    expect(screen.getByTestId("professional-card-2")).toBeVisible();
    expect(screen.getByTestId("professional-card-3")).toBeVisible();
  });

  test("Muestra contador de resultados cuando hay filtros activos", () => {
    render(<ProfessionalList professionals={professionals} isLoading={false} onSelect={vi.fn()} />);
    fireEvent.change(screen.getByPlaceholderText(/Buscar/), { target: { value: "Ana" } });
    expect(screen.getByText("1 profesional encontrado")).toBeVisible();
  });

  test("El select deduplica las especialidades", () => {
    render(<ProfessionalList professionals={professionals} isLoading={false} onSelect={vi.fn()} />);
    const options = screen.getAllByRole("option");
    const texts = options.map((o) => o.textContent);
    expect(texts.filter((t) => t === "Peluquería").length).toBe(1);
    expect(texts).toContain("Barbería");
  });
});
