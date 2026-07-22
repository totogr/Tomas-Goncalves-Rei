package ar.uba.fi.ingsoft1.turnos.schedule;

import ar.uba.fi.ingsoft1.turnos.professional.ProfessionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ScheduleServiceTest {

    private ScheduleService scheduleService;
    private ScheduleRepository scheduleRepository;
    private ProfessionalRepository professionalRepository;

    @BeforeEach
    void setUp() {
        scheduleRepository = mock(ScheduleRepository.class);
        professionalRepository = mock(ProfessionalRepository.class);
        scheduleService = new ScheduleService(scheduleRepository, professionalRepository);
    }

    @Test
    void getScheduleReturnsAllDaysWithEnabledFalseWhenEmpty() {
        when(scheduleRepository.findByProfessionalId(1L)).thenReturn(List.of());

        WorkingHoursDTO result = scheduleService.getSchedule(1L);

        assertEquals(7, result.schedule().size());
        assertTrue(result.schedule().stream().noneMatch(WorkingHoursDayDTO::enabled),
                "Todos los días deben estar deshabilitados cuando no hay horarios");
    }

    @Test
    void getScheduleReturnsDayNamesInOrder() {
        when(scheduleRepository.findByProfessionalId(1L)).thenReturn(List.of());

        WorkingHoursDTO result = scheduleService.getSchedule(1L);

        List<String> dayNames = result.schedule().stream().map(WorkingHoursDayDTO::day).toList();
        assertEquals(List.of("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"), dayNames);
    }

    @Test
    void getScheduleReturnsMappedRangesForConfiguredDay() {
        Schedule schedule = new Schedule();
        schedule.setProfessionalId(1L);
        schedule.setDayWeek(1); // monday
        schedule.setStart(LocalTime.of(9, 0));
        schedule.setEnd(LocalTime.of(17, 0));

        when(scheduleRepository.findByProfessionalId(1L)).thenReturn(List.of(schedule));

        WorkingHoursDTO result = scheduleService.getSchedule(1L);

        WorkingHoursDayDTO monday = result.schedule().get(0);
        assertTrue(monday.enabled());
        assertEquals(1, monday.ranges().size());
        assertEquals(LocalTime.of(9, 0), monday.ranges().get(0).from());
        assertEquals(LocalTime.of(17, 0), monday.ranges().get(0).to());
    }

    @Test
    void getScheduleHandlesMultipleRangesOnSameDay() {
        Schedule morning = new Schedule();
        morning.setProfessionalId(1L);
        morning.setDayWeek(2); // tuesday
        morning.setStart(LocalTime.of(9, 0));
        morning.setEnd(LocalTime.of(12, 0));

        Schedule afternoon = new Schedule();
        afternoon.setProfessionalId(1L);
        afternoon.setDayWeek(2);
        afternoon.setStart(LocalTime.of(14, 0));
        afternoon.setEnd(LocalTime.of(18, 0));

        when(scheduleRepository.findByProfessionalId(1L)).thenReturn(List.of(morning, afternoon));

        WorkingHoursDTO result = scheduleService.getSchedule(1L);

        WorkingHoursDayDTO tuesday = result.schedule().get(1);
        assertTrue(tuesday.enabled());
        assertEquals(2, tuesday.ranges().size());
    }

    @Test
    void saveScheduleDeletesExistingAndSavesNew() {
        WorkingHoursDayDTO monday = new WorkingHoursDayDTO(
                "monday", true, List.of(new TimeRangeDTO(LocalTime.of(9, 0), LocalTime.of(17, 0))));
        WorkingHoursDTO request = new WorkingHoursDTO(List.of(monday), 30);

        when(scheduleRepository.findByProfessionalId(1L)).thenReturn(List.of());

        scheduleService.saveSchedule(1L, request);

        verify(scheduleRepository).deleteByProfessionalId(1L);
        verify(scheduleRepository).flush();
        verify(scheduleRepository).saveAll(any());
    }

    @Test
    void saveScheduleIgnoresDisabledDays() {
        WorkingHoursDayDTO monday = new WorkingHoursDayDTO(
                "monday", false, List.of(new TimeRangeDTO(LocalTime.of(9, 0), LocalTime.of(17, 0))));
        WorkingHoursDTO request = new WorkingHoursDTO(List.of(monday), 30);

        when(scheduleRepository.findByProfessionalId(1L)).thenReturn(List.of());

        scheduleService.saveSchedule(1L, request);

        verify(scheduleRepository).saveAll(argThat(list -> {
            java.util.Collection<?> col = (java.util.Collection<?>) list;
            return col.isEmpty();
        }));
    }

    @Test
    void saveScheduleIgnoresNullRanges() {
        WorkingHoursDayDTO monday = new WorkingHoursDayDTO("monday", true, null);
        WorkingHoursDTO request = new WorkingHoursDTO(List.of(monday), 30);

        when(scheduleRepository.findByProfessionalId(1L)).thenReturn(List.of());

        scheduleService.saveSchedule(1L, request);

        verify(scheduleRepository).saveAll(argThat(list -> {
            java.util.Collection<?> col = (java.util.Collection<?>) list;
            return col.isEmpty();
        }));
    }

    @Test
    void saveScheduleIgnoresUnknownDayNames() {
        WorkingHoursDayDTO unknown = new WorkingHoursDayDTO(
                "funday", true, List.of(new TimeRangeDTO(LocalTime.of(9, 0), LocalTime.of(17, 0))));
        WorkingHoursDTO request = new WorkingHoursDTO(List.of(unknown), 30);

        when(scheduleRepository.findByProfessionalId(1L)).thenReturn(List.of());

        scheduleService.saveSchedule(1L, request);

        verify(scheduleRepository).saveAll(argThat(list -> {
            java.util.Collection<?> col = (java.util.Collection<?>) list;
            return col.isEmpty();
        }));
    }

    @Test
    void saveScheduleReturnsUpdatedSchedule() {
        Schedule saved = new Schedule();
        saved.setProfessionalId(1L);
        saved.setDayWeek(5); // friday
        saved.setStart(LocalTime.of(10, 0));
        saved.setEnd(LocalTime.of(14, 0));

        WorkingHoursDayDTO friday = new WorkingHoursDayDTO(
                "friday", true, List.of(new TimeRangeDTO(LocalTime.of(10, 0), LocalTime.of(14, 0))));
        WorkingHoursDTO request = new WorkingHoursDTO(List.of(friday), 30);

        when(scheduleRepository.findByProfessionalId(1L)).thenReturn(List.of(saved));

        WorkingHoursDTO result = scheduleService.saveSchedule(1L, request);

        WorkingHoursDayDTO fridayResult = result.schedule().get(4);
        assertTrue(fridayResult.enabled());
        assertEquals(1, fridayResult.ranges().size());
        assertEquals(LocalTime.of(10, 0), fridayResult.ranges().get(0).from());
    }
}
