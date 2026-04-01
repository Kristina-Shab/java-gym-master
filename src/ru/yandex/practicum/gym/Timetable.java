package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {
    private final Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();
    private final Map<Coach, Integer> counter = new HashMap<>();

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

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> coaches = addAllSessionsToCounter();
        List<CounterOfTrainings> counterOfTrainingsList = mapToCounterOfTrainingList(coaches);
        Collections.sort(counterOfTrainingsList);
        return counterOfTrainingsList;
    }

    private List<CounterOfTrainings> mapToCounterOfTrainingList(Map<Coach, Integer> coaches) {
        List<CounterOfTrainings> counterOfTrainingsList = new ArrayList<>();
        for (Map.Entry<Coach, Integer> coach : coaches.entrySet()) {
            CounterOfTrainings counterOfTrainings = new CounterOfTrainings(coach.getKey(), coach.getValue());
            counterOfTrainingsList.add(counterOfTrainings);
        }
        return counterOfTrainingsList;
    }

    private Map<Coach, Integer> addAllSessionsToCounter() {
        for (Map.Entry<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> day : timetable.entrySet()) {
            Map<TimeOfDay, List<TrainingSession>> times = day.getValue();
            for (Map.Entry<TimeOfDay, List<TrainingSession>> time : times.entrySet()) {
                List<TrainingSession> sessions = time.getValue();
                for (TrainingSession session : sessions) {
                    addSessionToCounter(session);
                }
            }
        }
        return counter;
    }

    private void addSessionToCounter(TrainingSession session) {
        Coach coach = session.getCoach();
        if (counter.containsKey(coach)) {
            int count = counter.get(coach);
            count++;
            counter.put(coach, count);
        } else {
            counter.put(coach, 1);
        }
    }

}
