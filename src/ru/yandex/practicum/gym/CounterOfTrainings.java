package ru.yandex.practicum.gym;

public class CounterOfTrainings implements Comparable<CounterOfTrainings>{
    private final Coach coach;
    private final int countTrainingSessions;

    public CounterOfTrainings(Coach coach, int countTrainingSessions) {
        this.coach = coach;
        this.countTrainingSessions = countTrainingSessions;
    }

    @Override
    public int compareTo(CounterOfTrainings o){
        return o.countTrainingSessions - countTrainingSessions;
    }

    public Coach getCoach() {
        return coach;
    }

    public int getCountTrainingSessions() {
        return countTrainingSessions;
    }
}
