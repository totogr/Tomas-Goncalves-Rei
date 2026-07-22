package ar.uba.fi.ingsoft1.turnos.schedule;

import ar.uba.fi.ingsoft1.turnos.professional.ProfessionalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ProfessionalRepository professionalRepository;

    private static final List<String> DAY_NAMES = List.of(
            "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday");

    public ScheduleService(ScheduleRepository scheduleRepository, ProfessionalRepository professionalRepository) {
        this.scheduleRepository = scheduleRepository;
        this.professionalRepository = professionalRepository;
    }

    @Transactional(readOnly = true)
    public WorkingHoursDTO getSchedule(Long professionalId) {
        List<Schedule> schedules = scheduleRepository.findByProfessionalId(professionalId);
        int slotInterval = professionalRepository.findById(professionalId)
                .map(p -> p.getSlotIntervalMinutes() != null ? p.getSlotIntervalMinutes() : 30)
                .orElse(30);
        return buildWorkingHoursDTO(schedules, slotInterval);
    }

    public WorkingHoursDTO saveSchedule(Long professionalId, WorkingHoursDTO request) {
        scheduleRepository.deleteByProfessionalId(professionalId);
        scheduleRepository.flush();

        int slotInterval = (request.slotIntervalMinutes() != null && request.slotIntervalMinutes() >= 5)
                ? request.slotIntervalMinutes() : 30;

        professionalRepository.findById(professionalId).ifPresent(p -> {
            p.setSlotIntervalMinutes(slotInterval);
            professionalRepository.save(p);
        });

        List<Schedule> newSchedules = new ArrayList<>();
        for (WorkingHoursDayDTO dayDTO : request.schedule()) {
            if (!dayDTO.enabled() || dayDTO.ranges() == null)
                continue;

            int dayWeek = DAY_NAMES.indexOf(dayDTO.day()) + 1;
            if (dayWeek == 0)
                continue;

            for (TimeRangeDTO range : dayDTO.ranges()) {
                Schedule s = new Schedule();
                s.setProfessionalId(professionalId);
                s.setDayWeek(dayWeek);
                s.setStart(range.from());
                s.setEnd(range.to());
                newSchedules.add(s);
            }
        }
        scheduleRepository.saveAll(newSchedules);

        List<Schedule> saved = scheduleRepository.findByProfessionalId(professionalId);
        return buildWorkingHoursDTO(saved, slotInterval);
    }

    private WorkingHoursDTO buildWorkingHoursDTO(List<Schedule> schedules, int slotIntervalMinutes) {
        Map<Integer, List<Schedule>> byDay = schedules.stream()
                .collect(Collectors.groupingBy(Schedule::getDayWeek));

        List<WorkingHoursDayDTO> days = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            List<Schedule> daySchedules = byDay.getOrDefault(i, List.of());
            boolean enabled = !daySchedules.isEmpty();
            List<TimeRangeDTO> ranges = daySchedules.stream()
                    .map(s -> new TimeRangeDTO(s.getStart(), s.getEnd()))
                    .toList();
            days.add(new WorkingHoursDayDTO(DAY_NAMES.get(i - 1), enabled, ranges));
        }
        return new WorkingHoursDTO(days, slotIntervalMinutes);
    }
}
