package ru.yandex.practicum.gym;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

public class TimetableTest {
    Timetable timetable;
    static Coach coach;
    static Group groupAdult;
    static Group groupChild;

    @BeforeAll
    static void beforeAll() {
        coach = new Coach("Васильев", "Николай", "Сергеевич");
        groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        groupChild = new Group("Акробатика для детей", Age.CHILD, 90);
    }

    @BeforeEach
    void beforeEach() {
        timetable = new Timetable();
    }

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        TrainingSession singleTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());
        assertEquals(1,
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).get(singleTrainingSession.getTimeOfDay()).size());
        //Проверить, что за вторник не вернулось занятий
        assertNull(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY));
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));
        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);
        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);
        Map<TimeOfDay, List<TrainingSession>> result = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        Map<TimeOfDay, List<TrainingSession>> reference = new TreeMap<>();
        reference.put(new TimeOfDay(13, 0), List.of(thursdayChildTrainingSession));
        reference.put(new TimeOfDay(20, 0), List.of(thursdayAdultTrainingSession));

        // Проверить, что за понедельник вернулось одно занятие
        Map<TimeOfDay, List<TrainingSession>> trainingSessionsForDay =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        TimeOfDay time = mondayChildTrainingSession.getTimeOfDay();
        assertEquals(1, trainingSessionsForDay.get(time).size());
        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        assertEquals(reference, result);
        // Проверить, что за вторник не вернулось занятий
        assertNull(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY));
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        TrainingSession singleTrainingSession = new TrainingSession(groupChild, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        assertEquals(1, timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0)).size());
        //Проверить, что за понедельник в 14:00 не вернулось занятий
        assertNull(timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0)));
    }

    @Test
    void testGetTrainingSessionsForDayAndTimeMultipleSessions() {
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(10, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);

        assertEquals(2, timetable.getTrainingSessionsForDayAndTime(DayOfWeek.THURSDAY, new TimeOfDay(10, 0)).size());
    }

    @Test
    void testGetTrainingSessionsForDaySmallTimeInterval() {
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(10, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(10, 1));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);

        Map<TimeOfDay, List<TrainingSession>> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        List<TimeOfDay> times = new ArrayList<>(thursdaySessions.keySet());
        assertTrue(thursdaySessions.containsKey(new TimeOfDay(10, 0)));
        assertTrue(thursdaySessions.containsKey(new TimeOfDay(10, 1)));
        assertEquals(new TimeOfDay(10, 0), times.get(0));
        assertEquals(new TimeOfDay(10, 1), times.get(1));
    }

    @Test
    void testGetTrainingSessionsForDayIfTimetableIsEmpty() {
        for (DayOfWeek day : DayOfWeek.values()) {
            assertNull(timetable.getTrainingSessionsForDay(day));
        }
    }

    @Test
    void getCountByCoachesForMultiSessionsOneCouch() {
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 30));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        List<CounterOfTrainings> counter = timetable.getCountByCoaches();

        assertEquals(1, counter.size());
        assertEquals(2, counter.get(0).getCountTrainingSessions());
    }

    @Test
    void getCountByCoachesForMultiSessionsMultiCouches() {
        Coach coach1 = new Coach("Ф1", null, null);
        Coach coach2 = new Coach("Ф2", null, null);
        TrainingSession session1 = new TrainingSession(groupAdult, coach1,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));
        TrainingSession session2 = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 30));
        TrainingSession session3 = new TrainingSession(groupAdult, coach2,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));
        TrainingSession session4 = new TrainingSession(groupChild, coach1,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 30));
        TrainingSession session5 = new TrainingSession(groupAdult, coach2,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));
        TrainingSession session6 = new TrainingSession(groupChild, coach1,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 30));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);
        timetable.addNewTrainingSession(session3);
        timetable.addNewTrainingSession(session4);
        timetable.addNewTrainingSession(session5);
        timetable.addNewTrainingSession(session6);

        List<CounterOfTrainings> counter = timetable.getCountByCoaches();
        assertEquals(3, counter.size());
        assertTrue(counter.get(0).getCountTrainingSessions() >= counter.get(1).getCountTrainingSessions());
        assertTrue(counter.get(1).getCountTrainingSessions() >= counter.get(2).getCountTrainingSessions());
    }

    @Test
    void getCountByCoachesIfTimetableIsEmpty() {
        List<CounterOfTrainings> counter = timetable.getCountByCoaches();
        assertEquals(0, counter.size());
    }
}
