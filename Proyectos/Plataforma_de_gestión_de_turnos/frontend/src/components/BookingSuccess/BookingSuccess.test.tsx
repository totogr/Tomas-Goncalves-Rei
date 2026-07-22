import { render, screen, fireEvent } from "@testing-library/react";
import { describe, expect, test, vi } from "vitest";

import { BookingSuccess } from "./BookingSuccess";

const mockNavigate = vi.fn();
vi.mock("wouter", () => ({
  useLocation: () => ["/", mockNavigate],
}));

vi.mock("@/components/BookingCalendar/calendarUtils", () => ({
  formatHumanDate: (date: string) => `formatted:${date}`,
}));

const professional = {
  id: 1,
  firstName: "Ana",
  lastName: "García",
  specialty: "Peluquería",
  rating: 4.5,
  reviewCount: 12,
  location: { neighborhood: "Palermo", city: "Buenos Aires", address: null },
  services: [],
};

const service = {
  id: "10",
  name: "Corte de cabello",
  duration_minutes: 45,
  price: 3000,
  max_capacity: 1,
  active: true,
};

describe("BookingSuccess", () => {
  test("Muestra título de confirmación", () => {
    render(
      <BookingSuccess
        professional={professional}
        service={service}
        date="2024-08-15"
        time="10:30" onViewMore={function (): void {
          throw new Error("Function not implemented.");
        } }      />
    );
    expect(screen.getByText("¡Reserva confirmada!")).toBeVisible();
  });

  test("Muestra nombre completo del profesional", () => {
    render(
      <BookingSuccess
        professional={professional}
        service={service}
        date="2024-08-15"
        time="10:30" onViewMore={function (): void {
          throw new Error("Function not implemented.");
        } }      />
    );
    expect(screen.getByText("Ana García")).toBeVisible();
  });

  test("Muestra el nombre del servicio", () => {
    render(
      <BookingSuccess
        professional={professional}
        service={service}
        date="2024-08-15"
        time="10:30" onViewMore={function (): void {
          throw new Error("Function not implemented.");
        } }      />
    );
    expect(screen.getByText("Corte de cabello")).toBeVisible();
  });

  test("Muestra la hora con sufijo 'hs'", () => {
    render(
      <BookingSuccess
        professional={professional}
        service={service}
        date="2024-08-15"
        time="10:30" onViewMore={function (): void {
          throw new Error("Function not implemented.");
        } }      />
    );
    expect(screen.getByText("10:30 hs")).toBeVisible();
  });

  test("Muestra la duración del servicio en minutos", () => {
    render(
      <BookingSuccess
        professional={professional}
        service={service}
        date="2024-08-15"
        time="10:30" onViewMore={function (): void {
          throw new Error("Function not implemented.");
        } }      />
    );
    expect(screen.getByText("45 minutos")).toBeVisible();
  });

  test("Muestra la fecha formateada usando formatHumanDate", () => {
    render(
      <BookingSuccess
        professional={professional}
        service={service}
        date="2024-08-15"
        time="10:30" onViewMore={function (): void {
          throw new Error("Function not implemented.");
        } }      />
    );
    expect(screen.getByText("formatted:2024-08-15")).toBeVisible();
  });


  test("Botón 'Ver otros profesionales' navega al listado", () => {
    render(
      <BookingSuccess
        professional={professional}
        service={service}
        date="2024-08-15"
        time="10:30" onViewMore={function (): void {
          throw new Error("Function not implemented.");
        } }      />
    );
    fireEvent.click(screen.getByText("Ver otros profesionales"));
    expect(mockNavigate).toHaveBeenCalledWith("/professionals");
  });
});
