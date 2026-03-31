package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();
        Map<TimeOfDay, List<TrainingSession>> dailySchedule = getOrCreateDailySchedule(day);
        List<TrainingSession> sessionsAtTime = getOrCreateTimeSlot(dailySchedule, time);
        sessionsAtTime.add(trainingSession);
    }

    private Map<TimeOfDay, List<TrainingSession>> getOrCreateDailySchedule(DayOfWeek day) {
        if (!timetable.containsKey(day)) {
            timetable.put(day, new TreeMap<>());
        }
        return timetable.get(day);
    }

    private List<TrainingSession> getOrCreateTimeSlot(
            Map<TimeOfDay, List<TrainingSession>> dailySchedule,
            TimeOfDay time
    ) {
        if (!dailySchedule.containsKey(time)) {
            dailySchedule.put(time, new ArrayList<>());
        }
        return dailySchedule.get(time);
    }

    public Map<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.get(dayOfWeek);
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        return timetable.get(dayOfWeek).get(timeOfDay);
    }
}
