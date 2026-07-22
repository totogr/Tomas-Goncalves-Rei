import { render, screen, fireEvent } from "@testing-library/react";
import { describe, expect, test, vi } from "vitest";

import { ProfessionalServiceList } from "./ProfessionalServiceList";

const mockNavigate = vi.fn();
vi.mock("wouter", () => ({
  useLocation: () => ["/", mockNavigate],
}));

vi.mock("@/components/BookingCalendar/calendarUtils", () => ({
  formatHumanDate: (date: string) => `formatted:${date}`,
}));

const mockMutate = vi.fn();
const mockReset = vi.fn();
const mockCreateAppointment = {
  mutate: mockMutate,
  reset: mockReset,
  isPending: false,
  isSuccess: false,
  isError: false,
};

vi.mock("@/services/AppointmentServices", () => ({
  useCreateAppointment: () => mockCreateAppointment,
}));

vi.mock("@/services/WaitListService", () => ({
  useMyWaitListPosition: () => ({ data: null, isLoading: false }),
  useJoinWaitList: () => ({ mutate: vi.fn(), isPending: false }),
  useLeaveWaitList: () => ({ mutate: vi.fn(), isPending: false }),
  useMyWaitListEntries: () => ({ data: [], isLoading: false }),
  useConfirmPromotion: () => ({ mutate: vi.fn(), isPending: false }),
}));

vi.mock("@/components/BookingCalendar/BookingCalendar", () => ({
  BookingCalendar: ({ onSelectDate, onSelectSlot }: { onSelectDate: (date: string) => void; onSelectSlot: (slot: string) => void }) => (
      <div data-testid="booking-calendar">
        <button onClick={() => onSelectDate("2024-08-15")}>Seleccionar fecha</button>
        <button onClick={() => onSelectSlot("10:30")}>Seleccionar horario</button>
      </div>
  ),
}));

vi.mock("@/components/BookingSuccess/BookingSuccess", () => ({
  BookingSuccess: () => <div data-testid="booking-success">Reserva exitosa</div>,
}));

const professional = {
  id: 1,
  firstName: "Ana",
  lastName: "García",
  specialty: "Peluquería",
  rating: 4.5,
  reviewCount: 12,
  location: { neighborhood: "Palermo", city: "Buenos Aires", address: null },
  services: [
    { id: "10", name: "Corte de cabello", duration_minutes: 45, price: 3000, max_capacity: 1, active: true },
    { id: "11", name: "Tinte", duration_minutes: 90, price: 6000, max_capacity: 1, active: true },
    { id: "12", name: "Servicio inactivo", duration_minutes: 30, price: 1000, max_capacity: 1, active: false },
  ],
};

describe("ProfessionalServiceList", () => {
  test("Muestra el nombre del profesional en el header", () => {
    render(<ProfessionalServiceList professional={professional} />);
    expect(screen.getAllByText("Ana García").length).toBeGreaterThan(0);
  });

  test("Muestra la especialidad y la ubicación", () => {
    render(<ProfessionalServiceList professional={professional} />);
    expect(screen.getByText(/Peluquería/)).toBeVisible();
    expect(screen.getByText(/Palermo/)).toBeVisible();
  });

  test("Lista solo los servicios activos", () => {
    render(<ProfessionalServiceList professional={professional} />);
    expect(screen.getByText("Corte de cabello")).toBeVisible();
    expect(screen.getByText("Tinte")).toBeVisible();
    expect(screen.queryByText("Servicio inactivo")).toBeNull();
  });

  test("Muestra mensaje vacío cuando no hay servicios activos", () => {
    const noServices = { ...professional, services: [] };
    render(<ProfessionalServiceList professional={noServices} />);
    expect(screen.getByText("Sin servicios disponibles")).toBeVisible();
  });

  test("Seleccionar un servicio muestra el calendario", () => {
    render(<ProfessionalServiceList professional={professional} />);
    expect(screen.queryByTestId("booking-calendar")).toBeNull();
    fireEvent.click(screen.getByText("Corte de cabello"));
    expect(screen.getByTestId("booking-calendar")).toBeVisible();
  });

  test("El resumen muestra el servicio seleccionado", () => {
    render(<ProfessionalServiceList professional={professional} />);
    fireEvent.click(screen.getByText("Corte de cabello"));
    expect(screen.getByText(/Corte de cabello · 45 min/)).toBeVisible();
  });

  test("Botón de confirmar está deshabilitado sin fecha ni horario", () => {
    render(<ProfessionalServiceList professional={professional} />);
    fireEvent.click(screen.getByText("Corte de cabello"));
    expect(screen.getByText(/Confirmar reserva/)).toBeDisabled();
  });

  test("Confirmar reserva llama a createAppointment.mutate con los datos correctos", () => {
    render(<ProfessionalServiceList professional={professional} />);
    fireEvent.click(screen.getByText("Corte de cabello"));
    fireEvent.click(screen.getByText("Seleccionar fecha"));
    fireEvent.click(screen.getByText("Seleccionar horario"));
    fireEvent.click(screen.getByText(/Confirmar reserva/));
    expect(mockMutate).toHaveBeenCalledWith({
      professionalId: 1,
      serviceId: "10",
      date: "2024-08-15",
      time: "10:30",
    });
  });

  test("El botón 'Volver al listado' navega a /professionals", () => {
    render(<ProfessionalServiceList professional={professional} />);
    fireEvent.click(screen.getByText("← Volver al listado"));
    expect(mockNavigate).toHaveBeenCalledWith("/professionals");
  });

  test("Muestra error cuando createAppointment falla", () => {
    mockCreateAppointment.isError = true;
    render(<ProfessionalServiceList professional={professional} />);
    expect(screen.getByText("No se pudo confirmar la reserva.")).toBeVisible();
    mockCreateAppointment.isError = false;
  });
});