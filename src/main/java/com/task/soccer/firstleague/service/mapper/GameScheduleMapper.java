package com.task.soccer.firstleague.service.mapper;

import com.task.soccer.firstleague.model.Team;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Mapper of the GameScheduleService and GameScheduleMultipleMatchesService.
 */
@Component
public class GameScheduleMapper {

    @Value("${league.matches.per.saturday}")
    private int league_matches;

    private final AtomicInteger index = new AtomicInteger(1);

    private final IntUnaryOperator calculateStartDate = (currentIndex) -> {
        if (currentIndex == league_matches) {
            index.set(1);
            return 1;
        } else {
            index.incrementAndGet();
            return 0;
        }
    };

    public List<String> generateMatches(List<Team> teams) {
        List<String> matches = IntStream.range(0, teams.size())
                .boxed()
                .flatMap(i -> IntStream.range(i + 1, teams.size())
                        .mapToObj(j -> teams.get(i).getName() + " vs " + teams.get(j).getName()))
                .collect(Collectors.toList());
        Collections.shuffle(matches); // Shuffle matches for randomness
        return matches;
    }
    public List<String> generateReverseFixtures(List<String> firstLegMatches) {
        List<String> reversed = new ArrayList<>();
        for (String match : firstLegMatches) {
            String[] teams = match.split(" vs ");
            reversed.add(teams[1] + " vs " + teams[0]);
        }
        return reversed;
    }

    public int calculateNewStartDate() {
        return calculateStartDate.applyAsInt(index.get());
    }
}
