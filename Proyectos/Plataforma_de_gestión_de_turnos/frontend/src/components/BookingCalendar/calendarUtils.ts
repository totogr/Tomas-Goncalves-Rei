export const WEEKDAY_LABELS = ["D", "L", "M", "M", "J", "V", "S"];

const MONTH_LABELS = [
  "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
  "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre",
];

const SHORT_WEEKDAY_LABELS = ["Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"];

export type DayCell = {
  iso: string;
  dayNumber: number;
  isCurrentMonth: boolean;
  isPast: boolean;
};

export const startOfMonth = (date: Date): Date =>
  new Date(date.getFullYear(), date.getMonth(), 1);

export const addMonths = (date: Date, months: number): Date =>
  new Date(date.getFullYear(), date.getMonth() + months, 1);

export const formatMonthYear = (date: Date): string =>
  `${MONTH_LABELS[date.getMonth()]} ${date.getFullYear()}`;

export const toIsoDate = (date: Date): string => {
  const yyyy = date.getFullYear();
  const mm = String(date.getMonth() + 1).padStart(2, "0");
  const dd = String(date.getDate()).padStart(2, "0");
  return `${yyyy}-${mm}-${dd}`;
};

export const formatHumanDate = (iso: string): string => {
  const [y, m, d] = iso.split("-").map(Number);
  const date = new Date(y, m - 1, d);
  const weekday = SHORT_WEEKDAY_LABELS[date.getDay()];
  const monthName = MONTH_LABELS[date.getMonth()].toLowerCase();
  return `${weekday} ${date.getDate()} de ${monthName}`;
};

export const generateMonthGrid = (viewMonth: Date): DayCell[] => {
  const year = viewMonth.getFullYear();
  const month = viewMonth.getMonth();
  const firstDay = new Date(year, month, 1);
  const startWeekday = firstDay.getDay();

  const gridStart = new Date(year, month, 1 - startWeekday);

  const today = new Date();
  today.setHours(0, 0, 0, 0);

  const cells: DayCell[] = [];
  for (let i = 0; i < 42; i++) {
    const d = new Date(gridStart);
    d.setDate(gridStart.getDate() + i);
    cells.push({
      iso: toIsoDate(d),
      dayNumber: d.getDate(),
      isCurrentMonth: d.getMonth() === month,
      isPast: d < today,
    });
  }
  return cells;
};
